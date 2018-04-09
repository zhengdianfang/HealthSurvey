package com.zhengdianfang.healthsurvey.views

import com.zhengdianfang.healthsurvey.R
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.indeterminateProgressDialog

open class BaseFragment: SupportFragment(), AnkoLogger {

    val progressDialog by lazy { indeterminateProgressDialog(getString(R.string.loading), "") }

    fun showDialog() {
       progressDialog.show()
    }

    fun hideDialog() {
       progressDialog.dismiss()
    }
}