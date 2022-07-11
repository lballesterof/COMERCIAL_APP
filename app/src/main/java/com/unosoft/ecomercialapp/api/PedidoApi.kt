package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto
import com.unosoft.ecomercialapp.entity.Stocks.ConsultaStocksResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PedidoApi{

    @GET("api/PedidoComercial/{cdg_ven}")
    suspend fun getPedido(@Path("cdg_ven") cdg_ven:String): Response<List<pedidosDto>>
}
