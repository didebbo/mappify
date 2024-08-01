package com.didebbo.mappify.presentation.baseclass.fragment.page

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivity

abstract class BaseFragmentPage<VM: ViewModel>(private val viewModelClass: Class<VM>): Fragment() {
    val parentActivity: BaseActivity<VM>? by lazy { activity as? BaseActivity<VM> }
    val viewModel: VM by lazy { ViewModelProvider(requireActivity())[viewModelClass] }

    private  val navController: NavController? by lazy { parentActivity?.navController }
    open fun onSupportNavigateUp(): Boolean? {
        return false
    }
}