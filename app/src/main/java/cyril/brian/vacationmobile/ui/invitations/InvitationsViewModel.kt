package cyril.brian.vacationmobile.ui.invitations

import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.repositories.IUserRepository
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.ui.invitations.adapter.InvitationModel

class InvitationsViewModel : ViewModel() {
    private var invitationsList: List<InvitationModel> = listOf()
    var _repository: IUserRepository? = IUserRepository.get()

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }

    private var error: String = ""

    fun getInvitations(): List<InvitationModel> {
        return invitationsList
    }

    fun acceptInvitationAt(position: Int): Boolean {
        val invit = invitationsList[position]
        try {
            repository.acceptInvitations(invit.vacationId, invit.invitationId)
        }  catch(e: AuthException) {
            error = e.message.toString()
        } catch(e: StockageException) {
            error = e.message.toString()
        }
        return true
    }

    fun gatherUserInvitations(): Boolean {
        error = ""
        return try {
            invitationsList =
                repository.getInvitations().map {
                    InvitationModel(
                        it.invitationId!!,
                        it.vacationName!!,
                        it.vacationId!!,
                        it.isAccepted!!
                    )
                }
            true
        } catch(e: AuthException) {
            error = e.message.toString()
            false
        } catch(e: StockageException) {
            error = e.message.toString()
            false
        }
    }

    fun getError(): String {
        return error
    }
}