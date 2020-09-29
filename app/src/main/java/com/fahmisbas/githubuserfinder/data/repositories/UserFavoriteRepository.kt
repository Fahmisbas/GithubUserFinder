package com.fahmisbas.githubuserfinder.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper
import com.fahmisbas.githubuserfinder.data.entities.UserData

class UserFavoriteRepository {

    fun getAllUsers(helper: UserFavoriteHelper) : LiveData<List<UserData>>{
        val liveData = MutableLiveData<List<UserData>>()
        liveData.postValue(helper.getAllUsers())
        return liveData
    }
}