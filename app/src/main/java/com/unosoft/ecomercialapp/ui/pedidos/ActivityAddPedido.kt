package com.unosoft.ecomercialapp.ui.pedidos

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.apppedido.Prefs
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.api.PedidoApi
import com.unosoft.ecomercialapp.databinding.ActivityAddPedidoBinding
import com.unosoft.ecomercialapp.entity.Pedidos.Detalle
import com.unosoft.ecomercialapp.entity.Pedidos.EnviarPedido
import com.unosoft.ecomercialapp.helpers.utils
import com.unosoft.ecomercialapp.ui.cotizacion.ActivityCardQuotation
import com.unosoft.ecomercialapp.ui.cotizacion.EditCabezera
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class ActivityAddPedido : AppCompatActivity() {
    private lateinit var binding: ActivityAddPedidoBinding

    var apiInterface: PedidoApi? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //*********************************************************
        apiInterface = APIClient.client?.create(PedidoApi::class.java)

        inicialDatos()
        eventsHandlers()
    }

    private fun eventsHandlers() {
        binding.ivDatosClientAddPedido.setOnClickListener { editDateClient() }
        binding.ivProductoAddPedido.setOnClickListener { addressCartQuotation() }
        binding.icObsAddPedido.setOnClickListener { observacion() }


        //** CONSULTAR **
        val btn_savePedido = findViewById<Button>(R.id.btn_savePedido)
        btn_savePedido.setOnClickListener {
            println("Pedido")
            enviarPedido() }
    }
    private fun inicialDatos() {

        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        binding.tvNumAddPedido.text = "Numero: "
        binding.tvFechaCreacionAddPedido.text = "Fecha y hora: ${formatter.format(date)}"
        binding.tvClienteADdPedido.text = "Nombre Cliente "
        binding.tvRucAddPedido.text = "RUC: "
        binding.tvMonedaAddPedido.text = "Moneda: "
        binding.tvCondicionPagoAddPedido.text = "Condición de Pago: "

        //**********  CORREGIR ************

        CoroutineScope(Dispatchers.IO).launch {
            println("***********  VALOR  *************")
            println(DATAGLOBAL.database.daoTblBasica().isExistsEntityProductList())


            if(DATAGLOBAL.database.daoTblBasica().isExistsEntityProductList()){

                DATAGLOBAL.database.daoTblBasica().getAllListProct().forEach {

                    runOnUiThread{

                        binding.tvSubTotalAddPedido.text = utils().pricetostringformat(it.montoSubTotal)
                        binding.tvValorVentaAddPedido.text = utils().pricetostringformat(it.montoSubTotal)
                        binding.tvValorIGVAddPedido.text = utils().pricetostringformat(it.montoTotalIGV)
                        binding.tvImporteTotalAddPedido.text = utils().pricetostringformat(it.montoTotal)

                    }
                }
            }

            if(DATAGLOBAL.database.daoTblBasica().isExistsEntityDataCabezera()){

                val nombreCliente = DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0].nombreCliente
                val rucCliente = DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0].rucCliente
                val tipoMoneda = DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0].tipoMoneda
                val condicionPago = DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0].condicionPago

                    runOnUiThread{
                        binding.tvFechaCreacionAddPedido.text = "Fecha y hora: ${formatter.format(date)}"
                        binding.tvNumAddPedido.text = "Numero: "
                        binding.tvClienteADdPedido.text = "Nombre Cliente ${nombreCliente}"
                        binding.tvRucAddPedido.text = "RUC: ${rucCliente}"
                        binding.tvMonedaAddPedido.text = "Moneda: ${tipoMoneda}"
                        binding.tvCondicionPagoAddPedido.text = "Condición de Pago ${condicionPago}"
                    }

            }

        }

    }
    private fun enviarPedido() {

        val listaPedido = ArrayList<Detalle>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosLista = database.daoTblBasica().getAllListProct()

            val idCliente = database.daoTblBasica().getAllDataLogin()[0].iD_CLIENTE.toString()

            runOnUiThread {
                datosLista.forEach {
                    listaPedido.add(
                        Detalle(
                        0,
                        it.id_Producto,
                        it.cantidad,
                        it.nombre,
                        it.precioUnidad,
                        0,
                        it.precioUnidad*0.18,
                        it.precioUnidad*it.cantidad,
                        0,
                        0,
                        "",
                        it.id,
                        0.0,
                        "1",
                        0,
                        "1",
                        0,
                        0,
                        "",
                        "0",
                        "",
                        "0",
                        it.unidad,
                        "2022-07-19T23:38:40.713Z",
                        "$idCliente",
                        "0001", //******
                        "",
                        0,
                        0,
                        0,
                        "",
                        "2022-07-19T23:38:40.713Z",
                        1,
                        "",
                        "S",
                        "",
                        0,
                        "N",
                        "0",
                        "N",
                        "",
                        0,
                        0,
                        0,
                        "",
                        "2022-07-19T23:38:40.713Z",
                        0,
                        0,
                        0
                        )
                    )
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val datosCabezera = database.daoTblBasica().getAllDataCabezera()
            val datoslogin = database.daoTblBasica().getAllDataLogin()[0]

            var datosPedido: EnviarPedido
            datosCabezera.forEach {
                datosPedido = EnviarPedido(
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    it.codCondicionPago!!,
                    it.codMoneda!!,
                    "2022-07-19T23:38:40.713Z",
                    "",
                    database.daoTblBasica().getAllListProct()[0].montoSubTotal,
                    database.daoTblBasica().getAllListProct()[0].montoTotalIGV,
                    0,
                    database.daoTblBasica().getAllListProct()[0].montoTotal,
                    0,
                    0,
                    "",
                    "",
                    "0001",
                    it.idCliente!!.toInt(),
                    0,
                    datoslogin.usuariocreacion,
                    datoslogin.usuarioautoriza,
                    "2022-07-19T23:38:40.713Z",
                    "2022-07-19T23:38:40.713Z",
                    datoslogin.codigO_EMPRESA,
                    datoslogin.sucursal,
                    0,
                    0,
                    "",
                    "2022-07-19T23:38:40.713Z",
                    "",
                    "",
                    "",
                    "",
                    0,
                    0,
                    "",
                    datoslogin.redondeo,
                    "",
                    "",
                    "",
                    "",
                    prefs.getTipoCambio().toDouble(),
                    datoslogin.sucursal,
                    "",
                    "",
                    listaPedido
                )

                runOnUiThread {

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = apiInterface!!.postCreatePedido(datosPedido)
                        runOnUiThread {
                            if(response.isSuccessful){
                                println("**********************************")
                                println("********      EXITO        *******")
                                println("**********************************")
                                Toast.makeText(this@ActivityAddPedido, "Exito", Toast.LENGTH_SHORT).show()
                            }else{
                                println("**********************************")
                                println("********      ERROR        *******")
                                println("**********************************")
                                Toast.makeText(this@ActivityAddPedido, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


                }
            }
        }


    }
    private fun addressCartQuotation() {
        val intent = Intent(this, ActivityCartPedido::class.java)
        startActivity(intent)
    }
    private fun editDateClient() {
        val intent = Intent(this, EditCabezeraPedido::class.java)
        startActivity(intent)
    }
    private fun observacion() {
        //***********  Alerta de Dialogo  ***********
        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.dialogue_observacion)
        dialogue.window!!.setGravity(Gravity.TOP)
        dialogue.show()

        //***********Declara elementos *****************
        var et_detalle = dialogue.findViewById<EditText>(R.id.et_detalle)
        val bt_guardarDetalle = dialogue.findViewById<Button>(R.id.bt_guardarDetalle)

        //*********** BOTON GUARDAR DEL DIALOGO ********
        bt_guardarDetalle.setOnClickListener {
            var detalle:String = et_detalle.text.toString()
            binding.tvObsAddPedido.text = detalle
            dialogue.hide()
        }
    }
}