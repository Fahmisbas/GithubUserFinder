package com.fahmisbas.githubuserfinder.ui.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahmisbas.githubuserfinder.data.httprequest.ApiService
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.entities.UserDataDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel(), IUsernamePath {

    val apiService = ApiService()

    private var _userDetail = MutableLiveData<UserDataDetail>()
    private var _error = MutableLiveData<Boolean>()
    private var _userFollowing = MutableLiveData<List<UserData>>()
    private var _userFollowers = MutableLiveData<List<UserData>>()

    var userDataDetail: LiveData<UserDataDetail> = _userDetail
    var error: LiveData<Boolean> = _error
    var userFollowing: LiveData<List<UserData>> = _userFollowing
    var userFollowers: LiveData<List<UserData>> = _userFollowers

    init {
        loadUserDetail()
    }

    private fun loadUserDetail() {
        apiService.getUserDetail()?.enqueue(object : Callback<UserDataDetail> {
            override fun onResponse(
                call: Call<UserDataDetail>,
                response: Response<UserDataDetail>
            ) {
                response.let { result ->
                    _userDetail.postValue(result.body())
                    _error.postValue(false)
                }
            }

            override fun onFailure(call: Call<UserDataDetail>, t: Throwable) {
                _error.postValue(true)
            }
        })

        loadUserFollowing()
        loadUserFollowers()
    }

    private fun loadUserFollowers() {
        apiService.getUserFollowers()?.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                response.let {
                    _userFollowers.postValue(response.body())
                    _error.postValue(false)
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                _error.postValue(true)
            }
        })
    }

    private fun loadUserFollowing() {
        apiService.getUserFollowing()?.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                response.let { result ->
                    _userFollowing.postValue(result.body())
                    _error.postValue(false)
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                _error.postValue(true)
            }
        })
    }

    override fun usernameId(username: String) {
        apiService.usernameId = username
        loadUserDetail()
    }
}