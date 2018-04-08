package com.zhengdianfang.healthsurvey.views


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.healthsurvey.AppApplication
import com.zhengdianfang.healthsurvey.R
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment


/**
 * A simple [Fragment] subclass.
 */
class FormStartFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notv.setOnClickListener {
            val fragment = instantiate(context, NotRequirementFragment::class.java.name) as ISupportFragment
            start(fragment)
        }
        back.setOnClickListener {
            pop()
        }

        yestv.setOnClickListener {
            val fragment = instantiate(context, FormPartOneFragment::class.java.name, arguments) as ISupportFragment
            start(fragment)
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        (context?.applicationContext as AppApplication).surveyStatusCache.clear()
    }
}// Required empty public constructor
