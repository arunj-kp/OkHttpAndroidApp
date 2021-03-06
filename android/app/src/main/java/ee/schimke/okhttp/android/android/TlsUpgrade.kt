package ee.schimke.okhttp.android.android

import android.content.Context
import android.util.Log
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import okhttp3.internal.platform.Platform
import org.conscrypt.Conscrypt
import java.security.Security


fun initConscrypt(): Boolean {
    try {
        Class.forName("org.conscrypt.OpenSSLProvider")

        if (Conscrypt.isAvailable()) {
            Security.insertProviderAt(
                    Conscrypt.newProviderBuilder().provideTrustManager(true).build(), 1)

            Log.i("AndroidNetworkManager", "Initialised Conscrypt " + Platform.get())

            return true
        } else {
            Log.w("AndroidNetworkManager", "Initialising Conscrypt failed")
        }
    } catch (e: Exception) {
        Log.w("AndroidNetworkManager", "Conscrypt not available", e)
    }
    return false
}

fun initGms(context: Context): Boolean {
    try {
        Log.w("AndroidNetworkManager", "Initialising GMS")
        ProviderInstaller.installIfNeeded(context)
        return true
    } catch (e: GooglePlayServicesRepairableException) {
        Log.w("AndroidNetworkManager", "Google Play Services repair", e)

        GoogleApiAvailability.getInstance().showErrorNotification(context, e.connectionStatusCode)
    } catch (e: GooglePlayServicesNotAvailableException) {
        Log.w("AndroidNetworkManager", "Google Play Services not available", e)
        // ignore
    }
    return false
}