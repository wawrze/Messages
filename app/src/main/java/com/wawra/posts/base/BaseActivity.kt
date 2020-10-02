package com.wawra.posts.base

import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    var deleteDialogCallBack: (() -> Unit)? = null
    var imageUrlCallBack: ((String) -> Unit)? = null

}