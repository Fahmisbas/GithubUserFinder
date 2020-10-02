package com.fahmisbas.githubuserfinder.ui.userdetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.repositories.UserDetailRepository

class UserDetailViewModel : ViewModel(), IUsernamePath {

    private val repository = UserDetailRepository()

    var userDetail: LiveData<UserData> = repository.getUserDetail()
    var error: LiveData<Boolean> = repository.getError()
    var following: LiveData<List<UserData>> = repository.getUserFollowing()
    var followers: LiveData<List<UserData>> = repository.getUserFollowers()

    fun getUserData(context: Context, userData: UserData): LiveData<UserData> =
        repository.getUserData(context, userData)

    fun deleteUserData(context: Context, userData: UserData): LiveData<Boolean> =
        repository.deleteUserData(context, userData)

    fun insertUserData(context: Context, userData: UserData): LiveData<Boolean> =
        repository.insertUserData(context, userData)

    override fun usernameId(username: String) {
        repository.usernameId(username)
    }
}