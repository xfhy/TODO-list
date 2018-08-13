package com.xfhy.todo.data.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by feiyang on 2018/8/13 11:44
 * Description :
 */

data class TodoBean(
        @SerializedName("data") var data: Data = Data(),
        @SerializedName("errorCode") var errorCode: Int = 0,
        @SerializedName("errorMsg") var errorMsg: String = ""
) : Serializable {

    data class Data(
            @SerializedName("doneList") var doneList: List<Done> = listOf(),
            @SerializedName("todoList") var todoList: List<Todo> = listOf(),
            @SerializedName("type") var type: Int = 0
    ) : Serializable {

        /**
         * 未完成
         */
        data class Todo(
                @SerializedName("date") var date: Long = 0,
                @SerializedName("todoList") var todoList: List<Todo> = listOf()
        ) : Serializable
    }

    /**
     * 已完成
     */
    data class Done(
            @SerializedName("date") var date: Long = 0,
            @SerializedName("todoList") var todoList: List<Todo> = listOf()
    ) : Serializable
}

data class Todo(
        @SerializedName("completeDate") var completeDate: Long = 0,
        @SerializedName("completeDateStr") var completeDateStr: String = "",
        @SerializedName("content") var content: String = "",
        @SerializedName("date") var date: Long = 0,
        @SerializedName("dateStr") var dateStr: String = "",
        @SerializedName("id") var id: Int = 0,
        @SerializedName("status") var status: Int = 0,
        @SerializedName("title") var title: String = "",
        @SerializedName("type") var type: Int = 0,
        @SerializedName("userId") var userId: Int = 0
) : Serializable