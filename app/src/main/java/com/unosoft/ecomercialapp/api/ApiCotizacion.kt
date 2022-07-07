package com.unosoft.ecomercialapp.api
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface ApiCotizacion {
    @GET("/api/Cotizacion/0005")
    @Headers("Content-Type:application/json")
    suspend fun fetchAllCotizaciones() : Response<List<cotizacionesDto>>
}