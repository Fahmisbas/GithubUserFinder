package com.fahmisbas.githubuserfinder.ui.favoriteuser


import androidx.lifecycle.ViewModel
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper
import com.fahmisbas.githubuserfinder.data.repositories.UserFavoriteRepository

class UserFavoriteViewModel : ViewModel() {

    private var repository = UserFavoriteRepository()

    fun getAllUsers(helper: UserFavoriteHelper) = repository.getAllUsers(helper)

}