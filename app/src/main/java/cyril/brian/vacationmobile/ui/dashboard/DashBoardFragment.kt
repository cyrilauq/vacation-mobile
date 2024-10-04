package cyril.brian.vacationmobile.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.databinding.FragmentDashboardBinding
import cyril.brian.vacationmobile.ui.RecyclerViewEvent
import cyril.brian.vacationmobile.ui.dashboard.adapter.VacationAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [DashBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashBoardFragment : Fragment(), RecyclerViewEvent {
    private lateinit var cardConsumer: (String, String) -> Unit
    private lateinit var addConsumer: () -> Unit
    private var _binding: FragmentDashboardBinding? = null
    private val viewModel: DashBoardViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        val verticalVacationRecyclerView = binding.verticalVacationRecyclerView

        binding.goToAddVacationBtn.setOnClickListener {
            addConsumer.invoke()
        }
        binding.goToInvitationsBtn.setOnClickListener {
            cardConsumer.invoke(
                "invitations", ""
            )
        }

        if(viewModel.gatherUserVacations()) {
            verticalVacationRecyclerView.adapter = VacationAdapter(viewModel.getVacations(), R.layout.item_vacation, this)
        } else {
            val error: String = viewModel.getError()
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(cardConsumer: (String, String) -> Unit, addConsumer: () -> Unit): DashBoardFragment {
            val frag = DashBoardFragment()
            frag.cardConsumer = cardConsumer
            frag.addConsumer = addConsumer
            return frag
        }
    }

    override fun onItemClick(position: Int) {
        cardConsumer.invoke(
            "see_vacation", viewModel.getVacation(position).id
        )
    }
}