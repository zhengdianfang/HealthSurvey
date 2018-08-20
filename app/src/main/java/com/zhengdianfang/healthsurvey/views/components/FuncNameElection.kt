package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.wdullaer.materialdatetimepicker.Utils
import com.zhengdianfang.healthsurvey.MainActivity
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.views.GroupListFragment
import com.zhengdianfang.healthsurvey.views.NotSatisfiedFragment


class FuncNameElection(context: Context, question: Question) : BaseComponent(context, question) {

    private lateinit var funcTextView: TextView

    private val func27Dialog: MaterialDialog by lazy {
        MaterialDialog.Builder(context)
                .items(R.array.func27_option)
                .positiveText(R.string.confrim)
                .itemsCallbackMultiChoice(null) { _, _, text ->
                    val sectionFunc27Str = text.joinToString(",")
                    fill(sectionFunc27Str, sectionFunc27Str)
                    true
                }
                .build()
    }

    override fun getQuestionType(): Int {
        return Question.FUNC_ELECTION
    }

    override fun verify(): Boolean {
        val content = question.answers?.answer
        if (isRequried() && TextUtils.isEmpty(content) ) {
            Toast.makeText(context, context.getString(R.string.please_input_x_content,question.title), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun fill(func: String, func27: String) {
        funcTextView?.text = func
        question.answers?.answer = func27
        if (context is MainActivity) {
            val groupListFragment = context.findFragment(GroupListFragment::class.java)
            val notSatisfiedFragment = context.findFragment(NotSatisfiedFragment::class.java)
            if (null == notSatisfiedFragment) {
                groupListFragment?.updateInUseGroupList(func27)
            }
        }
    }

    override fun bindData2OptionsView(view: View, type: Int) {
        funcTextView = view.findViewById(R.id.funcSpinner)
        fill(question.answers.answer, question.answers.answer)
        funcTextView.setOnClickListener {
            func27Dialog.show()
        }

    }

    override fun renderOptions(type: Int): View {
        val textView = TextView(this.context)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(48f, context.resources))
        textView.id = R.id.funcSpinner
        textView.setTextColor(Color.BLACK)
        textView.maxLines = 1
        textView.setBackgroundResource(R.drawable.default_input)
        return textView
    }
}