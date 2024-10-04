package cyril.brian.vacationmobile.ui.vacation

import cyril.brian.vacationmobile.domain.Coordinate
import cyril.brian.vacationmobile.domain.PeriodTime
import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.domain.Vacation
import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationUserResponse
import cyril.brian.vacationmobile.repositories.IVacationRepository

class FakeVacationRepo: IVacationRepository {
    override fun getVacationsUser(token: String): GetVacationUserResponse {
        TODO("Not yet implemented")
    }

    override fun newVacation(
        title: String,
        place: String,
        country: String,
        description: String,
        begin: PeriodTime?,
        end: PeriodTime?,
        coordinate: Coordinate
    ): Vacation? {
        TODO("Not yet implemented")
    }

    override fun getVacationById(id: String): Vacation = Vacation()

    override fun addMembers(vacationId: String, members: List<String>): List<User> {
        TODO("Not yet implemented")
    }

    override fun setCurrentVacation(vacationId: String) {
        TODO("Not yet implemented")
    }

    override fun getCurrentVacation(): String {
        TODO("Not yet implemented")
    }

    override fun addActivities(
        vacationId: String,
        activities: Array<VacationActivity>
    ): Iterable<VacationActivity> {
        TODO("Not yet implemented")
    }

    override fun getMembers(vacationId: String): List<User> {
        TODO("Not yet implemented")
    }

    override fun downloadPlanning(vacationId: String): String {
        TODO("Not yet implemented")
    }
}