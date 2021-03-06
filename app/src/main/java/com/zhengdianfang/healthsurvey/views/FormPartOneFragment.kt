package com.zhengdianfang.healthsurvey.views


import android.Manifest
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
import com.zhengdianfang.healthsurvey.MainActivity
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.Util
import com.zhengdianfang.healthsurvey.entities.*
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import com.zhengdianfang.healthsurvey.views.adapter.AttachmentAdapter
import com.zhengdianfang.healthsurvey.views.components.*
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.fragment_form_part_one.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
open class FormPartOneFragment : BaseFragment() {

    private val MAX_ATTACHMENT_COUNT = 9
    private val omits = mutableMapOf<String, String>()
    protected val components: MutableList<BaseComponent> = mutableListOf()
    private val formPartOneViewModel by lazy { ViewModelProviders.of(this) .get(FormViewModel::class.java) }
    private var form: Form? = null
    protected var phoneNumber: String = ""
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }
    private var takePhotoFilePath: String = ""
    protected val attachments = mutableListOf<String>()
    private var tagFlowLayout: TagFlowLayout? = null
    private val formViewGroup by lazy { find<ViewGroup>(R.id.formViewGroup) }

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
                if (null != this.form) {
                    form?.attachment_files = attachments.toTypedArray()
                    showDialog()
                    createUnqueId(phoneNumber)
                    formPartOneViewModel.submitUserInfo(getUnqueId(), this.form!!, this.org_number).observe(this, Observer {
                        hideDialog()
                        if (it != false) {
                            nextFragment()
                            formPartOneViewModel.uploadDeviceInfo(getUnqueId())
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

    open fun nextFragment() {
        val bundle = Bundle()
        bundle.putString("org_number", org_number)
        start(SupportFragment.instantiate(context, GroupListFragment::class.java.name, bundle) as ISupportFragment)
        if (TextUtils.isEmpty(this.form?.prize_url).not()) {
            bundle.putString("url", this.form?.prize_url)
            start(SupportFragment.instantiate(context, PrizeFragment::class.java.name, bundle) as ISupportFragment)
        }
    }

    open fun verifyAnswers(): Boolean {
        var right = true
        for (component in components) {
            if (!component.verify()) {
                    right = false
                    break
            }
            if (component.question.condition?.checkType == Condition.PHONENUMBER_TYPE.toString()) {
                this.phoneNumber = component.question?.answers.answer
            }
        }
        return right
    }

    protected fun fillProductCode(product: Product) {
        components.forEach { component ->
            when {
                component.getQuestionType() == Question.AUTO_FILL -> (component as ProductCodeElection).fill(product.code)
                component.getQuestionType() == Question.COMPANY_ELECTION -> (component as CompanyNameElection).fill(product.factory)
                component.getQuestionType() == Question.FUNC_ELECTION -> (component as FuncNameElection).fill(product.func, product.func27)
            }
        }
    }

    open fun initViews(form: Form?) {
        if (null != form && null != context) {
            this.form = form
            titleTextView.text = form.title
            form.subdata?.forEach { question ->
                var component = BaseComponent.getComponent(context!!, question)
                component?.onSelectOption = {question, option, isChecked ->
                    this.onSelectOption(question, option, isChecked)
                }
                if (null != component) {
                    components.add(component)
                    val view = component?.render()
                    renderQuestionCustomStyle(view)
                    if (question.type == Question.AUTOCOMPLETE.toString()) {
                        (component as ProductNameElection).bindData2View { product -> fillProductCode(product) }
                    } else {
                        component.bindData2View()
                    }
                    this.formViewGroup.addView(view)

                }
            }
            if (form.attachment == Form.HAVE_ATTACHMENT.toString()) {

                this.formViewGroup.addView(renderAttachment())
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
                Luban.with(context)
                        .load(File(takePhotoFilePath))
                        .setTargetDir(context?.externalCacheDir?.absolutePath)
                        .setCompressListener(object : OnCompressListener {
                            override fun onSuccess(file: File?) {
                                if (null != file) {
                                    uploadPic(file)
                                }
                            }

                            override fun onError(e: Throwable?) {
                            }

                            override fun onStart() {
                            }
                        }).launch()
            }
        }
    }

    fun uploadPic(file: File) {
        formPartOneViewModel.uploadPic(file).observe(this, Observer {
            hideDialog()
            if (TextUtils.isEmpty(it).not()) {
                this.attachments.add(it!!)
                tagFlowLayout?.adapter?.notifyDataChanged()
                toast(getString(R.string.upload_success))
            } else {
                toast(getString(R.string.upload_failure))
            }
        })
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
                selector("", listOf(getString(R.string.ablum), getString(R.string.camera))) { dialog, index ->
                    if (index == 0) {
                        requestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            startActivityForResult(Util.getIntentImageChooser(), Util.SELECT_PHOTO)
                        }
                    } else {
                        requestPermission(arrayOf(Manifest.permission.CAMERA)) {
                            takePhotoFilePath = Util.getTakePhotoFilePath(context!!).absolutePath
                            startActivityForResult(Util.getIntentCaptureCompat(context!!, File(takePhotoFilePath)), Util.OPEN_CAMERA)
                        }
                    }
                }
            }
        }
        tagFlowLayout = attachmentView.findViewById(R.id.tagFlowLayout)
        tagFlowLayout?.adapter = AttachmentAdapter(attachments) { pos ->
            attachments.removeAt(pos)
            tagFlowLayout?.adapter?.notifyDataChanged()
        }


        return attachmentView
    }

    protected open fun onSelectOption(question: Question, option: Option, isChecked: Boolean) {
        omits[question.qid] = option.omit
        (activity as MainActivity).addAdvice(question, option.advise, isChecked)
        val split = omits.values.reduce { acc, s -> acc.plus(",").plus(s) }.split(",")
        components.forEach { component ->
            if (split.contains(component.question.qid)) {
                component.disable()
            } else {
                component.enable()
            }
        }
    }

}// Required empty public constructor
