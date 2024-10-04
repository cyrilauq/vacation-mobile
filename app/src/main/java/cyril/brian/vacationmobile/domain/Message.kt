package cyril.brian.vacationmobile.domain

data class Message(
    val uid: String,
    val senderUserName: String,
    val content: String,
    val dateSent: String,
    val timeSent: String,
    val isFromOther: Boolean = false
)
