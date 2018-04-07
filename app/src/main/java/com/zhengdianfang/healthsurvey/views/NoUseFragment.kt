package com.zhengdianfang.healthsurvey.views


import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout

import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_no_use.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.SupportHelper


/**
 * A simple [Fragment] subclass.
 */
class NoUseFragment : SupportFragment() {

    private val formViewModel by lazy { ViewModelProviders.of(this).get(FormViewModel::class.java) }
    private var index = -1
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }
    private val uniqueid by lazy { arguments?.getString("uniqueid") ?: "" }

    private val editText by lazy {
        val editText = EditText(context)
        editText.setTextColor(Color.BLACK)
        editText.layoutParams = FrameLayout.LayoutParams(resources.getDimension(R.dimen.dialog_edittext_width).toInt(), FrameLayout.LayoutParams.WRAP_CONTENT)
        editText.setLines(4)
        editText.gravity = Gravity.TOP
        editText.setBackgroundResource(R.drawable.default_input)
        editText
    }

    private val alertDialog by lazy {
        AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_MinWidth)
                .setPositiveButton(R.string.confrim, { _, _ ->
                      val content = editText.text.toString()
                    if (TextUtils.isEmpty(content).not()) {
                        formViewModel.trachDirection(uniqueid, org_number, "2.4 其他-->$content").observe(this, Observer {
                            popTo(GroupListFragment::class.java, false)
                        })
                    }
                })
                .setView(editText)
                .create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_use, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleTextView.setText(R.string.nouse_title)

        noUseRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            index = i
            if (i == 4) {
               alertDialog.show()
            }
        }

        back.setOnClickListener {
            pop()
        }

        confrimBtn.setOnClickListener {
            when(index) {
                1-> {}
                2 -> {}
                3 -> {
                    formViewModel.surveyFinish(uniqueid, org_number)
                    popTo(GroupListFragment::class.java, false)
                }
            }

        }
    }
}// Required empty public constructor
