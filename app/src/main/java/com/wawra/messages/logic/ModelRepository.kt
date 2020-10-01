package com.wawra.messages.logic

import com.wawra.messages.database.daos.ModelDao
import com.wawra.messages.network.ApiInterface
import javax.inject.Inject

class ModelRepository @Inject constructor(
    private val modelDao: ModelDao,
    private val api: ApiInterface
) {


}