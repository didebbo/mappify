package com.didebbo.mappify.presentation.fragment.destination.postlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import com.github.didebbo.mappify.databinding.MapViewLayoutBinding

class MapViewPage: BaseFragmentDestination<PostLoginViewModel>(PostLoginViewModel::class.java) {

    private lateinit var mapViewLayoutBinding: MapViewLayoutBinding
    private val postLoginActivity: PostLoginActivity? by lazy { parentActivity as? PostLoginActivity }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewLayoutBinding = MapViewLayoutBinding.inflate(inflater,container,false)
        return  mapViewLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewLayoutBinding.logOutButton.setOnClickListener {
            postLoginActivity?.navigateToPreLogin()
        }
    }
}