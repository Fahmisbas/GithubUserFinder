package com.fahmisbas.consumerapp.data

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.fahmisbas.githubuserfinder"
    const val SCHEME = "content"

    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user_favorite"
            const val COLUMN_ID = "_id"
            const val COLUMN_USERNAME_ID = "username_id"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_TYPE = "type"
            const val COLUMN_FOLLOWING_URL = "following_url"
            const val COLUMN_FOLLOWERS_URL = "followers_url"
            const val COLUMN_PROFILE_IMAGE_URL = "profile_image_url"
            const val COLUMN_COMPANY = "company"
            const val COLUMN_LOCATION = "location"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}