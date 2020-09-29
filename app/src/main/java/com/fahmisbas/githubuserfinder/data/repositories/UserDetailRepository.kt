package com.fahmisbas.githubuserfinder.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.httprequest.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailRepository {

    val apiService = ApiService()

    private var userDetail = MutableLiveData<UserData>()
    private var error = MutableLiveData<Boolean>()

    private var following = MutableLiveData<List<UserData>>()
    private var followers = MutableLiveData<List<UserData>>()

    fun usernameId(username: String) {
        apiService.usernameId = username
        getUserDetail()
    }

    fun getUserDetail(): LiveData<UserData> {
        apiService.getUserDetail()?.enqueue(object : Callback<UserData> {
            override fun onResponse(
                call: Call<UserData>,
                response: Response<UserData>
            ) {
                response.let { result ->
                    userDetail.postValue(result.body())
                    error.postValue(false)
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                error.postValue(true)
            }
        })

        getUserFollowing()
        getUserFollowers()

        return userDetail
    }

    fun getUserFollowers(): LiveData<List<UserData>> {
        apiService.getUserFollowers()?.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                response.let {
                    followers.postValue(response.body())
                    error.postValue(false)
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                error.postValue(true)
            }
        })

        return followers

    }

    fun getUserFollowing(): LiveData<List<UserData>> {
        apiService.getUserFollowing()?.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                response.let { result ->
                    following.postValue(result.body())
                    error.postValue(false)
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                error.postValue(true)
            }
        })
        return following
    }

    fun getError() = error

    fun isUserExists(helper: UserFavoriteHelper, userData: UserData): LiveData<Boolean> {
        val isExist = MutableLiveData<Boolean>()
        GlobalScope.launch {
            val querying = helper.queryById(userData.id.toString())
            if (querying.count > 0) {
                isExist.postValue(true)
            } else {
                isExist.postValue(false)
            }
        }
        return isExist
    }

    fun deleteUserById(helper: UserFavoriteHelper, userData: UserData) : LiveData<Boolean>{
        val isSuccessful = MutableLiveData<Boolean>()
        if (userData.id ?: 0 > 0) {
            GlobalScope.launch {
                helper.deleteUserById(userData.id.toString()).toLong()
                isSuccessful.postValue(true)
            }
        } else {
            isSuccessful.postValue(false)
        }
        return isSuccessful
    }

    fun insertUserData(helper: UserFavoriteHelper, userData: UserData) : LiveData<Boolean> {
        val isSuccessful = MutableLiveData<Boolean>()
        helper.insertUserData(userData)
        isSuccessful.postValue(true)
        return isSuccessful
    }

}