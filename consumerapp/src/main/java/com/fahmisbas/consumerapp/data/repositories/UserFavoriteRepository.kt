package com.fahmisbas.consumerapp.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fahmisbas.consumerapp.data.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.fahmisbas.consumerapp.data.entities.UserData
import com.fahmisbas.consumerapp.util.MappingHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserFavoriteRepository {

    fun getUsersData(context: Context) : LiveData<List<UserData>>{
        val liveData = MutableLiveData<List<UserData>>()
        GlobalScope.launch {
            val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
            liveData.postValue(cursor?.let { MappingHelper.mapCursorToList(it) })
        }
        return liveData
    }
}