package com.fahmisbas.consumerapp.ui.userdetail.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fahmisbas.consumerapp.R
import com.fahmisbas.consumerapp.data.entities.UserData
import com.fahmisbas.consumerapp.ui.adapter.ListUserAdapter
import com.fahmisbas.consumerapp.ui.userdetail.UserDetailActivity
import com.fahmisbas.consumerapp.util.visible
import kotlinx.android.synthetic.main.fragment_tabs.*


class TabsFragment : Fragment() {

    private var adapter = ListUserAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_tabs.adapter = adapter

        var index = 1
        if (arguments != null) {
            index = arguments?.getInt(SECTION_NUMBER, 0) as Int
        }
        setFollowingList(index)
        setFollowersList(index)

    }

    private fun setFollowersList(index: Int) {
        if (index == 2) {
            arguments?.getParcelableArrayList<UserData>(USER_FOLLOWERS)?.let {
                adapter.updateList(it) { isNotEmpty ->
                    if (isNotEmpty) {
                        onItemClicked()
                    } else empty_user_list.visible()
                }
            }
        }
    }

    private fun setFollowingList(index: Int) {
        if (index == 1) {
            arguments?.getParcelableArrayList<UserData>(USER_FOLLOWING)?.let {
                adapter.updateList(it) { isNotEmpty ->
                    if (isNotEmpty) {
                        onItemClicked()
                    } else empty_user_list.visible()
                }
            }
        }
    }

    private fun onItemClicked() {
        adapter.onItemClickCallback = object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(userData: UserData) {
                Intent(context, UserDetailActivity::class.java).apply {
                    putExtra(UserDetailActivity.EXTRA_USER_PROFILE, userData)
                    startActivity(this)
                }
            }
        }
    }

    companion object {
        private const val SECTION_NUMBER = "section_number"
        private const val USER_FOLLOWING = "user_following"
        private const val USER_FOLLOWERS = "user_followers"

        fun newInstance(
            index: Int,
            following: List<UserData>,
            followers: List<UserData>
        ): TabsFragment {
            val fragment = TabsFragment()
            val bundle = Bundle()

            bundle.putParcelableArrayList(USER_FOLLOWING, following as ArrayList<UserData>)
            bundle.putParcelableArrayList(USER_FOLLOWERS, followers as ArrayList<UserData>)
            bundle.putInt(SECTION_NUMBER, index)

            fragment.arguments = bundle
            return fragment
        }
    }
}