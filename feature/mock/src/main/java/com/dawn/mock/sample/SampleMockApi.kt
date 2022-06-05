package com.dawn.mock.sample

import android.content.Context
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.dawn.mock.MockPath
import com.dawn.sample.export.api.ISampleService

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
@Route(path = MockPath.S_SERVICE)
class SampleMockApi : ISampleService {

    private var ctx: Context? = null


    override fun startListViewActivity() {
        ctx?.let {
            Toast.makeText(it, "startListViewActivity", Toast.LENGTH_SHORT).show()
        }
    }

    override fun startFindLocationActivity() {
        ctx?.let {
            Toast.makeText(it, "startFindLocationActivity", Toast.LENGTH_SHORT).show()
        }
    }

    override fun startSearchActivity() {
        ctx?.let {
            Toast.makeText(it, "startSearchActivity", Toast.LENGTH_SHORT).show()
        }
    }

    override fun startHexStatusManagerActivity() {
        ctx?.let {
            Toast.makeText(it, "startHexStatusManagerActivity", Toast.LENGTH_SHORT).show()
        }
    }

    override fun init(context: Context?) {
        ctx = context
    }

}