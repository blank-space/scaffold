package com.dawn.mock

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
interface MockPath {
    companion object{
        private const val GROUP = "/MockGroup/"
        const val S_SERVICE = GROUP + "MockService"
    }
}