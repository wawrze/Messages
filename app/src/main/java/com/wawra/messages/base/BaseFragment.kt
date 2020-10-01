package com.wawra.messages.base

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {

    protected var navigate: NavController? = null

    override fun onResume() {
        super.onResume()
        navigate = (activity as? Navigation)?.getNavigationController()
    }

    override fun onPause() {
        super.onPause()
        navigate = null
    }

    @MainThread
    protected fun <T> LiveData<T>.observe(action: (T) -> Unit) {
        this.observe(
            this@BaseFragment.viewLifecycleOwner,
            { action.invoke(it) }
        )
    }

}