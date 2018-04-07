package com.zhengdianfang.healthsurvey.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Group
import com.zhengdianfang.healthsurvey.entities.GroupChild
import com.zhengdianfang.healthsurvey.entities.Part
import com.zhengdianfang.healthsurvey.views.components.BaseComponent

/**
 * Created by dfgzheng on 06/04/2018.
 */
class GroupListAdapter(data: MutableList<Group>?, private val onGroupItemClick: (group: GroupChild) -> Unit) : BaseQuickAdapter<Group, GroupListViewHolder>(R.layout.group_list_item_layout, data) {
    override fun convert(helper: GroupListViewHolder?, item: Group?) {
        helper?.setText(R.id.groupTitleTextView, item?.list_title)
        val viewGroup = helper?.getView<ViewGroup>(R.id.groupItemViewGroup)
        item?.group?.forEachIndexed { index, groupChild ->
            val groupView = LayoutInflater.from(helper?.itemView?.context)
                    .inflate(R.layout.group_child_item_layout, null, false)
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
            viewGroup?.addView(groupView)

        }
    }
}