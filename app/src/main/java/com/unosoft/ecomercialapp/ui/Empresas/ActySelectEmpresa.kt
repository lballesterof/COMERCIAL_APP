package com.unosoft.ecomercialapp.ui.Empresas

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.unosoft.ecomercialapp.Adapter.Empresa.AdtEmpresa
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
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

        //INICIARDATAPRUEBA()
        iniciarData()
        eventsHandlers()

    }

    private fun eventsHandlers() {
        binding.btnNuevoUser.setOnClickListener { generarUser() }
        binding.btnLimpiarEmpresa.setOnClickListener { limpiarListaEmpresa() }
    }

    private fun limpiarListaEmpresa() {

        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE,)

        dialog.setTitleText("Limpiar Lista")
        dialog.setContentText("Se eliminara todos los datos de las empresas ¿Desea limpiar la lista?")

        dialog.setConfirmText("SI").setConfirmButtonBackgroundColor(Color.parseColor("#013ADF"))
        dialog.setConfirmButtonTextColor(Color.parseColor("#ffffff"))

        dialog.setCancelText("NO").setCancelButtonBackgroundColor(Color.parseColor("#c8c8c8"))

        dialog.setCancelable(false)

        dialog.setCancelClickListener { sDialog -> // Showing simple toast message to user
            sDialog.cancel()
        }

        dialog.setConfirmClickListener { sDialog ->
            CoroutineScope(Dispatchers.IO).launch {
                database.daoTblBasica().deleteTableEmpresa()
                database.daoTblBasica().clearPrimaryKeyEmpresa()
                runOnUiThread {
                    listaEmpresa.clear()
                    iniciarData()
                }
            }
            sDialog.cancel()
        }

        dialog.show()






    }

    private fun generarUser() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
    }

    private fun iniciarData() {

        CoroutineScope(Dispatchers.IO).launch {

            if (database.daoTblBasica().isExistsEntityEmpresa()){

                if (prefs.getUser().isEmpty()){
                    listaEmpresa.clear()
                    database.daoTblBasica().getAllEmpresa().forEach {
                        listaEmpresa.add(dcEmpresa(it.nameEmpresa, it.ruc,it.nameUser,it.usuario.uppercase(),it.url,it.Userkey))
                    }
                    runOnUiThread {
                        iniciarEmpresa()
                        adapterEmpresa.notifyDataSetChanged()
                    }
                }else{
                    runOnUiThread {
                        val i = Intent(applicationContext, ActyLoginPasscode::class.java)
                        startActivity(i)
                        finish()
                    }
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
        adapterEmpresa = AdtEmpresa(listaEmpresa) { data -> onItemDatosEmpresa(data) }
        binding.rvEmpresa.adapter = adapterEmpresa

        //**************** Implementacion de Swiped ********************
        val itemswipe = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listaEmpresa.removeAt(viewHolder.bindingAdapterPosition)
                if(listaEmpresa.isEmpty()){
                    iniciarData()
                }
                binding.rvEmpresa.adapter?.notifyDataSetChanged()
            }
        }
        val swap =  ItemTouchHelper(itemswipe)
        swap.attachToRecyclerView(binding.rvEmpresa)
    }

    private fun onItemDatosEmpresa(data: dcEmpresa) {

        prefs.save_User(data.usuario)
        prefs.save_URLBase(data.url)

        val i = Intent(applicationContext, ActyLoginPasscode::class.java)
        startActivity(i)
        finish()
    }





}