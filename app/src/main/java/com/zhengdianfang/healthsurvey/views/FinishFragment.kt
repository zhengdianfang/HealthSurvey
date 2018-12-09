package com.zhengdianfang.healthsurvey.views


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.healthsurvey.R
import kotlinx.android.synthetic.main.fragment_finish.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.jetbrains.anko.defaultSharedPreferences


/**
 * A simple [Fragment] subclass.
 */
class FinishFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.defaultSharedPreferences?.edit()?.clear()?.apply()
        goHomeButton.setOnClickListener {
            popTo(MainFragment::class.java, false)
        }

        titleTextView.setText(R.string.question_title)
        back.setOnClickListener {
            popTo(MainFragment::class.java, false)
        }

        val fragment = findFragment(MainFragment::class.java)
        if (null != fragment) {
            if (TextUtils.isEmpty(fragment.prizeUrlOnFinished)) {
                prizeButton.visibility = View.GONE
                spaceView.visibility = View.GONE
            } else {
                prizeButton.visibility = View.VISIBLE
                spaceView.visibility = View.VISIBLE
                prizeButton.setOnClickListener {
                    startActivity(Intent(activity, WebActivity::class.java)
                        .putExtra("url", fragment.prizeUrlOnFinished)
                        .putExtra("copy", true)
                    )
                }
            }
        }
    }

}// Required empty public constructor
