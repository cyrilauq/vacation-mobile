package cyril.brian.vacationmobile.repositories

import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.repositories.args.PlannifyArgs
import cyril.brian.vacationmobile.repositories.exception.ActivityNotFoundException
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.VacationNotFoundException

interface IVacationActivitiesRepository {
    companion object {
        private var _repository: IVacationActivitiesRepository? = null

        fun get() = _repository

        fun initialize(repository: IVacationActivitiesRepository) {
            _repository = repository
        }
    }

    /**
     * Add on or more activities to the given vacation
     *
     * @param vacationId        Id of the vacation
     * @param activities        The activities to add
     *
     * @return  A list with the added vacations.
     *
     * @throws AuthException
     * @throws VacationNotFoundException
     */
    @Throws(VacationNotFoundException::class, AuthException::class)
    fun addActivitiesToVacation(
        vacationId: String,
        activities: Array<VacationActivity>
    ): List<VacationActivity>

    /**
     * Add on or more activities to the given vacation
     *
     * @param args      Args to plannify an activity
     *
     * @return          The plannified activity.
     *
     * @throws AuthException
     */
    @Throws(AuthException::class, ActivityNotFoundException::class)
    fun plannifyActivity(args: PlannifyArgs): VacationActivity?

    @Throws(VacationNotFoundException::class, AuthException::class)
    fun getActivities(vacationId: String): List<VacationActivity>

}