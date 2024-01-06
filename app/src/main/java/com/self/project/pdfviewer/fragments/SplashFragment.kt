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
import androidx.fragment.app.Fragment
import com.self.project.pdfviewer.R
import java.io.File

class SplashFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (Intent.ACTION_VIEW == requireActivity().intent.action) {
            val bundle = Bundle()
            var filePath = requireActivity().intent.data?.path.toString()
            val fileName = filePath.let { File(it).name.toString() }

            if (filePath.startsWith("/external_files/$fileName")) {
                bundle.putString("filename", fileName)
                filePath = filePath.replace("/external_files", "/storage/emulated/0")
            } else if (filePath.startsWith("/external_files")) {
                bundle.putString("filename", fileName)
                filePath = filePath.replace("/external_files", "/storage/emulated/0")
            } else {
               val uri = requireActivity().intent.data
                filePath = uri.toString()
                bundle.putInt("uriData",1)
            }

            bundle.putString("filepath", filePath)
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