package com.zhengdianfang.healthsurvey.views.adapter

import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.entities.Product
import com.zhengdianfang.healthsurvey.entities.Question
import com.zhengdianfang.healthsurvey.views.components.BaseComponent
import com.zhengdianfang.healthsurvey.views.components.ProductCodeElection
import com.zhengdianfang.healthsurvey.views.components.ProductNameElection

/**
 * Created by dfgzheng on 06/04/2018.
 */
class QuestionAdapter(data: MutableList<MultipleItem>?)
    : BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder>(data) {

    init {
//        addItemType(Question.INPUT, R.layout.survey_input_item_layout)
//        addItemType(Question.PHONE_INPUT_ELECTION, R.layout.survey_phone_input_item_layout)
//        addItemType(Question.IDCARD_INPUT_ELECTION, R.layout.survey_input_item_layout)
//        addItemType(Question.SINGLE_ELECTION, R.layout.survey_single_election_layout)
//        addItemType(Question.MULTI_ELECTION, R.layout.survey_mutli_election_layout)
//        addItemType(Question.DATE, R.layout.survey_date_item_layout)
//        addItemType(Question.ADDRESS, R.layout.survey_address_item_layout)
//        addItemType(Question.AUTOCOMPLETE, R.layout.survey_product_name_item_layout)
//        addItemType(Question.AUTO_FILL, R.layout.survey_product_code_item_layout)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleItem?) {
        if (null != helper?.itemView) {
            helper?.itemView!!.setBackgroundResource(R.drawable.rect_item_shap)
            if (item?.component?.getQuestionType() == Question.AUTOCOMPLETE) {
                (item?.component as ProductNameElection).bindData2View(helper?.itemView!!, { product -> fillProductCode(product) })
            } else {
                item?.component?.bindData2View(helper?.itemView!!)
            }
        }
    }


    private fun fillProductCode(product: Product) {
        val index = data.indexOfFirst {  it.component.getQuestionType() == Question.AUTO_FILL  }
        if (index > 0) {
         val component =  data[index].component
            if (null != component && component is ProductCodeElection) {
                component.fill(product.code)
                notifyItemChanged(index)
            }
        }
    }

}

class MultipleItem(val component: BaseComponent) : MultiItemEntity {

    override fun getItemType(): Int {
        return  component.getQuestionType()
    }

}

