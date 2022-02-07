package com.dawn.base.ui.callback


/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/7
 * @desc   : 背景色透明
 */
class TransparentLoadingLayoutCallback(): LoadingLayoutCallback() {

    override fun getSuccessVisible() = true

}