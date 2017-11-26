package com.peopleflow.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.peopleflow.app.data.AppComponent

class App: Application() {
    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initStetho()
        initComponent()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initComponent() {
        appComponent = AppComponent.create(this)
    }
}