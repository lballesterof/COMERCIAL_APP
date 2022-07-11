package com.unosoft.ecomercialapp.api
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import retrofit2.Response
import retrofit2.http.*

interface ApiCotizacion {

    @GET("/api/Cotizacion/{cdg_ven}")
    suspend fun fetchAllCotizaciones(@Path("cdg_ven") cdg_ven:String) : Response<List<cotizacionesDto>>

}