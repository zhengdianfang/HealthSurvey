package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.*
import android.widget.RadioGroup.*
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Answer
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
class SingleElection(context: Context, question: Question) : BaseComponent(context, question) {

    override fun bindData2OptionsView(view: View, type: Int) {
        val radioGroup = view.findViewById<AppRadioGroup>(R.id.radioGroup)
        radioGroup.removeAllViews()
        question.options?.forEach {
            val radioButton = RadioButton(this.context)
            radioButton.text = it.name
            radioButton.setTextColor(Color.BLACK)
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = this.context.resources.getDimension(R.dimen.line_margin_top).toInt()
            radioGroup.addView(radioButton)
        }
        radioGroup.setOnCheckedChangeListener { view, i ->
            setAnswer(i % question.options?.count()!! - 1, (view as AppRadioGroup).type)
        }
    }

    private fun setAnswer(index: Int, type: Int) {
        if (question.answers == null) {
            question.answers = Answer("", "", "", "")
        }
        if (type == FRONT_OPTIONS) {
            this.question.answers?.answer = index.toString()
        } else {
            this.question.answers?.answer_end = index.toString()
        }
    }

    override fun renderOptions(type: Int): View {
        val radioGroup = AppRadioGroup(context, type)
        radioGroup.orientation = RadioGroup.VERTICAL
        radioGroup.id = R.id.radioGroup
        return radioGroup
    }

    override fun getQuestionType(): Int {
        return Question.SINGLE_ELECTION
    }
}