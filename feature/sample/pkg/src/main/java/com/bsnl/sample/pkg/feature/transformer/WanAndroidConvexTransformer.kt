package com.bsnl.sample.pkg.feature.transformer

import com.bsnl.common.BaseResponse
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.paradisehell.convex.annotation.AutoTransformer
import org.paradisehell.convex.transformer.ConvexTransformer
import java.io.IOException
import java.io.InputStream

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
@AutoTransformer
class WanAndroidConvexTransformer : ConvexTransformer {
    private val gson = Gson()

    @Throws(IOException::class)
    override fun transform(original: InputStream): InputStream {
        val response = gson.fromJson<BaseResponse<JsonElement>>(
            original.reader(),
            object : TypeToken<BaseResponse<JsonElement>>() {
            }.type
        )
        if (response.errorCode == 0 && response.data != null) {
            return response.data.toString().byteInputStream()
        }
        throw IOException(
            "errorCode : " + response.errorCode + " ; errorMsg : " + response.errorMsg
        )
    }
}