package com.didebbo.mappify.presentation.baseclass.fragment.page

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivity
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel

abstract class BaseFragmentPage<VM: ViewModel>(private val viewModelClass: Class<VM>): Fragment() {

    @Suppress("UNCHECKED_CAST")
    val parentActivity: BaseActivity<VM>? by lazy { activity as? BaseActivity<VM> }
    val viewModel: VM by lazy { ViewModelProvider(requireActivity())[viewModelClass] }

    private  val navController: NavController? by lazy { parentActivity?.navController }
    open fun onSupportNavigateUp(): Boolean? {
        return false
    }
}