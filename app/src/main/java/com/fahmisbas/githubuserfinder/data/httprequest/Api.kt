package com.fahmisbas.githubuserfinder.data.httprequest

import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.entities.UserDataDetail
import com.fahmisbas.githubuserfinder.data.entities.UserItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/users?")
    fun getUser(@Query("q") query: String): Call<UserItems>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") usernameId: String): Call<UserDataDetail>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") usernameId: String): Call<List<UserData>>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") usernameId: String): Call<List<UserData>>

}