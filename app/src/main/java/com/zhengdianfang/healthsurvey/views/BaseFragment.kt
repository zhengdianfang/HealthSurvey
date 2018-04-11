package com.zhengdianfang.healthsurvey.views

import android.os.Build
import com.zhengdianfang.healthsurvey.R
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.indeterminateProgressDialog

open class BaseFragment: SupportFragment(), AnkoLogger {

    private val REQUEST_PERMISSIONS = 0x000003
    val progressDialog by lazy { indeterminateProgressDialog(getString(R.string.loading), "") }
    protected var permissionCallback: ()->Unit = {}

    fun showDialog() {
       progressDialog.show()
    }

    fun hideDialog() {
       progressDialog.dismiss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS) {
           if (grantResults.isNotEmpty()) {
              this.permissionCallback()
           }
        }
    }

    fun requestPermission(permissions :Array<String>, callback: () ->Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_PERMISSIONS)
            this.permissionCallback = callback
        }
    }
}