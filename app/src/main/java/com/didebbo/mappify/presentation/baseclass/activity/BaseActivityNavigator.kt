package com.didebbo.mappify.presentation.baseclass.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.BaseActivityLayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

abstract class BaseActivityNavigator<VM: ViewModel>(): AppCompatActivity() {

    private lateinit var baseActivityLayoutBinding: BaseActivityLayoutBinding
    private lateinit var baseNavHostFragment: NavHostFragment

    val navController: NavController by lazy {
        baseNavHostFragment.navController
    }

    val bottomNavigationView: BottomNavigationView by lazy {
        baseActivityLayoutBinding.bottomNavigationView
    }

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivityLayoutBinding = BaseActivityLayoutBinding.inflate(layoutInflater)
        baseNavHostFragment = supportFragmentManager.findFragmentById(R.id.base_nav_host_fragment) as NavHostFragment
        bottomNavigationView.visibility = View.GONE
        setContentView(baseActivityLayoutBinding.root)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSupportNavigateUp(): Boolean {
        val currentFragment = baseNavHostFragment.childFragmentManager.primaryNavigationFragment as? BaseFragmentDestination<VM>
        currentFragment?.let {
            if(it.onSupportNavigateUp() == false)
                return navController.navigateUp()
        }
        return false
    }

    fun setupActionBarWithNavGraph(navigationId: Int) {
        try {
            navController.setGraph(navigationId)
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController,appBarConfiguration)
        } catch(e: Exception) {
            e.localizedMessage?.let { Log.e("setupActionBarWithNavGraph", it) }
        }
    }

    fun setupBottomBarWithMenu(bottomMenu: Int) {
        try {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(bottomMenu)
            bottomNavigationView.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.localizedMessage?.let { localizedMessage -> Log.e("setupActionBarWithNavGraph", localizedMessage) }
        }
    }

    fun navigateToDestination(resId: Int) {
        navController.navigate(resId)
    }
    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateToActivity(intent: Intent) {
        startActivity(intent)
    }

    fun hideSystemKeyboard() {
        currentFocus?.let {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}