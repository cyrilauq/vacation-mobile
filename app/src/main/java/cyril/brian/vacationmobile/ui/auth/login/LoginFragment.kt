package cyril.brian.vacationmobile.ui.auth.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cyril.brian.vacationmobile.PreferencesGiver
import cyril.brian.vacationmobile.databinding.FragmentLoginBinding
import cyril.brian.vacationmobile.ui.app.AppActivity
import cyril.brian.vacationmobile.ui.utils.Message

class LoginFragment : Fragment(), PreferencesGiver {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    lateinit var consumer: () -> Unit

    companion object {
        fun newInstance(consumer: () -> Unit): LoginFragment {
            val frag = LoginFragment()
            frag.consumer = consumer
            return frag
        }
    }


        /**
     * Va créer l'instane viewmodels tout seul
     */
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.loginLoginField.doOnTextChanged { text, _, _, _ -> viewModel.setLogin(
            String(text!!.toList().toCharArray())
        ) }
        binding.loginPasswordField.doOnTextChanged { text, _, _, _ -> viewModel.setPassword(
            String(text!!.toList().toCharArray())
        ) }
        binding.loginPasswordField.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                checkFor(binding.loginPasswordField, viewModel.checkPassword())
            }
        }
        binding.loginLoginField.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                checkFor(binding.loginPasswordField, viewModel.checkPassword())
            }
        }
        viewModel.setSharedPreferences(getSharedPreferences())

        binding.loginConfirmButton.setOnClickListener {
            it.isClickable = false
            if(checkAllFields()) {
                if(viewModel.isValid() && viewModel.signIn()) {
                    Log.d("LoginFragment", "The ViewModel Fields are valid!!!")
                    startActivityFor()
                } else {
                    Log.d("LoginFragment", viewModel.getError())
                    Toast.makeText(requireActivity(), viewModel.getError(), Toast.LENGTH_LONG).show()
                }
            }
            it.isClickable = true
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
        val password = checkFor(binding.loginPasswordField, viewModel.checkPassword())
        val login = checkFor(binding.loginLoginField, viewModel.checkLogin())
        return password &&
            login
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    override fun getSharedPreferences(): SharedPreferences {
        return requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    private fun startActivityFor(message: String = "dashboard") {
        val intent = Intent(activity, AppActivity::class.java).apply {
            putExtra("WANTED_SCREEN_KEY", message)
        }
        startActivity(intent)
    }

}