package com.unosoft.ecomercialapp.entity.Pedidos

data class EnviarPedido(
    val iD_PEDIDO: Int,
    val numerO_PEDIDO: String,
    val noM_MON: String,
    val smB_MON: String,
    val conD_PAGO: String,
    val persona: String,
    val ruc: String,
    val freC_DIAS: String,
    val codigO_VENDEDOR: String,
    val codigO_CPAGO: String,
    val codigO_MONEDA: String,
    val fechA_PEDIDO: String,
    val numerO_OCLIENTE: String,
    val importE_STOT: Int,
    val importE_IGV: Int,
    val importE_DESCUENTO: Int,
    val importE_TOTAL: Int,
    val porcentajE_DESCUENTO: Int,
    val porcentajE_IGV: Int,
    val observacion: String,
    val serie: String,
    val estado: String,
    val iD_CLIENTE: Int,
    val importE_ISC: Int,
    val usuariO_CREACION: String,
    val usuariO_AUTORIZA: String,
    val fechA_CREACION: String,
    val fechA_MODIFICACION: String,
    val codigO_EMPRESA: String,
    val codigO_SUCURSAL: String,
    val valoR_VENTA: Int,
    val iD_CLIENTE_FACTURA: Int,
    val codigO_VENDEDOR_ASIGNADO: String,
    val fechA_PROGRAMADA: String,
    val facturA_ADELANTADA: String,
    val contacto: String,
    val emaiL_CONTACTO: String,
    val lugaR_ENTREGA: String,
    val iD_COTIZACION: Int,
    val comision: Int,
    val puntO_VENTA: String,
    val redondeo: String,
    val validez: String,
    val motivo: String,
    val correlativo: String,
    val centrO_COSTO: String,
    val tipO_CAMBIO: Int,
    val sucursal: String,
    val mesa: String,
    val piso: String,
    val detalle: List<Detalle>
)

data class Detalle(
    val iD_PEDIDO: Int,
    val iD_PRODUCTO: Int,
    val cantidad: Int,
    val nombre: String,
    val precio: Int,
    val descuento: Int,
    val igv: Int,
    val importe: Int,
    val canT_DESPACHADA: Int,
    val canT_FACTURADA: Int,
    val observacion: String,
    val secuencia: Int,
    val preciO_ORIGINAL: Int,
    val tipo: String,
    val importE_DSCTO: Int,
    val afectO_IGV: String,
    val comision: Int,
    val iD_PRESUPUESTO: Int,
    val cdG_SERV: String,
    val flaG_C: String,
    val flaG_P: String,
    val flaG_COLOR: String,
    val noM_UNIDAD: String,
    val comanda: String,
    val mozo: String,
    val unidad: String,
    val codigO_BARRA: String,
    val poR_PERCEPCION: Int,
    val percepcion: Int,
    val valoR_VEN: Int,
    val uniD_VEN: String,
    val fechA_VEN: String,
    val factoR_CONVERSION: Int,
    val cdG_KIT: String,
    val swT_PIGV: String,
    val swT_PROM: String,
    val canT_KIT: Int,
    val swT_DCOM: String,
    val swT_SABOR: String,
    val swT_FREE: String,
    val noM_IMP: String,
    val seC_PROD: Int,
    val poR_DETRACCION: Int,
    val detraccion: Int,
    val usuariO_ANULA: String,
    val fechA_ANULA: String,
    val margen: Int,
    val importE_MARGEN: Int,
    val costO_ADIC: Int
)