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
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_mechanism.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class MechanismPartOneFragment : SupportFragment() {

    private val formPartOneViewModel by lazy { ViewModelProviders.of(this).get(FormViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mechanism, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        confrimTextView.setOnClickListener {
            val code = codeEditText.text.toString()
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(context, R.string.please_input_mechanism_code, Toast.LENGTH_SHORT).show()
            } else {

                formPartOneViewModel.checkMechanismCode(code).observe(this, Observer {
                    if (it != false) {
                        val bundle = Bundle()
                        bundle.putString("org_number", code)
                        val fragment = instantiate(context, FormStartFragment::class.java.name, bundle) as ISupportFragment
                        start(fragment)

                    } else {
                       Toast.makeText(context, R.string.check_mechanism_code_fail, Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }

        back.setOnClickListener { pop() }

    }
}// Required empty public constructor
