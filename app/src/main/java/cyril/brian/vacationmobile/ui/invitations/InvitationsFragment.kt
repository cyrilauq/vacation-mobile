package cyril.brian.vacationmobile.ui.invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cyril.brian.vacationmobile.databinding.FragmentInvitationsBinding
import cyril.brian.vacationmobile.ui.RecyclerViewEvent
import cyril.brian.vacationmobile.ui.dashboard.adapter.InvitationsAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [InvitationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InvitationsFragment : Fragment(), RecyclerViewEvent {
    private var _binding: FragmentInvitationsBinding? = null
    private val viewModel: InvitationsViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvitationsBinding.inflate(inflater, container, false)
        val view = binding.root

        val verticalVacationRecyclerView = binding.verticalVacationRecyclerView

        if(viewModel.gatherUserInvitations()) {
            verticalVacationRecyclerView.adapter = InvitationsAdapter(viewModel.getInvitations(), this)
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
        @JvmStatic
        fun newInstance() = InvitationsFragment()
    }

    override fun onItemClick(position: Int) {
        if(viewModel.acceptInvitationAt(position)) {
            Toast.makeText(requireActivity(), "Invitation accepted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireActivity(), viewModel.getError(), Toast.LENGTH_LONG).show()
        }
    }
}