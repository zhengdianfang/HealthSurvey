package com.zhengdianfang.healthsurvey.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Form
import com.zhengdianfang.healthsurvey.entities.NotSafitis
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_not_satisfied.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import java.io.InputStreamReader

class NotSatisfiedFragment : FormPartOneFragment() {

    private val formPartOneViewModel by lazy { ViewModelProviders.of(this) .get(FormViewModel::class.java) }
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_satisfied, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val inputStreamReader = InputStreamReader(resources.openRawResource(R.raw.custom_question_list))
        val text = inputStreamReader.readText()
        back.setOnClickListener { pop() }
        initViews(Gson().fromJson(text, Form::class.java))
    }

    override fun initDatas() {
    }

    override fun initEvents() {
        confirmButton.setOnClickListener {
            val notSafitis = NotSafitis(
                    this.org_number,
                    components[0].question.answers.answer,
                    components[1].question.answers.answer,
                    components[2].question.answers.answer,
                    components[3].question.answers.answer
            )
            val postNotSatisfiedData = formPartOneViewModel.postNotSatisfiedData(getUnqueId(), notSafitis, this.org_number)
            postNotSatisfiedData.observe(this, Observer {
                start(SupportFragment.instantiate(context, FinishFragment::class.java.name) as ISupportFragment)
            })

        }
    }

}
