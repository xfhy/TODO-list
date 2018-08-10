package com.xfhy.todo.data.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by feiyang on 2018/8/10 13:01
 * Description :
 */

data class LoginBean(
        @SerializedName("data") var data: Data = Data(),
        @SerializedName("errorCode") var errorCode: Int = 0,
        @SerializedName("errorMsg") var errorMsg: String = ""
) : Serializable {

    data class Data(
            @SerializedName("collectIds") var collectIds: List<Any> = listOf(),
            @SerializedName("email") var email: String = "",
            @SerializedName("icon") var icon: String = "",
            @SerializedName("id") var id: Int = 0,
            @SerializedName("password") var password: String = "",
            @SerializedName("type") var type: Int = 0,
            @SerializedName("username") var username: String = ""
    ) : Serializable
}