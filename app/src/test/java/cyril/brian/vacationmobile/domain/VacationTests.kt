package cyril.brian.vacationmobile.domain

import cyril.brian.vacationmobile.domain.exception.CreateVacationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class VacationTests {
    private lateinit var vacation: Vacation
    @Before
    fun setUp() {
        vacation = Vacation.newVacation(
            "Test",
            "Test to place of Testing",
            "description",
            PeriodTime("15:50", "12/12/2020"),
            PeriodTime("15:50", "12/12/2020"),
            Coordinate(1.0, 1.0)
        )!!
    }

    @Test(expected = CreateVacationException::class)
    fun titleCannotBeEmpty() {
        Vacation.newVacation(
            "",
            "Test to place of Testing",
            "description",
            PeriodTime("15:50", "12/12/2020"),
            PeriodTime("15:50", "12/12/2020"),
            Coordinate(1.0, 1.0)
        )!!
    }

    @Test(expected = CreateVacationException::class)
    fun placeCannotBeEmpty() {
        Vacation.newVacation(
            "Test",
            "",
            "description",
            PeriodTime("15:50", "12/12/2020"),
            PeriodTime("15:50", "12/12/2020"),
            Coordinate(1.0, 1.0)
        )!!
    }

    @Test
    fun knowItsTitle() {
        assertEquals("Test", vacation.title)
    }

    @Test
    fun knowItsPlace() {
        assertEquals("Test to place of Testing", vacation.place)
    }

    @Test
    fun knowItsBegin() {
        assertEquals(PeriodTime("15:50", "12/12/2020"), vacation.begin)
    }

    @Test
    fun knowItsEnd() {
        assertEquals(PeriodTime("15:50", "12/12/2020"), vacation.end)
    }
}