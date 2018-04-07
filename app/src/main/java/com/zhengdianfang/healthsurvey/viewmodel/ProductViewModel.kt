package com.zhengdianfang.healthsurvey.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.zhengdianfang.healthsurvey.repository.AppRepository

/**
 * Created by dfgzheng on 05/04/2018.
 */
class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository by lazy { AppRepository() }

    val productsLiveData by lazy { appRepository.getProducts() }
    val descriptionLiveData by lazy { appRepository.getDescription() }
}