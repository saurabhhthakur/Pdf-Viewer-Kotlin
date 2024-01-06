package com.self.project.pdfviewer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.self.project.pdfviewer.databinding.PdfRowBinding
import java.util.Locale

class Adapter(private var arrayList: MutableList<Model>, private val listener: ManageListener) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    private val filteredList = mutableListOf<Model>()

    init {
        filteredList.addAll(arrayList)
    }

    class ViewHolder(val binding: PdfRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(PdfRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.fileName.text = arrayList[position].fileName
        holder.binding.fileSize.text = arrayList[position].fileSize
        holder.binding.row.setOnClickListener {
            listener.click(position)
        }
        holder.binding.row.setOnLongClickListener {
            listener.longClick(position, it)
            true
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

        /*  if (arrayList.isEmpty()) {
              Toast.makeText(context,"Item not found",Toast.LENGTH_SHORT).show()
              arrayList.addAll(filteredList)
          }*/

        notifyDataSetChanged()
    }


}