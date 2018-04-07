package com.zhengdianfang.healthsurvey.views


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.datasource.cloud.WebService
import com.zhengdianfang.healthsurvey.entities.Condition
import com.zhengdianfang.healthsurvey.entities.Form
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.entities.QuestionTable
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import com.zhengdianfang.healthsurvey.views.adapter.MultipleItem
import com.zhengdianfang.healthsurvey.views.adapter.QuestionAdapter
import com.zhengdianfang.healthsurvey.views.components.BaseComponent
import com.zhengdianfang.healthsurvey.views.components.ProductNameElection
import kotlinx.android.synthetic.main.fragment_form_part_one.*
import kotlinx.android.synthetic.main.fragment_survey.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
open class SurveyFragment : FormPartOneFragment() {

    private val formViewModel by lazy { ViewModelProviders.of(this).get(FormViewModel::class.java) }
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }
    private val uniqueid by lazy { arguments?.getString("uniqueid") ?: "" }
    private val group_id by lazy { arguments?.getString("group_id") ?: "" }
    private var form: Form? = null

    private val components: MutableList<BaseComponent> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey, container, false)
    }

   override fun initEvents() {
        back.setOnClickListener {
            pop()
            this.form?.subdata?.forEach {
                val questionTable = QuestionTable()
                questionTable.qid = it.qid
                questionTable.question = WebService.gson.toJson(it)
            }
        }

        view?.findViewById<Button>(R.id.saveButton)?.setOnClickListener {
            if (null != this.form) {
                run breadking@ {
                    components.forEach {
                        if (!it.verify()) {
                            return@breadking
                        }
                        if (it.question.condition?.checkType == Condition.PHONENUMBER_TYPE.toString()) {
                            this.phoneNumber = it.question?.answers?.answer ?: ""
                        }
                    }

                }
                formViewModel.submitSurveyForm(this.form!!, this.uniqueid, this.org_number).observe(this, Observer {
                    if (it != false) {
                        pop()
                    } else {
                        Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }


    override fun initDatas() {
        formViewModel.getSurveyForm(uniqueid, org_number, group_id).observe(this, Observer {
            this.form = it
            initViews(it)
        })
    }

    override fun initViews(form: Form?) {
        if (null != form && null != context) {
            this.form = form
            titleTextView.text = form.title
            form.subdata?.forEach { question ->
                var component = BaseComponent.getComponent(context!!, question)
                if (null != component) {
                    components.add(component)
                    val view = component?.render()
                    renderQuestionCustomStyle(view)
                    if (question.type == Question.AUTOCOMPLETE.toString()) {
                        (component as ProductNameElection).bindData2View(view, { product -> fillProductCode(product) })
                    } else {
                        component.bindData2View(view)
                    }
                    surveyViewGroup.addView(view)

                }
            }
            if (form.attachment == Form.HAVE_ATTACHMENT.toString()) {
                val attachmentView = LayoutInflater.from(context).inflate(R.layout.attachment_layout, null)
                surveyViewGroup.addView(attachmentView)
            }
        }
    }

    override fun renderQuestionCustomStyle(view: View) {
        view.setBackgroundResource(R.drawable.rect_item_shap)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = context?.resources?.getDimension(R.dimen.survey_item_margin)?.toInt() ?: 0
        layoutParams.topMargin = margin
        layoutParams.bottomMargin = margin
        view.layoutParams = layoutParams
    }

}// Required empty public constructor
