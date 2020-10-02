package com.wawra.posts.presentation

import android.os.Bundle
import androidx.navigation.findNavController
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.Navigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), Navigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(activity_main_toolbar)
    }

    override fun getNavigationController() = findNavController(R.id.activity_main_fragment)

    override fun onSupportNavigateUp() = getNavigationController().navigateUp()

}