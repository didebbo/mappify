package com.didebbo.mappify.presentation.baseclass.fragment.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityInterface

interface BaseFragmentPageInterface {
    val parentActivity: BaseActivityInterface?
    fun onBackAction(): Boolean
}

abstract class BaseFragmentPage: Fragment(), BaseFragmentPageInterface {
    override val parentActivity: BaseActivityInterface?
        get() = activity as? BaseActivityInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflateView(inflater,container,savedInstanceState)
    }

    override fun onBackAction(): Boolean {
        return false
    }

    abstract fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
}