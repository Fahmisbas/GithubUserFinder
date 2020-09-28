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
    var id : Int? = 0,
    @SerializedName("login")
    var usernameId: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("avatar_url")
    var profileImageUrl: String? = null,
    @SerializedName("name")
    var username: String? = null,
    @SerializedName("company")
    var company: String? = null,
    @SerializedName("location")
    var location: String? = null,
) : Parcelable
