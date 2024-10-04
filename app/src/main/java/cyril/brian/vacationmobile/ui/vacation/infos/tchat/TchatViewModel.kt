package cyril.brian.vacationmobile.ui.vacation.infos.tchat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import cyril.brian.vacationmobile.infrastructure.dto.MessageDTO
import cyril.brian.vacationmobile.repositories.ITchatRepository
import cyril.brian.vacationmobile.repositories.exception.StockageException

class TchatViewModel : ViewModel() {
    private val _messageRepo = ITchatRepository.get()
    val repository get() = _messageRepo!!

    val message: MutableLiveData<MutableList<MessageInfos>> by lazy {
        MutableLiveData<MutableList<MessageInfos>>()
    }

    private lateinit var pusher: Pusher

    lateinit var vacationId: String

    var error: String = ""

    suspend fun loadMessages(vacationId: String): Boolean {
        return try {
            message.postValue(repository.getMessages(vacationId).map {
                MessageInfos(
                    it.senderUserName,
                    it.content,
                    it.dateSent + " at " + it.timeSent,
                    it.isFromOther
                )
            }.toMutableList())
            true
        } catch (e: StockageException) {
            error = e.message!!
            false
        }
    }

    suspend fun sendMessage(content: String, vacationId: String): Boolean {
        return try {
            repository.sendMessage(vacationId, content)
            true
        } catch (e: StockageException) {
            error = e.message!!
            false
        }
    }

    fun init(vacationId: String) {
        val options = PusherOptions()
        options.setCluster("eu")

        pusher = Pusher("5c748234e45bc3b0dc54", options)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
            }

            override fun onError(
                message: String,
                code: String,
                e: Exception
            ) {
                Log.i("Pusher", "There was a problem connecting! code ($code), message ($message), exception($e)")
            }
        }, ConnectionState.ALL)

        val channel = pusher.subscribe(vacationId)
        channel.bind("send-message") { event ->
            val deserialized = Gson().fromJson(event.data, MessageDTO::class.java)
            val temp = message.value!!

            temp += MessageInfos(
                deserialized.username!!,
                deserialized.message!!,
                "%s at %s".format(deserialized.sendDateTime!!.substring(0, deserialized.sendDateTime.indexOf("T")), deserialized.sendDateTime.substring(deserialized.sendDateTime.indexOf("T")+1, deserialized.sendDateTime.indexOf(".")-3)),
                deserialized.username != repository.getUserName()
            )
            message.postValue(temp)
            Log.i("Pusher","Received event with data: $event")
        }
    }

    fun onDestroy() {
        pusher.unsubscribe(vacationId)
    }
}