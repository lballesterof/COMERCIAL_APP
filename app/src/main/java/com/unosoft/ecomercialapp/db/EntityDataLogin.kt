package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityDataLogin (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo
    val usuario: String,
    @ColumnInfo
    val codigO_EMPRESA: String,
    @ColumnInfo
    val iD_CLIENTE: Int,
    @ColumnInfo
    val cdgmoneda: String,
    @ColumnInfo
    val validez: String,
    @ColumnInfo
    val cdgpago: String,
    @ColumnInfo  //****
    val sucursal: String,
    @ColumnInfo
    val usuarioautoriza: String,
    @ColumnInfo
    val usuariocreacion: String,
    @ColumnInfo
    val tipocambio: Double,
    @ColumnInfo
    val iD_COTIZACION: String,
    @ColumnInfo
    val redondeo: String,
    //@ColumnInfo
    //val motivo: String,
    //@ColumnInfo
    //val correlativo: String,
    @ColumnInfo
    val cdG_VENDEDOR: String
    )