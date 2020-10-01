package com.wawra.messages.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.wawra.messages.R
import com.wawra.messages.base.BaseFragment
import com.wawra.messages.base.ViewModelProviderFactory
import javax.inject.Inject

class MainFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

}