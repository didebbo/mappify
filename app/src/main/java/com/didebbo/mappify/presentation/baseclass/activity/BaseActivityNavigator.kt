package com.didebbo.mappify.presentation.baseclass.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.databinding.BaseActivityLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivityNavigator<VM: ViewModel>(): AppCompatActivity() {

    private lateinit var baseActivityLayoutBinding: BaseActivityLayoutBinding
    private lateinit var baseNavHostFragment: NavHostFragment

    val navController: NavController by lazy {
        baseNavHostFragment.navController
    }

    private val actionBar: ActionBar? by lazy {
        supportActionBar
    }

    val bottomNavigationView: BottomNavigationView by lazy {
        baseActivityLayoutBinding.bottomNavigationView
    }

    private val modalView: View by lazy {
        baseActivityLayoutBinding.modalView
    }

    private val loaderView: ProgressBar by lazy {
        baseActivityLayoutBinding.loaderView
    }

    private val alertView: View by lazy {
        baseActivityLayoutBinding.alertView
    }

    private val alertTextView: TextView by lazy {
        baseActivityLayoutBinding.alertTextView
    }

    private val alertConfirmButton: Button by lazy {
        baseActivityLayoutBinding.alertConfirmButton
    }

    private val alertDeleteButton: Button by lazy {
        baseActivityLayoutBinding.alertDeleteButton
    }

    private var alertConfirmAction: (()->Unit)? = null
    private var alertDeleteAction: (()->Unit)? = null

    abstract val viewModel: VM

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivityLayoutBinding = BaseActivityLayoutBinding.inflate(layoutInflater)
        baseNavHostFragment = supportFragmentManager.findFragmentById(R.id.base_nav_host_fragment) as NavHostFragment
        bottomNavigationView.visibility = View.GONE
        setContentView(baseActivityLayoutBinding.root)
        configureActivityResultLauncher()
    }

    override fun onStart() {
        super.onStart()

        hideModalView()

        alertConfirmButton.setOnClickListener {
            alertConfirmAction?.invoke()
            hideModalView()
        }
        alertDeleteButton.setOnClickListener {
            alertDeleteAction?.invoke()
            hideModalView()
        }
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

    private fun setUpNavControllerGraph(navigationId: Int) {
        try {
            navController.setGraph(navigationId)
        } catch(e: Exception) {
            e.localizedMessage?.let { Log.e("setupActionBarWithNavGraph", it) }
        }
    }

    private fun setupActionBarConfiguration(topLevelDestinations: Set<Int>?) {
        try {
            var appBarConfiguration = AppBarConfiguration(navController.graph)
            topLevelDestinations?.let {
                appBarConfiguration = AppBarConfiguration(it)
            }
            setupActionBarWithNavController(navController,appBarConfiguration)
        } catch(e: Exception) {
            e.localizedMessage?.let { Log.e("setupActionBarWithNavGraph", it) }
        }
    }

    private fun setupBottomBarWithMenu(bottomMenuId: Int) {
        try {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(bottomMenuId)
            bottomNavigationView.setupWithNavController(navController)
            bottomNavigationView.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.localizedMessage?.let { localizedMessage -> Log.e("setupActionBarWithNavGraph", localizedMessage) }
        }
    }

    private fun hideModalView() {
        loaderView.visibility = View.GONE
        alertView.visibility = View.GONE
        modalView.visibility = View.GONE
    }

    private fun showLoader(visible: Boolean) {
        val alertViewVisibility = alertView.isVisible
        loaderView.visibility = if(visible) View.VISIBLE else View.GONE
        modalView.visibility = if(alertViewVisibility) View.VISIBLE else if(visible) View.VISIBLE else View.GONE
    }

    private fun configureActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getBundleExtra("destination")?.let {
                    val destinationBundle = it
                    val resIdDestination = destinationBundle.getInt("resIdDestination")
                    val resDestinationBundle = destinationBundle.getBundle("resDestinationBundle")
                    navController.navigate(resIdDestination,resDestinationBundle)
                }
            }
        }
    }

    fun showAlertView(
        message: String,
        confirmAction: (()->Unit)? = null,
        deleteAction: (()->Unit)? = null
        ) {
        alertTextView.text = message
        alertConfirmAction = confirmAction
        alertDeleteAction = deleteAction

        alertView.visibility = View.VISIBLE
        alertDeleteButton.visibility = if(deleteAction != null) View.VISIBLE else View.GONE
        modalView.visibility = View.VISIBLE
    }

    fun configureSystemNavigation(
        navigationResId: Int,
        bottomMenuResId: Int? = null,
        topLevelDestinations: Set<Int>? = null
    ) {
        setUpNavControllerGraph(navigationResId)
        bottomMenuResId?.let { setupBottomBarWithMenu(it) }
        setupActionBarConfiguration(topLevelDestinations)
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

    fun showBackButton(showHomeAsUp: Boolean) {
        actionBar?.setDisplayHomeAsUpEnabled(showHomeAsUp)
    }

    fun loaderCoroutineScope(task: suspend ()->Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            showLoader(true)
            task.invoke()
            showLoader(false)
        }
    }

    fun navigateToIntentWithDismissDestination(intent: Intent) {
        activityResultLauncher.launch(intent)
    }
}