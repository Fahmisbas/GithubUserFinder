/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

package com.fahmisbas.consumerapp.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class UserItems(
    @SerializedName("items")
    val userData: List<UserData>
)

@Parcelize
class UserData(
    @SerializedName("id")
    var id : Int? = 0,
    @SerializedName("login")
    var usernameId: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("avatar_url")
    var profileImageUrl: String? = null,
    @SerializedName("following_url")
    var followingUrl : String? = null,
    @SerializedName("followers_url")
    var followersUrl : String? = null,
    @SerializedName("name")
    var username: String? = null,
    @SerializedName("company")
    var company: String? = null,
    @SerializedName("location")
    var location: String? = null,
) : Parcelable
