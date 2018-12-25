package com.tonyyang.common.itemselector

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

/**
 * @author tonyyang
 */
class CoreApplication : Application() {

    companion object {

        private lateinit var mInstance: CoreApplication

        fun getContext(): Context {
            return mInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        Stetho.initializeWithDefaults(this)
    }
}