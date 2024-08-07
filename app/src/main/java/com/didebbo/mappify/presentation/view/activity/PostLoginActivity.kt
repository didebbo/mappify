package com.didebbo.mappify.presentation.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.ActivityNavigator
import com.didebbo.mappify.R
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostLoginActivity: BaseActivityNavigator<PostLoginViewModel>() {

    override val viewModel: PostLoginViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var onDismissDestination: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSystemNavigation(
            R.navigation.post_login_navigation,
            R.menu.post_login_bottom_menu,
            setOf(
                R.id.map_view_page_navigation_fragment,
                R.id.menu_page_navigation_fragment
            )
        )
        configureActivityResultLauncher()
    }

    private fun configureActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onDismissDestination?.let {
                    navController.navigate(it)
                }
            }
        }
    }

    fun navigateToPreLogin() {
        finish()
    }

    fun navigateToIntentWithDismissDestination(intent: Intent, destination: Int) {
        onDismissDestination = destination
        activityResultLauncher.launch(intent)
    }
}