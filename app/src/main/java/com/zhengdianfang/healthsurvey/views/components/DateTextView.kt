package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.widget.TextView
import com.zhengdianfang.healthsurvey.R
import me.yokeyword.fragmentation.SupportActivity
import java.util.*

/**
 * Created by dfgzheng on 07/04/2018.
 */
class DateTextView(context: Context?, private val onDate: (date: String) -> Unit) : TextView(context) {

    private val datePickDialog by lazy {
        val calendar = Calendar.getInstance()
        val datePickDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickDialog
    }

    private val onDateSetListener =
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val dateStr = context?.getString(R.string.input_date, year, monthOfYear, dayOfMonth)!!
                this?.text = dateStr
                onDate(dateStr)
            }


    init {
        setOnClickListener {
            if (context is SupportActivity) {
                datePickDialog.show((context as SupportActivity).fragmentManager, "dateDialog")
            }
        }
    }
}