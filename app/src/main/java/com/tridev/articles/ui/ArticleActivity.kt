package com.tridev.articles.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tridev.articles.R
import com.tridev.articles.databinding.ActivityArticleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityArticleBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupNav()
    }

    private fun setupNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        mBinding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.articlesFragment -> showBottomNav()
                R.id.searchArticleFragment -> showBottomNav()
                R.id.articleSavedFragment -> showBottomNav()
                else -> {
                    hideBottomNav()
                }
            }
        }
    }

    private fun showBottomNav() {
        mBinding.bottomNavigationView.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }

    private fun hideBottomNav() {
        mBinding.bottomNavigationView.visibility = View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController.popBackStack()
        return super.onOptionsItemSelected(item)
    }
}