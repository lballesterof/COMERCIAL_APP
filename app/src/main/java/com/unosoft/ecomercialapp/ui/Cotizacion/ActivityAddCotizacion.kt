package com.unosoft.ecomercialapp.ui.Cotizacion

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.api.PDFApi
import com.unosoft.ecomercialapp.databinding.ActivityAddCotizacionBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.DetCotizacion
import com.unosoft.ecomercialapp.entity.Cotizacion.EnviarCotizacion
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ActivityAddCotizacion : AppCompatActivity() {

    private lateinit var binding: ActivityAddCotizacionBinding

    var apiInterface3: PDFApi? = null
    var apiInterface2: ClientApi? = null
    var apiInterface: ApiCotizacion? = null

    var tipomoneda = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //****************************************************************
        apiInterface3 = APIClient.client?.create(PDFApi::class.java)
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        apiInterface = APIClient.client?.create(ApiCotizacion::class.java)

        eventsHandlers()
        inicialDatos()
    }
    private fun eventsHandlers() {
        binding.ivDatosClientAddCot.setOnClickListener { editDateClient() }
        binding.ivProductoAddCot.setOnClickListener { addressCartQuotation() }
        binding.icObservacion.setOnClickListener { observacion() }

        //****  CONSULTAR  ****
        val btnsaveCotizacion = findViewById<Button>(R.id.btn_saveCotizacion)
        btnsaveCotizacion.setOnClickListener {
            println("Cotizacion")
            enviarCotizacion()
        }

        val btn_cancelCotizacion = findViewById<Button>(R.id.btn_cancelCotizacion)
        btn_cancelCotizacion.setOnClickListener {
            this.onBackPressed()
            finish()
        }
    }
    private fun inicialDatos() {

        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        binding.tvFecOrden.text = "Fecha y hora:"
        binding.tvCodCotizacion.text = "Numero: "
        binding.tvCliente.text = "Nombre Cliente "
        binding.tvRuc.text = "RUC: "
        binding.tvMoneda.text = "Moneda: "
        binding.tvCondicionPago.text = "Condición de Pago"

        CoroutineScope(Dispatchers.IO).launch {

            if(database.daoTblBasica().isExistsEntityDataCabezera()){

                val nombreCliente = database.daoTblBasica().getAllDataCabezera()[0].nombreCliente
                val rucCliente = database.daoTblBasica().getAllDataCabezera()[0].rucCliente
                val tipoMoneda = database.daoTblBasica().getAllDataCabezera()[0].tipoMoneda
                val condicionPago = database.daoTblBasica().getAllDataCabezera()[0].condicionPago

                tipomoneda = tipoMoneda!!

                    runOnUiThread {

                        binding.tvFecOrden.text = "Fecha y hora: ${formatter.format(date)}"
                        binding.tvCodCotizacion.text = "Numero: "
                        binding.tvCliente.text = "Nombre Cliente ${nombreCliente}"
                        binding.tvRuc.text = "RUC: ${rucCliente}"
                        binding.tvMoneda.text = "Moneda: ${tipoMoneda}"
                        binding.tvCondicionPago.text = "Condición de Pago ${condicionPago}"

                        binding.tvSubTotalAddCotizacion.text = "${tipoMoneda} 0.0"
                        binding.tvValorVentaAddCotizacion.text = "${tipoMoneda} 0.0"
                        binding.tvValorIGVAddCotizacion.text = "${tipoMoneda} 0.0"
                        binding.tvImporteTotal.text = "${tipoMoneda} 0.0"
                    }

            }

            if(database.daoTblBasica().isExistsEntityProductList()){

                val montoSubTotal = database.daoTblBasica().getAllListProct()[0].montoSubTotal
                val montoTotalIGV = database.daoTblBasica().getAllListProct()[0].montoTotalIGV
                val montoTotal = database.daoTblBasica().getAllListProct()[0].montoTotal
                val tipoMoneda = database.daoTblBasica().getAllDataCabezera()[0].tipoMoneda


                runOnUiThread {

                    binding.tvSubTotalAddCotizacion.text = "${tipoMoneda} ${utils().pricetostringformat(montoSubTotal)}"
                    binding.tvValorVentaAddCotizacion.text = "${tipoMoneda} ${utils().pricetostringformat(montoSubTotal)}"
                    binding.tvValorIGVAddCotizacion.text = "${tipoMoneda} ${utils().pricetostringformat(montoTotalIGV)}"
                    binding.tvImporteTotal.text = "${tipoMoneda} ${utils().pricetostringformat(montoTotal)}"

                }
            }
        }
    }
    private fun editDateClient() {
        val intent = Intent(this, EditCabezera::class.java)
        startActivity(intent)
        finish()
    }
    private fun addressCartQuotation() {

        CoroutineScope(Dispatchers.IO).launch {

            val valor = database.daoTblBasica().isExistsEntityDataCabezera()
            println("******** VALORES ********")
            println("$valor")

            runOnUiThread {
               if (valor){
                    val intent = Intent(this@ActivityAddCotizacion, ActivityCardQuotation::class.java)
                    intent.putExtra("TIPOMONEDA",tipomoneda)
                    startActivity(intent)
                    finish()

               }else{
                    Toast.makeText(this@ActivityAddCotizacion, "Rellenar datos clientes", Toast.LENGTH_SHORT).show()
               }
            }
        }
    }
    private fun enviarCotizacion() {
        CoroutineScope(Dispatchers.IO).launch {
            var validarCabezera = database.daoTblBasica().isExistsEntityDataCabezera()
            var validarListProct = database.daoTblBasica().isExistsEntityListProct()
            val listaCotizado = ArrayList<DetCotizacion>()
            runOnUiThread{
                if(validarCabezera){
                    if (validarListProct){
                        CoroutineScope(Dispatchers.IO).launch {
                            val datosLista = database.daoTblBasica().getAllListProct()
                            val datosCabezera = database.daoTblBasica().getAllDataCabezera()[0]
                            val datoslogin = database.daoTblBasica().getAllDataLogin()[0]
                            var secuencia = 0
                            datosLista.forEach {
                                secuencia += 1
                                listaCotizado.add(DetCotizacion(
                                    0,
                                    it.id_Producto,
                                    it.cantidad,
                                    it.precioUnidad,
                                    0,
                                    it.precioUnidad*it.cantidad*1.18,
                                    it.precioUnidad*it.cantidad,
                                    "",
                                    secuencia,
                                    it.precio_Venta,
                                    "1",
                                    1,
                                    "1",
                                    0,
                                    0,
                                    "",
                                    "",
                                    "",
                                    it.cdg_Unidad,
                                    0,
                                    0,
                                    it.precioUnidad,
                                    it.cdg_Unidad,
                                    "${LocalDateTime.now()}",
                                    "",
                                    "S",
                                    0,
                                    0,
                                    1,
                                    0,
                                    0,
                                    0,
                                    0
                                ))
                            }
                            val datosCotizacion = EnviarCotizacion(
                                datoslogin.iD_COTIZACION.toInt(),
                                "",
                                prefs.getCdgVendedor(),
                                datosCabezera.codVendedor!!,
                                datoslogin.cdgpago,
                                datosCabezera.codMoneda!!,
                                "${LocalDateTime.now()}",
                                "",
                                datosLista[0].montoSubTotal,
                                0,
                                datosLista[0].montoSubTotal,
                                datosLista[0].montoTotalIGV,
                                datosLista[0].montoTotal,
                                0,
                                datoslogin.poR_IGV.toDouble(),
                                binding.tvObsCotizacion.text.toString(),
                                "",
                                datoslogin.iD_CLIENTE,
                                datoslogin.iD_CLIENTE,
                                0,
                                "",
                                "",
                                datoslogin.usuariocreacion,
                                "${LocalDateTime.now()}",
                                "",
                                "${LocalDateTime.now()}",
                                datoslogin.codigO_EMPRESA,
                                datoslogin.sucursal,
                                "",
                                0,
                                "${LocalDateTime.now()}",
                                datoslogin.usuarioautoriza,
                                "${LocalDateTime.now()}",
                                "",
                                0,
                                datoslogin.redondeo,
                                datoslogin.validez,
                                "",
                                "",
                                "",
                                datoslogin.tipocambio,
                                0,
                                "",
                                datoslogin.sucursal,
                                "",
                                0,
                                "",
                                0,
                                0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                0,
                                "",
                                prefs.getCdgVendedor(),
                                "",
                                "",
                                datosCabezera.rucCliente!!,
                                "",
                                "",
                                listaCotizado
                            )
                            println("*******************************************************")
                            println("********     ${datosCabezera.rucCliente!!}      *******")
                            println("********************************************************")


                            val response = apiInterface!!.postCreateCotizacion(datosCotizacion)
                               runOnUiThread {
                                  if(response.isSuccessful){
                                     println("**********************************")
                                     println("********      EXITO        *******")
                                     println("**********************************")
                                     Toast.makeText(this@ActivityAddCotizacion, "Exito", Toast.LENGTH_SHORT).show()

                                      visualizarPDF(response.body()!!.iD_COTIZACION)

                                      binding.tvCodCotizacion.text = "N° Cotizacion: ${response.body()!!.iD_COTIZACION}"
                                      Toast.makeText(this@ActivityAddCotizacion, "${response.body()!!.iD_COTIZACION}", Toast.LENGTH_SHORT).show()

                                  }else{
                                     println("**********************************")
                                     println("********      ERROR        *******")
                                     println("**********************************")
                                     Toast.makeText(this@ActivityAddCotizacion, "ERROR", Toast.LENGTH_SHORT).show()
                                  }
                               }
                        }
                    }else{
                        Toast.makeText(this@ActivityAddCotizacion, "Falta ingresar productos", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ActivityAddCotizacion, "Falta datos cliente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun visualizarPDF(idCotizacion:Int) {
        val intent = Intent(this, VisorPDFCotizacion::class.java)
        //ENVIAR DATOS
        val bundle = Bundle()
        bundle.putString("ID", "$idCotizacion")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun observacion() {
        //***********  Alerta de Dialogo  ***********
        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.dialogue_observacion)
        dialogue.show()
        //***********Declara elementos *****************
        var et_detalle = dialogue.findViewById<EditText>(R.id.et_detalle)
        val bt_guardarDetalle = dialogue.findViewById<Button>(R.id.bt_guardarDetalle)
        //*********** BOTON GUARDAR DEL DIALOGO ********
        bt_guardarDetalle.setOnClickListener {
            var detalle:String = et_detalle.text.toString()
            binding.tvObsCotizacion.text = detalle
            dialogue.hide()
        }
    }
    override fun onBackPressed() {
        clearTable()
        super.onBackPressed()
    }
    private fun clearTable() {
        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().deleteTableListProct()
            database.daoTblBasica().clearPrimaryKeyListProct()

            database.daoTblBasica().deleteTableDataCabezera()
            database.daoTblBasica().clearPrimaryKeyDataCabezera()

            println(database.daoTblBasica().getAllListProct())
        }
    }

}