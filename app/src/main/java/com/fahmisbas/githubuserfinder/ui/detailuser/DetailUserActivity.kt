package com.fahmisbas.githubuserfinder.ui.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.data.entities.UserDataDetail
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

    private var usernamePath: IUsernamePath? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        initialVisibility()
        initViewModel()
        setUserProfile()

    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        observeChanges()
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

    private fun setUserProfile() {
        userDataProfile = intent.getParcelableExtra(EXTRA_USER_PROFILE) as UserData
        usernamePath?.usernameId(userDataProfile.usernameId)
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
        observe(viewModel.userDataDetail, ::bindData)
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

    private fun bindData(userDataDetail: UserDataDetail) {
        userDataDetail.let {
            loading.gone()
            tabs_layout.visible()
            img_profile_picture.visible()
            img_location.visible()
            img_company.visible()

            nullCheckSetVisibility(userDataDetail) {
                tv_id_name.text = userDataProfile.usernameId
                tv_name.text = it.name
                tv_company.text = it.company
                tv_location.text = it.location
                Glide.with(applicationContext)
                    .load(userDataProfile.profileImageUrl)
                    .placeholder(R.drawable.ic_error_24)
                    .into(img_profile_picture)
            }
        }
    }

    private fun nullCheckSetVisibility(it: UserDataDetail, function: () -> Unit) {
        if (it.company.isNullOrEmpty() && it.location.isNullOrEmpty()) {
            img_company.gone()
            tv_company.gone()
            tv_location.gone()
            img_location.gone()
        } else if (it.company.isNullOrEmpty()) {
            img_company.gone()
            tv_company.gone()
        } else if (it.location.isNullOrEmpty()) {
            tv_location.gone()
            img_location.gone()
        }

        function.invoke()
    }


    companion object {
        const val EXTRA_USER_PROFILE = "user profile"
    }
}