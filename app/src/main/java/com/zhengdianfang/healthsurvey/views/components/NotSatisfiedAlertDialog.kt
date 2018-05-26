package com.zhengdianfang.healthsurvey.views.components

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioGroup
import com.zhengdianfang.healthsurvey.R

class NotSatisfiedAlertDialog(context: Context?, themeResId: Int) : AlertDialog(context, themeResId) {

    private val inflate: RadioGroup by lazy { LayoutInflater.from(context).inflate(R.layout.not_sattisfied_reasons_layout, null) as RadioGroup }

    init {
        setView(inflate)
    }

    fun getRadioCheckedIndex(): Int {
        return inflate.indexOfChild(inflate.findViewById(inflate.checkedRadioButtonId))
    }
}