/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

package com.fahmisbas.githubuserfinder.ui.searchuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.repositories.SearchUserRepository

class SearchUserViewModel : ViewModel(), IOnQueryTextChangeListener {

    private val repository = SearchUserRepository()

    var error: LiveData<Boolean> = repository.getError()
    var users: LiveData<List<UserData>> = repository.getUsers()

    override fun onQueryTextSubmit(query: String) {
        repository.onQueryTextSubmit(query)
    }
}