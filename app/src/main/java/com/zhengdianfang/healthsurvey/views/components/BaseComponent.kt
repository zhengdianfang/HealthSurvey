package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.datasource.cloud.WebService
import com.zhengdianfang.healthsurvey.entities.Answer
import com.zhengdianfang.healthsurvey.entities.Condition
import com.zhengdianfang.healthsurvey.entities.Option
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.views.PerviewFragment
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.SupportHelper
import org.jetbrains.anko.defaultSharedPreferences

/**
 * Created by dfgzheng on 05/04/2018.
 */
abstract class BaseComponent(val context: Context, val question: Question) {

    protected lateinit var rootView: View

    var onSelectOption: ((qid: String, option: Option) -> Unit)? = null

    init {
        val json = context.defaultSharedPreferences.getString(question.qid, "")
        if (TextUtils.isEmpty(json).not()) {
            val questionCache = WebService.gson.fromJson<Question>(json, Question::class.java)
            question.answers = questionCache.answers
        } else {
            question.answers = Answer("","", "", "")
        }
    }

    private fun isDoubleQuestion(): Boolean {
       return question.isComparisonBeforeEat == "1"
    }

    fun isRequried(): Boolean {
        return question.condition?.required == "1"
    }

    fun renderTitle(title: String): View {
        val textView = TextView(this.context)
        textView.setTextColor(Color.BLACK)
        textView.textSize = 16f
        textView.setTypeface(null, Typeface.BOLD)
        textView.text = if(isRequried())  getRequriedSpanableString(title) else "   $title"
        return textView
    }

    fun renderEditText(): EditText {

        val editText = EditText(this.context)
        editText.id = R.id.editText
        editText.setTextColor(Color.BLACK)
        editText.maxLines = 1
        editText.setBackgroundResource(R.drawable.default_input)
        if (question.condition?.checkType == Condition.PHONENUMBER_TYPE.toString()
            || question.condition?.checkType == Condition.NUMBER_TYPE.toString()) {
            editText.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED.or(InputType.TYPE_DATETIME_VARIATION_NORMAL)
        }
        return editText
    }

    protected fun renderDescImageView(view: View) {
        val imageView = view.findViewById<ImageView>(R.id.descImageView)
        if (TextUtils.isEmpty(question.pic)) {
            imageView?.visibility = View.GONE
        } else {
            imageView?.visibility = View.VISIBLE
            Glide.with(context).load(question.pic).apply(RequestOptions.fitCenterTransform()).into(imageView)
        }
        imageView?.setOnClickListener {
           if (context is FragmentActivity) {
               val topFragment = SupportHelper.getTopFragment(context.supportFragmentManager) as SupportFragment
               val bundle = Bundle()
               bundle.putString("url", question.pic)
               topFragment.start(SupportFragment.instantiate(context, PerviewFragment::class.java.name, bundle) as ISupportFragment)
           }
        }
    }



    open fun render(): View {
        rootView = LayoutInflater.from(context).inflate(getLayoutResId(), null)
        val frontQuestionContentView = rootView.findViewById<ViewGroup>(R.id.frontQuestionContentView)
        frontQuestionContentView.removeAllViews()
        frontQuestionContentView.addView(renderOptions(FRONT_OPTIONS))
        if (isDoubleQuestion()) {
            val endQuestionContentView = rootView.findViewById<ViewGroup>(R.id.endQuestionContentView)
            endQuestionContentView.removeAllViews()
            endQuestionContentView.addView(renderOptions(END_OPTIONS))
        }
        rootView.isEnabled = false
        return rootView
    }


    open fun getLayoutResId(): Int {
        if (isDoubleQuestion())
            return R.layout.survey_double_question_templete_layout
        return R.layout.survey_single_question_templete_layout
    }

    abstract fun renderOptions(type: Int):View

    abstract fun getQuestionType(): Int

    abstract fun bindData2OptionsView(view: View, type: Int)

    open fun verify(): Boolean {
        if (isRequried() && rootView.findViewById<View>(R.id.disableMaskView).visibility == View.GONE) {
            if (TextUtils.isEmpty(question.answers.answer)) {
                Toast.makeText(context, context.getString(R.string.please_input_x_content, question.title), Toast.LENGTH_SHORT).show()
                return false
            }
            if (isDoubleQuestion()) {
                if (TextUtils.isEmpty(question.answers.answer_end)) {
                    Toast.makeText(context, context.getString(R.string.please_input_x_content, question.title), Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        return true
    }

    open fun bindData2View() {
        rootView.findViewById<TextView>(R.id.questionTitleView).text =
                if(isRequried()) BaseComponent.getRequriedSpanableString(question.title) else "   ${question.title}"
        renderDescImageView(rootView)
        bindData2OptionsView(rootView.findViewById(R.id.frontQuestionContentView), FRONT_OPTIONS)
        if (isDoubleQuestion()) {
            val findViewById = rootView.findViewById<View>(R.id.endQuestionContentView)
            bindData2OptionsView(findViewById, END_OPTIONS)
        }
    }

    fun disable() {
        rootView.findViewById<View>(R.id.disableMaskView).visibility = View.VISIBLE
    }

    fun enable() {
        rootView.findViewById<View>(R.id.disableMaskView).visibility = View.GONE
    }

    companion object {
        const val FRONT_OPTIONS = 0
        const val END_OPTIONS = 1

        fun getComponent(context: Context, question: Question): BaseComponent? {
            var component: BaseComponent? = null
            when(question.parseType()) {
                Question.INPUT -> component = InputElection(context, question)
                Question.PHONE_INPUT_ELECTION -> component = PhoneInputElection(context, question)
                Question.IDCARD_INPUT_ELECTION -> component = IdCardInputElection(context, question)
                Question.SINGLE_ELECTION -> component = SingleElection(context, question)
                Question.MULTI_ELECTION -> component = MultiElection(context, question)
                Question.DATE -> component = DateElection(context, question)
                Question.ADDRESS -> component = AddressElection(context, question)
                Question.AUTOCOMPLETE -> component = ProductNameElection(context, question)
                Question.AUTO_FILL -> component = ProductCodeElection(context, question)
                Question.COMPANY_ELECTION -> component = CompanyNameElection(context, question)
            }
            return component
        }
        fun getRequriedSpanableString(title: String): SpannableString {
            val spannableString = SpannableString("* $title")
            spannableString.setSpan(ForegroundColorSpan(Color.RED), 0 , 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableString
        }
    }


}