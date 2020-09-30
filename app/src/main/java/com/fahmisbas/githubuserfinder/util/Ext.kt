package com.fahmisbas.githubuserfinder.util

import android.content.ContentValues
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_COMPANY
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_FOLLOWERS_URL
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_FOLLOWING_URL
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_ID
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_LOCATION
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_PROFILE_IMAGE_URL
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_TYPE
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_USERNAME
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_USERNAME_ID
import com.fahmisbas.githubuserfinder.data.entities.UserData


fun Context.makeToast(text : String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, { it?.let { t -> action(t) } })
}

fun UserData.toContentValues() : ContentValues =
    ContentValues().apply {
        put(COLUMN_USERNAME_ID, usernameId)
        put(COLUMN_ID, id)
        put(COLUMN_TYPE, type)
        put(COLUMN_PROFILE_IMAGE_URL, profileImageUrl)
        put(COLUMN_FOLLOWING_URL, followingUrl)
        put(COLUMN_FOLLOWERS_URL, followersUrl)
        put(COLUMN_USERNAME, username)
        put(COLUMN_COMPANY, company)
        put(COLUMN_LOCATION, location)
    }