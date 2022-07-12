package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Cliente.Cliente
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ListaPrecio {
    @GET("api/TablasBasicas/Listasdeprecio")
    suspend fun getAllListaPrecio(): Response<List<pedidosDto>>
}