package com.unosoft.ecomercialapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail

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
                EntityListaPrecio::class,
                EntityListProctCot::class,EntityEditPedidoDetail::class],
    version = 8)

abstract class TablaBasica : RoomDatabase() {
    abstract fun daoTblBasica(): DAO
}