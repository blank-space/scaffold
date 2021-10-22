package com.dawn.base.utils

import android.view.View

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/19
 * @desc   :
 */

var View.onClick: (View) -> Unit
    get() {
        return {}
    }
    set(value) {
        setOnClickListener { v -> value(v) }
    }
