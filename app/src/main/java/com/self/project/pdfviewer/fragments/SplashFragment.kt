package com.self.project.pdfviewer.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.self.project.pdfviewer.R
import java.io.File

class SplashFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val themePreference = sharedPreferences.getString("mode", "3") // Default to system

        // Apply the saved theme mode
        when (themePreference?.toInt()) {
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Light mode
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Dark mode
            3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) // Follow system
        }

        if (Intent.ACTION_VIEW == requireActivity().intent.action) {
            val bundle = Bundle()
            val uri = requireActivity().intent.data
            val fileName =
                requireActivity().intent.data?.path.toString().let { File(it).name.toString() }

            bundle.putString("fileUri", uri.toString())
            bundle.putString("filename", fileName)
            val fragment = SecondFragment()
            fragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragment)
                ?.commit()

        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, FirstFragment())?.commit()
            }, 1500)
        }


        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

}