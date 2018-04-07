package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.zhengdianfang.healthsurvey.MainActivity
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Answer
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
class ProductCodeElection(context: Context, question: Question) : BaseComponent(context, question) {

    private var editText: EditText? = null

    override fun getQuestionType(): Int {
        return Question.AUTO_FILL
    }

    override fun verify(): Boolean {
        val content = question.answers?.answer ?: ""
        if (isRequried() && TextUtils.isEmpty(content) ) {
            Toast.makeText(context, context.getString(R.string.please_input_x_content,question.title), Toast.LENGTH_SHORT).show()
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
                question.answers?.answer = p0?.toString() ?: ""
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    fun fill(code: String) {
        editText?.setText(code)
    }

    override fun renderOptions(type: Int): View {
        return renderEditText()
    }
}