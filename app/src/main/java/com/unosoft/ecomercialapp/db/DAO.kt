package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse

@Dao
interface DAO {

    //*************   QUERY DE TABLAS    *********************
    @Query("SELECT * FROM EntityCondicionPago")
    fun getAllCondicionPago(): List<EntityCondicionPago>

    @Query("SELECT * FROM EntityDepartamento")
    fun getAllDepartamento(): List<EntityDepartamento>

    @Query("SELECT * FROM EntityDistrito")
    fun getAllDistrito(): List<EntityDistrito>

    @Query("SELECT * FROM EntityDocIdentidad")
    fun getAllDocIdentidad(): List<EntityDocIdentidad>

    @Query("SELECT * FROM EntityFrecuenciaDias")
    fun getAllFrecuenciaDias(): List<EntityFrecuenciaDias>

    @Query("SELECT * FROM EntityMoneda")
    fun getAllMoneda(): List<EntityMoneda>

    @Query("SELECT * FROM EntityProvincia")
    fun getAllProvincia(): List<EntityProvincia>

    @Query("SELECT * FROM EntityUnidadMedida")
    fun getAllUnidadMedida(): List<EntityUnidadMedida>

    @Query("SELECT * FROM EntityPedidoMaster")
    fun getAllPedidoMaster(): List<EntityPedidoMaster>

    @Query("SELECT * FROM EntityListaPrecio")
    fun getAllListaPrecio(): List<EntityListaPrecio>

    @Query("SELECT * FROM EntityListProctCot")
    fun getAllListProctCot(): List<EntityListProctCot>


    //*************   INSERT DE TABLAS    *********************
    @Insert
    fun insertCondicionPago( insertCondicionPago: EntityCondicionPago)

    @Insert
    fun insertDepartamento( insertDepartamento: EntityDepartamento)

    @Insert
    fun insertDistrito( insertDistrito: EntityDistrito)

    @Insert
    fun insertDocIdentidad( insertDocIdentidad: EntityDocIdentidad)

    @Insert
    fun insertFrecuenciaDias( insertFrecuenciaDias: EntityFrecuenciaDias)

    @Insert
    fun insertMoneda( insertMoneda: EntityMoneda)

    @Insert
    fun insertProvincia( insertProvincia: EntityProvincia)

    @Insert
    fun insertUnidadMedida( insertUnidadMedida: EntityUnidadMedida)

    @Insert
    fun insertPedidoMaster( insertPedidoMaster: EntityPedidoMaster)

    @Insert
    fun insertListaPrecio( insertListaPrecio: EntityListaPrecio)

    @Insert
    fun insertListProctCot( insertListProctCot: EntityListProctCot)



    //*************   Datos    *********************
    @Query("SELECT Nombre,Numero,Referencia1  FROM EntityMoneda WHERE id= :id")
    fun getTipoMoneda(id:Int): MonedaResponse

    @Query("SELECT COUNT(*) from EntityMoneda")
    fun getSizeMoneda(): Int


    @Query("SELECT id_Producto,codigo,codigo_Barra,nombre,mon,precio_Venta,factor_Conversion,cdg_Unidad,unidad,moneda_Lp,cantidad,precioUnidad,precioTotal  FROM EntityListProctCot WHERE id= :id")
    fun getEntityListProctCot(id:Int): productlistcot

    @Query("SELECT COUNT(*) from EntityListProctCot")
    fun getSizeListProctCot(): Int





    //*********  ELIMINAR DATOS DE LAS TABLAS  **************
    @Query("DELETE FROM EntityCondicionPago")
    fun deleteTableCondicionPago()

    @Query("DELETE FROM EntityDepartamento")
    fun deleteTableDepartamento()

    @Query("DELETE FROM EntityDistrito")
    fun deleteTableDistrito()

    @Query("DELETE FROM EntityDocIdentidad")
    fun deleteTableDocIdentidad()

    @Query("DELETE FROM EntityFrecuenciaDias")
    fun deleteTableFrecuenciaDias()

    @Query("DELETE FROM EntityMoneda")
    fun deleteTableMoneda()

    @Query("DELETE FROM EntityProvincia")
    fun deleteTableProvincia()

    @Query("DELETE FROM EntityUnidadMedida")
    fun deleteTableUnidadMedida()

    @Query("DELETE FROM EntityPedidoMaster")
    fun deleteTablePedidoMaster()

    @Query("DELETE FROM EntityListaPrecio")
    fun deleteTableListaPrecio()

    @Query("DELETE FROM EntityListProctCot")
    fun deleteTableListProctCot()



    //*********  REINICIAR LOS ID AUTOGENERADOS   **************

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityCondicionPago'")
    fun clearPrimaryKeyCondicionPago()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDepartamento'")
    fun clearPrimaryKeyDepartamento()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDistrito'")
    fun clearPrimaryKeyDistrito()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDocIdentidad'")
    fun clearPrimaryKeyDocIdentidad()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityFrecuenciaDias'")
    fun clearPrimaryKeyFrecuenciaDias()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityMoneda'")
    fun clearPrimaryKeyMoneda()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityProvincia'")
    fun clearPrimaryKeyProvincia()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityUnidadMedida'")
    fun clearPrimaryKeyUnidadMedida()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityPedidoMaster'")
    fun clearPrimaryKeyPedidoMaster()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityListaPrecio'")
    fun clearPrimaryKeyListaPrecio()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityListProctCot'")
    fun clearPrimaryKeyListProctCot()


    //*****************  CONSULTA DE LA EXISTENCIA DE DATABASE *******************
    @Query("SELECT EXISTS(SELECT * FROM EntityCondicionPago)")
    fun isExists(): Boolean

    //*****************  CONSULTA DE LA EXISTENCIA DE DATABASE *******************
    @Query("SELECT EXISTS(SELECT * FROM EntityListProctCot)")
    fun isExistsEntityListProctCot(): Boolean





    //*****************  CONSULTA TABLAS BASICAS *******************
    @Query("SELECT Nombre FROM EntityCondicionPago WHERE Codigo = :Numero  ")
    fun findnamecategoriapagowithnumero(Numero :String): String
}
