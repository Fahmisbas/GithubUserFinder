package com.fahmisbas.consumerapp.data.httprequest

import com.fahmisbas.consumerapp.data.entities.UserData
import com.fahmisbas.consumerapp.data.entities.UserItems
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {

    var userNameQuery: String? = null
    var usernameId: String? = null

    companion object {

        private const val baseUrl = "https://api.github.com/"

        @Volatile
        private var instance: Api? = null
        private val LOCK = Any()

        operator fun invoke(): Api = instance ?: synchronized(LOCK) {
            instance ?: retrofitBuild().also {
                instance = it
            }
        }

        private fun retrofitBuild() = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    fun getUser(): Call<UserItems>? = userNameQuery?.let { retrofitBuild().getUser(it) }
    fun getUserFollowing(): Call<List<UserData>>? = usernameId?.let { retrofitBuild().getUserFollowing(it) }
    fun getUserFollowers(): Call<List<UserData>>? = usernameId?.let { retrofitBuild().getUserFollowers(it) }
    fun getUserDetail(): Call<UserData>? = usernameId?.let { retrofitBuild().getUserDetail(it) }
}

