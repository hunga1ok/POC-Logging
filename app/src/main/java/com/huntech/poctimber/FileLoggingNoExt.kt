package com.huntech.poctimber

import android.content.Context
import android.util.Log
import fr.bipi.treessence.file.FileLoggerTree.Builder.Companion.NB_FILE_LIMIT
import fr.bipi.treessence.file.FileLoggerTree.Builder.Companion.SIZE_LIMIT
import timber.log.Timber
import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object TimberLogImplementation {

    fun init(context: Context) {
        val path: String = File(context.getExternalFilesDir("MyLog"), "file%g.log").absolutePath
        val TAG = "FileLoggerTree"
        val fileHandler: FileHandler
        val logger: Logger = MyLogger(TAG)
        logger.level = Level.ALL
        if (logger.handlers.isEmpty()) {
            fileHandler = FileHandler(path, SIZE_LIMIT, NB_FILE_LIMIT, true)
            fileHandler.formatter = SimpleFormatter()
            logger.addHandler(fileHandler)
        } else {
            fileHandler = logger.handlers[0] as FileHandler
            logger.addHandler(fileHandler)
        }
        val fileLoggerTree = FileLoggingNoExt(logger)
        Timber.plant(fileLoggerTree)
    }

}

class FileLoggingCustom private constructor(
    context: Context,
    fileDir: String,
    fileName: String,
    sizeLimit: Int,
    fileLimitCount: Int,
    isAppendable: Boolean,
    tag: String,
    levelLog: Level
): Timber.Tree() {

    private var myLogger: Logger
    init {
        val path: String = File(context.getExternalFilesDir(fileDir), fileName).absolutePath
        val fileHandler: FileHandler
        val logger: Logger = MyLogger(tag)
        logger.level = levelLog
        if (logger.handlers.isEmpty()) {
            fileHandler = FileHandler(path, sizeLimit, fileLimitCount, isAppendable)
            fileHandler.formatter = SimpleFormatter()
            logger.addHandler(fileHandler)
        } else {
            fileHandler = logger.handlers[0] as FileHandler
            logger.addHandler(fileHandler)
        }

        myLogger = logger
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        myLogger.log(fromPriorityToLevel(priority), message)
    }

    private fun fromPriorityToLevel(priority: Int): Level {
        return when (priority) {
            Log.VERBOSE -> Level.FINER
            Log.DEBUG -> Level.FINE
            Log.INFO -> Level.INFO
            Log.WARN -> Level.WARNING
            Log.ERROR -> Level.SEVERE
            Log.ASSERT -> Level.SEVERE
            else -> Level.FINEST
        }
    }
    data class Builder(
        val context: Context,
        private var fileDir: String = "LogDir",
        private var fileName: String = "File%g.log",
        private var sizeLimit: Int = 1000_000,
        private var fileLimitCount: Int = 3,
        private var isAppendable: Boolean = true,
        private var tag: String = "FileLoggerTree",
        private var levelLog: Level = Level.ALL
    ) {
        fun fileDir(dir: String) = apply { this.fileDir = dir }
        fun fileName(name: String) = apply { this.fileName = name }
        fun sizeLimit(limit: Int) = apply { this.sizeLimit = limit }
        fun fileLimit(limit: Int) = apply { this.fileLimitCount = limit }
        fun isAppendable(append: Boolean) = apply { this.isAppendable = append }
        fun tag(tag: String) = apply { this.tag = tag }
        fun levelLog(level: Level) = apply { this.levelLog = level }
        fun build() = FileLoggingCustom(context, fileDir, fileName, sizeLimit, fileLimitCount, isAppendable, tag, levelLog)
    }
}


class FileLoggingNoExt(
    private val logger: Logger
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logger.log(fromPriorityToLevel(priority), message)
    }

    private fun fromPriorityToLevel(priority: Int): Level {
        return when (priority) {
            Log.VERBOSE -> Level.FINER
            Log.DEBUG -> Level.FINE
            Log.INFO -> Level.INFO
            Log.WARN -> Level.WARNING
            Log.ERROR -> Level.SEVERE
            Log.ASSERT -> Level.SEVERE
            else -> Level.FINEST
        }
    }
}

class MyLogger(name: String) : Logger(name, null)