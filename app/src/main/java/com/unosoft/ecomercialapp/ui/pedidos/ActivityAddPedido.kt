package com.unosoft.ecomercialapp.ui.pedidos

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.api.PedidoApi
import com.unosoft.ecomercialapp.databinding.ActivityAddPedidoBinding
import com.unosoft.ecomercialapp.databinding.ActivityDetallePedidoBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.DetCotizacion
import com.unosoft.ecomercialapp.entity.Cotizacion.EnviarCotizacion
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
                DATAGLOBAL.database.daoTblBasica().getAllDataCabezera().forEach {
                    withContext(Dispatchers.IO){
                        binding.tvFechaCreacionAddPedido.text = "Fecha y hora: ${formatter.format(date)}"
                        binding.tvNumAddPedido.text = "Numero: "
                        binding.tvClienteADdPedido.text = "Nombre Cliente ${it.nombreCliente}"
                        binding.tvRucAddPedido.text = "RUC: ${it.rucCliente}"
                        binding.tvMonedaAddPedido.text = "Moneda: ${it.tipoMoneda}"
                        binding.tvCondicionPagoAddPedido.text = "Condición de Pago ${it.condicionPago}"
                    }
                }
            }

        }

    }

    private fun eventsHandlers() {
        binding.ivDatosClientAddPedido.setOnClickListener { editDateClient() }
        binding.ivProductoAddPedido.setOnClickListener { addressCartQuotation() }
        binding.icObsAddPedido.setOnClickListener { observacion() }

        //** CONSULTAR **
        val btn_savePedido = findViewById<Button>(R.id.btn_savePedido)
        btn_savePedido.setOnClickListener { enviarPedido() }
    }

    private fun enviarPedido() {

        val listaPedido = ArrayList<Detalle>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosLista = DATAGLOBAL.database.daoTblBasica().getAllListProct()
            runOnUiThread {
                datosLista.forEach {
                    listaPedido.add(
                        Detalle(
                        0,
                        it.id_Producto,
                        it.cantidad,
                        "",
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        "1",
                        1,
                        0,
                        "",
                        0,
                        "",
                        0,
                        0,
                        it.cdg_Unidad,
                        "",
                        "",
                        "",
                        it.cdg_Unidad,
                        "2022-07-19T23:38:40.713Z",
                        "",
                        "S",
                        "",
                        0,
                        1,
                        0,
                        "",
                        "",
                        0,
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        0,
                        0,
                        0,
                        "",
                        "",
                        0,
                        0,
                        0

                    )
                    )
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val datosCabezera = DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()
            val datoslogin = DATAGLOBAL.database.daoTblBasica().getAllDataLogin()[0]

            var datosPedido: EnviarPedido
            datosCabezera.forEach {
                datosPedido = EnviarPedido(
                    datoslogin.iD_COTIZACION.toInt(),
                    "",
                    datoslogin.cdG_VENDEDOR,
                    it.codVendedor!!,
                    datoslogin.cdgpago,
                    it.codMoneda!!,
                    "2022-07-19T23:38:40.713Z",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    0,
                    0,
                    0,
                    datoslogin.iD_CLIENTE,
                    datoslogin.iD_CLIENTE,
                    "",
                    "",
                    "",
                    0,
                    0,
                    "",
                    "2022-07-19T23:38:40.713Z",
                    datoslogin.codigO_EMPRESA,
                    datoslogin.sucursal,
                    "",
                    "",
                    0,
                    0,
                    "2022-07-19T23:38:40.713Z",
                    "",
                    "",
                    datoslogin.redondeo,
                    datoslogin.validez,
                    "",
                    0,
                    0,
                    "",
                    "",
                    "",
                    datoslogin.sucursal,
                    "",
                    "",
                    0,
                    "",
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
        dialogue.show()

        //***********Declara elementos *****************
        var et_detalle = dialogue.findViewById<EditText>(R.id.et_detalle)
        val bt_guardarDetalle = dialogue.findViewById<Button>(R.id.bt_guardarDetalle)
        val tv_AlerObservacion = dialogue.findViewById<TextView>(R.id.tv_AlerObservacion)

        //*********** BOTON GUARDAR DEL DIALOGO ********
        bt_guardarDetalle.setOnClickListener {
            var detalle:String = et_detalle.text.toString()
            dialogue.hide()
        }
    }
}