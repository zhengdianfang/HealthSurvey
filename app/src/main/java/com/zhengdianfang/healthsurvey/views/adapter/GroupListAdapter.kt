package com.zhengdianfang.healthsurvey.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zhengdianfang.healthsurvey.AppApplication
import com.zhengdianfang.healthsurvey.MainActivity
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Group
import com.zhengdianfang.healthsurvey.entities.GroupChild
import com.zhengdianfang.healthsurvey.views.components.BaseComponent

/**
 * Created by dfgzheng on 06/04/2018.
 */
class GroupListAdapter(data: MutableList<Group>?, private val onGroupItemClick: (group: GroupChild) -> Unit) : BaseQuickAdapter<Group, GroupListViewHolder>(R.layout.group_list_item_layout, data) {
    override fun convert(helper: GroupListViewHolder?, item: Group?) {
        helper?.setText(R.id.groupTitleTextView, item?.list_title)
        val viewGroup = helper?.getView<ViewGroup>(R.id.groupItemViewGroup)
        viewGroup?.removeAllViews()
        item?.group?.forEachIndexed { index, groupChild ->
            val groupView = LayoutInflater.from(helper?.itemView?.context)
                    .inflate(R.layout.group_child_item_layout, null, false)
            renderStatus(groupView, groupChild.group_id)
            groupView.setOnClickListener {
                onGroupItemClick(groupChild)
            }
            if (index == item?.group?.count() - 1) {
               groupView.setBackgroundResource(R.drawable.bottom_corner_background)
            } else {
                groupView.setBackgroundResource(R.drawable.group_children_item_background)
            }
            groupView.findViewById<TextView>(R.id.formTitleTextView).text =
                    if(groupChild?.isRequired()) BaseComponent.getRequriedSpanableString(groupChild?.group_name) else "   ${groupChild?.group_name}"

            val adviceTextView = groupView?.findViewById<TextView>(R.id.adviceTextView)
            val mainActivity = helper?.itemView?.context as MainActivity
            if (mainActivity != null) {
                adviceTextView?.visibility = if (mainActivity.isAdvice(groupChild.group_id))  View.VISIBLE else View.INVISIBLE
            } else {
                adviceTextView?.visibility = View.INVISIBLE
            }
            viewGroup?.addView(groupView)
        }
    }

    fun renderStatus(view: View, id: String) {
        val noFillTextView = view.findViewById<TextView>(R.id.noFillTextView)
        val filled = (view.context.applicationContext as AppApplication).surveyStatusCache[id] ?: false
        if (filled) {
            noFillTextView.setBackgroundResource(R.drawable.fill_background)
            noFillTextView.setText(R.string.fill)
        } else {
            noFillTextView.setText(R.string.no_fill)
            noFillTextView.setBackgroundResource(R.drawable.no_fill_background)
        }
    }
}