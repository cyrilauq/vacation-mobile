package cyril.brian.vacationmobile.ui.auth.register

import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.repositories.IUserRepository
import cyril.brian.vacationmobile.repositories.args.RegisterArgs
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.ui.utils.Message

class RegisterViewModel : ViewModel() {
    private var name: String = ""
    private var login: String = ""
    private var mail: String = ""
    private var password: String = ""
    var confirmPassword: String = ""
    var firstname: String = ""
    private var error: String = ""
    private var _repository = IUserRepository.get()

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }

    fun setLogin(text: String) {
        this.login = text
    }

    fun setMail(text: String) {
        this.mail = text
    }

    fun setName(text: String) {
        this.name = text
    }

    fun setPassword(text: String) {
        this.password = text
    }

    fun isValid(): Boolean {
        if(this.login.isEmpty()) {
            this.error = "The login field shouldn't be empty. "
            return false
        }
        if(this.name.isEmpty()) {
            this.error = "The name field shouldn't be empty. "
        }
        if(this.mail.isEmpty()) {
            this.error = "The mail field shouldn't be empty. "
            return false
        }
        if(this.password.isEmpty()) {
            this.error = "The password field shouldn't be empty. "
            return false
        }
        if(this.password != this.confirmPassword) {
            this.error = "The password should be equals to the confirm password. "
            return false
        }
        this.error = ""
        return true
    }

    fun signUp(): Boolean {
        this.error = ""
        return try {
            repository.register(RegisterArgs(
                this.name,
                this.firstname,
                this.mail,
                this.login,
                this.password
            ))
            this.error = "Sign up successful !"
            true
        } catch(e: AuthException) {
            this.error = e.message.toString()
            false
        }
    }

    fun checkPassword(): Message {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{6,}\$")
        if(!passwordRegex.matches(password)) {
            return Message(true, "Password need at leat to have one uppercase character, one special charecter, one minuscule charecter and be at leat six characters long")
        }
        return Message(false, null)
    }

    fun checkConfirmPassword(): Message {
        if(confirmPassword.trim().isEmpty()) {
            return Message(true, "The confirm password shouldn't be empty")
        }
        if(password != confirmPassword) {
            return Message(true, "The confirm password and the password should be equals")
        }
        return Message(false, null)
    }

    fun checkMail(): Message {
        val mailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        if(!mailRegex.matches(mail)) {
            return Message(true, "The given email isn't valid")
        }
        return Message(false, null)
    }

    fun checkName(): Message {
        if(name.trim().isEmpty()) {
            return Message(true, "The name shouldn't be empty")
        }
        return Message(false, null)
    }

    fun checkFirstName(): Message {
        if(firstname.trim().isEmpty()) {
            return Message(true, "The firstname shouldn't be empty")
        }
        return Message(false, null)
    }

    fun checkLogin(): Message {
        if(login.trim().isEmpty()) {
            return Message(true, "The login shouldn't be empty")
        }
        return Message(false, null)
    }

    fun getError(): String {
        return this.error
    }
}