package cyril.brian.vacationmobile.ui.vacation.infos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.databinding.FragmentVacationBinding
import cyril.brian.vacationmobile.ui.vacation.infos.adapter.MemberAdapter
import cyril.brian.vacationmobile.ui.vacation.infos.adapter.VacationActivityAdapter
import cyril.brian.vacationmobile.ui.vacation.infos.adapter.WeatherAdapter
import cyril.brian.vacationmobile.ui.vacation.infos.dialog.MemberDialogFragment
import cyril.brian.vacationmobile.ui.vacation.infos.dialog_add_activity.AddActivityDialogFragment
import cyril.brian.vacationmobile.ui.vacation.infos.dialog_plannify.PlannifyDialogFragment
import cyril.brian.vacationmobile.ui.vacation.infos.tchat.TchatFragment


class VacationFragment : Fragment() {
    private var _binding: FragmentVacationBinding? = null
    private val binding get() = _binding!!
    val viewModel: VacationViewModel by viewModels()

    lateinit var consumer: () -> Unit
    private lateinit var dialog: MemberDialogFragment
    private lateinit var plannifyActivityDialog: PlannifyDialogFragment
    private lateinit var addActivityDialog: AddActivityDialogFragment

    private var vacationId: String? = null

    companion object {
        fun newInstance(vacationId: String? = null, consumer: () -> Unit): VacationFragment {
            val frag = VacationFragment()
            frag.consumer = consumer
            frag.vacationId = vacationId
            return frag
        }
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.downloadPlanning()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacationBinding.inflate(inflater, container, false)
        val view = binding.root

        setEventListeners()

        dialog = MemberDialogFragment.newInstance {
                members ->
            val add = viewModel.addMembers(members)
            Toast.makeText(requireActivity(), if(add) "Invation(s) sent" else viewModel.errorMsg, Toast.LENGTH_LONG).show()
        }

        addActivityDialog = AddActivityDialogFragment.newInstance()
        plannifyActivityDialog = PlannifyDialogFragment.newInstance()

        viewModel.load()

        if(viewModel.hasVacation) {
            displayVacationInfos()
        } else {
            Toast.makeText(requireActivity(), viewModel.message, Toast.LENGTH_LONG).show()
        }

        if(viewModel.gatherActivities()) {
            binding.activitiesRecyclerView.adapter = VacationActivityAdapter(this, viewModel.activities, R.layout.item_activity) {activityId ->
                if(viewModel.data.isPublished) {
                    Toast.makeText(requireContext(), "Vacation published so it cannot be modified", Toast.LENGTH_LONG).show()
                } else {
                    plannifyActivityDialog.activityId = activityId
                    plannifyActivityDialog.show(childFragmentManager, "PlannifyActivityDialog")
                }

            }
        } else {
            val error: String = viewModel.getError()!!
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }

        if (viewModel.gatherMembers()) {
            binding.membersRecyclerView.adapter = MemberAdapter(this, viewModel.getMembers(), R.layout.item_member)
        } else {
            val error: String = viewModel.getError()!!
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }
        Log.d("VacationFragment", "$vacationId")
        if(vacationId != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.tchat_container, TchatFragment.newInstance(vacationId!!))
                .addToBackStack("")
                .commit()
        }
        hideBtn(viewModel.data.isPublished)
        return view
    }

    private fun hideBtn(hide: Boolean) {
        binding.addMemberBtn.visibility = if(hide) View.GONE else View.VISIBLE
        binding.addActivityBtn.visibility = if(hide) View.GONE else View.VISIBLE
    }

    private fun setEventListeners() {
        binding.addMemberBtn.setOnClickListener {
            dialog.show(childFragmentManager, "MemberDialogFragmentTag")
        }
        binding.addActivityBtn.setOnClickListener {
            addActivityDialog.show(childFragmentManager, "AddActivityDialogFragment")
        }
        binding.openMapBtn.setOnClickListener{
            /**
             * Création d'intent pour rediriger l'app vers Google Maps et lancer un itinéraire
             */
            val gmmIntentUri = Uri.parse("google.navigation:q=${viewModel.data.lat},${viewModel.data.lon}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(requireActivity().packageManager)?.let {
                startActivity(mapIntent)
            }
        }
        binding.showActivities.setOnClickListener {
            binding.activitiesTitle.visibility = View.VISIBLE
            binding.activitiesRecyclerView.visibility = View.VISIBLE
            binding.membersTitle.visibility = View.GONE
            binding.membersRecyclerView.visibility = View.GONE
            binding.tchatContainer.visibility = View.GONE
            binding.weatherRecyclerView.visibility = View.GONE
        }
        binding.showMembers.setOnClickListener {
            binding.activitiesTitle.visibility = View.GONE
            binding.activitiesRecyclerView.visibility = View.GONE
            binding.membersTitle.visibility = View.VISIBLE
            binding.membersRecyclerView.visibility = View.VISIBLE
            binding.tchatContainer.visibility = View.GONE
            binding.weatherRecyclerView.visibility = View.GONE
        }
        binding.showTchat.setOnClickListener {
            binding.activitiesTitle.visibility = View.GONE
            binding.activitiesRecyclerView.visibility = View.GONE
            binding.membersTitle.visibility = View.GONE
            binding.membersRecyclerView.visibility = View.GONE
            binding.tchatContainer.visibility = View.VISIBLE
            binding.weatherRecyclerView.visibility = View.GONE
        }
        binding.showWeatherBtn.setOnClickListener {
            if(viewModel.getForecast().isEmpty()) {
                if(viewModel.gatherForecast()) {
                    binding.weatherRecyclerView.adapter = WeatherAdapter(this, viewModel.getForecast(), R.layout.item_weather)
                } else {
                    val error: String = viewModel.getError()!!
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }

            binding.activitiesTitle.visibility = View.GONE
            binding.activitiesRecyclerView.visibility = View.GONE
            binding.membersTitle.visibility = View.GONE
            binding.membersRecyclerView.visibility = View.GONE
            binding.tchatContainer.visibility = View.GONE
            binding.weatherRecyclerView.visibility = View.VISIBLE
        }
        binding.downloadPlnningBtn.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                cameraPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                val message = viewModel.downloadPlanning()
                if(message.isNotEmpty()) {
                    Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun displayVacationInfos() {
        val data = viewModel.data
        binding.vacationDescription.text = data.description
        binding.vacationTitle.text = data.title
        binding.vacationPlace.text = data.place
        binding.endDate.text = data.endDate
        binding.endTime.text = data.endTime
        binding.beginDate.text = data.beginDate
        binding.beginTime.text = data.beginTime
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }
}