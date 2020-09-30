/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

/*
 * Copyright (c) 2020 by Fahmi Sulaiman Baswedan
 */

package com.fahmisbas.githubuserfinder.ui.detailuser.tabs

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.entities.UserData

class SectionPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    private var following: List<UserData>,
    private var followers: List<UserData>
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.tab_title_following,
        R.string.tab_title_followers
    )

    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {
        return TabsFragment.newInstance(position + 1, following, followers)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }
}