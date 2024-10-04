package cyril.brian.vacationmobile.ui.auth.login

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.SharedPreferencesKeys
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import cyril.brian.vacationmobile.repositories.IUserRepository
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.ui.utils.Message


class LoginViewModel : ViewModel() {
    private var login: String = ""
    private var password: String = ""
    private var error: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var _repository = IUserRepository.get()

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }

    fun getLogin(): String {
        return this.login
    }

    fun getPassword(): String {
        return this.password
    }

    fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    fun setLogin(text: String) {
        this.login = text
    }

    fun setPassword(text: String) {
        this.password = text
    }

    fun checkPassword(): Message {
        if(password.isEmpty()) {
            return Message(true, "The password field shouldn't be empty")
        }
        return Message(false, null)
    }

    fun checkLogin(): Message {
        if(login.isEmpty()) {
            return Message(true, "The login field shouldn't be empty")
        }
        return Message(false, null)
    }

    fun isValid(): Boolean {
        if(this.login.isEmpty()) {
            this.error = "The login field shouldn't be empty."
            return false
        }
        if(this.password.isEmpty()) {
            this.error = "The password field shouldn't be empty."
            return false
        }
        return true
    }

    fun signIn(): Boolean {
        try {
            val data: UserLoginResponse = repository.login(this.login, this.password)

            sharedPreferences.edit {
                putString(SharedPreferencesKeys.TOKEN_KEY.key, data.token)
                commit()
            }
            return true
        } catch (e: AuthException) {
            error = "Error while attempting to connect: ${e.message}"
            return false
        } catch (e: Exception) {
            error = e.message
            return false
        }
    }

    fun getError(): String {
        return if (error == null) "" else error!!
    }
}