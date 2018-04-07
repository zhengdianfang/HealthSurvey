package com.zhengdianfang.healthsurvey.views


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : SupportFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initData()
        bindEvent()
    }

    private fun initData() {
        val productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        productViewModel.descriptionLiveData.observe(this, Observer<String> {
            contentTextView.text = it
        })
    }

    private fun bindEvent() {
        personalTextView.setOnClickListener {
            val fragment = instantiate(context, FormStartFragment::class.java.name) as ISupportFragment
            start(fragment)
        }

        orgtv.setOnClickListener {
            val fragment = instantiate(context, MechanismPartOneFragment::class.java.name) as ISupportFragment
            start(fragment)
        }
    }

}// Required empty public constructor
