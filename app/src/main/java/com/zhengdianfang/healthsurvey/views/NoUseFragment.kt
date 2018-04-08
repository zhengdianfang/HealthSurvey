package com.zhengdianfang.healthsurvey.views


import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Part
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_no_use.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


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
                            start(SupportFragment.instantiate(context, FinishFragment::class.java.name) as ISupportFragment)
                        })
                    }
                })
                .setTitle(getString(R.string.please_input_other_reason))
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

        noUseRadioGroup.setOnCheckedChangeListener { groupView, checkId ->
            this.index = groupView.indexOfChild(groupView.findViewById<RadioButton>(checkId))
            if (this.index == 3) {
                alertDialog.show()
            }
        }

        back.setOnClickListener {
            pop()
        }

        confrimBtn.setOnClickListener {
            when(index) {
                0-> {
                    arguments?.putInt("partType", Part.NOUSE)
                    start(SupportFragment.instantiate(context, GroupListFragment::class.java.name, arguments) as ISupportFragment)
                }
                1 -> {}
                2 -> {
                    formViewModel.surveyFinish(uniqueid, org_number)
                    start(SupportFragment.instantiate(context, FinishFragment::class.java.name) as ISupportFragment)
                }
            }

        }
    }
}// Required empty public constructor
