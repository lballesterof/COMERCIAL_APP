package com.unosoft.ecomercialapp.ui.Empresas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Activity.inicio.InicioActivity
import com.unosoft.ecomercialapp.Adapter.Empresa.AdtEmpresa
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.MainActivity
import com.unosoft.ecomercialapp.databinding.ActivityActySelectEmpresaBinding
import com.unosoft.ecomercialapp.db.EntityEmpresa
import com.unosoft.ecomercialapp.entity.Empresa.dcEmpresa
import com.unosoft.ecomercialapp.ui.LoginPasscode.ActyLoginPasscode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActySelectEmpresa : AppCompatActivity() {
    private lateinit var binding: ActivityActySelectEmpresaBinding

    private lateinit var adapterEmpresa: AdtEmpresa
    private val listaEmpresa = ArrayList<dcEmpresa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActySelectEmpresaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        INICIARDATAPRUEBA()
        iniciarData()
    }

    private fun INICIARDATAPRUEBA() {
        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().insertEmpresa(EntityEmpresa(0,"Arteus SAC","123456789","Jhon"))
            database.daoTblBasica().insertEmpresa(EntityEmpresa(0,"Arteus COM","123456788","Jhon"))
        }
    }


    private fun iniciarData() {
        CoroutineScope(Dispatchers.IO).launch {

            if (database.daoTblBasica().isExistsEntityEmpresa()){
                database.daoTblBasica().getAllEmpresa().forEach {
                    listaEmpresa.add(dcEmpresa(it.nameEmpresa, it.ruc, it.usuario))
                }

                runOnUiThread {
                    iniciarEmpresa()
                    adapterEmpresa.notifyDataSetChanged()
                }

            }else{
                runOnUiThread {
                    val i = Intent(applicationContext, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }
    }

    private fun iniciarEmpresa() {
        binding.rvEmpresa.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapterEmpresa = AdtEmpresa(listaEmpresa) { data -> onItemDatosProductList(data) }
        binding.rvEmpresa.adapter = adapterEmpresa
    }

    private fun onItemDatosProductList(data: dcEmpresa) {
        val i = Intent(applicationContext, ActyLoginPasscode::class.java)
        startActivity(i)
    }

}