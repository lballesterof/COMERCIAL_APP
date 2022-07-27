package com.unosoft.ecomercialapp.ui.pedidos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Clientes.listclientesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.databinding.ActivityEditCabezeraPedidoBinding
import com.unosoft.ecomercialapp.db.EntityDataCabezera
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.DatosCabezeraCotizacion.datosCabezera
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.ui.Cotizacion.ActivityAddCotizacion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditCabezeraPedido : AppCompatActivity() {
    private lateinit var binding: ActivityEditCabezeraPedidoBinding
    //***************************************************************

    private val listaTipoMoneda = ArrayList<MonedaResponse>()
    private val listaClient = ArrayList<ClientListResponse>()

    private var DatosCabezeraPedido : datosCabezera = datosCabezera()

    private lateinit var adapterCliente : listclientesadapter
    var apiInterface2: ClientApi? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCabezeraPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //*******************************************************************
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        eventsHanlder()
    }

    private fun eventsHanlder() {
        binding.ivDatosClientCabezera.setOnClickListener { listarClientes() }
        iniciarSpinnerMoneda()
        iniciarSpinnerListPrecio()
        iniciarSpinnerValidez()
        iniciarSpinnerCondicionPago()
        iniciarSpinnerVendedor()

        val btn_guardarCabeceraCot = findViewById<Button>(R.id.btn_guardarCabeceraPedido)
        btn_guardarCabeceraCot.setOnClickListener { guardarInfo() }
    }

    private fun guardarInfo() {
        CoroutineScope(Dispatchers.IO).launch{

            DATAGLOBAL.database.daoTblBasica().deleteTableDataCabezera()
            DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDataCabezera()

            DATAGLOBAL.database.daoTblBasica().insertDataCabezera(
                EntityDataCabezera(0,
                    DatosCabezeraPedido.idCliente,
                    DatosCabezeraPedido.nombreCliente,
                    DatosCabezeraPedido.rucCliente,
                    DatosCabezeraPedido.tipoMoneda,
                    DatosCabezeraPedido.codMoneda,
                    DatosCabezeraPedido.listPrecio,
                    DatosCabezeraPedido.codListPrecio,
                    DatosCabezeraPedido.validesDias,
                    DatosCabezeraPedido.codValidesDias,
                    DatosCabezeraPedido.condicionPago,
                    DatosCabezeraPedido.codCondicionPago,
                    DatosCabezeraPedido.vendedor,
                    DatosCabezeraPedido.codVendedor
                )
            )

            println("***********   DATA CABEZERA   ***************")
            println(DATAGLOBAL.database.daoTblBasica().getAllDataCabezera())

            //************ LIMPIA DATOS LISTA PRODUCTO  *************
            DATAGLOBAL.database.daoTblBasica().deleteTableListProct()
            DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListProct()

            runOnUiThread{
                if (DatosCabezeraPedido.nombreCliente == "" ||
                    DatosCabezeraPedido.rucCliente == "" ||
                    DatosCabezeraPedido.idCliente == ""){
                    Toast.makeText(this@EditCabezeraPedido, "Falta ingresar datos cliente", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this@EditCabezeraPedido, ActivityAddPedido::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


    //****************  SPINNER  ***********************
    private fun iniciarSpinnerVendedor() {
        val listVendedor = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosVendedor = DATAGLOBAL.database.daoTblBasica().getAllVendedor()
            runOnUiThread{
                datosVendedor.forEach {
                    listVendedor.add("${it.Nombre}")
                }
                val spVendedorasignadoCabezera = binding.spVendedorasignadoCabezera
                val AdaptadorVendedor = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listVendedor)
                spVendedorasignadoCabezera.adapter = AdaptadorVendedor
                spVendedorasignadoCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraPedido.vendedor = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosVendedor.forEach { if (it.Nombre == item){DatosCabezeraPedido.codVendedor = it.Codigo} }
                        }
                        Toast.makeText(this@EditCabezeraPedido,"Lista $item", Toast.LENGTH_SHORT).show()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }
    private fun iniciarSpinnerCondicionPago() {
        val listCondicionPago = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosCondicionPago = DATAGLOBAL.database.daoTblBasica().getAllCondicionPago()
            runOnUiThread{
                datosCondicionPago.forEach {
                    listCondicionPago.add("${it.Nombre}")
                }
                val sp_ListCondicionPago = binding.spCondicionpagoCabezera
                val AdaptadorCondicionPago = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listCondicionPago)
                sp_ListCondicionPago.adapter = AdaptadorCondicionPago
                sp_ListCondicionPago.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraPedido.condicionPago = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosCondicionPago.forEach { if (it.Nombre == item){DatosCabezeraPedido.codCondicionPago = it.Numero} }
                        }
                        Toast.makeText(this@EditCabezeraPedido,"Lista $item", Toast.LENGTH_SHORT).show()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }


    }
    private fun iniciarSpinnerValidez() {
        val listFrecuenciaDia = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosFrecuenciaDia = DATAGLOBAL.database.daoTblBasica().getAllFrecuenciaDias()

            runOnUiThread{
                datosFrecuenciaDia.forEach {
                    listFrecuenciaDia.add("Dias habiles ${it.Nombre}")
                }

                val sp_ListFrecuenciaDia = binding.spValidezCabezera
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listFrecuenciaDia)
                sp_ListFrecuenciaDia.adapter = AdaptadorListPrecio
                sp_ListFrecuenciaDia.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        CoroutineScope(Dispatchers.IO).launch {
                            datosFrecuenciaDia.forEach {if ("Dias habiles ${it.Nombre}" == item){
                                DatosCabezeraPedido.codValidesDias = it.Codigo
                                DatosCabezeraPedido.validesDias = it.Nombre
                            } }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }
    private fun iniciarSpinnerListPrecio() {
        val listspListPrecio = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosListaPrecio = DATAGLOBAL.database.daoTblBasica().getAllListaPrecio()
            runOnUiThread{
                datosListaPrecio.forEach {
                    listspListPrecio.add(it.nombre)
                }
                val sp_ListPrecioCabezera = binding.spListPrecioCabezera
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listspListPrecio)
                sp_ListPrecioCabezera.adapter = AdaptadorListPrecio
                sp_ListPrecioCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraPedido.listPrecio = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosListaPrecio.forEach { if (it.nombre == item){DatosCabezeraPedido.codListPrecio = it.codigo} }
                        }
                        Toast.makeText(this@EditCabezeraPedido, "$item", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }

            }
        }



    }
    private fun iniciarSpinnerMoneda() {
        val listspMoneda = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {

            DATAGLOBAL.database.daoTblBasica().getAllMoneda().forEach {
                listaTipoMoneda.add(
                    MonedaResponse(it.Nombre,it.Numero,it.Referencia1)
                )
            }

            runOnUiThread{
                listaTipoMoneda.forEach { listspMoneda.add(it.Nombre) }

                val sp_filtroMonedaCabezera = binding.spMonedaCabezera
                val AdaptadorMoneda = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listspMoneda)
                sp_filtroMonedaCabezera.adapter = AdaptadorMoneda
                sp_filtroMonedaCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraPedido.tipoMoneda = item
                        listaTipoMoneda.forEach { if(it.Nombre == item) {DatosCabezeraPedido.codMoneda = it.Numero} }
                        Toast.makeText(this@EditCabezeraPedido, item, Toast.LENGTH_SHORT).show()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            }
        }
    }

    //************ CLIENTE ******************
    private fun listarClientes() {
        //***********  Alerta de Dialogo  ************
        val builder = AlertDialog.Builder(this@EditCabezeraPedido)
        val vista = layoutInflater.inflate(R.layout.dialogue_cliente, null)
        vista.setBackgroundResource(R.color.transparent)

        //Pasar vista al buielder
        builder.setView(vista)
        //*********************************************
        val sv_buscadorCliente = vista.findViewById<SearchView>(R.id.sv_buscadorCliente)

        val dialog = builder.create()

        dialog.window!!.setGravity(Gravity.TOP)
        //*********************************************


        fun onItemDatosClientes(data: ClientListResponse) {
            binding.tvCliente.text = "Nombre: ${data.nombre}"
            binding.tvRuc.text = "RUC: ${data.ruc}"

            DatosCabezeraPedido.nombreCliente = data.nombre
            DatosCabezeraPedido.rucCliente = data.ruc
            DatosCabezeraPedido.idCliente = data.idpersona

            dialog.hide()
            dialog.dismiss()
        }
        if (!isFinishing)
            dialog.show()

        val rv_buscarCliente = vista.findViewById<RecyclerView>(R.id.rv_buscarCliente)
        rv_buscarCliente?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
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
                    Toast.makeText(this@EditCabezeraPedido, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        sv_buscadorCliente?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
    //******** FIN DE CLIENTE ******************

    override fun onBackPressed() {
        val intent = Intent(this@EditCabezeraPedido, ActivityAddPedido::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

}