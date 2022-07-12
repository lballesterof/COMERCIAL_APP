package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


data class EntityListaPrecio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo
    val codigo: String,
    @ColumnInfo
    val nombre: String,
)