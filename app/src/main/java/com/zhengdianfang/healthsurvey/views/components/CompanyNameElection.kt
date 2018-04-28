package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.zhengdianfang.healthsurvey.MainActivity
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
class CompanyNameElection(context: Context, question: Question) : BaseComponent(context, question) {

    private var editText: AutoCompleteTextView? = null

    override fun getQuestionType(): Int {
        return Question.COMPANY_ELECTION
    }

    override fun verify(): Boolean {
        val content = question?.answers?.answer
        if (isRequried() && TextUtils.isEmpty(content) ) {
            Toast.makeText(context, context.getString(R.string.please_input_x_content, question.title), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun bindData2View() {
        super.bindData2View()
        editText = rootView.findViewById(R.id.editText)
        editText?.setText(question.answers.answer)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                question.answers?.answer = p0?.toString() ?: ""
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        if (context is MainActivity) {
            initAdapter(context)
            editText?.setOnItemClickListener { _, _, _, _ ->
                val input = editText?.text.toString()
                question.answers?.answer = input
            }
        }

    }

    private fun initAdapter(context: MainActivity) {
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.products.map { it.factory }.filter { !TextUtils.isEmpty(it) }.toMutableList())
        editText?.setAdapter(adapter)
    }

    override fun bindData2OptionsView(view: View, type: Int) {

    }

    override fun renderOptions(type: Int): View {
        val editText = AutoCompleteTextView(this.context)
        editText.threshold = 1
        editText.setTextColor(Color.BLACK)
        editText.id = R.id.editText
        editText.setBackgroundResource(R.drawable.default_input)
        return editText
    }

}