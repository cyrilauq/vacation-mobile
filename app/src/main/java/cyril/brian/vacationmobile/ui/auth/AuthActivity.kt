package cyril.brian.vacationmobile.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.auth.login.LoginFragment
import cyril.brian.vacationmobile.ui.auth.register.RegisterFragment
import cyril.brian.vacationmobile.ui.vacation.create.CreateVacationFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authMethod = intent.getStringExtra("AUTH_METHOD_KEY")!!
        Log.d("AuthActivity", authMethod)
        if (savedInstanceState == null) {
            if(authMethod == "login") {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.auth_fragment_container_view, LoginFragment.newInstance {})
                    .commitNow()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.auth_fragment_container_view, RegisterFragment.newInstance {})
                    .commitNow()
            }
        }

        setContentView(R.layout.activity_auth)
    }
}