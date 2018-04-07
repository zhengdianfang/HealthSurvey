package com.zhengdianfang.healthsurvey.entities

/**
 * Created by dfgzheng on 05/04/2018.
 */
data class PartGroup(var base: Part, var inuse: Part, var nouse: Part)

data class Part(var title: String, var list: MutableList<Group>)

data class Group(var list_title: String, var group: MutableList<GroupChild>)

data class GroupChild(var group_name: String, var group_id: String, var required: String) {
    fun isRequired(): Boolean {
        return required == "1"
    }
}