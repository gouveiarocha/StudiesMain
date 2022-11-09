package com.gouveia.studiesmain.utils.others

import android.util.Log
import timber.log.Timber

// import com.microsoft.appcenter.crashes.Crashes

// TIMBER CUSTOMIZADO
class CustomLogger : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.ERROR && throwable != null) {
            // Logue o que quiser com lib de Microsoft, AppCenter, etc...
            Timber.e(throwable)
            // Crashes.trackError(throwable) //Ex. usando a lib da Microsoft.
        }
    }
}