package com.fahmisbas.githubuserfinder.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_COMPANY
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_FOLLOWERS_URL
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_FOLLOWING_URL
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_ID
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_LOCATION
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_PROFILE_IMAGE_URL
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_TYPE
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_USERNAME
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_USERNAME_ID
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_user_favorite"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_USER_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " ($COLUMN_ID INTEGER," +
                " $COLUMN_USERNAME TEXT," +
                " $COLUMN_USERNAME_ID TEXT," +
                " $COLUMN_TYPE TEXT," +
                " $COLUMN_FOLLOWING_URL TEXT," +
                " $COLUMN_FOLLOWERS_URL TEXT," +
                " $COLUMN_PROFILE_IMAGE_URL TEXT," +
                " $COLUMN_COMPANY TEXT," +
                " $COLUMN_LOCATION TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_USER_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}