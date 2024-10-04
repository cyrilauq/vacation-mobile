package cyril.brian.vacationmobile.ui.vacation.infos.tchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.databinding.FragmentTchatBinding
import cyril.brian.vacationmobile.ui.vacation.infos.tchat.adapter.MessageAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TchatFragment : Fragment() {
    companion object {
        private const val ARG_VACATION_ID = "vacation_id"
        fun newInstance(vacationId: String) = TchatFragment().also {
            it.vacationId = vacationId
            it.arguments = Bundle().apply {
                putString(ARG_VACATION_ID, vacationId)
            }
        }
    }

    private lateinit var vacationId: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    val viewModel: TchatViewModel by viewModels()

    private var _binding: FragmentTchatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve vacationId from arguments
        vacationId = arguments?.getString(ARG_VACATION_ID) ?: ""
        viewModel.vacationId = vacationId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTchatBinding.inflate(inflater, container, false)
        val view = binding.root

        setUpRecyclerView()
        getMessages()
        viewModel.init(viewModel.vacationId)

        viewModel.message.observe(viewLifecycleOwner) {
            adapter.loadMessages(it!!)
        }

        binding.tchatMsgBtn.setOnClickListener {
            if(binding.tchatMsgInput.text.isNotEmpty()) {
                lifecycleScope.launch {
                    if(!viewModel.sendMessage(binding.tchatMsgInput.text.toString(), viewModel.vacationId)) {
                        Toast.makeText(requireActivity(), viewModel.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return view
    }

    private fun setUpRecyclerView() {
        adapter = MessageAdapter(requireContext())
        recyclerView = binding.tchatRecyclerView
        recyclerView.adapter = adapter
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager
        recyclerView.scrollToPosition(0)
    }

    private fun getMessages() {
        runBlocking {
            viewModel.loadMessages(viewModel.vacationId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.onDestroy()
    }

}