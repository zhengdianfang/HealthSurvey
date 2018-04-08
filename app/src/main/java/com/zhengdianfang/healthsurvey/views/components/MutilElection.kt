package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup.LayoutParams
import android.widget.TextView
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
class MutilElection(context: Context, question: Question) : BaseComponent(context, question) {

    override fun bindData2OptionsView(view: View, type: Int) {
        val lastCheckeds = getCheckedCache(question, type)
        val checkboxGroup = view.findViewById<ViewGroup>(R.id.checkLinearLayout)
        checkboxGroup.removeAllViews()
        question.options?.forEachIndexed { index, option ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val checkBox = AppCheckBox(this.context, type)
            checkBox.isChecked = lastCheckeds.contains(index.toString())
            checkBox.setOnCheckedChangeListener { view, b ->
                setAnswer(b, index, (view as AppCheckBox).type)
            }
            linearLayout.addView(checkBox)
            val textView = TextView(context)
            textView.text = option.name
            textView.setTextColor(Color.BLACK)
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutParams.leftMargin = this.context.resources.getDimension(R.dimen.line_margin_top).toInt()
            linearLayout.addView(textView, layoutParams)
            checkboxGroup.addView(linearLayout)
        }
    }

    private fun getCheckedCache(question: Question, type: Int): MutableList<String> {
        val cacheStr = if (type == FRONT_OPTIONS) question.answers.answer  else question.answers.answer_end
        if (TextUtils.isEmpty(cacheStr).not()) {
            return cacheStr.split(",").toMutableList()
        }
        return mutableListOf<String>()
    }

   private fun setAnswer(checked: Boolean, index: Int, type: Int) {
       if (type == FRONT_OPTIONS) {
           var list = this.question.answers?.answer?.split(",")?.toMutableList()
           if (list == null) {
               list = mutableListOf()
           }
           if (checked) {
               list?.add(index.toString())
           } else {
               list?.remove(index.toString())
           }
           this.question.answers?.answer =  TextUtils.join("," , list)
       } else {
           var list = this.question.answers?.answer_end?.split(",")?.toMutableList()
           if (list == null) {
               list = mutableListOf()
           }
           if (checked) {
               list?.add(index.toString())
           } else {
               list?.remove(index.toString())
           }
           this.question.answers?.answer_end =  TextUtils.join("," , list)
       }
   }



    override fun getQuestionType(): Int {
        return  Question.MULTI_ELECTION
    }

    override fun renderOptions(type: Int): View {
        val linearLayout = LinearLayout(this.context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.id = R.id.checkLinearLayout
        return linearLayout
    }

}