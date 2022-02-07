package com.dawn.base.contentProvider

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author : LeeZhaoXing
 * @date   : 2021/4/20
 * @desc   :
 */
@SuppressLint("StaticFieldLeak")
class BaseArchContentProvider : ContentProvider(), ViewModelStoreOwner {
    companion object {
        /**
         * Get Context at anywhere
         */
        lateinit var ctx: Context

        /**
         * Get Application at anywhere
         */
        lateinit var app: Application

        lateinit var instance: BaseArchContentProvider

    }

    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate(): Boolean {
        instance = this
        ctx = context!!
        app = ctx.applicationContext as Application
        mAppViewModelStore = ViewModelStore()
        return true
    }

    override fun query(
        uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?,
        s1: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        return 0
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }



}