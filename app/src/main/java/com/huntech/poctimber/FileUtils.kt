package com.huntech.poctimber

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.io.OutputStreamWriter

object FileUtils {
    fun appendFile(fileNames: List<String>, targetFileName: String) {
        // List of file names you want to append

        // Name of the target file where you want to append the contents
        try {
            // Create a target file with append mode
            val targetFile = File(targetFileName)
            if (targetFile.exists()) {
                targetFile.delete()
            }
            val fos = FileOutputStream(targetFileName, true)
            val bw = BufferedWriter(OutputStreamWriter(fos))
            bw.flush()

            for (fileName in fileNames) {
                // Read the contents of each file
                val br = BufferedReader(FileReader(fileName))
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    // Append the content to the target file
                    bw.write(line)
                    bw.newLine() // Add a new line for each file
                }
                br.close()
            }

            // Close the target file
            bw.close()
            fos.close()

            println("Files appended successfully.")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}