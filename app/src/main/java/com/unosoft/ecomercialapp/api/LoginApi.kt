package com.unosoft.ecomercialapp.api
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import com.unosoft.ecomercialapp.entity.Login.Login
import com.unosoft.ecomercialapp.entity.Login.LoginComercialResponse
import com.unosoft.ecomercialapp.entity.Login.LoginResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers

interface LoginApi {
    @POST("api/Users/Login")
    fun login(@Body data: Login?): Call<LoginResponse?>?

    @POST("api/Users/LoginComercial")
    suspend fun checkLoginComanda(@Body loginMozo: DCLoginUser) : Response<LoginComercialResponse>
}