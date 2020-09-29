package com.fahmisbas.githubuserfinder.util

import android.database.Cursor
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract
import com.fahmisbas.githubuserfinder.data.entities.UserData

object MappingHelper {

    fun mapCursorToObject(cursor: Cursor?): UserData {
        var userData = UserData()
        cursor?.apply {
            moveToFirst()
            val id = getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_ID))
            val usernameId = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    DatabaseContract.NoteColumns.COLUMN_USERNAME_ID
                )
            )
            val followersUrl = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    DatabaseContract.NoteColumns.COLUMN_FOLLOWERS_URL
                )
            )
            val followingUrl = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    DatabaseContract.NoteColumns.COLUMN_FOLLOWING_URL
                )
            )
            val location =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_LOCATION))
            val company =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_COMPANY))
            val username =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_USERNAME))
            val profileImageUrl = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    DatabaseContract.NoteColumns.COLUMN_PROFILE_IMAGE_URL
                )
            )
            val type =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_TYPE))

            userData = UserData(
                id = id,
                usernameId = usernameId,
                followersUrl = followersUrl,
                followingUrl = followingUrl,
                location = location,
                company = company,
                username = username,
                profileImageUrl = profileImageUrl,
                type = type
            )
        }

        return userData
    }

}