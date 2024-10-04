package cyril.brian.vacationmobile.ui.vacation.infos.dialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.repositories.IUserRepository
import cyril.brian.vacationmobile.repositories.IVacationRepository
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.exception.UserNotFoundException
import cyril.brian.vacationmobile.ui.vacation.infos.UserViewData

class MemberDialogViewModel: ViewModel() {
    private var _repository: IVacationRepository? = IVacationRepository.get()
    private var _userRepo: IUserRepository? = IUserRepository.get()
    val selectedUsers: MutableList<String> = mutableListOf()

    var message = ""

    var userRepo
        get() = _userRepo!!
        set(value) {
            _userRepo = value
        }

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }

    val usersDatas: MutableLiveData<List<UserViewData>> by lazy {
        MutableLiveData<List<UserViewData>>()
    }

    var currentInput = ""

    fun searchUser(query: String): Boolean {
        return try {
            val users = userRepo.search(query)
            usersDatas.value = if(usersDatas.value == null) null else usersDatas.value!!.filter { user -> user.isSelected }
            usersDatas.postValue(
                users.map {user ->
                    UserViewData(
                        user.fullName,
                        user.fullName,
                        user.mail,
                        user.id,
                        false
                    )
                }
            )
            true
        } catch (e: StockageException) {
            Log.d("MemberDialogViewModel", e.message!!)
            false
        } catch (e: UserNotFoundException) {
            usersDatas.value = if(usersDatas.value == null) null else usersDatas.value!!.filter { user -> user.isSelected }
            message = "Not user found"
            Log.d("MemberDialogViewModel", e.message!!)
            false
        }
    }

    fun selectUser(uid: String) {
        val result = usersDatas.value!!.find {
            it.uid == uid
        }!!
        result.isSelected = !result.isSelected
        selectedUsers += uid
    }
}