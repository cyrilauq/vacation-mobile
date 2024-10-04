package cyril.brian.vacationmobile.ui.auth.register

import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class RegisterViewModelTest {
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        viewModel = RegisterViewModel()
    }

    @Test
    fun whenTheTwoPwdAreDifferentThenVMIsNotValid() {
        viewModel.setLogin("touka_ki")
        viewModel.setMail("touka_ki")
        viewModel.setName("touka_ki")
        viewModel.setPassword("touka_ki")
        viewModel.firstname = "touka_ki"
        viewModel.confirmPassword = "touka_ki2"
        assertFalse(viewModel.isValid())
    }

}