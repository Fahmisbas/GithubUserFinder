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
    @SerializedName("login")
    val usernameId: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("avatar_url")
    val profileImageUrl: String
) : Parcelable

class UserDataDetail(
    @SerializedName("name")
    val name: String?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("location")
    val location: String?
)