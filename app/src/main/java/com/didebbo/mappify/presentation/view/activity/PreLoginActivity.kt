package com.didebbo.mappify.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivity
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.PreLoginActivityLayoutBinding

class PreLoginActivity: BaseActivity() {

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setupActionBarWithNavGraph(R.navigation.pre_login_navigation)
    }
}