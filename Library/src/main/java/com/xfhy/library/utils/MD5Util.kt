package com.xfhy.library.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Created by xfhy on 2017/4/17.
 * 进行字符串的加密  算法是MD5
 */

object MD5Util {

    private val CONFUSED = "a@#sdsafada^&.46465456'ld'a.*/-+s"  //加盐   混淆

    /**
     * 将字符串加密   算法是MD5
     * 已加盐  混淆处理
     *
     * @param str 需要加密的字符串
     * @return 返回MD5算法加密之后的32位字符
     */
    fun encoder(str: String): String {

        try {
            val digest = MessageDigest.getInstance("MD5")
            val bs = digest.digest("$str$CONFUSED".toByteArray())
            //System.out.println(bs.length);   //这里的长度是16位
            val stringBuffer = StringBuffer()
            for (b in bs) {
                val i = b and 0xff.toByte()   //将字节码转换成整数
                var hexString = Integer.toHexString(Math.abs(i.toInt()))  //将整数转换成16进制数
                if (hexString.length == 1) {   //如果是1位  则强行弄成2位
                    hexString = "0$hexString"
                }
                stringBuffer.append(hexString)   //拼接字符串
            }
            return stringBuffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""

    }

}
