package com.didebbo.mappify.presentation.baseclass.fragment.page

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragmentDestination<VM: ViewModel>(private val viewModelClass: Class<VM>): Fragment() {

    @Suppress("UNCHECKED_CAST")
    val parentActivity: BaseActivityNavigator<VM>? by lazy { activity as? BaseActivityNavigator<VM> }
    val viewModel: VM by lazy { ViewModelProvider(requireActivity())[viewModelClass] }

    val navController: NavController? by lazy { parentActivity?.navController }
    open fun onSupportNavigateUp(): Boolean? {
        return false
    }
}