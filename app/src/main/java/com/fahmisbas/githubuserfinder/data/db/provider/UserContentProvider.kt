/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

package com.fahmisbas.githubuserfinder.data.db.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.AUTHORITY
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper

class UserContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        helper = UserFavoriteHelper.getInstance(context as Context)
        helper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            USERDATA -> helper.queryAll()
            USERDATA_ID -> helper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USERDATA) {
            uriMatcher.match(uri) -> helper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val delete: Int = when (USERDATA_ID) {
            uriMatcher.match(uri) -> helper.deleteUserById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return delete
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    companion object {
        private const val USERDATA = 1
        private const val USERDATA_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var helper: UserFavoriteHelper

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, USERDATA)
            uriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#", USERDATA_ID
            )
        }
    }

}