package com.huntech.poctimber

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huntech.poctimber.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when(intent.getSerializableExtra(SelectModeActivity.DATA) as LoggingType) {
            LoggingType.TIMBER_LOGBACK -> {
                Timber.plant(Timber.DebugTree())
                Timber.plant(FileLoggingTree())
            }

            LoggingType.TIMBER_LOGBACK2 -> {
                Timber.plant(Timber.DebugTree())
                Timber.plant(FileLoggingTree2(this))
            }
            else -> {}
        }

        with(binding){
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