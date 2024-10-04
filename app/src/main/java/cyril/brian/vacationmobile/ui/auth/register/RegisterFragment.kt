package cyril.brian.vacationmobile.ui.auth.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cyril.brian.vacationmobile.databinding.FragmentRegisterBinding
import cyril.brian.vacationmobile.ui.utils.Message

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    lateinit var consumer: () -> Unit

    companion object {
        fun newInstance(consumer: () -> Unit): RegisterFragment {
            val frag = RegisterFragment()
            frag.consumer = consumer
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.registerNameField.doOnTextChanged { text, start, before, count -> viewModel.setName(
            String(text!!.toList().toCharArray())
        ) }
        binding.registerLoginField.doOnTextChanged { text, start, before, count -> viewModel.setLogin(
            String(text!!.toList().toCharArray())
        ) }
        binding.registerPasswordField.doOnTextChanged { text, start, before, count -> viewModel.setPassword(
            String(text!!.toList().toCharArray())
        ) }
        binding.registerEmailField.doOnTextChanged { text, start, before, count -> viewModel.setMail(
            String(text!!.toList().toCharArray())
        ) }
        binding.registerFirstnameFld.doOnTextChanged { text, start, before, count ->
            viewModel.firstname = String(text!!.toList().toCharArray())
        }
        binding.registerConfirmPwdFld.doOnTextChanged { text, start, before, count ->
            viewModel.confirmPassword = String(text!!.toList().toCharArray())
        }

        binding.registerButton.setOnClickListener {
            if(checkAllFields() && viewModel.isValid()) {
                Log.d("RegisterFragment", "Viewmodel fields are valid")
                if(viewModel.signUp()) {
                    binding.registerError.text = ""
                    binding.registerError.isVisible = false
                    Toast.makeText(requireActivity(), "You've been successfully register now go to login page", Toast.LENGTH_LONG).show()
                } else {
                    binding.registerError.text = viewModel.getError()
                    binding.registerError.isVisible = true
                }
            } else {
                binding.registerError.text = viewModel.getError()
                binding.registerError.isVisible = true
            }
        }
        return view
    }

    private fun checkFor(input: EditText, message: Message): Boolean {
        if(message.isError) {
            input.error = message.message
        }
        return !message.isError
    }

    private fun checkAllFields(): Boolean {
        val password = checkFor(binding.registerPasswordField, viewModel.checkPassword())
        val confirmPassword = checkFor(binding.registerConfirmPwdFld, viewModel.checkConfirmPassword())
        val mail = checkFor(binding.registerEmailField, viewModel.checkMail())
        val name = checkFor(binding.registerNameField, viewModel.checkName())
        val firstname = checkFor(binding.registerFirstnameFld, viewModel.checkFirstName())
        val login = checkFor(binding.registerLoginField, viewModel.checkLogin())
        return password && mail && name && firstname && login && confirmPassword
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }
}