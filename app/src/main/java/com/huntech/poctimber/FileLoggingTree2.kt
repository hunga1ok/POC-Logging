package com.huntech.poctimber

import android.content.Context
import android.util.Log
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.html.HTMLLayout
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.LoggerFactory
import timber.log.Timber


class FileLoggingTree2(context: Context) : Timber.DebugTree() {
    init {
        val logDirectory: String = context.filesDir.path + "/logs"
        configureLogger(logDirectory)
    }

    private fun configureLogger(logDirectory: String) {
        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.reset()
        val rollingFileAppender = RollingFileAppender<ILoggingEvent>()
        rollingFileAppender.context = loggerContext
        rollingFileAppender.isAppend = true
        rollingFileAppender.file = "$logDirectory/$LOG_PREFIX-latest.html"
        val fileNamingPolicy = SizeAndTimeBasedFNATP<ILoggingEvent>()
        fileNamingPolicy.context = loggerContext
        fileNamingPolicy.setMaxFileSize(FileSize.valueOf("5MB"))
        val rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>()
        rollingPolicy.context = loggerContext
        rollingPolicy.fileNamePattern = "$logDirectory/$LOG_PREFIX.%d{yyyy-MM-dd}.%i.html"
        rollingPolicy.maxHistory = 5
        rollingPolicy.timeBasedFileNamingAndTriggeringPolicy = fileNamingPolicy
        rollingPolicy.setParent(rollingFileAppender) // parent and context required!
        rollingPolicy.start()

        val htmlLayout = HTMLLayout()
        htmlLayout.context = loggerContext
        htmlLayout.pattern = "%d{HH:mm:ss.SSS}%level%thread%msg"
        htmlLayout.start()
        val encoder = LayoutWrappingEncoder<ILoggingEvent>()
        encoder.context = loggerContext
        encoder.layout = htmlLayout
        encoder.start()

        // Alternative text encoder - very clean pattern, takes up less space
//        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
//        encoder.setContext(loggerContext);
//        encoder.setCharset(Charset.forName("UTF-8"));
//        encoder.setPattern("%date %level [%thread] %msg%n");
//        encoder.start();
        rollingFileAppender.rollingPolicy = rollingPolicy
        rollingFileAppender.encoder = encoder
        rollingFileAppender.start()

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger
        val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        root.level = Level.DEBUG
        root.addAppender(rollingFileAppender)

        // print any status messages (warnings, etc) encountered in logback config
        StatusPrinter.print(loggerContext)
    }

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
        private val mLogger: org.slf4j.Logger =
            LoggerFactory.getLogger(FileLoggingTree2::class.java)
        private const val LOG_PREFIX = "my-log"
    }
}