package com.huntech.poctimber

import android.util.Log
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import timber.log.Timber

class FileLoggingTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE) {
            return
        }
        val logMessage = "$tag: $message"
        when (priority) {
            Log.DEBUG -> mLogger.debug(logMessage)
            Log.INFO -> mLogger.info(logMessage)
            Log.WARN -> mLogger.warn(logMessage)
            Log.ERROR -> mLogger.error(logMessage)
        }
    }

    companion object {
        var mLogger: Logger = LoggerFactory.getLogger(FileLoggingTree::class.java)
    }
}