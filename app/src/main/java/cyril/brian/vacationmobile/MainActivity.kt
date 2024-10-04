package cyril.brian.vacationmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cyril.brian.vacationmobile.databinding.ActivityMainBinding
import cyril.brian.vacationmobile.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = _binding!!.root

        _binding!!.signInButton.setOnClickListener {
            Log.d("MainActivity", "Sign In button clicked")
            startActivityFor("register")
        }
        _binding!!.loginButton.setOnClickListener {
            Log.d("MainActivity", "Login button clicked")
            startActivityFor("login")
        }

        setContentView(view)
    }

    private fun startActivityFor(message: String = "login") {
        val intent = Intent(this, AuthActivity::class.java).apply {
            putExtra("AUTH_METHOD_KEY", message)
        }
        startActivity(intent)
    }
}