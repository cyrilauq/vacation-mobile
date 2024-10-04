package cyril.brian.vacationmobile.ui.vacation.create

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import cyril.brian.vacationmobile.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
//class CreateVacationInstrumentedTests {
//
//    @Test
//    fun thingAndOthers() {
//        val scenario = launchFragmentInContainer<CreateVacationFragment>()
//        onView(withId(R.id.begin_date_picker)).perform(PickerActions.setDate(2025, 5, 15))
//        onView(withId(R.id.vacation_confirm_button)).perform(click())
//        scenario.onFragment {
//            assertEquals(it.viewModel.date_begin, "15/05/2025")
//        }
//    }
//
//}