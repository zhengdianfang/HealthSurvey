package com.zhengdianfang.healthsurvey.views


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.Util
import com.zhengdianfang.healthsurvey.entities.Condition
import com.zhengdianfang.healthsurvey.entities.Form
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import com.zhengdianfang.healthsurvey.views.components.BaseComponent
import com.zhengdianfang.healthsurvey.views.components.ProductCodeElection
import com.zhengdianfang.healthsurvey.views.components.ProductNameElection
import kotlinx.android.synthetic.main.fragment_form_part_one.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
open class FormPartOneFragment : SupportFragment() {

    private val components: MutableList<BaseComponent> = arrayListOf()
    private val formPartOneViewModel by lazy { ViewModelProviders.of(this) .get(FormViewModel::class.java) }
    private var form: Form? = null
    protected var phoneNumber: String = ""
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_part_one, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDatas()
        initEvents()

    }

    open fun initEvents() {
        nextStepButton.setOnClickListener {
            Util.saveQuestionCache(context, form?.subdata)
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
            if (null != this.form && !TextUtils.isEmpty(phoneNumber)) {
                formPartOneViewModel.submitUserInfo(Util.getUnquieid(phoneNumber) , this.form!!, this.org_number).observe(this, Observer {
                    if (it != false) {
                        val bundle = Bundle()
                        bundle.putString("uniqueid", Util.getUnquieid(phoneNumber))
                        bundle.putString("org_number", org_number)
                        start(SupportFragment.instantiate(context, GroupListFragment::class.java.name, bundle) as ISupportFragment)
                    } else {
                        Toast.makeText(context, getString(R.string.save_fail), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        back.setOnClickListener {
            Util.saveQuestionCache(context, form?.subdata)
            pop()
        }
    }

    protected fun fillProductCode(product: Product) {
        val component = components.first { it.getQuestionType() == Question.AUTO_FILL }
        if (null != component && component is ProductCodeElection) {
            component.fill(product.code)
        }
    }

    open fun initViews(form: Form?) {
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
                    formViewGroup.addView(view)

                }
            }
            if (form.attachment == Form.HAVE_ATTACHMENT.toString()) {
                val attachmentView = LayoutInflater.from(context).inflate(R.layout.attachment_layout, null)
                formViewGroup.addView(attachmentView)
            }
        }
    }

    open fun renderQuestionCustomStyle(view: View) {
    }


    open fun initDatas() {
        formPartOneViewModel
                .getUserBase(org_number ?: "")
                .observe(this, Observer { form ->
                    initViews(form)
                })
    }

}// Required empty public constructor
