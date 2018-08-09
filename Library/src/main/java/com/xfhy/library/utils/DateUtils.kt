package com.xfhy.library.utils


import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * author xfhy
 * create at 2017/10/12 11:58
 * description：日期的工具类
 */
object DateUtils {

    /**
     * 格式化Date
     *
     * @param date   Date
     * @param format 日期的格式
     * @return 返回格式化之后的字符串
     */
    fun getDateFormatText(date: Date, format: String): String {
        if (date == null || format == null) {
            return ""
        }
        val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
        return simpleDateFormat.format(date)
    }

    /**
     * 格式化Date
     *
     * @param date   Date
     * @param format 日期的格式
     * @return 返回格式化之后的字符串
     */
    fun getDateFormatText(time: Long, format: String): String {
        if (time == null || format == null) {
            return ""
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * 计算Date-pastDays的日期,并返回该日期格式化之后的字符串
     *
     * @param date     Date
     * @param pastDays 减去的天数
     * @return 返回减去天数之后的日期对应的Date
     */
    fun getPastDate(date: Date, pastDays: Int): Date {
        if (date == null) {
            return Date()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -pastDays)
        return calendar.time
    }

    fun formatSecondToMinute(duration: Int): String {
        val second = duration % 60
        val minute = duration / 60
        return "${if (minute >= 10) minute else "0${minute}"}: ${if (second >= 10) second else "0${second}"}"
    }

    /**
     * 字符串转日期
     */
    fun getDateByText(date: String, format: String): Date {
        val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
        //万一转换出错了呢....
        return try {
            simpleDateFormat.parse(date)
        } catch (e: Exception) {
            Date()
        }
    }

    /**
     * 获取月份缩写
     */
    fun getMonthAbridge(month: Int) = when (month) {
        1 -> "Jan."
        2 -> "Feb."
        3 -> "Mar."
        4 -> "Apr."
        5 -> "May."
        6 -> "June."
        7 -> "July."
        8 -> "Aug."
        9 -> "Sept."
        10 -> "Oct."
        11 -> "Nov."
        12 -> "Dec."
        else -> "Jan."
    }

    /**
     * 获取星期文本
     */
    fun getWeekText(week: Int) = when (week) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "Monday"
    }

}
