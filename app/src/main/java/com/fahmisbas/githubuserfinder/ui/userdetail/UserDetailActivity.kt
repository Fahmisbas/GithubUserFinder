package com.fahmisbas.githubuserfinder.ui.userdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.ui.userdetail.tabs.SectionPagerAdapter
import com.fahmisbas.githubuserfinder.util.gone
import com.fahmisbas.githubuserfinder.util.makeToast
import com.fahmisbas.githubuserfinder.util.observe
import com.fahmisbas.githubuserfinder.util.visible
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.layout_empty_indicator.*
import kotlinx.android.synthetic.main.layout_tabs.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class UserDetailActivity : AppCompatActivity() {

    private lateinit var userDataProfile: UserData
    private lateinit var viewModel: UserDetailViewModel

    private var usernamePath: IUsernamePath? = null
    private var statusFavorite = false
    private var isUserExist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        initViewModel()
        initialVisibility()
        userDataExtra()
        setUsernamePath()

    }

    private fun userDataExtra() {
        userDataProfile = intent.getParcelableExtra(EXTRA_USER_PROFILE) as UserData

    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        setUserFavorite()
        observeDataChanges()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserDetailViewModel::class.java)
    }

    private fun initToolbar() {
        val toolbar = toolbar_detailuser as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.icon_github.gone()
        toolbar.toolbar_title.gone()
        title = userDataProfile.usernameId
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun initialVisibility() {
        img_profile_picture.gone()
        img_location.gone()
        img_company.gone()
        img_search_icon.gone()
        tv_waiting_for_search.gone()
        img_failed_to_load_data.gone()
        tv_failed_to_load_data.gone()
    }

    private fun observeDataChanges() {
        observe(viewModel.userDetail, ::updateData)
        observe(viewModel.error, ::isError)
        observe(viewModel.getUserData(applicationContext, userDataProfile), ::userDataDb)
        viewModel.following.observe(this, { following ->
            viewModel.followers.observe(this@UserDetailActivity, { followers ->
                following?.let {
                    load_viewpager.gone()
                    tabLayout(following, followers)
                }
            })
        })
    }

    private fun setUserFavorite() {
        btn_favorite.setOnClickListener {
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            btn_favorite.setImageResource(R.drawable.ic_favorite_filled)
            if (isUserExist) {
                if (btn_favorite.isPressed) {
                    btn_favorite.setImageResource(R.drawable.ic_favorite)
                    deleteUserFavorite()
                    isUserExist = false
                    this@UserDetailActivity.statusFavorite = false
                }
                return
            }
            addUserFavorite()
        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite)
            deleteUserFavorite()
            isUserExist = false
        }
    }

    private fun deleteUserFavorite() {
        viewModel.deleteUserData(applicationContext, userDataProfile)
            .observe(this, { isSuccessful ->
                if (isSuccessful) {
                    this.makeToast(resources.getString(R.string.removed))
                }
            })
    }

    private fun addUserFavorite() {
        viewModel.insertUserData(applicationContext, userDataProfile)
            .observe(this, { isSuccessful ->
                if (isSuccessful) {
                    this.makeToast(resources.getString(R.string.added))
                }
            })
    }


    private fun setUsernamePath() {
        usernamePath = viewModel
        userDataProfile.usernameId?.let {
            usernamePath?.usernameId(it)
        }
    }

    private fun userDataDb(userData: UserData?) {
        if (userData != null) {
            isUserExist = true
            setStatusFavorite(true)
            updateData(userData)
        } else {
            isUserExist = false
            btn_favorite.setImageResource(R.drawable.ic_favorite)
        }
    }

    private fun tabLayout(following: List<UserData>, followers: List<UserData>) {
        val sectionsPagerAdapter =
            SectionPagerAdapter(this, supportFragmentManager, following, followers)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    private fun isError(error: Boolean) {
        if (error) {
            if (isUserExist) {
                tv_failed_to_load_data.gone()
                img_failed_to_load_data.gone()
                this.makeToast(resources.getString(R.string.error_to_load_data_following_followers))
            } else {
                img_failed_to_load_data.visible()
                tv_failed_to_load_data.visible()
                this.makeToast(resources.getString(R.string.error_to_load_data))
            }
            tabs_layout.gone()
            loading.gone()
        } else {
            loading.gone()
        }
    }


    private fun updateData(userData: UserData) {
        userData.let {
            userDataProfile.username = userData.username
            userDataProfile = userData
            userDataProfile.company = userData.company
            userDataProfile.location = userData.location
            userDataProfile.id = userData.id
            userDataProfile.followingUrl = userData.followingUrl
            userDataProfile.followersUrl = userData.followersUrl

            loadDataVisibility()
            updateViews()
        }
    }

    private fun loadDataVisibility() {
        loading.gone()
        tabs_layout.visible()
        img_profile_picture.visible()
        img_location.visible()
        img_company.visible()
    }

    private fun updateViews() {
        nullCheckSetVisibility {
            tv_id_name.text = userDataProfile.usernameId
            tv_name.text = userDataProfile.username
            tv_company.text = userDataProfile.company
            tv_location.text = userDataProfile.location
            Glide.with(applicationContext)
                .load(userDataProfile.profileImageUrl)
                .placeholder(R.drawable.ic_github)
                .into(img_profile_picture)
        }
    }

    private fun nullCheckSetVisibility(function: () -> Unit) {
        if (userDataProfile.company.isNullOrEmpty() && userDataProfile.location.isNullOrEmpty()) {
            img_company.gone()
            tv_company.gone()
            tv_location.gone()
            img_location.gone()
        } else if (userDataProfile.company.isNullOrEmpty()) {
            img_company.gone()
            tv_company.gone()
        } else if (userDataProfile.location.isNullOrEmpty()) {
            tv_location.gone()
            img_location.gone()
        }
        function.invoke()
    }

    companion object {
        const val EXTRA_USER_PROFILE = "user profile"
        const val EXTRA_USER_ID = "user_id"
    }
}