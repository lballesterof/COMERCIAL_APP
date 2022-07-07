package com.unosoft.ecomercialapp.api
import com.unosoft.ecomercialapp.entity.Cliente.Cliente
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ClientsApi {
    @GET("/api/Persona?\u0024filter=TIPO eq '0002'&\u0024select=codigo,nombre,ruc")
    @Headers("Content-Type:application/json")
    suspend fun getAllClients(): Response<Cliente>
}