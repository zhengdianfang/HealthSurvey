package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Answer
import com.zhengdianfang.healthsurvey.entities.Condition
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
open class InputElection(context: Context, question: Question) : BaseComponent(context, question) {

    protected var editText: EditText? = null

    override fun getQuestionType(): Int {
        return Question.INPUT
    }

    override fun verify(): Boolean {
        val content = editText?.text?.toString() ?: ""
        if (isRequried() && TextUtils.isEmpty(content) ) {
            Toast.makeText(context, context.getString(R.string.please_input_x_content, question.title), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun bindData2OptionsView(view: View, type: Int) {
        editText = view.findViewById(R.id.editText)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (question.answers == null) {
                    question.answers = Answer("", "", "", "")
                }
                if (type == FRONT_OPTIONS) {
                    question.answers?.answer = p0?.toString() ?: ""
                } else {
                    question.answers?.answer_end = p0?.toString() ?: ""
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        if (question.condition?.checkType == Condition.NUMBER_TYPE.toString()) {
           editText?.inputType = InputType.TYPE_CLASS_NUMBER
        }
        val unitTextView = view.findViewById<TextView>(R.id.unitTextView)
        if (TextUtils.isEmpty(question.condition?.unit).not()) {
            unitTextView?.visibility = View.VISIBLE
            unitTextView?.text = question.condition?.unit
        } else {
            unitTextView?.visibility = View.GONE
        }

    }

    override fun renderOptions(type: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.survey_input_item_layout, null)
    }

}