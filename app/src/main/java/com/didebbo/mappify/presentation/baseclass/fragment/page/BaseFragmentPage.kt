package com.didebbo.mappify.presentation.baseclass.fragment.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivity
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseFragmentPage<VM: ViewModel>(private val viewModelClass: Class<VM>): Fragment() {
    val parentActivity: BaseActivity<VM>? by lazy { activity as? BaseActivity<VM> }
    val viewModel: ViewModel by lazy { ViewModelProvider(requireActivity())[viewModelClass] }

    private  val navController: NavController? by lazy { parentActivity?.navController }
    open fun onSupportNavigateUp(): Boolean? {
        return false
    }
}