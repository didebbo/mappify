package com.didebbo.mappify.presentation.baseclass.fragment.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseFragmentPage: Fragment() {

    val parentActivity: BaseActivity? by lazy { activity as? BaseActivity }
    private  val navController: NavController? by lazy { parentActivity?.navController }
    open fun onSupportNavigateUp(): Boolean? {
        return false
    }
}