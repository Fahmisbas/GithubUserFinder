/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

package com.fahmisbas.githubuserfinder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.entities.UserData
import kotlinx.android.synthetic.main.item_user.view.*

class ListUserAdapter(
    private val userDataList: ArrayList<UserData>
) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    var onItemClickCallback: OnItemClickCallback? = null

    fun updateList(userData: List<UserData>, isNotEmpty: (Boolean) -> Unit) {
        userDataList.clear()
        userDataList.addAll(userData)
        isNotEmpty.invoke(userDataList.isNotEmpty())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = userDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userDataList[position]
        holder.bind(user) {
            onItemClickCallback?.onItemClicked(user)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userData: UserData, itemClicked: (View) -> Unit) {
            with(itemView) {
                tv_id_name.text = userData.usernameId
                user_type.text = userData.type
                Glide.with(context)
                    .load(userData.profileImageUrl)
                    .into(img_profile_picture)
                setOnClickListener {
                    itemClicked.invoke(itemView)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(userData: UserData)
    }
}

