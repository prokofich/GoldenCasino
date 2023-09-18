package com.example.goldencasino

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.onesignal.OneSignal

class OneSignallApplication:Application() {

    val ONESIGNAL_APP_ID = "bf49dff0-0415-4825-9e7a-4c0f5076cac1"

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

}