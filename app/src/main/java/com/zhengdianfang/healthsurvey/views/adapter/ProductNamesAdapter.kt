package com.zhengdianfang.healthsurvey.views.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter

class ProductNamesAdapter(context: Context?, resource: Int, var datas: MutableList<String>) : ArrayAdapter<String>(context, resource, datas) {

    private var originDatas: MutableList<String> = mutableListOf()
    init {
       originDatas.addAll(datas)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (TextUtils.isEmpty(p0))  {
                    filterResults.count = originDatas.count()
                    filterResults.values = originDatas
                } else {
                    val res = originDatas.filter{ it.contains(p0!!) }
                    filterResults.values = res
                    filterResults.count = res.count()
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (p1?.count!! > 0 && p1.values != null) {
                    val collection = p1.values as Collection<String>
                    Log.d("1123123", "${collection.count()}")
                    datas.clear()
                    datas.addAll(collection as MutableList<String>)
                    notifyDataSetChanged()
                }
            }

        }

    }
}