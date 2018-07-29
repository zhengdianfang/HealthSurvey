package com.zhengdianfang.healthsurvey

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.reflect.TypeToken
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.zhengdianfang.healthsurvey.datasource.cloud.WebService
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.entities.Response
import com.zhengdianfang.healthsurvey.entities.Version
import com.zhengdianfang.healthsurvey.repository.AppRepository
import com.zhengdianfang.healthsurvey.viewmodel.ProductViewModel
import com.zhengdianfang.healthsurvey.views.GroupListFragment
import com.zhengdianfang.healthsurvey.views.MainFragment
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.browse
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.Call
import retrofit2.Callback

class MainActivity : SupportActivity(), AnkoLogger {

    val products by lazy {
        var list = mutableListOf<Product>()
        val json = defaultSharedPreferences.getString(PRODUCT_SAVE_KEY, "")
        if (!TextUtils.isEmpty(json)){
            list =  WebService.gson.fromJson<MutableList<Product>>(json, object : TypeToken<List<Product>>() {}.type)
        }
        list

    }
    private val appRepository by lazy { AppRepository() }
    private val PRODUCT_SAVE_KEY = "product_save_key"
    private var retryCount = 3
    private val advices = hashMapOf<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = SupportFragment.instantiate(this, MainFragment::class.java.name) as ISupportFragment
        loadRootFragment(android.R.id.content, fragment)

        val productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        requestProductList(productViewModel)

        checkUpdate()
    }

    fun addAdvice(question: Question, adviceStr: String, isChecked: Boolean) {
        if (question.type ==  Question.SINGLE_ELECTION.toString()) {
            advices[question.qid] = adviceStr
        } else if (question.type == Question.MULTI_ELECTION.toString()) {
            if (isChecked) {
                advices[question.qid] = advices[question.qid] + "," + adviceStr
            } else {
                advices[question.qid] = advices[question.qid]?.replace(adviceStr, "") ?: ""
            }
        }
        findFragment(GroupListFragment::class.java)?.update()
    }

    fun isAdvice(groupId: String): Boolean {
        val groupIds = arrayListOf<String>()
        advices.values.forEach { groupIdStr ->
           groupIds.addAll(groupIdStr.split(","))
        }
        Log.d("advicelist", groupIds.toString())
        return groupIds.contains(groupId)
    }

    private fun requestProductList(productViewModel: ProductViewModel) {
        productViewModel.productsLiveData.observe(this, Observer<MutableList<Product>> {
            products.clear()
            if (null != it && !it.isEmpty()) {
                products.addAll(it)
                defaultSharedPreferences.edit().putString(PRODUCT_SAVE_KEY, WebService.gson.toJson(it)).apply()
            } else if (retryCount > 0){
                --retryCount
                requestProductList(productViewModel)
            }
        })
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
            if (version.newversion > packageManager.getPackageInfo(packageName, 0).versionName) {
                val alertDialog = AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_MinWidth)
                        .setPositiveButton(getString(R.string.upgradel), { _, _ ->
                            browse(version?.android_url)
                            System.exit(-1)
                        })
                        .setMessage(version?.updateInfos)
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
