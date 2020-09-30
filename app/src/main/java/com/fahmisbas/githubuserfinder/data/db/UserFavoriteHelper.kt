package com.fahmisbas.githubuserfinder.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_ID
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

class UserFavoriteHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private var instance: UserFavoriteHelper? = null

        fun getInstance(context: Context): UserFavoriteHelper =
            instance ?: synchronized(this) {
                instance ?: UserFavoriteHelper(context)
            }
    }

    @Throws(SQLiteException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) {
            database.close()
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID ASC"
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun deleteUserById(id: String): Int {
        return database.delete(TABLE_NAME, "$COLUMN_ID = '$id'", null)
    }
}