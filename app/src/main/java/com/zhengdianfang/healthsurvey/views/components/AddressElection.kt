package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import chihane.jdaddressselector.BottomDialog
import chihane.jdaddressselector.OnAddressSelectedListener
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.R.id.addressTextView
import com.zhengdianfang.healthsurvey.R.id.title
import com.zhengdianfang.healthsurvey.entities.Answer
import com.zhengdianfang.healthsurvey.entities.Question
import me.yokeyword.fragmentation.SupportActivity
import java.util.*

/**
 * Created by dfgzheng on 05/04/2018.
 */
open class AddressElection(context: Context, question: Question) : BaseComponent(context, question) {

    override fun getQuestionType(): Int {
        return Question.ADDRESS
    }

    override fun bindData2OptionsView(view: View, type: Int) {
    }

    override fun renderOptions(type: Int): View {
        val textView = AddressTextView(context, { address -> onAddress(address, type)})
        textView.setTextColor(Color.BLACK)
        textView.setText(R.string.default_address)
        textView.id = R.id.dateTextView
        return textView
    }

    private fun onAddress(address: String, type: Int) {
       if (question.answers == null) {
           question.answers = Answer("", "", "", "")
       }
        when(type){
            FRONT_OPTIONS -> question.answers?.answer = address
            END_OPTIONS -> question.answers?.answer_end = address
        }
    }

}