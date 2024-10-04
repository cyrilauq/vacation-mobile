package cyril.brian.vacationmobile.ui.dashboard.dialog_plannify

import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.args.PlannifyArgs
import cyril.brian.vacationmobile.ui.vacation.infos.dialog_plannify.PlannifyDialogViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PlannifyDialogViewModelTest {
    private lateinit var mockedActivitiesRepo: IVacationActivitiesRepository
    private lateinit var viewModel: PlannifyDialogViewModel

    @Before
    fun setUp() {
        val argsSlot = slot<PlannifyArgs>()
        val beginDateSlot = slot<String>()
        val endDateSlot = slot<String>()
        val beginTimeSlot = slot<String>()
        val endTimeSlot = slot<String>()

        mockedActivitiesRepo = mockk()

        every {
            mockedActivitiesRepo.plannifyActivity(
                capture(argsSlot)
            )
        } answers {
            VacationActivity(
                "kjlhkjhklh",
                "rekjgh fd gf",
                "fd ghdfkj g",
                3.0,
                3.0,
                "flmkjg ldfkjgofdmlj b"
            )
        }

        viewModel = PlannifyDialogViewModel().apply {
            repository = mockedActivitiesRepo
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun whenBeginIsGreaterThanTheEndThePlannifyFailed() {
        viewModel.endTime = "15:50"
        viewModel.endDate = "22/10/2020"
        viewModel.beginDate = "28/10/2020"
        viewModel.beginTime = "15:50"
        assertFalse(viewModel.plannify(""))
    }

    @Test
    fun whenBeginIsLessThanTheEndThePlannifySucceed() {
        viewModel.endTime = "15:50"
        viewModel.endDate = "28/10/2020"
        viewModel.beginDate = "22/10/2020"
        viewModel.beginTime = "15:50"
        assertTrue(viewModel.plannify(""))
    }

    @Test
    fun whenDateBeginIsSameThanTheDateEndButTimeBeginLessThanTimeEndThenPlannifySucceed() {
        viewModel.endTime = "15:50"
        viewModel.endDate = "22/10/2020"
        viewModel.beginDate = "22/10/2020"
        viewModel.beginTime = "5:50"
        assertTrue(viewModel.plannify(""))
    }

    @Test
    fun whenDateBeginIsSameThanTheDateEndButTimeBeginGreaterThanTimeEndThenPlannifyFailed() {
        viewModel.endTime = "1:50"
        viewModel.endDate = "22/10/2020"
        viewModel.beginDate = "22/10/2020"
        viewModel.beginTime = "15:50"
        assertFalse(viewModel.plannify(""))
    }

    @Test
    fun whenDateBeginIsSameThanTheDateEndButTimeBeginSameThanTimeEndThenPlannifyFailed() {
        viewModel.endTime = "15:50"
        viewModel.endDate = "22/10/2020"
        viewModel.beginDate = "22/10/2020"
        viewModel.beginTime = "15:50"
        assertFalse(viewModel.plannify(""))
    }
}