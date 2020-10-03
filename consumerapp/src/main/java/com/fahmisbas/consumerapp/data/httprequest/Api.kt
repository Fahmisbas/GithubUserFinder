package com.fahmisbas.consumerapp.data.httprequest

import com.fahmisbas.consumerapp.data.entities.UserData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("users/{username}")
    fun getUserDetail(@Path("username") usernameId: String): Call<UserData>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") usernameId: String): Call<List<UserData>>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") usernameId: String): Call<List<UserData>>

}