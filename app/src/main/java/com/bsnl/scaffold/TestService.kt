package com.bsnl.scaffold

import com.dawn.wan.data.model.Response
import com.dawn.wan.data.model.User
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/12
 * @desc   :
 */
interface TestService {

    @POST("user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<User>

}