package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.databinding.MenuPageLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel

class MenuPage: BaseFragmentDestination<PostLoginViewModel>(PostLoginViewModel::class.java) {

    private var binding: MenuPageLayoutBinding? = null
    private val postLoginActivity: PostLoginActivity? by lazy { parentActivity as? PostLoginActivity }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MenuPageLayoutBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.logOutItemMenu?.setOnClickListener{
            postLoginActivity?.navigateToPreLogin()
        }
    }
}