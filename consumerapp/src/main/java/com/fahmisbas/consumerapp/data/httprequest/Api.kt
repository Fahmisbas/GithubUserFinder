/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

package com.fahmisbas.consumerapp.data.httprequest

import com.fahmisbas.consumerapp.data.entities.UserData
import com.fahmisbas.consumerapp.data.entities.UserItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {


    @GET("search/users?")
    @Headers("Authorization: token YOUR API KEY")
    fun getUser(@Query("q") query: String): Call<UserItems>

    @GET("users/{username}")
    @Headers("Authorization: token YOUR API KEY")
    fun getUserDetail(@Path("username") usernameId: String): Call<UserData>

    @GET("users/{username}/following")
    @Headers("Authorization: token YOUR API KEY")
    fun getUserFollowing(@Path("username") usernameId: String): Call<List<UserData>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token YOUR API KEY")
    fun getUserFollowers(@Path("username") usernameId: String): Call<List<UserData>>

}