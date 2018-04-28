package com.zhengdianfang.healthsurvey.views.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter

class ProductNamesAdapter(context: Context?, resource: Int, val datas: MutableList<String>?) : ArrayAdapter<String>(context, resource, datas) {

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (TextUtils.isEmpty(p0))  {
                    filterResults.count = 0
                    filterResults.values = arrayOf<String>()
                } else {
                    val res = datas?.filter{ it->
                         Log.d("11111", "${it.contains(p0!!) }")
                         it.contains(p0!!)
                    }
                    Log.d("123123123", res?.count()?.toString() ?: "0")
                    filterResults.values = res
                    filterResults.count = res?.count() ?: 0
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (p1?.count!! > 0 && p1.values != null) {
                    datas?.clear()
                    datas?.addAll(p1.values as Collection<String>)
                }
            }

        }

    }
}