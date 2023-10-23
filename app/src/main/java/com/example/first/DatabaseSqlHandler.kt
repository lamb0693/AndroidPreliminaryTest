package com.example.first

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DatabaseSqlHandler private constructor(context : Context){
    private val openHelper : DatabaseOpenHelper = DatabaseOpenHelper(context, "member.db", null, 1)
    private val sqLiteDatabase: SQLiteDatabase = openHelper.writableDatabase

    companion object {
        private var sqlHandler : DatabaseSqlHandler? = null

        fun open(context : Context) : DatabaseSqlHandler {
            if(sqlHandler == null) {
                sqlHandler = DatabaseSqlHandler(context)
            }
            return sqlHandler as DatabaseSqlHandler
        }
    }
    fun close() {
        openHelper.close()
    }

    fun insert(dto: MemberDTO){
        val values = ContentValues()
        values.put("name", dto.name)
        values.put("age", dto.age)
        values.put("mobile", dto.mobile)
        sqLiteDatabase.insert("member", null, values)
    }

    fun update(dto:MemberDTO){
        val values = ContentValues()
        values.put("_id", dto.id)
        values.put("name", dto.name)
        values.put("age", dto.age)
        values.put("mobile", dto.mobile)
        sqLiteDatabase.update("member", null, "_id=?", arrayOf(dto.id.toString()))
    }

    fun delete(id:Int) {
        sqLiteDatabase.delete("member", "_id=?", arrayOf(id.toString()))
    }

    fun get(id:Int):MemberDTO? {
        val cursor : Cursor = sqLiteDatabase.query(
            "member", null, "_id=?", arrayOf(id.toString()), null, null, null, )

        return if(cursor.moveToNext()) {
            @SuppressLint("Range") val _id =  cursor.getInt(cursor.getColumnIndex("_id"))
            @SuppressLint("Range") val name = cursor.getString(cursor.getColumnIndex("name"))
            @SuppressLint("Range") val age =  cursor.getInt(cursor.getColumnIndex("age"))
            @SuppressLint("Range") val mobile=cursor.getString(cursor.getColumnIndex("mobile"))
            MemberDTO(_id, name, age, mobile )
        } else {
            null
        }
    }

    fun getList() : ArrayList<MemberDTO> {
        var list : ArrayList<MemberDTO> = ArrayList()

        val cursor : Cursor = sqLiteDatabase.query(
            "member", null, null, null, null, null, null, )

        while(cursor.moveToNext()) {
            @SuppressLint("Range") val _id =  cursor.getInt(cursor.getColumnIndex("_id"))
            @SuppressLint("Range") val name = cursor.getString(cursor.getColumnIndex("name"))
            @SuppressLint("Range") val age =  cursor.getInt(cursor.getColumnIndex("age"))
            @SuppressLint("Range") val mobile=cursor.getString(cursor.getColumnIndex("mobile"))
            list.add( MemberDTO(_id, name, age, mobile ) )
        }

        return list
    }
}