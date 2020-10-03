package com.fahmisbas.githubuserfinder.ui.favoriteuser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.ui.adapter.ListUserAdapter
import com.fahmisbas.githubuserfinder.ui.userdetail.UserDetailActivity
import com.fahmisbas.githubuserfinder.ui.userdetail.UserDetailActivity.Companion.EXTRA_USER_ID
import com.fahmisbas.githubuserfinder.ui.userdetail.UserDetailActivity.Companion.EXTRA_USER_PROFILE
import com.fahmisbas.githubuserfinder.util.gone
import com.fahmisbas.githubuserfinder.util.observe
import com.fahmisbas.githubuserfinder.util.visible
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*


class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var viewModel: UserFavoriteViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)

        initToolbar()
        initViewModel()
        initRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        observeChanges()
    }

    private fun initRecyclerView() {
        listUserAdapter = ListUserAdapter(arrayListOf())
        rv_users.apply {
            adapter = listUserAdapter
            layoutManager = LinearLayoutManager(this@UserFavoriteActivity)
        }
        onClickItem()
    }

    private fun onClickItem() {
        listUserAdapter.onItemClickCallback = object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(userData: UserData) {
                Intent(this@UserFavoriteActivity, UserDetailActivity::class.java).apply {
                    putExtra(EXTRA_USER_PROFILE, userData)
                    putExtra(EXTRA_USER_ID, userData.id)
                    startActivity(this)
                }
            }
        }
    }

    private fun observeChanges() {
        observe(viewModel.getUsersData(applicationContext), ::updateList)
    }

    private fun updateList(users: List<UserData>) {
        listUserAdapter.updateList(users) { isNotEmpty ->
            if (isNotEmpty) {
                empty_list.gone()
                tv_empty_list.gone()
            } else {
                empty_list.visible()
                tv_empty_list.visible()
            }
        }
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
        title = resources.getString(R.string.favorite_user)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}