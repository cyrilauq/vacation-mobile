package cyril.brian.vacationmobile

import android.content.SharedPreferences

interface PreferencesGiver {

    fun getSharedPreferences(): SharedPreferences

}