package com.fahmisbas.githubuserfinder.ui.favoriteuser


import android.content.Context
import androidx.lifecycle.ViewModel
import com.fahmisbas.githubuserfinder.data.repositories.UserFavoriteRepository

class UserFavoriteViewModel : ViewModel() {

    private var repository = UserFavoriteRepository()

    fun getUsersData(context: Context) = repository.getUsersData(context)

}