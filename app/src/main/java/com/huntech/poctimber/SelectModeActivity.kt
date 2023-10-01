package com.huntech.poctimber

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huntech.poctimber.databinding.ActivitySelectModeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


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

            btnTimberLogback3.setOnClickListener {
                startActivity(
                    Intent(this@SelectModeActivity, MainActivity::class.java).apply {
                        putExtra(DATA, LoggingType.TIMBER_PLUGIN)
                    }
                )
            }

            btnTimberLogback4.setOnClickListener {
                startActivity(
                    Intent(this@SelectModeActivity, MainActivity::class.java).apply {
                        putExtra(DATA, LoggingType.TIMBER_WITHOUT_EXT)
                    }
                )
            }

            btnTimberLogback5.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {

                    val dirFolder = getExternalFilesDir("DirFile")
                    val path = dirFolder?.absolutePath
                    val listFile =
                        dirFolder?.listFiles { _, name -> !name.contains("lck") && name.contains(".log") }?.toList()?.mapNotNull { it.absolutePath }.orEmpty()
                    val targetFilePath = path.orEmpty() + "/append_lck.log"
                    FileUtils.appendFile(listFile, targetFilePath)
                }
            }

            btnTimberLogback6.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {

                    val keyAlias = "my_secret_key_my_secret_key"

                    val secretKey = CipherUtils.getSecretKey(keyAlias)

                    val dirFolder = getExternalFilesDir("DirFile")
                    val path = dirFolder?.absolutePath
                    val targetFilePath = path.orEmpty() + "/append_lck.log"
                    val targetEncryptedFilePath = path.orEmpty() + "/append_encrypt_lck.log"
                    val targetDecryptedFilePath = path.orEmpty() + "/append_decrypt_lck.log"
                    secretKey?.let {
                        CipherUtils.encryptFile(File(targetFilePath), File(targetEncryptedFilePath), secretKey)
//                        CipherUtils.decryptFile(File(targetEncryptedFilePath), File(targetDecryptedFilePath), secretKey)
                    }

                }
            }



            btnTimberLogback7.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {

                    val keyAlias = "my_secret_key_my_secret_key"

                    val secretKey = CipherUtils.getSecretKey(keyAlias)

                    val dirFolder = getExternalFilesDir("DirFile")
                    val path = dirFolder?.absolutePath
                    val targetEncryptedFilePath = path.orEmpty() + "/append_encrypt_lck.log"
                    val targetDecryptedFilePath = path.orEmpty() + "/append_decrypt_lck.log"
                    secretKey?.let {
//                        CipherUtils.encryptFile(File(targetFilePath), File(targetEncryptedFilePath), secretKey)
                        CipherUtils.decryptFile(File(targetEncryptedFilePath), File(targetDecryptedFilePath), secretKey)
                    }

                }
            }
        }
    }

    companion object {
        const val DATA = "DATA"
    }
}