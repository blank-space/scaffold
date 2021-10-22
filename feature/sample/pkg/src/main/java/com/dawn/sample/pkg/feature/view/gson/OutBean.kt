package com.dawn.sample.pkg.feature.view.gson

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/1
 * @desc   :
 */
class OutBean(
    var outName: String,
    var age: Int,
    var bean: InnerBean,
    var list: MutableList<String>) {

    class InnerBean(var innerName: String) {
        override fun toString(): String {
            return "InnerBean(innerName='$innerName')"
        }
    }

    override fun toString(): String {
        return "OutBean(outName='$outName', age=$age, bean=$bean, list=$list)"
    }
}
