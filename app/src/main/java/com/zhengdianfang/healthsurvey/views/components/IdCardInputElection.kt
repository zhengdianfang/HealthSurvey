package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.Util
import com.zhengdianfang.healthsurvey.entities.Question

/**
 * Created by dfgzheng on 05/04/2018.
 */
class IdCardInputElection(context: Context, question: Question) : InputElection(context, question) {

    override fun getQuestionType(): Int {
        return Question.IDCARD_INPUT_ELECTION
    }

    override fun verify(): Boolean {
        if (rootView.visibility == View.VISIBLE) {
            val content = editText?.text?.toString() ?: ""
            if (isRequried() && TextUtils.isEmpty(content) ) {
                Toast.makeText(context, context.getString(R.string.please_input_idcard), Toast.LENGTH_SHORT).show()
                return false
            }
            if (!Util.is18ByteIdCard(content)) {
                Toast.makeText(context, context.getString(R.string.please_input_right_idcard), Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

}