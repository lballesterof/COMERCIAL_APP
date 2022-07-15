package com.unosoft.ecomercialapp.db.pedido

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
@Entity
data class EntityEditPedidoDetail(

        @PrimaryKey(autoGenerate = true)
        val id: Int = 1,
        @ColumnInfo
        val iD_PEDIDO: Int?,
        @ColumnInfo
        val iD_PRODUCTO: Int?,
        @ColumnInfo
        val cantidad: Int?,
        @ColumnInfo
        val nombre:String?,
        @ColumnInfo
        val precio: Int?,
        @ColumnInfo
        val descuento: Int?,
        @ColumnInfo
        val igv: Double,
        @ColumnInfo
        val importe: Int?,
        @ColumnInfo
        val canT_DESPACHADA: Int?,
        @ColumnInfo
        val canT_FACTURADA: Int?,
        @ColumnInfo
        val observacion:String?,
        @ColumnInfo
        val secuencia: Int?,
        @ColumnInfo
        val preciO_ORIGINAL: Int?,
        @ColumnInfo
        val tipo:String?,
        @ColumnInfo
        val importE_DSCTO: Int?,
        @ColumnInfo
        val afectO_IGV:String?,
        @ColumnInfo
        val comision: Int?,
        @ColumnInfo
        val iD_PRESUPUESTO: Int?,
        @ColumnInfo
        val cdG_SERV:String?,
        @ColumnInfo
        val flaG_C:String?,
        @ColumnInfo
        val flaG_P:String?,
        @ColumnInfo
        val flaG_COLOR:String?,
        @ColumnInfo
        val noM_UNIDAD:String?,
        @ColumnInfo
        val comanda:String?,
        @ColumnInfo
        val mozo:String?,
        @ColumnInfo
        val unidad:String?,
        @ColumnInfo
        val codigO_BARRA:String?,
        @ColumnInfo
        val poR_PERCEPCION: Int?,
        @ColumnInfo
        val percepcion: Int?,
        @ColumnInfo
        val valoR_VEN: Int?,
        @ColumnInfo
        val uniD_VEN:String?,
        @ColumnInfo
        val fechA_VEN:String?,
        @ColumnInfo
        val factoR_CONVERSION: Int?,
        @ColumnInfo
        val cdG_KIT:String?,
        @ColumnInfo
        val swT_PIGV:String?,
        @ColumnInfo
        val swT_PROM:String?,
        @ColumnInfo
        val canT_KIT: Int?,
        @ColumnInfo
        val swT_DCOM:String?,
        @ColumnInfo
        val swT_SABOR:String?,
        @ColumnInfo
        val swT_FREE:String?,
        @ColumnInfo
        val noM_IMP:String?,
        @ColumnInfo
        val seC_PROD:String?,
        @ColumnInfo
        val poR_DETRACCION:String?,
        @ColumnInfo
        val detraccion:String?,
        @ColumnInfo
        val usuariO_ANULA:String?,
        @ColumnInfo
        val fechA_ANULA:String?,
        @ColumnInfo
        val margen: Double,
        @ColumnInfo
        val importE_MARGEN: Double,
        @ColumnInfo
        val costO_ADIC: Double
    )
