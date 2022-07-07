package com.unosoft.ecomercialapp.api
import com.unosoft.ecomercialapp.entity.Login.Login
import com.unosoft.ecomercialapp.entity.Login.LoginResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers

interface LoginApi {
    @POST("api/Users/Login")
    @Headers("Content-Type:application/json")
    fun login(@Body data: Login?): Call<LoginResponse?>?
}