package com.zhengdianfang.healthsurvey.views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zhengdianfang.healthsurvey.R
import kotlinx.android.synthetic.main.fragment_finish.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class FinishFragment : SupportFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        goHomeButton.setOnClickListener {
            popTo(FormStartFragment::class.java, false)
        }

        titleTextView.setText(R.string.question_title)
        back.setOnClickListener {
            popTo(FormStartFragment::class.java, false)
        }
    }

}// Required empty public constructor
