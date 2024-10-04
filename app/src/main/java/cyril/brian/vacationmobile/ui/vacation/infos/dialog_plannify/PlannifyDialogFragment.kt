package cyril.brian.vacationmobile.ui.vacation.infos.dialog_plannify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cyril.brian.vacationmobile.databinding.FragmentPlannifyDialogBinding
import cyril.brian.vacationmobile.ui.components.PeriodPickerFragment

/**
 * A simple [Fragment] subclass.
 * Use the [PlannifyDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlannifyDialogFragment : DialogFragment() {
    private var _binding: FragmentPlannifyDialogBinding? = null
    var activityId: String? = null

    private val binding get() = _binding!!

    val viewModel: PlannifyDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannifyDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        setListener()

        return view
    }

    private fun setListener() {
        binding.confirmBtn.setOnClickListener {
            val periodPicker = binding.periodPicker.getFragment<PeriodPickerFragment>()
            viewModel.beginDate = periodPicker.selectedBeginDate
            viewModel.endDate = periodPicker.selectedEndDate
            viewModel.beginTime = periodPicker.selectedBeginTime
            viewModel.endTime = periodPicker.selectedEndTime

            if(viewModel.plannify(activityId!!)) {
                Toast.makeText(requireActivity(), viewModel.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireActivity(), viewModel.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PlannifyDialogFragment.
         */
        fun newInstance(): PlannifyDialogFragment = PlannifyDialogFragment()
    }
}

