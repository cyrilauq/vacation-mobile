package cyril.brian.vacationmobile.infrastructure

import android.security.NetworkSecurityPolicy
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@Implements(NetworkSecurityPolicy::class)
class NetworkSecurityPolicyShadow {
    @Implementation
    fun isCleartextTrafficPermitted(host: String): Boolean {
        return true
    }
    companion object {
        @JvmStatic
        val instance: NetworkSecurityPolicy
            @Implementation
            get() {
                try {
                    val shadow = Class.forName("android.security.NetworkSecurityPolicy")
                    return shadow.newInstance() as NetworkSecurityPolicy
                } catch (e: Exception) {
                    throw AssertionError()
                }
            }
    }
}