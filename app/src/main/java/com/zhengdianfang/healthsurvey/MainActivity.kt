package com.zhengdianfang.healthsurvey

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Response
import com.zhengdianfang.healthsurvey.entities.Version
import com.zhengdianfang.healthsurvey.repository.AppRepository
import com.zhengdianfang.healthsurvey.viewmodel.ProductViewModel
import com.zhengdianfang.healthsurvey.views.MainFragment
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback

class MainActivity : SupportActivity() {

    val products = mutableListOf<Product>()
    private val appRepository by lazy { AppRepository() }

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

//        requestPermission()
        checkUpdate()
    }


    private fun checkUpdate() {
        appRepository.upgradleApp(object : Callback<Response<Version>> {
            override fun onResponse(call: Call<Response<Version>>?, response: retrofit2.Response<Response<Version>>?) {
                alertUpdateDialog(response?.body()?.data)
            }

            override fun onFailure(call: Call<Response<Version>>?, t: Throwable?) {
            }

        })
    }


    private fun alertUpdateDialog(version: Version?) {
        if (version != null) {
            if (version.newversion != packageManager.getPackageInfo(packageName, 0).versionName) {
                val alertDialog = AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_MinWidth)
                        .setPositiveButton(getString(R.string.upgradel), { _, _ ->
                            browse(version?.android_url)
                            System.exit(-1)
                        })
                        .setMessage(R.string.goon_dailog_content)
                        .create()

                if (version.type != Version.MUST) {
                    alertDialog.setCancelable(false)
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), {_, _  ->})
                }
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()

            }
        }
    }

}
