package com.fahmisbas.githubuserfinder.ui.searchuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahmisbas.githubuserfinder.R
import com.fahmisbas.githubuserfinder.data.entities.UserData
import com.fahmisbas.githubuserfinder.ui.adapter.ListUserAdapter
import com.fahmisbas.githubuserfinder.ui.favoriteuser.UserFavoriteActivity
import com.fahmisbas.githubuserfinder.ui.settings.SettingsActivity
import com.fahmisbas.githubuserfinder.ui.userdetail.UserDetailActivity
import com.fahmisbas.githubuserfinder.ui.userdetail.UserDetailActivity.Companion.EXTRA_USER_PROFILE
import com.fahmisbas.githubuserfinder.util.gone
import com.fahmisbas.githubuserfinder.util.makeToast
import com.fahmisbas.githubuserfinder.util.observe
import com.fahmisbas.githubuserfinder.util.visible
import kotlinx.android.synthetic.main.activity_seacrhuser.*
import kotlinx.android.synthetic.main.layout_empty_indicator.*

class SearchUserActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchUserViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    private var onQueryTextChangeListener: IOnQueryTextChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seacrhuser)

        initViewModel()
        initToolbar()
        initRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        observeChanges()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchUserViewModel::class.java)
        onQueryTextChangeListener = viewModel
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_searchuser as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initRecyclerView() {
        listUserAdapter = ListUserAdapter(arrayListOf())
        rv_users.apply {
            adapter = listUserAdapter
            layoutManager = LinearLayoutManager(this@SearchUserActivity)
        }

        onItemClicked()
    }

    private fun onItemClicked() {
        listUserAdapter.onItemClickCallback = object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(userData: UserData) {
                Intent(this@SearchUserActivity, UserDetailActivity::class.java).apply {
                    putExtra(EXTRA_USER_PROFILE, userData)
                    startActivity(this)
                }
            }
        }
    }

    private fun observeChanges() {
        observe(viewModel.users, ::updateList)
        observe(viewModel.error, ::isError)
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

    private fun isError(error: Boolean) {
        if (error) {
            this.makeToast(resources.getString(R.string.error_to_load_data))
            img_failed_to_load_data.visible()
            tv_failed_to_load_data.visible()
            rv_users.gone()
        } else {
            img_failed_to_load_data.gone()
            tv_failed_to_load_data.gone()
            rv_users.visible()
        }
        loading.gone()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.query_hint)

        onQueryChange(searchView)
        return true
    }

    private fun onQueryChange(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loading.visible()
                img_search_icon.gone()
                tv_waiting_for_search.gone()
                img_failed_to_load_data.gone()
                tv_failed_to_load_data.gone()
                query?.let { onQueryTextChangeListener?.onQueryTextSubmit(query) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Intent(this@SearchUserActivity, UserFavoriteActivity::class.java).apply {
                    startActivity(this)
                }
            }

            R.id.settings -> {
                Intent(this@SearchUserActivity, SettingsActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}