package com.zhengdianfang.healthsurvey.views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Part
import kotlinx.android.synthetic.main.fragment_form_ask.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class FormAskFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_ask, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleTextView.setText(R.string.question_title)

        notv.setOnClickListener {
            start(SupportFragment.instantiate(context, NoUseFragment::class.java.name) as ISupportFragment)
        }
        yestv.setOnClickListener {
            arguments?.putInt("partType", Part.INUSE)
            start(SupportFragment.instantiate(context, GroupListFragment::class.java.name, arguments) as ISupportFragment)

        }

        back.setOnClickListener { pop() }
    }

}// Required empty public constructor
