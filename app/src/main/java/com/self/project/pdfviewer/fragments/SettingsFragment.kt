package com.self.project.pdfviewer.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.self.project.pdfviewer.R

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val themePreference = sharedPreferences.getString("mode", "3") // Default to system

        // Apply the saved theme mode
        when (themePreference?.toInt()) {
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.my_background_color
                    )
                )
            }

            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.my_background_color
                    )
                )
            } // Dark mode
            3 -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.my_background_color
                    )
                )} // Follow system
        }

        listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreference, key ->
                if (key == "mode") {
                    val pref = sharedPreference.getString(key, "3")
                    when (pref?.toInt()) {
                        1 ->
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                        2 ->
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                        3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            }

        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(listener)


        return view
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceType")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(listener)
    }

}