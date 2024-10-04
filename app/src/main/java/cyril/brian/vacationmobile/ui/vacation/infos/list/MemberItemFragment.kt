package cyril.brian.vacationmobile.ui.vacation.infos.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.databinding.FragmentMemberItemBinding
import cyril.brian.vacationmobile.ui.vacation.infos.UserViewData

/**
 * A simple [Fragment] subclass.
 * Use the [MemberItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemberItemFragment : Fragment() {
    private lateinit var consumer: (String) -> Unit
    private lateinit var data: UserViewData
    private var _binding: FragmentMemberItemBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberItemBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.checkboxAdd.isChecked = data.isSelected
        binding.checkboxName.text = data.name
        binding.checkboxEmail.text = getString(R.string.mail_add_member_template, data.mail)

        binding.checkboxAdd.setOnClickListener {
            consumer.invoke(data.uid)
        }

        view.setOnClickListener {
            binding.checkboxAdd.isChecked = !binding.checkboxAdd.isChecked
            consumer.invoke(data.uid)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param data      Data of the user that will be displayed.
         * @param consumer  Action to do when the fragment is clicked or checked.
         *
         * @return A new instance of fragment MemberItemFragment.
         */
        fun newInstance(data: UserViewData, consumer: (String) -> Unit): MemberItemFragment {
            val frag = MemberItemFragment()
            frag.consumer = consumer
            frag.data = data
            return frag
        }
    }
}