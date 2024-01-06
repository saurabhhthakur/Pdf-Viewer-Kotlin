package com.self.project.pdfviewer.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.self.project.pdfviewer.Adapter
import com.self.project.pdfviewer.Repository
import com.self.project.pdfviewer.ManageListener
import com.self.project.pdfviewer.Model
import com.self.project.pdfviewer.R
import com.self.project.pdfviewer.SqliteDatabase
import com.self.project.pdfviewer.SqliteModel
import com.self.project.pdfviewer.databinding.AlertDialogBinding
import com.self.project.pdfviewer.databinding.FirstFragmentBinding
import java.io.File
import java.util.Locale

class FirstFragment : Fragment(), ManageListener {
    private lateinit var binding: FirstFragmentBinding
    private lateinit var pdfFiles: MutableList<Model>
    private lateinit var recyclerAdapter: Adapter
    private lateinit var getFile: Repository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?
    ): View {
        binding = FirstFragmentBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        setHasOptionsMenu(true)
        getFile = Repository()
        setData()
        return binding.root
    }

    private fun setData() {
        val request =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                    permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                ) {
                    fetchData()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission denied. Cannot access external storage.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest
                    .permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest
                    .permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
            fetchData()
        else
            request.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )

            )

    }

    private fun fetchData() {
        pdfFiles = mutableListOf()
        pdfFiles.addAll(getFile.getSortedData())
        recyclerAdapter = Adapter(pdfFiles, this)
        binding.recycler.adapter = recyclerAdapter
    }

    override fun click(position: Int) {
        val db = context?.let { SqliteDatabase(it) }
        val bundle = Bundle()
        bundle.putString("filename", pdfFiles[position].fileName)
        bundle.putString("filepath", pdfFiles[position].filePath)

        val obj: SqliteModel? = db?.getId(pdfFiles[position].fileName)
        if (obj?.id == null) {
            val a = SqliteModel()
            a.page = 0
            a.name = pdfFiles[position].fileName
            db?.addFile(a)
        }

        val fragment = SecondFragment()
        fragment.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragment)
            ?.addToBackStack(null)?.commit()

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun longClick(position: Int, it: View) {
        val dg = Dialog(requireContext())
        val dialogBinding = AlertDialogBinding.inflate(layoutInflater)
        dg.setContentView(dialogBinding.root)
        dg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.apply {
            rename.setOnClickListener { _ ->
                dg.dismiss()
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle("Rename PDF")
                builder?.setIcon(R.drawable.baseline_drive_file_rename_outline_24)

                val inputNewName = EditText(context)
                inputNewName.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                builder?.setView(inputNewName)
                inputNewName.setPadding(30, 75, 30, 30)

                inputNewName.setText(pdfFiles[position].fileName)

                builder?.setNegativeButton("Rename") { _: DialogInterface, _: Int ->
                    if (inputNewName.text.isNotEmpty()) {
                        if (!inputNewName.text.endsWith(".pdf"))
                            inputNewName.setText(inputNewName.text.toString() + ".pdf")

                        val oldFile = File(pdfFiles[position].filePath)
                        val newFile = File(oldFile.parent, inputNewName.text.toString())

                        if (oldFile.renameTo(newFile)) {
                            pdfFiles[position].fileName = inputNewName.text.toString()
                            Snackbar.make(it, "File name changed", Snackbar.LENGTH_LONG)
                                .show()
                            recyclerAdapter.notifyDataSetChanged()
                        } else {
                            Snackbar.make(it, "File name changed", Snackbar.LENGTH_LONG)
                                .show()
                        }

                    }
                }

                builder?.setPositiveButton("Cancel", null)

                builder?.show()
            }

            delete.setOnClickListener {
                dg.dismiss()
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle("Delete File")
                builder?.setIcon(R.drawable.baseline_delete_24)
                builder?.setMessage("Are you sure you want to delete this PDF file?")

                builder?.setNegativeButton("Yes") { _: DialogInterface, _: Int ->
                    val file = File(pdfFiles[position].filePath)
                    if (file.exists()) {
                        val isDeleted = file.delete()
                        if (isDeleted) {
                            Snackbar.make(it, "File Deleted", Snackbar.LENGTH_LONG)
                                .show()
                            pdfFiles.removeAt(position)
                            recyclerAdapter.notifyItemRemoved(position)
                        } else
                            Snackbar.make(it, "Unable to delete the file", Snackbar.LENGTH_LONG)
                                .show()
                    } else {
                        Snackbar.make(it, "File does not exist", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

                builder?.setPositiveButton("No", null)
                builder?.show()

            }

            share.setOnClickListener {
                dg.dismiss()
                context?.startActivity(
                    ShareCompat.IntentBuilder(context as Activity)
                        .setType("application/pdf")
                        .setStream(Uri.parse(pdfFiles[position].filePath))
                        .setChooserTitle("Choose app")
                        .createChooserIntent()
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                )

            }

            details.setOnClickListener {
                dg.dismiss()
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle("File Details")
                builder?.setIcon(R.drawable.baseline_info_24)
                builder?.setMessage(
                    "\nName:   ${pdfFiles[position].fileName}\n\nPath:   ${
                        pdfFiles[position]
                            .filePath
                    }\n\nSize:   ${pdfFiles[position].fileSize}\n\nLast Modified:   ${pdfFiles[position].lastModified}\n"
                )
                builder?.setPositiveButton("Ok", null)
                builder?.show()
            }
        }

        dg.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.search_icon)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Here..."
        searchView.background =
            activity?.let { ContextCompat.getDrawable(it, R.drawable.toolbar_drawable) }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null)
                    recyclerAdapter.myFilter(newText.lowercase(Locale.getDefault()))
                else
                    Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
                return true
            }
        })
    }

}