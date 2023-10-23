package com.example.first

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(
    context:Context?,
    name : String?,
    factory : SQLiteDatabase.CursorFactory?,
    version : Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table if not exists member " +
                    "( _id integer primary key autoincrement, name text, " +
                    " age integer, mobile text)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion : Int) {
        if(oldVersion != newVersion){
            db.execSQL("drop table if exists member")
            onCreate(db)
        }
    }
}
