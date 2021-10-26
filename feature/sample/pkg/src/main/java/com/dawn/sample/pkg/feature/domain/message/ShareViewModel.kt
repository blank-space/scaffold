package com.dawn.sample.pkg.feature.domain.message

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/26
 * @desc   : Event-ViewModel 的职责仅限于在"跨页面通信"的场景下，承担"唯一可信源"，
 */
class ShareViewModel : ViewModel() {
    val countLiveData = UnPeekLiveData<Int>()
}