package com.fahmisbas.githubuserfinder.ui.detailuser

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_USERNAME
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.COLUMN_USERNAME_ID
import com.fahmisbas.githubuserfinder.data.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.ui.detailuser.tabs.SectionPagerAdapter
import com.fahmisbas.githubuserfinder.util.gone
import com.fahmisbas.githubuserfinder.util.makeToast
import com.fahmisbas.githubuserfinder.util.observe
import com.fahmisbas.githubuserfinder.util.visible
import kotlinx.android.synthetic.main.layout_blank_indicator.*
import kotlinx.android.synthetic.main.layout_tabs.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import kotlinx.android.synthetic.main.activity_detail_user.*


class DetailUserActivity : AppCompatActivity() {

    private lateinit var userDataProfile: UserData
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var helper: UserFavoriteHelper

    private var usernamePath: IUsernamePath? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)




        initDatabase()

        queryUserData()

        initialVisibility()

        initViewModel()

        setUserDataProfile()
    }

    private fun queryUserData() {
        userDataProfile = intent.getParcelableExtra(EXTRA_USER_PROFILE) as UserData

    }

    private fun initDatabase() {
        helper = UserFavoriteHelper.getInstance(applicationContext)
        helper.open()
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        setUserFavorite()
    }

    private fun setUserFavorite() {
        var statusFavorite = false
        btn_favorite.setOnClickListener {
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            btn_favorite.setImageResource(R.drawable.ic_favorite_pressed)
            addUserFavorite()
        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite)
            deleteUserFavorite()
        }
    }

    private fun deleteUserFavorite() {
        if (userDataProfile.id ?: 0 > 0) {
            helper.deleteUserById(userDataProfile.id.toString()).toLong()
            Log.i("hehe", userDataProfile.id.toString())
            Toast.makeText(this, "Satu item berhasil di hapus", Toast.LENGTH_SHORT).show()
        } else {
            return
        }
    }

    private fun addUserFavorite() {
        val result = helper.insertUserData(userDataProfile)
        if (result.toInt() == userDataProfile.id) {
            return
        }else {
            userDataProfile.id = result.toInt()
            Log.i("haha", userDataProfile.id.toString())
            Toast.makeText(this, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show()
        }
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
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)
        usernamePath = viewModel
    }

    private fun setUserDataProfile() {
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
        observe(viewModel.userDataDetail, ::updateData)
        observe(viewModel.error, ::isError)

        viewModel.userFollowing.observe(this, { following ->
            viewModel.userFollowers.observe(this@DetailUserActivity, { followers ->
                following?.let {
                    tabLayout(following, followers)
                }
            })
        })
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
            this.makeToast(resources.getString(R.string.error_to_load_data))
            img_failed_to_load_data.visible()
            tv_failed_to_load_data.visible()
            loading.gone()
        } else {
            loading.gone()
        }
    }

    private fun updateData(userData: UserData) {
        userData.let {
            userDataProfile.username = userData.username
            userDataProfile.company = userData.company
            userDataProfile.location = userData.location

            loadDataVisibility()

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
    }


    private fun loadDataVisibility() {
        loading.gone()
        tabs_layout.visible()
        img_profile_picture.visible()
        img_location.visible()
        img_company.visible()
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
    }
}