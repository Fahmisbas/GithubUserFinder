package com.fahmisbas.githubuserfinder.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
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
import com.fahmisbas.githubuserfinder.data.entities.UserData

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

    fun getAllUsers() : ArrayList<UserData> {
        val arrayList = ArrayList<UserData>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
            "$COLUMN_ID ASC", null)
        cursor.moveToFirst()
        var userData : UserData
        if (cursor.count > 0) {
            do {
                userData = UserData()
                userData.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                userData.usernameId = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_USERNAME_ID))
                userData.followersUrl = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_FOLLOWERS_URL))
                userData.followingUrl = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_FOLLOWING_URL))
                userData.location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))
                userData.company = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPANY))
                userData.username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                userData.profileImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_PROFILE_IMAGE_URL))
                userData.type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))

                arrayList.add(userData)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }


    fun insertUserData(userData: UserData?): Long {
        val args = ContentValues()
        if (userData != null) {
            args.put(COLUMN_USERNAME_ID, userData.usernameId)
            args.put(COLUMN_ID, userData.id)
            args.put(COLUMN_TYPE, userData.type)
            args.put(COLUMN_PROFILE_IMAGE_URL, userData.profileImageUrl)
            args.put(COLUMN_FOLLOWING_URL, userData.followingUrl)
            args.put(COLUMN_FOLLOWERS_URL, userData.followersUrl)
            args.put(COLUMN_USERNAME, userData.username)
            args.put(COLUMN_COMPANY, userData.company)
            args.put(COLUMN_LOCATION, userData.location)
        }
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun deleteUserById(id : String) : Int {
        return database.delete(TABLE_NAME, "$COLUMN_ID = '$id'", null)
    }

    fun updateUserData(userData: UserData?): Int {
        val args = ContentValues()
        if (userData != null) {
            args.put(COLUMN_USERNAME_ID, userData.usernameId)
            args.put(COLUMN_TYPE, userData.type)
            args.put(COLUMN_PROFILE_IMAGE_URL, userData.profileImageUrl)
            args.put(COLUMN_FOLLOWING_URL, userData.followingUrl)
            args.put(COLUMN_FOLLOWERS_URL, userData.followersUrl)
            args.put(COLUMN_USERNAME, userData.username)
            args.put(COLUMN_COMPANY, userData.company)
            args.put(COLUMN_LOCATION, userData.location)
        }
        return database.update(DATABASE_TABLE, args, COLUMN_ID + "= '" + userData?.id + "'", null)
    }

}