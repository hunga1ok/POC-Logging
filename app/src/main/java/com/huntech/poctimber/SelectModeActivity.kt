package com.huntech.poctimber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.huntech.poctimber.databinding.ActivitySelectModeBinding

class SelectModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            btnTimberLogback.setOnClickListener {
                startActivity(
                    Intent(this@SelectModeActivity, MainActivity::class.java).apply {
                        putExtra(DATA, LoggingType.TIMBER_LOGBACK)
                    }
                )
            }

            btnTimberLogback2.setOnClickListener {
                startActivity(
                    Intent(this@SelectModeActivity, MainActivity::class.java).apply {
                        putExtra(DATA, LoggingType.TIMBER_LOGBACK2)
                    }
                )
            }
        }
    }

    companion object {
        const val DATA = "DATA"
    }
}