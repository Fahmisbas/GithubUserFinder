package com.fahmisbas.githubuserfinder.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.entities.UserItems
import com.fahmisbas.githubuserfinder.data.httprequest.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserRepository {

    private val apiService = ApiService()

    private var error = MutableLiveData<Boolean>()
    private var users = MutableLiveData<List<UserData>>()

    fun onQueryTextSubmit(keyword : String) {
        apiService.userNameQuery = keyword
        getUsers()
    }

    fun getUsers() : LiveData<List<UserData>> {
        apiService.getUser()?.enqueue(object : Callback<UserItems> {
            override fun onResponse(call: Call<UserItems>, response: Response<UserItems>) {
                response.let { result ->
                    error.postValue(false)
                    users.postValue(result.body()?.userData)
                }
            }

            override fun onFailure(call: Call<UserItems>, t: Throwable) {
                error.postValue(true)
            }

        })
        return users
    }

    fun getError() = error
}