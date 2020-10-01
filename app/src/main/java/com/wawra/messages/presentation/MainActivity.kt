package com.wawra.messages.presentation

import android.os.Bundle
import androidx.navigation.findNavController
import com.wawra.messages.R
import com.wawra.messages.base.BaseActivity
import com.wawra.messages.base.Navigation

class MainActivity : BaseActivity(), Navigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun getNavigationController() = findNavController(R.id.activity_main_fragment)

}