package com.self.project.pdfviewer.adapter

import android.view.View

interface ManageListener {
    fun click(position:Int)
    fun longClick(position: Int,it: View)

}