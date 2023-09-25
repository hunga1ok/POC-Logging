package com.huntech.poctimber

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huntech.poctimber.databinding.ActivityMainBinding
import fr.bipi.treessence.file.FileLoggerTree
import timber.log.Timber
import timber.log.Timber.*
import java.util.logging.Level


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when (intent.getSerializableExtra(SelectModeActivity.DATA) as LoggingType) {
            LoggingType.TIMBER_LOGBACK -> {
                Timber.plant(DebugTree())
                Timber.plant(FileLoggingTree())
            }

            LoggingType.TIMBER_LOGBACK2 -> {
                Timber.plant(DebugTree())
                Timber.plant(FileLoggingTree2(this))
            }

            LoggingType.TIMBER_PLUGIN -> {
                val t: Tree = FileLoggerTree.Builder()
                    .withFileName("file%g.log")
                    .withDirName(this.getExternalFilesDir("timberExtensionLog")?.path.orEmpty())
                    .withSizeLimit(20_000_000)
                    .withFileLimit(3)
                    .withMinPriority(Log.DEBUG)
                    .appendToFile(true)
                    .build()
                Timber.plant(t)
            }

            LoggingType.TIMBER_WITHOUT_EXT -> {
                val t: Tree = FileLoggingCustom.Builder(this)
                    .fileDir("DirFile")
                    .fileName("file%g.log")
                    .tag("CustomTag")
                    .sizeLimit(1000_000_000) // ~1MB
                    .fileLimit(3) // create limit 3 file
                    .levelLog(Level.ALL) // receive all log
                    .isAppendable(true) // true: append log to file, false: create new file
                    .build()
                Timber.plant(t)

            }

            else -> {}
        }

        with(binding) {
            button.setOnClickListener {
                textView.append(" ${System.currentTimeMillis()}")
                Timber.d(textView.text.toString())
            }
        }
        Timber.d("onCreate")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        Timber.uprootAll()
    }

}