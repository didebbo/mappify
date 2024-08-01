package com.didebbo.mappify.presentation.fragment.destination

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.RegisterPageLayoutBinding
import com.google.android.material.snackbar.Snackbar

class RegisterPage: BaseFragmentPage() {

    private lateinit var registerPageLayoutBinding: RegisterPageLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerPageLayoutBinding = RegisterPageLayoutBinding.inflate(inflater,container,false)
        return registerPageLayoutBinding.root
    }
}