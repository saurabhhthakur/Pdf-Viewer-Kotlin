package com.self.project.pdfviewer

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
class Model(var fileName: String, var filePath: String, fileSize: Long, time: Long) {
    var fileSize: String
    var lastModified : String

    init {
        val df = DecimalFormat("0.00")
        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTerra = sizeGb * sizeKb
        this.fileSize = if (fileSize < sizeKb) {
            df.format(fileSize) + " B"
        } else if (fileSize < sizeMb)
            df.format((fileSize / sizeKb).toDouble()) + " KB"
        else if (fileSize < sizeGb)
            df.format((fileSize / sizeMb).toDouble()) + " MB"
        else if (fileSize < sizeTerra)
            df.format((fileSize / sizeGb).toDouble()) + " GB"
        else
            "0B"
    }

    init {
            val date = Date(time)
            val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
            lastModified = formatter.format(date)
    }


}