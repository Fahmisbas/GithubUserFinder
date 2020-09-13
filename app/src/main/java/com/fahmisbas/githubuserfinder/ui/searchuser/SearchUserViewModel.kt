package com.fahmisbas.githubuserfinder.ui.searchuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahmisbas.githubuserfinder.data.httprequest.ApiService
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.entities.UserItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserViewModel : ViewModel(), IOnQueryTextChangeListener {

    private val apiService = ApiService()

    private var _loading = MutableLiveData<Boolean>()
    private var _error = MutableLiveData<Boolean>()
    private var _users = MutableLiveData<List<UserData>>()

    var error: LiveData<Boolean> = _error
    var users: LiveData<List<UserData>> = _users

    init {
        loadData()
    }

    private fun loadData() {
        apiService.getUser()?.enqueue(object : Callback<UserItems> {
            override fun onResponse(call: Call<UserItems>, response: Response<UserItems>) {
                response.let { result ->
                    _loading.postValue(result.isSuccessful)
                    _error.postValue(false)
                    _users.postValue(result.body()?.userData)
                }
            }

            override fun onFailure(call: Call<UserItems>, t: Throwable) {
                _error.postValue(true)
            }

        })
    }

    override fun onQueryTextSubmit(query: String) {
        apiService.userNameQuery = query
        loadData()
    }
}