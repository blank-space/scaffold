package com.bsnl.sample.pkg.feature.view.databinding

import androidx.databinding.Bindable
import com.bsnl.sample.pkg.BR

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/10
 * @desc   :
 */
class GoodsBean : ObservableViewModel {

    constructor(count: Long) : super() {
        this.count = count
    }

    var count: Long = 0
        @Bindable
        set(value) {
            if (value == field) {
                return
            }
            field = value
            notifyPropertyChanged(BR._all)
        }
}