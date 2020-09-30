package com.fahmisbas.githubuserfinder.ui.detailuser

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.repositories.UserDetailRepository

class UserDetailViewModel : ViewModel(), IUsernamePath {

    private val repository = UserDetailRepository()

    var userDetail: LiveData<UserData> = repository.getUserDetail()
    var error: LiveData<Boolean> = repository.getError()
    var following: LiveData<List<UserData>> = repository.getUserFollowing()
    var followers: LiveData<List<UserData>> = repository.getUserFollowers()

    fun queryById(helper : UserFavoriteHelper, userData: UserData) : LiveData<Cursor> = repository.isUserExists(helper, userData)
    fun deleteUserById(helper : UserFavoriteHelper, userData: UserData) : LiveData<Boolean> = repository.deleteUserById(helper, userData)
    fun insertUserData(helper: UserFavoriteHelper, userData: UserData) : LiveData<Boolean> = repository.insertUserData(helper, userData)

    override fun usernameId(username: String) {
        repository.usernameId(username)
    }
}