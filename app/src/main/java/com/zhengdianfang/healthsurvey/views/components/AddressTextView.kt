package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import chihane.jdaddressselector.BottomDialog
import chihane.jdaddressselector.OnAddressSelectedListener
import com.zhengdianfang.healthsurvey.R

/**
 * Created by dfgzheng on 07/04/2018.
 */
class AddressTextView(context: Context?, private val onAnswer: (answer: String) -> Unit) : TextView(context) {

    private val addressDialog by lazy {
        val dialog = BottomDialog(context)
        dialog.setOnAddressSelectedListener(onAddressSelectedListener)
        dialog
    }

    private val onAddressSelectedListener = OnAddressSelectedListener { province, city, county, street ->
        val address = "${province.name}${city.name}${county.name}"
        this.text = address
        onAnswer(address)
    }

    init {
        setTextColor(Color.BLACK)
        setText(R.string.default_address)
        setOnClickListener { addressDialog.show() }
    }

}