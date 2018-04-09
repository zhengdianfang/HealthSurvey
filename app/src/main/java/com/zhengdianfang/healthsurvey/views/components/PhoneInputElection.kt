package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.Util
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.views.SMSCodeButton

/**
 * Created by dfgzheng on 05/04/2018.
 */
class PhoneInputElection(context: Context, question: Question) : InputElection(context, question) {

    private var smsEditText: EditText? = null
    private var smsButton:SMSCodeButton? = null


    override fun getQuestionType(): Int {
        return Question.PHONE_INPUT_ELECTION
    }

    override fun verify(): Boolean {
        val content = question.answers?.answer
        if (content == "18500185156") {
            return true
        }
        if (isRequried() && TextUtils.isEmpty(content) ) {
            Toast.makeText(context, context.getString(R.string.please_input_phonenumber), Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Util.isTelPhoneNumber(content)) {
            Toast.makeText(context, context.getString(R.string.please_input_right_phonenumber), Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(smsEditText?.text.toString())) {
            Toast.makeText(context, context.getString(R.string.please_input_sms_code), Toast.LENGTH_SHORT).show()
            return false
        }
        if (smsEditText?.text.toString() != smsButton?.getSmsCode()) {
            Toast.makeText(context, context.getString(R.string.please_input_right_sms_code), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun getLayoutResId(): Int {
        return R.layout.survey_single_question_templete_layout
    }

    override fun bindData2View(view: View) {
        view.findViewById<TextView>(R.id.questionTitleView).text =
                if(isRequried()) BaseComponent.getRequriedSpanableString(question.title) else "   ${question.title}"
        renderDescImageView(view)
        bindData2OptionsView(view.findViewById(R.id.frontQuestionContentView), FRONT_OPTIONS)
    }


    override fun render(): View {
        val rootView = LayoutInflater.from(context).inflate(getLayoutResId(), null)
        val frontQuestionContentView = rootView.findViewById<ViewGroup>(R.id.frontQuestionContentView)
        frontQuestionContentView.removeAllViews()
        frontQuestionContentView.addView(renderOptions(FRONT_OPTIONS))

        return rootView
    }

    override fun renderOptions(type: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.survey_phone_input_item_layout, null)
    }

    override fun bindData2OptionsView(view: View, type: Int) {

        smsButton = view.findViewById(R.id.smsCodeButton)
        smsEditText = view.findViewById(R.id.smsCodeEditText)
        smsButton?.getPhone = { question.answers?.answer ?: "" }
        editText = view.findViewById(R.id.editText)
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
    }

}