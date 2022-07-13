package com.unosoft.ecomercialapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EntityCondicionPago::class,
                EntityDepartamento::class,
                EntityDistrito::class,
                EntityDocIdentidad::class,
                EntityFrecuenciaDias::class,
                EntityMoneda::class,
                EntityProvincia::class,
                EntityUnidadMedida::class,
                EntityPedidoMaster::class,
                EntityListaPrecio::class],
    version = 4)

abstract class TablaBasica : RoomDatabase() {
    abstract fun daoTblBasica(): DAO
}