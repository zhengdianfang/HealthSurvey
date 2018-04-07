package com.zhengdianfang.healthsurvey.views


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout

import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Group
import com.zhengdianfang.healthsurvey.entities.GroupChild
import com.zhengdianfang.healthsurvey.viewmodel.GroupListViewModel
import com.zhengdianfang.healthsurvey.views.adapter.GroupListAdapter
import kotlinx.android.synthetic.main.fragment_group_list.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class GroupListFragment : SupportFragment() {

    private val groupListViewModel by lazy { ViewModelProviders.of(this).get(GroupListViewModel::class.java) }
    private var groups = mutableListOf<Group>()
    private val adapter = GroupListAdapter(groups, { onGroupItemClick(it) })
    private val nextbutton by lazy { LayoutInflater.from(context).inflate(R.layout.next_button_layout, null) as ViewGroup }
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }
    private val uniqueid by lazy { arguments?.getString("uniqueid") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initEvents()
        initViews()
        initDatas()
    }

    private fun onGroupItemClick(groupChild: GroupChild) {
        val bundle = Bundle()
        bundle.putString("org_number", this.org_number)
        bundle.putString("uniqueid", this.uniqueid)
        bundle.putString("group_id", groupChild.group_id)
        start(SupportFragment.instantiate(context, SurveyFragment::class.java.name, bundle) as ISupportFragment)
    }

    private fun initViews() {
        groupList.adapter = adapter
        adapter.addFooterView(nextbutton)
    }

    private fun initEvents() {
        back.setOnClickListener { pop() }
        nextbutton.getChildAt(0).setOnClickListener {
            start(SupportFragment.instantiate(context, FormAskFragment::class.java.name) as ISupportFragment)
        }
    }

    private fun initDatas() {
        groupListViewModel.getQuestionGroupList(this.uniqueid, this.org_number).observe(this, Observer {
            titleTextView.text = it?.base?.title
            this.groups.clear()
            this.groups.addAll(it?.base?.list ?: mutableListOf())
            adapter.notifyDataSetChanged()

        })
    }

}// Required empty public constructor
