package com.xfhy.todo.data.bean

import com.google.gson.annotations.SerializedName
import com.xfhy.library.adapter.entity.SectionEntity
import java.io.Serializable


/**
 * Created by feiyang on 2018/8/13 11:44
 * Description : tudo列表的bean
 */


data class TodoBean(
        @SerializedName("data") var data: Data = Data(),
        @SerializedName("errorCode") var errorCode: Int = 0,
        @SerializedName("errorMsg") var errorMsg: String = ""
) {

    data class Data(
            @SerializedName("curPage") var curPage: Int = 0,
            @SerializedName("datas") var todoList: MutableList<TodoItem> = mutableListOf(),
            @SerializedName("offset") var offset: Int = 0,
            @SerializedName("over") var over: Boolean = false,
            @SerializedName("pageCount") var pageCount: Int = 0,
            @SerializedName("size") var size: Int = 0,
            @SerializedName("total") var total: Int = 0
    ) {

        class TodoItem : SectionEntity<TodoItem>, Serializable {
            @SerializedName("completeDate")
            var completeDate: Any = Any()
            @SerializedName("completeDateStr")
            var completeDateStr: String = ""
            @SerializedName("content")
            var content: String = ""
            @SerializedName("date")
            var date: Long = 0
            @SerializedName("dateStr")
            var dateStr: String = ""
            @SerializedName("id")
            var id: Int = 0
            @SerializedName("status")
            var status: Int = 0
            @SerializedName("title")
            var title: String = ""
            @SerializedName("type")
            var type: Int = 0
            @SerializedName("userId")
            var userId: Int = 0

            constructor() : super()
            constructor(isHeader: Boolean) : super(isHeader)
            constructor(t: TodoItem) : super(t)
            constructor(isHeader: Boolean, header: String) : super(isHeader, header)
        }
    }
}