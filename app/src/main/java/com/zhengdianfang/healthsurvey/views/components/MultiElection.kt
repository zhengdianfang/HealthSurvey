package com.zhengdianfang.healthsurvey.views.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioGroup.LayoutParams
import android.widget.TextView
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
class MultiElection(context: Context, question: Question) : BaseComponent(context, question) {

    private val editText by lazy {
        val editText = EditText(context)
        editText.setTextColor(Color.BLACK)
        editText.layoutParams = FrameLayout.LayoutParams(context.resources.getDimension(R.dimen.dialog_edittext_width).toInt(), FrameLayout.LayoutParams.WRAP_CONTENT)
        editText.setLines(4)
        editText.gravity = Gravity.TOP
        editText.setBackgroundResource(R.drawable.default_input)
        editText
    }

    private val alertDialog by lazy {
        val dialog = SingleElectionAlertDialog(context, android.R.style.Theme_Material_Light_Dialog_MinWidth)
        dialog.setButton(Dialog.BUTTON_POSITIVE, context.resources.getString(R.string.confrim), { dialog, _ ->

            val content = editText.text.toString()
            if (TextUtils.isEmpty(content).not()) {
                val singleElectionAlertDialog = dialog as SingleElectionAlertDialog
                setAnswer(true, singleElectionAlertDialog.radioIndex, singleElectionAlertDialog.type, editText.text.toString())
            }
        })
        dialog.setButton(Dialog.BUTTON_NEGATIVE, context.getString( R.string.cancel), { _, _ ->
            dialog.dismiss()
        })
        dialog.setTitle(context.resources.getString(R.string.please_input_other_reason))
        dialog.setView(editText)
        dialog.setOnDismissListener{
           editText.setText("")
        }
        dialog
    }
    override fun bindData2OptionsView(view: View, type: Int) {
        val lastCheckeds = getCheckedCache(question, type)
        val checkboxGroup = view.findViewById<ViewGroup>(R.id.checkLinearLayout)
        checkboxGroup.removeAllViews()
        question.options?.forEachIndexed { index, option ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val checkBox = AppCheckBox(this.context, type)
            val isChecked = lastCheckeds.contains(index.toString())
            if (isChecked) {
                checkBox.isChecked = isChecked
                this.onSelectOption?.invoke(question.qid, option)
            }
            checkBox.setOnCheckedChangeListener { view, b ->
                this.onSelectOption?.invoke(question.qid, option)
                if (option?.isMore()!! && b) {
                    alertDialog.show(index, (view as AppCheckBox).type)
                } else {
                    setAnswer(b, index, (view as AppCheckBox).type)
                }
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

   private fun setAnswer(checked: Boolean, index: Int, type: Int, extStr: String = "") {
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
           this.question.answers?.answer_ext = extStr
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
           this.question.answers?.answer_ext_end  = extStr
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