package com.xfhy.library.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileOutputStream

/**
 * Created by xfhy on 2018/3/7 18:35
 * Description : 图片工具类
 */
object ImageUtil {

    /**
     * 压缩图片
     * @param path 图片路径
     */
    @JvmStatic
    fun compressImage(context: Context, path: String): Bitmap? {
        //创建bitmap工厂的配置参数
        val options = BitmapFactory.Options()
        //这个设置为true 则不返回bitmap直接返回null  然后decodeFile()方法将图片的信息封装到Options里面
        options.inJustDecodeBounds = true
        //根据路径加载图片  将图片转换成bitmap,这里只是获取图片宽高
        BitmapFactory.decodeFile(path, options)
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight
        Logger.e("宽 :$imageWidth   高:$imageHeight")

        //根据需要显示的控件宽高来获取缩放比例   因为用户头像展示ImageView宽高为80dp
        var scale = 1
        val scaleX = imageWidth / DensityUtil.dip2px(context, 80f)
        val scaleY = imageHeight / DensityUtil.dip2px(context, 80f)
        //挑选其中缩放比较大的来
        if (scaleX > scale && scaleX > scaleY) {
            scale = scaleX
        }
        if (scaleY > scale && scaleY > scaleX) {
            scale = scaleY
        }

        Logger.e("缩放比:$scale")
        //设置图片的缩放比 ,用来节约内存
        options.inSampleSize = scale
        options.inJustDecodeBounds = false
        //重新设置Options 并生成Bitmap
        return BitmapFactory.decodeFile(path, options)
    }

    /**
     * 保存Bitmap成图片文件到本地  文件名为当前时间
     * @param bitmap  bitmap
     * @param path 保存路径
     */
    @JvmStatic
    fun saveImageToLocal(bitmap: Bitmap?, path: String): File? {
        var file: File? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            //记录图片的真实路径
            val stringBuffer = StringBuffer(path)
            val lastIndexOf = stringBuffer.lastIndexOf("/")
            stringBuffer.delete(lastIndexOf, stringBuffer.length)
            file = File(stringBuffer.toString(), "/${System.currentTimeMillis()}.jpg")
            fileOutputStream = FileOutputStream(file)
            //压缩  输出到输出流
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return file
    }

}