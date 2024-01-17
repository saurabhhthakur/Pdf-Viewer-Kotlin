package com.self.project.pdfviewer.fragments

import android.annotation.SuppressLint
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.self.project.pdfviewer.R
import com.self.project.pdfviewer.database.SqliteDatabase
import com.self.project.pdfviewer.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding
    private var pageNo: Int = 0
    private lateinit var db: SqliteDatabase

    @SuppressLint("ResourceType", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(layoutInflater)

        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding.toolbarViewer)
            title = arguments?.getString("filename")
        }

        var total = 0
        db = context?.let { SqliteDatabase(it) }!!

        var value = 0
        if (arguments?.getString("filename")?.let { db.getPage(it) } != null) {
            value = arguments?.getString("filename")?.let { db.getPage(it)?.page }!!
        }

        binding.pdfView.apply {
            binding.pdfView.fromUri(Uri.parse(arguments?.getString("fileUri")))
                .spacing(10)
                .defaultPage(value)
                .enableSwipe(true)
                .onPageChange { page, _ ->
                    pageNo = page
                }
                .onLoad { nbPages -> total = nbPages }
                .onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                    if (displayedPage < total - 1) {
                        val paint = Paint()
                        paint.color = ContextCompat.getColor(context, R.color.background_color)
                        canvas.drawRect(
                            0f,
                            pageHeight,
                            pageWidth,
                            (pageHeight + 10),
                            paint
                        )
                    }
                }
                .scrollHandle(DefaultScrollHandle(context))
                // .nightMode(checkMode)
                .load()
        }


        return binding.root
    }


    override fun onPause() {
        super.onPause()
        arguments?.getString("filename")?.let { db.addPage(it, pageNo) }
    }
}