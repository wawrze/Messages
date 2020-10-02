package com.wawra.posts.base

import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    var dialogCallBack: (() -> Unit)? = null
    var imageUrlCallBack: ((String) -> Unit)? = null
    var confirmationDialogCallback: (() -> Unit)? = null

}