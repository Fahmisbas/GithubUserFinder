package com.fahmisbas.githubuserfinder.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class UserItems(
    @SerializedName("items")
    val userData: List<UserData>
)

@Parcelize
class UserData(
    var id: Int? = 0,
    var type: String? = null,
    var location: String? = null,
    @SerializedName("login")
    var usernameId: String? = null,
    @SerializedName("avatar_url")
    var profileImageUrl: String? = null,
    @SerializedName("following_url")
    var followingUrl: String? = null,
    @SerializedName("followers_url")
    var followersUrl: String? = null,
    @SerializedName("name")
    var username: String? = null,
    @SerializedName("company")
    var company: String? = null,
) : Parcelable
