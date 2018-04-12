package com.zhengdianfang.healthsurvey.views


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.healthsurvey.R
import kotlinx.android.synthetic.main.fragment_prize.*
import kotlinx.android.synthetic.main.tool_bar.*


/**
 * A simple [Fragment] subclass.
 *
 */
class PrizeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prize, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        titleTextView.text = getString(R.string.question_title)
        yestv.setOnClickListener {
            pop()
        }

        notv.setOnClickListener {
            pop()
            startActivity(Intent(activity, WebActivity::class.java).putExtra("url", arguments?.getString("url")))
        }
        back.setOnClickListener { pop() }
    }

}
