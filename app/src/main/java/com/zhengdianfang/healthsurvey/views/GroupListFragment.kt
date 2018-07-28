package com.zhengdianfang.healthsurvey.views


import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zhengdianfang.healthsurvey.AppApplication
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.Util
import com.zhengdianfang.healthsurvey.entities.Group
import com.zhengdianfang.healthsurvey.entities.GroupChild
import com.zhengdianfang.healthsurvey.entities.Part
import com.zhengdianfang.healthsurvey.viewmodel.FormViewModel
import com.zhengdianfang.healthsurvey.viewmodel.GroupListViewModel
import com.zhengdianfang.healthsurvey.views.adapter.GroupListAdapter
import kotlinx.android.synthetic.main.fragment_group_list.*
import kotlinx.android.synthetic.main.tool_bar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * A simple [Fragment] subclass.
 */
class GroupListFragment : BaseFragment() {

    private val groupListViewModel by lazy { ViewModelProviders.of(this).get(GroupListViewModel::class.java) }
    private val formViewModel by lazy { ViewModelProviders.of(this).get(FormViewModel::class.java) }
    private var groups = mutableListOf<Group>()
    private val adapter = GroupListAdapter(groups) { onGroupItemClick(it) }
    private val nextbutton by lazy { LayoutInflater.from(context).inflate(R.layout.next_button_layout, null) as ViewGroup }
    private val org_number by lazy { arguments?.getString("org_number") ?: "" }
    private val partType by lazy { arguments?.getInt("partType") ?: Part.BASE }
    private var part: Part? = null

    private val goOnDialog by lazy {
        val alertDialog = AlertDialog.Builder(context,  android.R.style.Theme_Material_Light_Dialog_MinWidth)
                .setPositiveButton(getString(R.string.goon_reply)) { _, _ ->
                    arguments?.putBoolean("increase", true)
                    unquieIdIncrease()
                    start(SupportFragment.instantiate(context, GroupListFragment::class.java.name, arguments) as ISupportFragment)
                }
                .setNegativeButton(getString(R.string.finish)) { _, _ ->
                    resetUnquieId()
                    formViewModel.surveyFinish(getUnqueId(), org_number)
                    start(SupportFragment.instantiate(context, FinishFragment::class.java.name, arguments) as ISupportFragment)
                }
                .setMessage(R.string.goon_dailog_content)
                .create()

        alertDialog
    }

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
        arguments?.putString("group_id", groupChild.group_id)
        start(SupportFragment.instantiate(context, SurveyFragment::class.java.name, arguments) as ISupportFragment)
    }

    private fun initViews() {
        groupList.adapter = adapter
        adapter.addFooterView(nextbutton)
    }

    private fun initEvents() {
        back.setOnClickListener { pop() }
        nextbutton.getChildAt(0).setOnClickListener {
            if (checkFill()){
                when(partType) {
                    Part.BASE -> {
                        start(SupportFragment.instantiate(context, FormAskFragment::class.java.name, arguments) as ISupportFragment)
                    }
                    Part.INUSE -> {
                        goOnDialog.show()
                    }
                    Part.NOUSE -> {
                        goOnDialog.show()
                    }
                }
            } else {
                Toast.makeText(context, "请回答必作问卷", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFill(): Boolean {
        val surveyStatusCache = (context?.applicationContext as AppApplication).surveyStatusCache
        this.groups.forEach { group ->
            group.group.forEach {
                if (it.isRequired() && !surveyStatusCache.containsKey(it.group_id)) {
                    return false
                }
            }
        }
        return true

    }

    private fun initDatas() {
        showDialog()
        groupListViewModel.getQuestionGroupList(getUnqueId(), this.org_number).observe(this, Observer {
            hideDialog()
            when(partType) {
                Part.BASE -> part = it?.base
                Part.INUSE -> part = it?.inuse
                Part.NOUSE -> part = it?.nouse
            }
            titleTextView.text = part?.title
            if (partType == Part.INUSE) {
                updateInUseGroupList()
            } else {
                this.groups.clear()
                this.groups.addAll(part?.list ?: mutableListOf())
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun updateInUseGroupList(filterFuncStr: String = "") {
        this.groups.clear()
        part?.list?.forEach { group ->
            group.group_all.forEach { key, list ->
                if (key == "问卷表") {
                    this.groups.add(Group(group.list_title, list, mutableMapOf()))
                }
                if (!TextUtils.isEmpty(filterFuncStr) && filterFuncStr.contains(key)){
                    this.groups.add(Group(group.list_title, list, mutableMapOf()))
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        adapter.notifyDataSetChanged()
    }

    fun update() {
        adapter.notifyDataSetChanged()
    }


}// Required empty public constructor
