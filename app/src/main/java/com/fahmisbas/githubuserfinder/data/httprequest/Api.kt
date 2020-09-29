package com.fahmisbas.githubuserfinder.data.httprequest

import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.entities.UserItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/users?")
    @Headers("Authorization: token bbc75e90acbc00adffa11bd57c79e9c2ec71d9ff")
    fun getUser(@Query("q") query: String): Call<UserItems>

    @GET("users/{username}")
    @Headers("Authorization: token bbc75e90acbc00adffa11bd57c79e9c2ec71d9ff")
    fun getUserDetail(@Path("username") usernameId: String): Call<UserData>

    @GET("users/{username}/following")
    @Headers("Authorization: token bbc75e90acbc00adffa11bd57c79e9c2ec71d9ff")
    fun getUserFollowing(@Path("username") usernameId: String): Call<List<UserData>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token bbc75e90acbc00adffa11bd57c79e9c2ec71d9ff")
    fun getUserFollowers(@Path("username") usernameId: String): Call<List<UserData>>

}