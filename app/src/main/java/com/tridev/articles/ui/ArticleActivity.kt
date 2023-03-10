package com.tridev.articles.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tridev.articles.R
import com.tridev.articles.databinding.ActivityArticleBinding
import com.tridev.articles.notification.NotificationManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityArticleBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolbar)
        setupNav()
    }

    private fun setupNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        mBinding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.articlesFragment -> {
                    showBottomNav()
                    supportActionBar?.title = "Articles"
                }
                R.id.searchArticleFragment -> {
                    showBottomNav()
                    supportActionBar?.title = "Search Articles"

                }
                R.id.articleSavedFragment -> {
                    showBottomNav()
                    supportActionBar?.title = "Saved Articles"
                }
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
        supportActionBar?.title = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController.popBackStack()
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManager.askNotificationPermissionForApi33orAbove(this)
        }
    }
}