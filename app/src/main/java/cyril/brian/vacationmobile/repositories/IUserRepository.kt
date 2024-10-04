package cyril.brian.vacationmobile.repositories

import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.infrastructure.dto.InvitationsDTO
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import cyril.brian.vacationmobile.infrastructure.responses.UserRegisterResponse
import cyril.brian.vacationmobile.repositories.args.RegisterArgs
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.exception.UserNotFoundException

interface IUserRepository {
    companion object {
        private var _repository: IUserRepository? = null

        fun get() = _repository

        fun initialize(repository: IUserRepository) {
            _repository = repository
        }
    }

    @Throws(AuthException::class)
    fun login(login: String, password: String): UserLoginResponse

    @Throws(AuthException::class)
    fun register(args: RegisterArgs): UserRegisterResponse

    /**
     * Retrieve a collection of user that match the given search query.
     *
     * @param query     Search query
     *
     * @throws StockageException        Throws StockageException when there is an issue while communicating with the storage system.
     * @throws UserNotFoundException    Throws UserNotFoundException when no user was found by the request.
     */
    @Throws(StockageException::class, UserNotFoundException::class)
    fun search(query: String): List<User>

    fun getInvitations(): List<InvitationsDTO>

    fun acceptInvitations(vacationId: String, invitationId: String): Boolean
}