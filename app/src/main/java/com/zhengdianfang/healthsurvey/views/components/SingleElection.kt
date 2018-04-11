package com.zhengdianfang.healthsurvey.views.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RadioGroup.LayoutParams
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
class SingleElection(context: Context, question: Question) : BaseComponent(context, question) {

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
                        setAnswer(singleElectionAlertDialog.radioIndex, singleElectionAlertDialog.type, editText.text.toString())
                    }
                })
        dialog.setButton(Dialog.BUTTON_NEGATIVE, context.getString( R.string.cancel), { _, _ ->
            dialog.dismiss()
                })
        dialog.setTitle(context.resources.getString(R.string.please_input_other_reason))
        dialog.setView(editText)
        dialog
    }

    override fun bindData2OptionsView(view: View, type: Int) {
        val radioGroup = view.findViewById<AppRadioGroup>(R.id.radioGroup)
        radioGroup.removeAllViews()
        question.options?.forEachIndexed { index, option ->
            val radioButton = RadioButton(this.context)
            radioButton.text = option.name
            radioButton.setTextColor(Color.BLACK)
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = this.context.resources.getDimension(R.dimen.line_margin_top).toInt()
            radioGroup.addView(radioButton)
        }
        radioGroup.setOnCheckedChangeListener { groupView, checkId ->
            val indexOfChild = groupView.indexOfChild(groupView.findViewById<RadioButton>(checkId))
            if (question.options?.get(indexOfChild)?.isMore()!!) {
                alertDialog.show(indexOfChild, (groupView as AppRadioGroup).type)
            } else {
                setAnswer(indexOfChild, (groupView as AppRadioGroup).type)
            }
        }
        val lastCheckedIndex = getLastChecked(type)
        radioGroup.check(radioGroup.getChildAt(lastCheckedIndex).id)
    }

    private fun getLastChecked(type: Int): Int {
        if (type == FRONT_OPTIONS) {
            if (TextUtils.isEmpty(question.answers.answer).not()) {
                return question.answers.answer.toInt()
            }
        } else {
            if (TextUtils.isEmpty(question.answers.answer_end).not()) {
                return question.answers.answer_end.toInt()
            }
        }
        return -1
    }

    private fun setAnswer(index: Int, type: Int, extStr: String = "") {
        if (type == FRONT_OPTIONS) {
            this.question.answers?.answer = index.toString()
            this.question.answers?.answer_ext = extStr
        } else {
            this.question.answers?.answer_end = index.toString()
            this.question.answers?.answer_ext_end = extStr
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