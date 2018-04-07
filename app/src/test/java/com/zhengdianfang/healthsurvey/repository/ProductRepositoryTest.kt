package com.zhengdianfang.healthsurvey.repository

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

/**
 * Created by dfgzheng on 05/04/2018.
 */
class ProductRepositoryTest {

    private val productRepository = AppRepository()

    @Test
    fun getProducts() {
    }

}