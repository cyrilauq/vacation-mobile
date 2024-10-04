package cyril.brian.vacationmobile.repositories

import cyril.brian.vacationmobile.domain.Message
import cyril.brian.vacationmobile.repositories.exception.StockageException

/**
 * This interface declare the methods that will be usefull to use for the tchat funcitonnality.
 */
interface ITchatRepository {
    companion object {
        private var _repository: ITchatRepository? = null

        fun get() = _repository

        fun initialize(repository: ITchatRepository) {
            _repository = repository
        }
    }

    fun getUserName(): String

    /**
     * Get and return the messages for a given vacation.
     *
     * @param vacationId    Id of the vacation
     *
     * @return              A list that contains all of the messages
     */
    @Throws(StockageException::class)
    suspend fun getMessages(vacationId: String): List<Message>

    /**
     * Send the given message for the given vacation.
     *
     * @param vacationId    Id of the vacation
     * @param message       Content of the message to send
     *
     * @return              True if the message is successfully sent
     *                      False otherwise.
     */
    @Throws(StockageException::class)
    suspend fun sendMessage(vacationId: String, message: String): Boolean

}