package com.fahmisbas.consumerapp.ui.favoriteuser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahmisbas.consumerapp.R
import com.fahmisbas.consumerapp.data.entities.UserData
import com.fahmisbas.consumerapp.ui.adapter.ListUserAdapter
import com.fahmisbas.consumerapp.ui.settings.SettingsActivity
import com.fahmisbas.consumerapp.ui.userdetail.UserDetailActivity
import com.fahmisbas.consumerapp.ui.userdetail.UserDetailActivity.Companion.EXTRA_USER_ID
import com.fahmisbas.consumerapp.ui.userdetail.UserDetailActivity.Companion.EXTRA_USER_PROFILE
import com.fahmisbas.consumerapp.util.gone
import com.fahmisbas.consumerapp.util.observe
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.android.synthetic.main.layout_empty_indicator.*


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
        onClick()
    }

    private fun onClick() {
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
                loading.gone()
                img_search_icon.gone()
                tv_waiting_for_search.gone()
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
        setSupportActionBar(toolbar_userfav as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                Intent(this@UserFavoriteActivity, SettingsActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}