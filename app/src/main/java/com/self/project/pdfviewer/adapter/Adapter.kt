package com.self.project.pdfviewer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.self.project.pdfviewer.model.Model
import com.self.project.pdfviewer.databinding.PdfRowBinding
import java.util.Locale

class Adapter(private var arrayList: MutableList<Model>, private val listener: ManageListener) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    private val filteredList = mutableListOf<Model>()
    init {
        filteredList.addAll(arrayList)
    }

    inner class ViewHolder(val binding: PdfRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(PdfRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            myVariable = arrayList[position]
            executePendingBindings()
            hasPendingBindings()

            row.setOnClickListener {
                listener.click(position)
            }

            val preferences = PreferenceManager.getDefaultSharedPreferences(holder.itemView.context)
            val abc = preferences.getString("fontTitle","16").toString().trimEnd()
            val abc2 = preferences.getString("fontSize","12").toString().trimEnd()
            fileName.textSize = abc.toFloat()
            fileSize.textSize = abc2.toFloat()

            row.setOnLongClickListener {
                listener.longClick(position, it)
                true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun myFilter(text: String) {
        arrayList.clear()
        filteredList.forEach {
            if (text.lowercase(Locale.getDefault()).let
                { it1 -> it.fileName.lowercase(Locale.getDefault()).contains(it1) }
            )
                arrayList.add(it)
        }
        notifyDataSetChanged()
    }


}
