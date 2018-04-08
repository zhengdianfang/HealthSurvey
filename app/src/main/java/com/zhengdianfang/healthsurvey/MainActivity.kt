package com.zhengdianfang.healthsurvey

import android.Manifest
import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Response
import com.zhengdianfang.healthsurvey.entities.Version
import com.zhengdianfang.healthsurvey.repository.AppRepository
import com.zhengdianfang.healthsurvey.viewmodel.ProductViewModel
import com.zhengdianfang.healthsurvey.views.MainFragment
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import retrofit2.Call
import retrofit2.Callback

class MainActivity : SupportActivity() {

    val products = mutableListOf<Product>()
    private val REQUEST_PERMISSIONS = 0x000003
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

        requestPermission()
        checkUpdate()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA),
                    REQUEST_PERMISSIONS)
        }
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

    private fun downloadNewVersionApk(url: String) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setNotificationVisibility(View.VISIBLE)
        downloadManager.enqueue(request)
    }

    private fun alertUpdateDialog(version: Version?) {
        if (version != null) {
            if (version.newversion != packageManager.getPackageInfo(packageName, 0).versionName) {
                alert(version.updateInfos){
                    yesButton { downloadNewVersionApk(version?.android_url) }
                    noButton {
                        if (version.type == Version.MUST){
                            System.exit(-1)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS) {

        }
    }

}
