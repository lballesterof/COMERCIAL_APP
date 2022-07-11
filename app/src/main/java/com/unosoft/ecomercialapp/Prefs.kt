package com.example.apppedido

import android.content.Context

class Prefs (contexto:Context){
    val SHARE_DB = "Mydtb"

    val SHARE_CDGVENDEDOR = "CDGVENDEDOR"
    val SHARE_TIPOCAMBIO = "TIPOCAMBIO"

    val storege = contexto.getSharedPreferences(SHARE_DB,0)

    fun save_CdgVendedor(CDGVENDEDOR:String){
        storege.edit().putString(SHARE_CDGVENDEDOR,CDGVENDEDOR).apply()
    }

    fun save_TipoCambio(TIPOCAMBIO:String){
        storege.edit().putString(SHARE_TIPOCAMBIO,TIPOCAMBIO).apply()
    }

    fun getCdgVendedor(): String {
        return storege.getString(SHARE_CDGVENDEDOR,"")!!
    }

    fun getTipoCambio(): String {
        return storege.getString(SHARE_TIPOCAMBIO,"")!!
    }

    fun wipe(){
        storege.edit().clear().apply()
    }


}