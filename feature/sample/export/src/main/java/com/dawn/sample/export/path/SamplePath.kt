package com.dawn.sample.export.path

/**
 * @author : LeeZhaoXing
 * @date   : 2021/7/28
 * @desc   :
 */
interface SamplePath {
    companion object {
        private const val GROUP = "/SampleGroup/"
        const val S_SAMPLE_SERVICE = GROUP + "SampleService"

        const val A_LISTVIEW_ACTIVITY = GROUP + "ListViewActivity"

        const val A_WEBVIEW_ACTIVITY = GROUP + "WebViewActivity"
    }
}