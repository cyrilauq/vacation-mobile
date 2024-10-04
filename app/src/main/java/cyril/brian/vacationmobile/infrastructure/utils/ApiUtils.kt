package cyril.brian.vacationmobile.infrastructure.utils

import com.google.gson.JsonParseException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

@Throws(StockageException::class)
fun tryCatch(block: () -> Unit) {
    try {
        block()
    } catch (e: SocketTimeoutException) {
        throw StockageException("No response found")
    } catch (e: SSLHandshakeException) {
        throw StockageException("Maybe you forgot to open your vpn")
    } catch (e: JsonParseException) {
        throw StockageException("Error in the received data")
    } catch (e: IOException) {
        throw StockageException("The connection with the server is not possible")
    }
}