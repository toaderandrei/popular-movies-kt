package com.ant.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VIEW_MODEL : ViewModel, VIEW_BINDING : ViewDataBinding> : Fragment() {

    lateinit var binding: VIEW_BINDING

    protected abstract val viewModel: VIEW_MODEL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VIEW_BINDING

    abstract fun getThisFragment(): Fragment
}