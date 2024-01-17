package com.self.project.pdfviewer.repository

import android.os.Environment
import com.self.project.pdfviewer.model.Model
import java.io.File

object Repository {
    private lateinit var data: MutableList<Model>
    private val directory = File(Environment.getExternalStorageDirectory().absolutePath)

    fun getSortedData(): List<Model> {
        data = mutableListOf()
        dataFiles(directory, data)
        return sortByName(data)
    }

    private fun dataFiles(directory: File, pdfFiles: MutableList<Model>) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    dataFiles(file, pdfFiles)
                } else if (file.isFile && file.name.endsWith(".pdf")) {
                    pdfFiles.add(Model(file.name, file.path, file.length(), file.lastModified()))
                }
            }
        }
    }

    private fun sortByName(pdfFiles: List<Model>): List<Model> {
        val sort = pdfFiles.sortedWith(compareBy {
            it.fileName
        })
        return sort
    }

}