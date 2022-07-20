package com.unosoft.ecomercialapp.ui.cotizacion

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Clientes.listclientesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.databinding.ActivityAddCotizacionBinding
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.Cotizacion.DetCotizacion
import com.unosoft.ecomercialapp.entity.Cotizacion.EnviarCotizacion
import com.unosoft.ecomercialapp.entity.TableBasic.CondicionPagoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class ActivityAddCotizacion : AppCompatActivity() {

    private lateinit var binding: ActivityAddCotizacionBinding

    private val listaTipoMoneda = ArrayList<MonedaResponse>()
    private val listaCondicionPago = ArrayList<CondicionPagoResponse>()
    private val listaClient = ArrayList<ClientListResponse>()

    private lateinit var adapterCliente : listclientesadapter

    var apiInterface2: ClientApi? = null
    var apiInterface: ApiCotizacion? = null
    var nombreCliente: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //****************************************************************
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        apiInterface = APIClient.client?.create(ApiCotizacion::class.java)




        eventsHandlers()
        inicialDatos()
    }

    private fun inicialDatos() {

        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        binding.tvFecOrden.text = "Fecha y hora: ${formatter.format(date)}"
        binding.tvCodCotizacion.text = "Numero: "
        binding.tvCliente.text = "Nombre Cliente "
        binding.tvRuc.text = "RUC: "
        binding.tvMoneda.text = "Moneda: "
        binding.tvCondicionPago.text = "Condición de Pago"


        CoroutineScope(Dispatchers.IO).launch {
            println("***********  VALOR  *************")
            println(database.daoTblBasica().isExistsEntityProductListCot())


            if(database.daoTblBasica().isExistsEntityProductListCot()){

                database.daoTblBasica().getAllListProct().forEach {
                    withContext(Dispatchers.IO){

                        binding.tvSubTotalAddCotizacion.text = utils().pricetostringformat(it.montoSubTotal)
                        binding.tvValorVentaAddCotizacion.text = utils().pricetostringformat(it.montoSubTotal)
                        binding.tvValorIGVAddCotizacion.text = utils().pricetostringformat(it.montoTotalIGV)
                        binding.tvImporteTotal.text = utils().pricetostringformat(it.montoTotal)

                    }
                }
            }

            if(database.daoTblBasica().isExistsEntityDataCabezera()){
                database.daoTblBasica().getAllDataCabezera().forEach {
                    withContext(Dispatchers.IO){
                        binding.tvFecOrden.text = "Fecha y hora: ${formatter.format(date)}"
                        binding.tvCodCotizacion.text = "Numero: "
                        binding.tvCliente.text = "Nombre Cliente ${it.nombreCliente}"
                        binding.tvRuc.text = "RUC: ${it.rucCliente}"
                        binding.tvMoneda.text = "Moneda: ${it.tipoMoneda}"
                        binding.tvCondicionPago.text = "Condición de Pago ${it.condicionPago}"
                    }
                }
            }

        }

    }

    private fun editDateClient() {

        val intent = Intent(this, EditCabezera::class.java)
        startActivity(intent)



        //spinnerMoneda()
        //spinnerCondicionPago()
    }

    private fun spinnerCondicionPago() {
        //******************** EXTRAR DATA BASE ********************//
        val listspMoneda = ArrayList<String>()
        val listspCondicionPago = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {

            database.daoTblBasica().getAllCondicionPago().forEach {
                listaCondicionPago.add(
                    CondicionPagoResponse(
                        it.Codigo,it.Nombre,it.Numero,it.Referencia1
                    )
                )
            }

            database.daoTblBasica().getAllMoneda().forEach {
                listaTipoMoneda.add(
                    MonedaResponse(
                        it.Nombre,it.Numero,it.Referencia1
                    )
                )
            }

        }

        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.dialogue_editarinformacion)
        dialogue.show()


        //**************** SPINNER *****************

        if (listspCondicionPago.size==0){
            listaCondicionPago.forEach { listspCondicionPago.add(it.Nombre) }
        }
        if (listspMoneda.size==0){
            listaTipoMoneda.forEach { listspMoneda.add(it.Nombre) }
        }

        val sp_filtroMoneda = dialogue.findViewById<Spinner>(R.id.sp_filtroMoneda)
        val sp_CondicionPago = dialogue.findViewById<Spinner>(R.id.sp_filtroCondicionPago)
        val iv_filtroCliente = dialogue.findViewById<ImageView>(R.id.iv_filtroCliente)
        val tv_nameClienteDialogue = dialogue.findViewById<TextView>(R.id.tv_nameClienteDialogue)


        val AdaptadorCondicionPago = ArrayAdapter(this, android.R.layout.simple_spinner_item, listspCondicionPago)
        sp_CondicionPago?.adapter = AdaptadorCondicionPago
        sp_CondicionPago.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSelectCondicionPago = listspCondicionPago[position]

                binding.tvCondicionPago.text =  "CONDICION DE PAGO: $itemSelectCondicionPago"

                Toast.makeText(this@ActivityAddCotizacion,"Lista $itemSelectCondicionPago", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val AdaptadorMoneda = ArrayAdapter(this, android.R.layout.simple_spinner_item, listspMoneda)
        sp_filtroMoneda?.adapter = AdaptadorMoneda
        sp_filtroMoneda.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSelectMoneda = listspMoneda[position]

                binding.tvMoneda.text = "MONEDA: $itemSelectMoneda"

                Toast.makeText(this@ActivityAddCotizacion,"Lista $itemSelectMoneda", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        iv_filtroCliente.setOnClickListener {
            buscarCliente()
        }

        tv_nameClienteDialogue.text = nombreCliente


        //*******************************************
    }

    fun buscarCliente(){
        //***********  Alerta de Dialogo  ************
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.dialogue_cliente, null)
        vista.setBackgroundResource(R.color.transparent)

        builder.setView(vista)

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.TOP)
        dialog.show()
        //*********************************************


        val sv_buscadorCliente = vista.findViewById<SearchView>(R.id.sv_buscadorCliente)

        fun onItemDatosClientes(data: ClientListResponse) {

            dialog.hide()
        }

        val rv_buscarCliente = vista.findViewById<RecyclerView>(R.id.rv_buscarCliente)
        rv_buscarCliente?.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        adapterCliente = listclientesadapter(listaClient) { data -> onItemDatosClientes(data) }
        rv_buscarCliente?.adapter = adapterCliente


        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface2!!.getAllClients()
            runOnUiThread {
                if(response.isSuccessful){
                    listaClient.clear()
                    listaClient.addAll(response.body()!!)
                    adapterCliente.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@ActivityAddCotizacion, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        sv_buscadorCliente?.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                println("$newText")
                filterCliente(newText.toString())
                return false
            }
        })
    }



    fun filterCliente(text: String) {
        val filterdNamePlato: ArrayList<ClientListResponse> = ArrayList()
        for (i in listaClient.indices) {
            if (listaClient[i].nombre.lowercase().contains(text.lowercase())) {
                filterdNamePlato.add(listaClient[i])
            }
        }
        adapterCliente.filterCliente(filterdNamePlato)
    }

















    private fun addressCartQuotation() {
        val intent = Intent(this, ActivityCardQuotation::class.java)
        startActivity(intent)
    }

    private fun eventsHandlers() {
        binding.ivDatosClientAddCot.setOnClickListener { editDateClient() }
        binding.ivProductoAddCot.setOnClickListener { addressCartQuotation() }
        binding.icObservacion.setOnClickListener { observacion() }

        //** CONSULTAR **
        val btnsaveCotizacion = findViewById<Button>(R.id.btn_saveCotizacion)
        btnsaveCotizacion.setOnClickListener { enviarCotizacion() }
    }

    private fun enviarCotizacion() {

        val listaCotizado = ArrayList<DetCotizacion>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosLista = database.daoTblBasica().getAllListProct()
            runOnUiThread {
                datosLista.forEach {
                    listaCotizado.add(DetCotizacion(
                        0,
                        it.id_Producto,
                        it.cantidad,
                        it.precioUnidad,
                        0,
                        it.precioUnidad*it.cantidad*1.18,
                        it.precioUnidad*it.cantidad,
                        "",
                        0,
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
                        "2022-07-19T23:38:40.713Z",
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
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val datosCabezera = database.daoTblBasica().getAllDataCabezera()
            val datoslogin = database.daoTblBasica().getAllDataLogin()[0]



                var datosCotizacion:EnviarCotizacion
                datosCabezera.forEach {
                    datosCotizacion = EnviarCotizacion(
                        datoslogin.iD_COTIZACION.toInt(),
                        "",
                        datoslogin.cdG_VENDEDOR,
                        it.codVendedor!!,
                        datoslogin.cdgpago,
                        it.codMoneda!!,
                        "2022-07-19T23:38:40.713Z",
                        "",
                        database.daoTblBasica().getAllListProct()[0].montoSubTotal,
                        0,
                        database.daoTblBasica().getAllListProct()[0].montoSubTotal,
                        database.daoTblBasica().getAllListProct()[0].montoTotalIGV,
                        database.daoTblBasica().getAllListProct()[0].montoTotal,
                        0,
                        0,
                        "",
                        "",
                        datoslogin.iD_CLIENTE,
                        datoslogin.iD_CLIENTE,
                        0,
                        "",
                        "",
                        datoslogin.usuariocreacion,
                        "2022-07-19T23:38:40.713Z",
                        "",
                        "2022-07-19T23:38:40.713Z",
                        datoslogin.codigO_EMPRESA,
                        datoslogin.sucursal,
                        "",
                        0,
                        "2022-07-19T23:38:40.713Z",
                        datoslogin.usuarioautoriza,
                        "2022-07-19T23:38:40.713Z",
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
                        "",
                        "",
                        "",
                        listaCotizado
                    )

                    runOnUiThread {

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = apiInterface!!.postCreateCotizacion(datosCotizacion)
                        runOnUiThread {
                            if(response.isSuccessful){
                                println("**********************************")
                                println("********      EXITO        *******")
                                println("**********************************")
                                Toast.makeText(this@ActivityAddCotizacion, "Exito", Toast.LENGTH_SHORT).show()
                            }else{
                                println("**********************************")
                                println("********      ERROR        *******")
                                println("**********************************")
                                Toast.makeText(this@ActivityAddCotizacion, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


                }
            }
        }


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