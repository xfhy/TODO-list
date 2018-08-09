package com.xfhy.library.utils

/**
 * @author xfhy
 * @create at 2017/11/11 20:22
 * description：html工具类,方便显示
 * 在html中引入外部css,js文件   常规拼接顺序css->html->js
 */
object HtmlUtil {
    //css样式,隐藏header
    private const val HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>"
    //css style tag,需要格式化
    private const val NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>"
    // js script tag,需要格式化
    private const val NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>"
    const val MIME_TYPE = "text/html; charset=utf-8"
    const val ENCODING = "utf-8"

    /**
     * 根据css链接生成Link标签
     *
     * @param url String
     * @return String
     */
    fun createCssTag(url: String): String {
        return String.format(NEEDED_FORMAT_CSS_TAG, url)
    }

    fun createCssTag(urls: List<String>): String {
        val sb = StringBuffer()
        for (url in urls) {
            sb.append(createCssTag(url))
        }
        return sb.toString()
    }

    fun createJsTag(url: String): String {
        return String.format(NEEDED_FORMAT_JS_TAG, url)
    }

    fun createJsTag(urls: List<String>): String {
        val sb = StringBuilder()
        for (url in urls) {
            sb.append(createJsTag(url))
        }
        return sb.toString()
    }

    fun createHtmlData(html: String, cssList: List<String>, jsList: List<String>): String {
        val css = createCssTag(cssList)
        val js = createJsTag(jsList)
        return "$css$HIDE_HEADER_STYLE$html$js"
    }

}