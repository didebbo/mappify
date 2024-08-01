package com.didebbo.mappify.presentation.baseclass.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.BaseActivityLayoutBinding

abstract class BaseActivity<VM: ViewModel>(): AppCompatActivity() {

    private lateinit var baseActivityLayoutBinding: BaseActivityLayoutBinding
    private lateinit var baseNavHostFragment: NavHostFragment

    val navController: NavController by lazy {
        baseNavHostFragment.navController
    }

    abstract val navigationId: Int
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivityLayoutBinding = BaseActivityLayoutBinding.inflate(layoutInflater)
        baseNavHostFragment = supportFragmentManager.findFragmentById(R.id.base_nav_host_fragment) as NavHostFragment
        setupActionBarWithNavGraph()
        setContentView(baseActivityLayoutBinding.root)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSupportNavigateUp(): Boolean {
        val currentFragment = baseNavHostFragment.childFragmentManager.primaryNavigationFragment as? BaseFragmentPage<VM>
        currentFragment?.let {
            if(it.onSupportNavigateUp() == false)
                return navController.navigateUp()
        }
        return false
    }

    private fun setupActionBarWithNavGraph() {
        navController.setGraph(navigationId)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController,appBarConfiguration)
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
}