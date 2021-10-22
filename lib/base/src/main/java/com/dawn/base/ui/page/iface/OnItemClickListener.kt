package com.dawn.base.ui.page.iface

import android.view.View

interface OnItemClickListener {
    fun onItemClick(v: View, position: Int)

    fun onChildClick(v: View,position: Int)
}