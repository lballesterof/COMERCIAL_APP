package com.unosoft.ecomercialapp.ui.Cotizacion

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Clientes.listclientesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.databinding.ActivityEditCabezeraBinding
import com.unosoft.ecomercialapp.db.EntityDataCabezera
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.DatosCabezeraCotizacion.datosCabezera
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditCabezera : AppCompatActivity() {
    private lateinit var binding: ActivityEditCabezeraBinding
    //********************************************************************

    private val listaTipoMoneda = ArrayList<MonedaResponse>()
    private val listaClient = ArrayList<ClientListResponse>()

    private var DatosCabezeraCotizacion : datosCabezera = datosCabezera()

    private lateinit var adapterCliente : listclientesadapter
    var apiInterface2: ClientApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCabezeraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //******************************************************************
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        eventsHanlder()
    }


    //********** EVENTO DE CLICK ************
    fun eventsHanlder() {

        binding.ivDatosClientCabezera.setOnClickListener { listarClientes() }

        iniciarSpinnerMoneda()
        iniciarSpinnerListPrecio()
        iniciarSpinnerValidez()
        iniciarSpinnerCondicionPago()
        iniciarSpinnerVendedor()

        val btn_guardarCabeceraCot = findViewById<Button>(R.id.btn_guardarCabeceraCot)
        btn_guardarCabeceraCot.setOnClickListener { guardarInfo() }
    }
    private fun guardarInfo() {

        CoroutineScope(Dispatchers.IO).launch{

            DATAGLOBAL.database.daoTblBasica().deleteTableDataCabezera()
            DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDataCabezera()

            DATAGLOBAL.database.daoTblBasica().insertDataCabezera(
                EntityDataCabezera(0,
                    DatosCabezeraCotizacion.idCliente,
                    DatosCabezeraCotizacion.nombreCliente,
                    DatosCabezeraCotizacion.rucCliente,
                    DatosCabezeraCotizacion.tipoMoneda,
                    DatosCabezeraCotizacion.codMoneda,
                    DatosCabezeraCotizacion.listPrecio,
                    DatosCabezeraCotizacion.codListPrecio,
                    DatosCabezeraCotizacion.validesDias,
                    DatosCabezeraCotizacion.codValidesDias,
                    DatosCabezeraCotizacion.condicionPago,
                    DatosCabezeraCotizacion.codCondicionPago,
                    DatosCabezeraCotizacion.vendedor,
                    DatosCabezeraCotizacion.codVendedor
                )
            )

            println("***********   DATA CABEZERA   ***************")
            println(DATAGLOBAL.database.daoTblBasica().getAllDataCabezera())

            runOnUiThread{
                val intent = Intent(this@EditCabezera, ActivityAddCotizacion::class.java)
                startActivity(intent)
                finish()
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
                val AdaptadorVendedor = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listVendedor)
                spVendedorasignadoCabezera.adapter = AdaptadorVendedor
                spVendedorasignadoCabezera.setSelection(-1)
                spVendedorasignadoCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraCotizacion.vendedor = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosVendedor.forEach { if (it.Nombre == item){DatosCabezeraCotizacion.codVendedor = it.Codigo} }
                        }
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
                val AdaptadorCondicionPago = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listCondicionPago)
                sp_ListCondicionPago.adapter = AdaptadorCondicionPago
                sp_ListCondicionPago.setSelection(-1)
                sp_ListCondicionPago.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraCotizacion.condicionPago = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosCondicionPago.forEach { if (it.Nombre == item){DatosCabezeraCotizacion.codCondicionPago = it.Numero} }
                        }
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
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listFrecuenciaDia)
                sp_ListFrecuenciaDia.adapter = AdaptadorListPrecio
                sp_ListFrecuenciaDia.setSelection(-1)
                sp_ListFrecuenciaDia.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        CoroutineScope(Dispatchers.IO).launch {
                            datosFrecuenciaDia.forEach {if ("Dias habiles ${it.Nombre}" == item){
                                DatosCabezeraCotizacion.codValidesDias = it.Codigo
                                DatosCabezeraCotizacion.validesDias = it.Nombre
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
                datosListaPrecio.forEach {listspListPrecio.add(it.nombre)}
                val sp_ListPrecioCabezera = binding.spListPrecioCabezera
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listspListPrecio)
                sp_ListPrecioCabezera.adapter = AdaptadorListPrecio
                sp_ListPrecioCabezera.setSelection(-1)
                sp_ListPrecioCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraCotizacion.listPrecio = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosListaPrecio.forEach { if (it.nombre == item){DatosCabezeraCotizacion.codListPrecio = it.codigo} }
                        }
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
                val AdaptadorMoneda = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listspMoneda)
                sp_filtroMonedaCabezera.adapter = AdaptadorMoneda
                sp_filtroMonedaCabezera.setSelection(-1)
                sp_filtroMonedaCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraCotizacion.tipoMoneda = item
                        listaTipoMoneda.forEach { if(it.Nombre == item) {DatosCabezeraCotizacion.codMoneda = it.Numero} }
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
        val builder = AlertDialog.Builder(this@EditCabezera)
        val vista = layoutInflater.inflate(R.layout.dialogue_cliente, null)
        vista.setBackgroundResource(R.color.transparent)

        //pasar vista al buielder
        builder.setView(vista)
        //*********************************************
        val sv_buscadorCliente = vista.findViewById<SearchView>(R.id.sv_buscadorCliente)

        //Creamos dialogue
        val dialog = builder.create()

        dialog.window!!.setGravity(Gravity.TOP)
        //*********************************************


        fun onItemDatosClientes(data: ClientListResponse) {
            binding.tvCliente.text = "Nombre: ${data.nombre}"
            binding.tvRuc.text = "RUC: ${data.ruc}"

            DatosCabezeraCotizacion.nombreCliente = data.nombre
            DatosCabezeraCotizacion.rucCliente = data.ruc
            DatosCabezeraCotizacion.idCliente = data.idpersona

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
            runOnUiThread{
                if(response.isSuccessful){
                    listaClient.clear()
                    listaClient.addAll(response.body()!!)
                    adapterCliente.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@EditCabezera, "Error", Toast.LENGTH_SHORT).show()
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
        val intent = Intent(this@EditCabezera, ActivityAddCotizacion::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}


