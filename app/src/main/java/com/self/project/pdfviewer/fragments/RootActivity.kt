package com.self.project.pdfviewer.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.self.project.pdfviewer.R

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        supportFragmentManager.beginTransaction().add(R.id.container, SplashFragment()).commit()
    }
}