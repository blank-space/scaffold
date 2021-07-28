package com.bsnl.common.iface

/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/11
 * @desc   :
 */
/**
 * @param illustrateStrId :详细说明的文字 设计成Int型是为了与msg做区分
 * @param msg : 标题
 */
data class ViewStateWithMsg(var illustrateStrId: Int? = null, var msg: String? = "", val state: ViewState?)