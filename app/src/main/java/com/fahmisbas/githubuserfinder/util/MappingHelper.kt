/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

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

    fun mapCursorToList(cursor: Cursor): ArrayList<UserData> {
        val arrayList = ArrayList<UserData>()
        cursor.moveToNext()
        var userData: UserData
        if (cursor.count > 0) {
            do {
                userData = UserData()
                userData.id =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_ID))
                userData.usernameId = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.COLUMN_USERNAME_ID
                    )
                )
                userData.followersUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.COLUMN_FOLLOWERS_URL
                    )
                )
                userData.followingUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.COLUMN_FOLLOWING_URL
                    )
                )
                userData.location =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_LOCATION))
                userData.company =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_COMPANY))
                userData.username =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_USERNAME))
                userData.profileImageUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.COLUMN_PROFILE_IMAGE_URL
                    )
                )
                userData.type =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.COLUMN_TYPE))

                arrayList.add(userData)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
            cursor.close()
        }
        return arrayList
    }

}