package com.huntech.poctimber

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.*
import java.security.*
import javax.crypto.*
import javax.crypto.spec.*

object CipherUtils {


    fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128) // You can change the key size as needed
        return keyGenerator.generateKey()
    }

    fun encryptFile(inputFile: File, outputFile: File, secretKey: SecretKey) {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val ivSpec =
                IvParameterSpec(ByteArray(16)) // You should generate a random IV for production use

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

            val fis = FileInputStream(inputFile)
            val fos = FileOutputStream(outputFile)
            val cos = CipherOutputStream(fos, cipher)

            val buffer = ByteArray(1024)
            var bytesRead: Int

            while (fis.read(buffer).also { bytesRead = it } != -1) {
                cos.write(buffer, 0, bytesRead)
            }

            cos.close()
            fos.close()
            fis.close()

            println("Encryption completed.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decryptFile(inputFile: File, outputFile: File, secretKey: SecretKey) {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val ivSpec =
                IvParameterSpec(ByteArray(16)) // You should use the same IV as used for encryption

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

            val fis = FileInputStream(inputFile)
            val fos = FileOutputStream(outputFile)
            val cis = CipherInputStream(fis, cipher)

            val buffer = ByteArray(1024)
            var bytesRead: Int

            while (cis.read(buffer).also { bytesRead = it } != -1) {
                fos.write(buffer, 0, bytesRead)
            }

            fos.close()
            cis.close()
            fis.close()

            println("Decryption completed.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun main() {
        val inputFile = File("input.txt")
        val encryptedFile = File("encrypted.txt")
        val decryptedFile = File("decrypted.txt")

        val secretKey = generateSecretKey()

        // Encryption
        encryptFile(inputFile, encryptedFile, secretKey)

        // Decryption
        decryptFile(encryptedFile, decryptedFile, secretKey)
    }


    fun generateAndStoreSecretKey(keyAlias: String): SecretKey? {
        try {
            // Create an instance of KeyGenerator for the specified key type (e.g., AES)
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            // Initialize the KeyGenParameterSpec for the key
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC) // Specify the block mode
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7) // Specify padding scheme
                .setUserAuthenticationRequired(false) // Set to true for requiring user authentication (e.g., fingerprint)
                .build()

            // Initialize the KeyGenerator with the KeyGenParameterSpec
            keyGenerator.init(keyGenParameterSpec)

            // Generate the secret key and store it securely in the Android Keystore
            return keyGenerator.generateKey()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getSecretKeyFromStorage(keyAlias: String): SecretKey? = try {
        // Load the Android Keystore instance
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        // Get the secret key by its alias
        keyStore.getKey(keyAlias, null) as? SecretKey
    } catch (e: Exception) {
        e.printStackTrace()
        null

    }

    fun getSecretKey(keyAlias: String): SecretKey? = try {
        val keyBytes = keyAlias.toByteArray(Charsets.UTF_8) // Replace with your actual key bytes
        val paddedArray = keyBytes.copyOf(newSize = 16)
        SecretKeySpec(paddedArray, KeyProperties.KEY_ALGORITHM_AES)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}