package com.zhengdianfang.healthsurvey.views


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Part
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form_ask.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class FormAskFragment : BaseFragment() {

    private val formViewModel by lazy { ViewModelProviders.of(this).get(FormViewModel::class.java) }
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }
    private val uniqueid by lazy { arguments?.getString("uniqueid") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_ask, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleTextView.setText(R.string.question_title)

        notv.setOnClickListener {
            start(SupportFragment.instantiate(context, NoUseFragment::class.java.name, arguments) as ISupportFragment)
        }
        yestv.setOnClickListener {
            arguments?.putInt("partType", Part.INUSE)
            formViewModel.trachDirection(uniqueid, org_number, "1 仍在使用(正常问卷)")
            start(SupportFragment.instantiate(context, GroupListFragment::class.java.name, arguments) as ISupportFragment)

        }

        back.setOnClickListener { pop() }
    }

}// Required empty public constructor
