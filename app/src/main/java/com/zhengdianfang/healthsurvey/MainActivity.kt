package com.zhengdianfang.healthsurvey

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.viewmodel.ProductViewModel
import com.zhengdianfang.healthsurvey.views.MainFragment
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment

class MainActivity : SupportActivity() {

    val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = SupportFragment.instantiate(this, MainFragment::class.java.name) as ISupportFragment
        loadRootFragment(android.R.id.content, fragment)


        val productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        productViewModel.productsLiveData.observe(this, Observer<MutableList<Product>> {
            products.clear()
            if (null != it)
                products.addAll(it)
        })

    }
}
