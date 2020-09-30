package com.fahmisbas.githubuserfinder.ui.detailuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.ui.detailuser.tabs.SectionPagerAdapter
import com.fahmisbas.githubuserfinder.util.*
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.layout_blank_indicator.*
import kotlinx.android.synthetic.main.layout_tabs.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*


class UserDetailActivity : AppCompatActivity() {

    private lateinit var userDataProfile: UserData

    private lateinit var detailViewModel: UserDetailViewModel
    private lateinit var helper: UserFavoriteHelper

    private var usernamePath: IUsernamePath? = null

    private var statusFavorite = false
    private var isUserExist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        initDatabase()
        initViewModel()
        initialVisibility()
        isUserDataExist()
        setUsernamePath()

    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        setUserFavorite()
        observeChanges()
    }

    private fun initDatabase() {
        userDataProfile = intent.getParcelableExtra(EXTRA_USER_PROFILE) as UserData
        helper = UserFavoriteHelper.getInstance(applicationContext)
        helper.open()
    }

    private fun isUserDataExist() {
        detailViewModel.queryById(helper, userDataProfile).observe(this, { cursor ->
            if (cursor.count > 0) {
                userDataProfile = MappingHelper.mapCursorToObject(cursor)
                isUserExist = true
                setStatusFavorite(true)
                updateData(userDataProfile)
            } else {
                isUserExist = false
                btn_favorite.setImageResource(R.drawable.ic_favorite)
            }
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
        detailViewModel.deleteUserById(helper, userDataProfile).observe(this, { isSuccessful ->
            if (isSuccessful) {
                this.makeToast("Sukses menghapus dari favorit")
            }
        })
    }

    private fun addUserFavorite() {
        detailViewModel.insertUserData(helper, userDataProfile).observe(this, { isSuccessful ->
            if (isSuccessful) {
                this.makeToast("Sukses menambah daftar favorit")
            }
        })
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

    private fun initViewModel() {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserDetailViewModel::class.java)
    }

    private fun setUsernamePath() {
        usernamePath = detailViewModel
        userDataProfile.usernameId?.let {
            usernamePath?.usernameId(it)
        }
    }

    private fun initToolbar() {
        val toolbar = toolbar_detailuser as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.icon_github.gone()
        toolbar.toolbar_title.gone()
        title = userDataProfile.usernameId
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun observeChanges() {
        observe(detailViewModel.userDetail, ::updateData)
        observe(detailViewModel.error, ::isError)
        detailViewModel.following.observe(this, { following ->
            detailViewModel.followers.observe(this@UserDetailActivity, { followers ->
                following?.let {
                    tabLayout(following, followers)
                }
            })
        })
    }

    private fun tabLayout(following: List<UserData>, followers: List<UserData>) {
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, following, followers)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    private fun isError(error: Boolean) {
        if (error) {
            if (isUserExist) {
                tv_failed_to_load_data.gone()
                img_failed_to_load_data.gone()
                loading.gone()
            } else {
                img_failed_to_load_data.visible()
                tv_failed_to_load_data.visible()
                loading.gone()
            }
            this.makeToast(resources.getString(R.string.error_to_load_data))
        } else {
            loading.gone()

        }
    }

    private fun updateData(userData: UserData) {
        userData.let {
            userDataProfile.username = userData.username
            userDataProfile.company = userData.company
            userDataProfile.location = userData.location
            userDataProfile.id = userData.id
            userDataProfile.followingUrl = userData.followingUrl
            userDataProfile.followersUrl = userData.followersUrl
            loadDataVisibility()
            updateViews()
        }
    }

    private fun updateViews() {
        nullCheckSetVisibility() {
            tv_id_name.text = userDataProfile.usernameId
            tv_name.text = userDataProfile.username
            tv_company.text = userDataProfile.company
            tv_location.text = userDataProfile.location
            Glide.with(applicationContext)
                .load(userDataProfile.profileImageUrl)
                .placeholder(R.drawable.ic_error_24)
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


    private fun loadDataVisibility() {
        loading.gone()
        tabs_layout.visible()
        img_profile_picture.visible()
        img_location.visible()
        img_company.visible()
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }

    companion object {
        const val EXTRA_USER_PROFILE = "user profile"
    }
}