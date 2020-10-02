package com.fahmisbas.consumerapp.data.httprequest

import com.fahmisbas.consumerapp.data.entities.UserData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface Api {

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