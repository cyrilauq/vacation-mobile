package cyril.brian.vacationmobile.ui.vacation.infos.dialog_add_activity

import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.repositories.IGeocodingRepository
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.exception.StockageException
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddActivityDialogViewModelTest {
    private lateinit var mockedRepo: IGeocodingRepository
    private lateinit var mockedActivitiesRepo: IVacationActivitiesRepository
    private lateinit var viewModel: AddActivityDialogViewModel

    @Before
    fun setUp() {
        val activityArraySlot = slot<Array<VacationActivity>>()
        val vacationIdSlot = slot<String>()

        mockedRepo = mockk()
        mockedActivitiesRepo = mockk()

        every { mockedRepo.findLocationFor("   fvgfdg      ") } answers {
            null
        }

        every { mockedRepo.findLocationFor("") } answers {
            null
        }

        every { mockedRepo.findLocationFor("stockage_error") } throws StockageException("stockage_error")

        every {
            mockedActivitiesRepo.addActivitiesToVacation(
                capture(vacationIdSlot),
                capture(activityArraySlot)
            )
        } answers {
            listOf(
                VacationActivity(
                    "kjlhkjhklh",
                    "rekjgh fd gf",
                    "fd ghdfkj g",
                    3.0,
                    3.0,
                    "flmkjg ldfkjgofdmlj b"
                )
            )
        }

        viewModel = AddActivityDialogViewModel().apply {
            geoRepository = mockedRepo
            activitiesRepo = mockedActivitiesRepo
        }

        viewModel.lat = 3.0
        viewModel.lon = 3.0
        viewModel.title = "i am here"
        viewModel.description = "Fucking description you shit"
        viewModel.place = "ljljlkj"
    }

    @After
    fun tearDown() {
    }

    @Test
    fun whenAddressIsEmptyThenNoCalledOfTheRepo() {
        viewModel.getCoordinatesFor("")

        verify(exactly = 0) { mockedRepo.findLocationFor("") }
    }

    @Test
    fun whenAddressLengthIsLessThan10CharactersThenNoCalledOfTheRepo() {
        viewModel.getCoordinatesFor("   fvgfdg      ")

        verify(exactly = 0) { mockedRepo.findLocationFor("   fvgfdg      ") }
    }

    @Test
    fun whenStockageExceptionOccursThenErrosMessageNotEmpty() {
        viewModel.getCoordinatesFor("stockage_error")

        verify(exactly = 1) { mockedRepo.findLocationFor("stockage_error") }
        assert(viewModel.error.isNotEmpty())
    }

    @Test
    fun test() {
        assertTrue(viewModel.save())
    }
}