package cyril.brian.vacationmobile.ui.app

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.dashboard.DashBoardFragment
import cyril.brian.vacationmobile.ui.invitations.InvitationsFragment
import cyril.brian.vacationmobile.ui.vacation.create.CreateVacationFragment
import cyril.brian.vacationmobile.ui.vacation.infos.VacationFragment

class AppActivity : AppCompatActivity() {
    companion object {
        private const val WANTED_SCREEN_KEY = "WANTED_SCREEN_KEY"
        private const val WANTED_SCREEN_FIELDS = "WANTED_SCREEN_FIELDS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        if (savedInstanceState == null) {
            val screenName = intent.getStringExtra(WANTED_SCREEN_KEY)!!
            val fields = intent.getStringExtra(WANTED_SCREEN_FIELDS) ?: ""
            displayScreen(screenName, fields)
        }
    }

    override fun onResume() {
        super.onResume()
        val screenName = intent.getStringExtra(WANTED_SCREEN_KEY)!!
        val fields = intent.getStringExtra(WANTED_SCREEN_FIELDS) ?: ""
        displayScreen(screenName, fields)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val screenName = intent.getStringExtra(WANTED_SCREEN_KEY)!!
        val fields = intent.getStringExtra(WANTED_SCREEN_FIELDS) ?: ""
        displayScreen(screenName, fields)
    }

    /**
     * Display a given screen
     * If the screen is not know then display dashboard.
     */
    private fun displayScreen(screenName: String, vararg fields: String) {
        lateinit var screen: Fragment
        intent.putExtra(WANTED_SCREEN_KEY, screenName)
        intent.putExtra(WANTED_SCREEN_FIELDS, if(fields.isEmpty()) "" else fields[0])
        when (screenName) {
            "create_vacation" -> {
                screen = CreateVacationFragment.newInstance { displayScreen("dashboard") }
            }
            "see_vacation" -> {
                screen = VacationFragment.newInstance(fields[0]) { displayScreen("dashboard", fields[0]) }
            }
            "invitations" -> {
                screen = InvitationsFragment.newInstance()
            }
            else -> {
                screen = DashBoardFragment.newInstance(
                    { screenN, args ->
                        displayScreen(screenN, args)
                    },
                    { displayScreen("create_vacation") }
                )
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_fragment_container_view, screen)
            .addToBackStack(screenName)
            .commit()
    }
}