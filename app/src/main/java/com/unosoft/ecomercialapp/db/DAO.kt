package com.unosoft.ecomercialapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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


    //*************   INSERT DE TABLAS    *********************







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


    //*********  REINICIAR LOS ID AUTOGENERADOS   **************
    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityCondicionPago'")
    fun clearPrimaryKeyCondicionPago()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityDepartamento'")
    fun clearPrimaryKeyDepartamento()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityDistrito'")
    fun clearPrimaryKeyDistrito()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityDocIdentidad'")
    fun clearPrimaryKeyDocIdentidad()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityFrecuenciaDias'")
    fun clearPrimaryKeyFrecuenciaDias()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityMoneda'")
    fun clearPrimaryKeyMoneda()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityProvincia'")
    fun clearPrimaryKeyProvincia()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityUnidadMedida'")
    fun clearPrimaryKeyUnidadMedida()
}