package com.tonyyang.common.itemselector

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * @author tonyyang
 */
class CoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}