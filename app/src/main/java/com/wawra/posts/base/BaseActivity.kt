package com.wawra.posts.base

import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    var dialogCallback: (() -> Unit)? = null

}