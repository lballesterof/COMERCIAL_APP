package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class EntityPedidoMaster(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val iD_PEDIDO: Int,
    @ColumnInfo
    val numerO_PEDIDO: String,
    @ColumnInfo
    val noM_MON: String,
    @ColumnInfo
    val smB_MON: String,
    @ColumnInfo
    val conD_PAGO: String,
    @ColumnInfo
    val persona: String,
    @ColumnInfo
    val ruc: String,
    @ColumnInfo
    val freC_DIAS: String,
    @ColumnInfo
    val codigO_VENDEDOR: String,
    @ColumnInfo
    val codigO_CPAGO: String,
    @ColumnInfo
    val codigO_MONEDA: String,
    @ColumnInfo
    val fechA_PEDIDO: String,
    @ColumnInfo
    val numerO_OCLIENTE: String,
    @ColumnInfo
    val importE_STOT: Int,
    @ColumnInfo
    val importE_IGV: Double,
    @ColumnInfo
    val importE_DESCUENTO: Int,
    @ColumnInfo
    val importE_TOTAL: Int,
    @ColumnInfo
    val porcentajE_DESCUENTO: Int,
    @ColumnInfo
    val porcentajE_IGV: Int,
    @ColumnInfo
    val observacion: String,
    @ColumnInfo
    val serie: String,
    @ColumnInfo
    val estado: String,
    @ColumnInfo
    val iD_CLIENTE: Int,
    @ColumnInfo
    val importE_ISC: Int,
    @ColumnInfo
    val usuariO_CREACION: String,
    @ColumnInfo
    val usuariO_AUTORIZA: String,
    @ColumnInfo
    val fechA_CREACION: String,
    @ColumnInfo
    val fechA_MODIFICACION: String,
    @ColumnInfo
    val codigO_EMPRESA: String,
    @ColumnInfo
    val codigO_SUCURSAL: String,
    @ColumnInfo
    val valoR_VENTA: Double,
    @ColumnInfo
    val iD_CLIENTE_FACTURA: Int,
    @ColumnInfo
    val codigO_VENDEDOR_ASIGNADO: String,
    @ColumnInfo
    val fechA_PROGRAMADA: String,
    @ColumnInfo
    val facturA_ADELANTADA: String,
    @ColumnInfo
    val contacto: String,
    @ColumnInfo
    val emaiL_CONTACTO: String,
    @ColumnInfo
    val lugaR_ENTREGA: String,
    @ColumnInfo
    val iD_COTIZACION: Int,
    @ColumnInfo
    val comision: Int,
    @ColumnInfo
    val puntO_VENTA: String,
    @ColumnInfo
    val redondeo: String,
    @ColumnInfo
    val validez: String,
    @ColumnInfo
    val motivo: String,
    @ColumnInfo
    val correlativo: String,
    @ColumnInfo
    val centrO_COSTO: String,
    @ColumnInfo
    val tipO_CAMBIO: Double,
    @ColumnInfo
    val sucursal: String,

)

data class DetDevolucion(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo
    val iD_PEDIDO: Int,
    @ColumnInfo
    val iD_PRODUCTO: Int,
    @ColumnInfo
    val cantidad: Int,
    @ColumnInfo
    val nombre: String,
    @ColumnInfo
    val precio: Int,
    @ColumnInfo
    val descuento: Int,
    @ColumnInfo
    val igv: Double,
    @ColumnInfo
    val importe: Int,
    @ColumnInfo
    val canT_DESPACHADA: Int,
    @ColumnInfo
    val canT_FACTURADA: Int,
    @ColumnInfo
    val observacion: String,
    @ColumnInfo
    val secuencia: Int,
    @ColumnInfo
    val preciO_ORIGINAL: Int,
    @ColumnInfo
    val tipo: String,
    @ColumnInfo
    val importE_DSCTO: Int,
    @ColumnInfo
    val afectO_IGV: String,
    @ColumnInfo
    val comision: Int,
    @ColumnInfo
    val iD_PRESUPUESTO: Int,
    @ColumnInfo
    val cdG_SERV: String,
    @ColumnInfo
    val flaG_C: String,
    @ColumnInfo
    val flaG_P: String,
    @ColumnInfo
    val flaG_COLOR: String,
    @ColumnInfo
    val noM_UNIDAD: String,
    @ColumnInfo
    val comanda: String,
    @ColumnInfo
    val mozo: String,
    @ColumnInfo
    val unidad: String,
    @ColumnInfo
    val codigO_BARRA: String,
    @ColumnInfo
    val poR_PERCEPCION: Int,
    @ColumnInfo
    val percepcion: Int,
    @ColumnInfo
    val valoR_VEN: Int,
    @ColumnInfo
    val uniD_VEN: String,
    @ColumnInfo
    val fechA_VEN: String,
    @ColumnInfo
    val factoR_CONVERSION: Int,
    @ColumnInfo
    val cdG_KIT: String,
    @ColumnInfo
    val swT_PIGV: String,
    @ColumnInfo
    val swT_PROM: String,
    @ColumnInfo
    val canT_KIT: Int,
    @ColumnInfo
    val swT_DCOM: String,
    @ColumnInfo
    val swT_SABOR: String,
    @ColumnInfo
    val swT_FREE: String,
    @ColumnInfo
    val noM_IMP: String,
    @ColumnInfo
    val seC_PROD: String,
    @ColumnInfo
    val poR_DETRACCION: String,
    @ColumnInfo
    val detraccion: String,
    @ColumnInfo
    val usuariO_ANULA: String,
    @ColumnInfo
    val fechA_ANULA: LocalDateTime,
    @ColumnInfo
    val margen: Double,
    @ColumnInfo
    val importE_MARGEN: Double,
    @ColumnInfo
    val costO_ADIC: Double
)

