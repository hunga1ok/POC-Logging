package com.huntech.poctimber

import android.app.Application
import android.util.Log
import fr.bipi.treessence.dsl.TimberApplication.textViewTree
import timber.log.Timber

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.uprootAll()
    }
}