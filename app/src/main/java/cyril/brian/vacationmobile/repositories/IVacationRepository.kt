package cyril.brian.vacationmobile.repositories

import cyril.brian.vacationmobile.domain.Coordinate
import cyril.brian.vacationmobile.domain.PeriodTime
import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.domain.Vacation
import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationUserResponse
import cyril.brian.vacationmobile.repositories.exception.ActivityFormatException
import cyril.brian.vacationmobile.repositories.exception.AddVacationException
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.exception.VacationNotFoundException
import cyril.brian.vacationmobile.repositories.exception.VacationPublishedException

/**
 * This interface is in charge of telling what we have to do with the data source for the vacations.
 *
 * This interface also has a companion object to give the same instance of one repository all over the application.
 */
interface IVacationRepository {

    /**
     * We have a companion object, so we can get the same repository everywhere in the application.
     */
    companion object {
        private var _repository: IVacationRepository? = null

        fun get() = _repository

        fun initialize(repository: IVacationRepository) {
            _repository = repository
        }
    }

    /**
     * Retrieve the vacations of the connected user.
     */
    fun getVacationsUser(token: String): GetVacationUserResponse

    /**
     * Create a new vacation for the connected user, with the given informations.
     *
     * @param title         Title of the vacation
     * @param place         Where the vacation will be
     * @param begin         When the vacation begins
     * @param end           When the vacation ends
     * @param coordinate    The geographics coordinates of where the vacation is
     *
     * @throws AddVacationException     Throws AddVacationException when the vacation isn't added successfully.
     */
    @Throws(AddVacationException::class)
    fun newVacation(title: String, place: String, country: String, description: String, begin: PeriodTime?, end: PeriodTime?, coordinate: Coordinate): Vacation?

    /**
     * Retrieve the vacation with the given id.
     *
     * @param id    Id of the wanted vacation
     *
     * @return à vacation
     *
     * @throws VacationNotFoundException    Throws VacationNotFoundException when there is no vacation for the given id.
     */
    @Throws(VacationNotFoundException::class)
    fun getVacationById(id: String): Vacation

    /**
     *
     * @throws VacationNotFoundException    Throws VacationNotFoundException when there is no vacation for the given id.
     * @throws AuthException                Throws AuthException when the user is not authorized to perform the action.
     * @throws VacationPublishedException   Throws VacationPublishedException when the vacation is published.
     */
    @Throws(VacationNotFoundException::class, AuthException::class, VacationPublishedException::class)
    fun addMembers(vacationId: String, members: List<String>): List<User>

    /**
     * Let us memorize the current vacation that is seen by the user.
     *
     * @param vacationId    Id the the vacation the user want to see
     */
    fun setCurrentVacation(vacationId: String)

    /**
     * Return the vacation the user want to see at this moment.
     */
    fun getCurrentVacation(): String

    /**
     * Add multiple activities to the given vacation.
     *
     * @param vacationId    Id of the vacation
     * @param activities    Array of activities
     *
     * @throws VacationNotFoundException    Throws VacationNotFoundException when there is no vacation for the given id.
     * @throws AuthException                Throws AuthException when the user is not authorized to perform the action.
     * @throws VacationPublishedException   Throws VacationPublishedException when the vacation is published.
     * @throws ActivityFormatException      Throws ActivityFormatException when one or more activity has wrong information(s).
     * @throws StockageException            Throws StockageException when there is an issue with the storage system.
     */
    @Throws(VacationNotFoundException::class, StockageException::class, AuthException::class, VacationPublishedException::class, ActivityFormatException::class)
    fun addActivities(vacationId: String, activities: Array<VacationActivity>): Iterable<VacationActivity>

    /**
     * Get all the members of a specific vacation (owner included)
     */
    @Throws(VacationNotFoundException::class, AuthException::class, VacationPublishedException::class)
    fun getMembers(vacationId: String): List<User>

    /**
     * Will download the planning of the given vacation on the device and return the path to its location.
     *
     * @param vacationId    Id of the vacation wich the user want the planning
     */
    fun downloadPlanning(vacationId: String): String
}