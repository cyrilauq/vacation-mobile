package cyril.brian.vacationmobile.ui.vacation.infos.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.vacation.infos.list.MemberItemFragment

/**
 * A simple [Fragment] subclass.
 * Use the [MemberDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemberDialogFragment : DialogFragment() {
    private lateinit var fragContainer: LinearLayout
    private lateinit var addMemberConsumer: (List<String>) -> Unit

    private var lastApiCallTime = 0L

    val viewModel: MemberDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_member_layout, container, false)

        // Inside onCreateView method of MyDialogFragment
        val inputEditText = view.findViewById<EditText>(R.id.search_member_input)
        fragContainer = view.findViewById(R.id.members_container)
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Implement if needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentTime = System.currentTimeMillis()

                // Check if at least 500ms have passed since the last API call
                if (currentTime - lastApiCallTime >= 500) {
                    // Update the last API call time
                    lastApiCallTime = currentTime

                    viewModel.currentInput = String(s!!.toList().toCharArray())
                    if(!viewModel.searchUser(viewModel.currentInput)) {
                        val input = view.findViewById<TextView>(R.id.error_container)
                        input.visibility = View.VISIBLE
                        input.text = viewModel.message
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Implement if needed
            }
        })

        viewModel.usersDatas.observe(viewLifecycleOwner) { itemList ->
            val input = view.findViewById<TextView>(R.id.error_container)
            input.visibility = View.GONE
            fragContainer.visibility = View.GONE
            // Clear existing fragments in the container (if needed)
            childFragmentManager.beginTransaction().replace(R.id.members_container, Fragment())
                .commit()
            if(itemList != null && itemList.isNotEmpty()) {
                fragContainer.visibility = View.VISIBLE
                // Iterate through the list and add a fragment for each item
                for (item in itemList) {
                    val newFragment = MemberItemFragment.newInstance(item) { uid -> viewModel.selectUser(uid) }
                    val transaction = childFragmentManager.beginTransaction()
                    transaction.add(
                        R.id.members_container,
                        newFragment,
                        "tag_${item}"
                    ) // Use a tag to identify the fragment
                    // Optionally, you can use addToBackStack here if needed
                    transaction.commit()
                }
            }
        }

        view.findViewById<Button>(R.id.close_btn).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.members_confirm_btn).setOnClickListener {
            addMemberConsumer.invoke(viewModel.selectedUsers)
        }

        view.findViewById<ImageButton>(R.id.search_member_btn).setOnClickListener {
            it.isClickable = false
            if(!viewModel.searchUser(viewModel.currentInput)) {
                val input = view.findViewById<TextView>(R.id.error_container)
                input.visibility = View.VISIBLE
                input.text = viewModel.message
            }
            it.isClickable = true
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param addMemberConsumer  Method to invoke when the job is done
         *
         * @return A new instance of fragment MemberDialogFragment.
         */
        fun newInstance(addMemberConsumer: (List<String>) -> Unit): MemberDialogFragment {
            val frag = MemberDialogFragment()
            frag.addMemberConsumer = addMemberConsumer
            return frag
        }
    }
}