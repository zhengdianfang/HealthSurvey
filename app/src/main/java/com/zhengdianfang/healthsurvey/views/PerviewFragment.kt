package com.zhengdianfang.healthsurvey.views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.zhengdianfang.healthsurvey.R
import kotlinx.android.synthetic.main.fragment_perview.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class PerviewFragment : SupportFragment() {

    private val url by lazy { arguments?.getString("url") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(this).load(url).apply(RequestOptions.fitCenterTransform()).into(photoView)
        back.setOnClickListener { pop() }
    }

}// Required empty public constructor
