package com.self.project.pdfviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.self.project.pdfviewer.fragments.SplashFragment

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        supportFragmentManager.beginTransaction().add(R.id.container, SplashFragment()).commit()
    }
}