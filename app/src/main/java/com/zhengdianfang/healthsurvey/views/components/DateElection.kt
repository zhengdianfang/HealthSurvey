package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
open class DateElection(context: Context, question: Question) : BaseComponent(context, question) {

    private var dateTextView: TextView? = null


    override fun getQuestionType(): Int {
        return Question.DATE
    }


    override fun renderOptions(type: Int): View {
        val textView = DateTextView(context, { date -> onDate(date, type)})
        textView.setTextColor(Color.BLACK)
        textView.setText(R.string.default_date)
        textView.id = R.id.dateTextView
        return textView

    }

    override fun bindData2OptionsView(view: View, type: Int) {
    }

    private fun onDate(address: String, type: Int) {
        when(type){
            FRONT_OPTIONS -> question.answers?.answer = address
            END_OPTIONS -> question.answers?.answer_end = address
        }
    }

}