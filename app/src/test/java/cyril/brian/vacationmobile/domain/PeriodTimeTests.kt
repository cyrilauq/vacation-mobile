package cyril.brian.vacationmobile.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PeriodTimeTests {
    private lateinit var period: PeriodTime

    @Before
    fun setUp() {
        period = PeriodTime("15:30", "28/12/2023")
    }

    @Test
    fun konwsItsTime() {
        assertEquals("15:30", period.time)
    }

    @Test
    fun konwsItsDate() {
        assertEquals("28/12/2023", period.date)
    }
}