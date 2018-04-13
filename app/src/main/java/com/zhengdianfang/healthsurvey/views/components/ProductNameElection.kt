package com.zhengdianfang.healthsurvey.views.components

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.zhengdianfang.healthsurvey.MainActivity
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.views.adapter.ProductNamesAdapter

/**
 * Created by dfgzheng on 05/04/2018.
 */
class ProductNameElection(context: Context, question: Question) : BaseComponent(context, question) {

    private var editText: AutoCompleteTextView? = null

    override fun getQuestionType(): Int {
        return Question.AUTOCOMPLETE
    }

    override fun verify(): Boolean {
        val content = question?.answers?.answer
        if (isRequried() && TextUtils.isEmpty(content) ) {
            Toast.makeText(context, context.getString(R.string.please_input_x_content, question.title), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun bindData2View(view: View, autoFill: ((product: Product) -> Unit)?) {
        super.bindData2View(view)
        editText = view.findViewById(R.id.editText)
        editText?.setText(question.answers.answer)
        if (context is MainActivity) {
            initAdapter(context)
            editText?.setOnItemClickListener { _, _, _, _ ->
                val input = editText?.text.toString()
                question.answers?.answer = input
                val product = context.products.first{ it.name == input }
                if (product != null) {
                    autoFill?.invoke(product)
                }
            }
        }

    }

    private fun initAdapter(context: MainActivity) {
        val adapter = ProductNamesAdapter(context, android.R.layout.simple_list_item_1, context.products.map { it.name }.toMutableList())
        editText?.setAdapter(adapter)
    }

    override fun bindData2OptionsView(view: View, type: Int) {

    }

    override fun renderOptions(type: Int): View {
        val editText = AutoCompleteTextView(this.context)
        editText.setTextColor(Color.BLACK)
        editText.id = R.id.editText
        editText.setBackgroundResource(R.drawable.default_input)
        return editText
    }

}