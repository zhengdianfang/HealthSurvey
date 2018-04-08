package com.zhengdianfang.healthsurvey.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zhengdianfang.healthsurvey.R
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class AttachmentAdapter(urls: MutableList<String>, val onDeleteItem: (index: Int) -> Unit): TagAdapter<String>(urls) {

    override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.attacment_item_layout, parent, false)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        Glide.with(parent?.context!!).load(t).apply(RequestOptions.centerCropTransform()).into(imageView)
        itemView.findViewById<View>(R.id.deleteView).setOnClickListener {
           onDeleteItem(position)
        }
        return itemView
    }
}