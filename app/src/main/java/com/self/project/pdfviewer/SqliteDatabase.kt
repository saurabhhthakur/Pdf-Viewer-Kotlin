package com.self.project.pdfviewer

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteDatabase(context: Context) : SQLiteOpenHelper(context, "pdfViewer", null, 1) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE pdfFiles(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,page INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS pdfFiles")
        onCreate(db)
    }

    fun addFile(obj: SqliteModel): Boolean {
        val db = this.writableDatabase
        val map = ContentValues()
        map.put("name", obj.name)
        map.put("page", obj.page)
        val l = db.insert("pdfFiles", null, map)
        db.close()
        return l > 0
    }

    fun addPage(name: String, page: Int) {
        val db = this.writableDatabase
        val map = ContentValues()
        map.put("page", page)
        db.update("pdfFiles", map, "name = '${name}'", null)
    }

    fun getId(name: String): SqliteModel? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pdfFiles WHERE name = ?", arrayOf(name))

        return if (cursor.moveToFirst()) {
            val result = SqliteModel()
            result.id = cursor.getInt(0)
            result.name = cursor.getString(1)
            result.page = cursor.getInt(2)
            cursor.close()
            result
        } else {
            null
        }
    }

    fun getPage(name: String): SqliteModel? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pdfFiles WHERE name = ?", arrayOf(name))

        return if (cursor.moveToFirst()) {
            val result = SqliteModel()
            result.id = cursor.getInt(0)
            result.page = cursor.getInt(2)
            result.name = cursor.getString(1)
            cursor.close()
            result
        } else {
            null
        }
    }

}