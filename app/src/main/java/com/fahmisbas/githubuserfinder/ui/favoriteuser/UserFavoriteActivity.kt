package com.fahmisbas.githubuserfinder.ui.favoriteuser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.db.UserFavoriteHelper
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.ui.adapter.ListUserAdapter
import com.fahmisbas.githubuserfinder.ui.detailuser.UserDetailActivity
import com.fahmisbas.githubuserfinder.ui.detailuser.UserDetailActivity.Companion.EXTRA_USER_PROFILE
import com.fahmisbas.githubuserfinder.util.gone
import com.fahmisbas.githubuserfinder.util.visible
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*


class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var viewModel: UserFavoriteViewModel
    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var helper: UserFavoriteHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)

        initToolbar()
        initDatabase()
        initViewModel()
        initRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        observeChanges()
    }

    private fun initDatabase() {
        helper = UserFavoriteHelper.getInstance(applicationContext)
        helper.open()
    }

    private fun initRecyclerView() {
        listUserAdapter = ListUserAdapter(arrayListOf())
        rv_users.apply {
            adapter = listUserAdapter
            layoutManager = LinearLayoutManager(this@UserFavoriteActivity)
        }
        onClick()
    }

    private fun onClick() {
        listUserAdapter.onItemClickCallback = object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(userData: UserData) {
                Intent(this@UserFavoriteActivity, UserDetailActivity::class.java).apply {
                    putExtra(EXTRA_USER_PROFILE, userData)
                    putExtra("id", userData.id)
                    startActivity(this)
                }
            }
        }
    }

    private fun observeChanges() {
        viewModel.getAllUsers(helper).observe(this, { users ->
            listUserAdapter.updateList(users) { isNotEmpty ->
                if (!isNotEmpty) {
                    empty_list.visible()
                    tv_empty_list.visible()
                }else {
                    empty_list.gone()
                    tv_empty_list.gone()
                }
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserFavoriteViewModel::class.java)

    }

    private fun initToolbar() {
        val toolbar = toolbar_userfav as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.icon_github.gone()
        toolbar.toolbar_title.gone()
        title = "Favorites"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}