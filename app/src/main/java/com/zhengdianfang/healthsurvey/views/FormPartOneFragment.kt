package com.zhengdianfang.healthsurvey.views


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.Util
import com.zhengdianfang.healthsurvey.entities.Condition
import com.zhengdianfang.healthsurvey.entities.Form
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import com.zhengdianfang.healthsurvey.views.adapter.AttachmentAdapter
import com.zhengdianfang.healthsurvey.views.components.BaseComponent
import com.zhengdianfang.healthsurvey.views.components.ProductCodeElection
import com.zhengdianfang.healthsurvey.views.components.ProductNameElection
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.fragment_form_part_one.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
open class FormPartOneFragment : BaseFragment() {

    private val MAX_ATTACHMENT_COUNT = 9

    private val components: MutableList<BaseComponent> = arrayListOf()
    private val formPartOneViewModel by lazy { ViewModelProviders.of(this) .get(FormViewModel::class.java) }
    private var form: Form? = null
    protected var phoneNumber: String = ""
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }
    private var takePhotoFilePath: String = ""
    protected val attachments = mutableListOf<String>()
    private var tagFlowLayout: TagFlowLayout? = null

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
            if (verifyAnswers()) {
                if (null != this.form && !TextUtils.isEmpty(phoneNumber)) {
                    form?.attachment_files = attachments.toTypedArray()
                    showDialog()
                    formPartOneViewModel.submitUserInfo(Util.getUnquieid(phoneNumber) , this.form!!, this.org_number).observe(this, Observer {
                        hideDialog()
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
        }

        back.setOnClickListener {
            Util.saveQuestionCache(context, form?.subdata)
            pop()
        }
    }

    protected fun verifyAnswers(): Boolean {
        var right = true
        run breadking@{
            components.forEach {
                if (!it.verify()) {
                    right = false
                    return@breadking
                }
                if (it.question.condition?.checkType == Condition.PHONENUMBER_TYPE.toString()) {
                    this.phoneNumber = it.question?.answers?.answer ?: ""
                }
            }

        }
        return right
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

                formViewGroup.addView(renderAttachment())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == ISupportFragment.RESULT_OK) {
            when(requestCode) {
                Util.SELECT_PHOTO -> {
                    takePhotoFilePath = if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
                        Util.getFileAbsolutePath(context, data?.data) ?: ""
                    else
                        Util.getRealPathBelowVersion(context, data?.data)

                }
                Util.OPEN_CAMERA -> {
                    AnkoLogger<String>().debug { data?.toString() }
                }
            }
            if (TextUtils.isEmpty(takePhotoFilePath).not()) {
                showDialog()
                formPartOneViewModel.uploadPic(File(takePhotoFilePath)).observe(this, Observer {
                    hideDialog()
                    if (TextUtils.isEmpty(it).not()) {
                        this.attachments.add(it!!)
                        tagFlowLayout?.adapter?.notifyDataChanged()
                    }
                })
            }
        }
    }

    open fun renderQuestionCustomStyle(view: View) {}


    open fun initDatas() {
        showDialog()
        formPartOneViewModel
                .getUserBase(org_number)
                .observe(this, Observer { form ->
                    hideDialog()
                    initViews(form)
                })
    }



    protected fun renderAttachment(): View {
        val attachmentView = LayoutInflater.from(context).inflate(R.layout.attachment_layout, null)
        attachmentView.findViewById<TextView>(R.id.addPicBtn).setOnClickListener {
            if (this.attachments.count() == MAX_ATTACHMENT_COUNT) {
                toast(getString(R.string.max_upload_pic_count))
            } else {
                selector("", listOf(getString(R.string.ablum), getString(R.string.camera)), { dialog, index ->
                    if (index == 0) {
                        startActivityForResult(Util.getIntentImageChooser(), Util.SELECT_PHOTO)
                    } else {
                        takePhotoFilePath = Util.getTakePhotoFilePath(context!!).absolutePath
                        startActivityForResult(Util.getIntentCaptureCompat(context!!, File(takePhotoFilePath)), Util.OPEN_CAMERA)
                    }
                })
            }
        }
        tagFlowLayout = attachmentView.findViewById<TagFlowLayout>(R.id.tagFlowLayout)
        tagFlowLayout?.adapter = AttachmentAdapter(attachments, {pos ->
            attachments.removeAt(pos)
            tagFlowLayout?.adapter?.notifyDataChanged()
        })


        return attachmentView
    }

}// Required empty public constructor
