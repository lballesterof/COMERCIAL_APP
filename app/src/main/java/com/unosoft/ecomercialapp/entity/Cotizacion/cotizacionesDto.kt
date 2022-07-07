package com.unosoft.ecomercialapp.entity.Cotizacion

import java.time.LocalDateTime
import java.util.*

data class cotizacionesDto (val id_cotizacion: Int,
                            val numero_Cotizacion: String,
                            val persona: String,
                            val ruc: String,
                            val documento: String,
                            val mon: String,
                            val importe_descuento: Double,
                            val importe_igv: Double,
                            val importe_total: Double,
                            val eSTADO: String)