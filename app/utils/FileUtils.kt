package com.example.mynilu.utils


import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {
    fun exportData(context: Context, data: String, fileName: String) {
        val file = File(context.filesDir, fileName)
        try {
            FileOutputStream(file).use { output ->
                output.write(data.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun importData(context: Context, fileName: String): String? {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }
}