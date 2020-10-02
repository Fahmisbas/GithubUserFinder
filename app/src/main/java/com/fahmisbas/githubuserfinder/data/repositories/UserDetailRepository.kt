package com.fahmisbas.githubuserfinder.data.repositories

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.httprequest.ApiService
import com.fahmisbas.githubuserfinder.util.MappingHelper
import com.fahmisbas.githubuserfinder.util.toContentValues
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailRepository {

    private val apiService = ApiService()

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

    fun getUserData(context: Context, userData: UserData): LiveData<UserData> {
        val data = MutableLiveData<UserData>()

        val uri = "$CONTENT_URI/${userData.id}".toUri()

        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor?.count ?: 0 > 0) {
            data.postValue(MappingHelper.mapCursorToObject(cursor))
        }
        return data
    }

    fun deleteUserData(context: Context, userData: UserData): LiveData<Boolean> {
        val isSuccessful = MutableLiveData<Boolean>()

        val uri = "$CONTENT_URI/${userData.id}".toUri()

        if (userData.id ?: 0 > 0) {
            context.contentResolver.delete(uri, null, null)
            isSuccessful.postValue(true)
        } else {
            isSuccessful.postValue(false)
        }
        return isSuccessful
    }

    fun insertUserData(context: Context, userData: UserData): LiveData<Boolean> {
        val isSuccessful = MutableLiveData<Boolean>()
        context.contentResolver.insert(CONTENT_URI, userData.toContentValues())
        isSuccessful.postValue(true)
        return isSuccessful
    }

}