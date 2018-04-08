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
open class AddressElection(context: Context, question: Question) : BaseComponent(context, question) {

    override fun getQuestionType(): Int {
        return Question.ADDRESS
    }

    override fun bindData2OptionsView(view: View, type: Int) {
        view.findViewById<TextView>(R.id.addressTextView).text = question.answers.answer
    }

    override fun renderOptions(type: Int): View {
        val textView = AddressTextView(context, { address -> onAddress(address, type)})
        textView.setTextColor(Color.BLACK)
        textView.setText(R.string.default_address)
        textView.id = R.id.addressTextView
        return textView
    }

    private fun onAddress(address: String, type: Int) {
        when(type){
            FRONT_OPTIONS -> question.answers?.answer = address
            END_OPTIONS -> question.answers?.answer_end = address
        }
    }

}