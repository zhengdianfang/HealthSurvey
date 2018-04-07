package com.zhengdianfang.healthsurvey.views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zhengdianfang.healthsurvey.R
import kotlinx.android.synthetic.main.fragment_not_requirement.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class NotRequirementFragment : SupportFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_requirement, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        back.setOnClickListener {
           pop()
        }

        suretv.setOnClickListener {
            pop()
        }
    }
}// Required empty public constructor
