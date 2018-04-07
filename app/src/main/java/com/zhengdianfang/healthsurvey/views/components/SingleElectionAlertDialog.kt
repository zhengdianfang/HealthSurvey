package com.zhengdianfang.healthsurvey.views.components

import android.app.AlertDialog
import android.content.Context

class SingleElectionAlertDialog(context: Context?, themeResId: Int) : AlertDialog(context, themeResId) {
    var radioIndex = -1
    var type = -1
    fun show(index: Int, type: Int) {
        this.radioIndex = index
        this.type = type
        super.show()
    }
}