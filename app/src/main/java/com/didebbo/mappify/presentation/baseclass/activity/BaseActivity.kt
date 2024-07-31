package com.didebbo.mappify.presentation.baseclass.activity

import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.BaseActivityLayoutBinding


interface BaseActivityInterface {
    val viewBinding: ViewBinding
    val navController: NavController
}
abstract class BaseActivity: AppCompatActivity(), BaseActivityInterface {

    private lateinit var baseActivityLayoutBinding: BaseActivityLayoutBinding
    private lateinit var baseNavHostFragment: NavHostFragment

    override val viewBinding: ViewBinding
        get() = baseActivityLayoutBinding

    override val navController: NavController
        get() = baseNavHostFragment.navController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivityLayoutBinding = BaseActivityLayoutBinding.inflate(layoutInflater)
        baseNavHostFragment = supportFragmentManager.findFragmentById(R.id.base_nav_host_fragment) as NavHostFragment

        setContentView(baseActivityLayoutBinding.root)
        afterOnCreate(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        val currentFragment = baseNavHostFragment.childFragmentManager.primaryNavigationFragment as? BaseFragmentPage
        currentFragment?.let { currentFragment ->
            Log.i("GN", "$currentFragment")
            if(!currentFragment.onBackAction()) return navController.navigateUp()
        }
        return super.onSupportNavigateUp()
    }

    fun setupActionBarWithNavGraph(graphResId: Int) {
        navController.setGraph(graphResId)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    open fun afterOnCreate(savedInstanceState: Bundle?) {}
}