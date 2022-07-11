package com.unosoft.ecomercialapp.entity.Pedidos

import java.time.LocalDateTime
import java.util.*

data class pedidosDto(
    val id_pedido: Int,
    val numero_Pedido: Any,
    val fecha_pedido: String,
    val persona: String,
    val ruc: String,
    val documento: String,
    val direccion: Any,
    val telefono: Any,
    val lugar_entrega: Any,
    val mon: String,
    val importe_descuento: Int,
    val valor_venta: Int,
    val nom_moneda: Any,
    val importe_igv: Double,
    val importe_Total: Double,
    val estado: String
)