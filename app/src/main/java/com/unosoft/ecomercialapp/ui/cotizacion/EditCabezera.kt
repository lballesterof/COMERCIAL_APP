package com.unosoft.ecomercialapp.ui.cotizacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.unosoft.ecomercialapp.databinding.ActivityEditCabezeraBinding
import com.unosoft.ecomercialapp.databinding.ActivityEditCotizacionBinding
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.TableBasic.CondicionPagoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditCabezera : AppCompatActivity() {

    private lateinit var binding: ActivityEditCabezeraBinding

    private val listaTipoMoneda = ArrayList<MonedaResponse>()


    private val listaClient = ArrayList<ClientListResponse>()
    private lateinit var adapterCliente : listclientesadapter

    var apiInterface2: ClientApi? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCabezeraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        //**************************************************************

        eventsHanlder()
    }


    //********** EVENTO DE CLICK ************
    fun eventsHanlder() {
        binding.ivDatosClientCabezera.setOnClickListener { listarClientes() }
        iniciarSpinner()
    }

    private fun iniciarSpinner() {
        val listspMoneda = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {

            DATAGLOBAL.database.daoTblBasica().getAllMoneda().forEach {
                listaTipoMoneda.add(
                    MonedaResponse(
                        it.Nombre,it.Numero,it.Referencia1
                    )
                )
            }

        }

        val sp_filtroMoneda = binding.spMonedaCabezera

        val AdaptadorMoneda = ArrayAdapter(this, android.R.layout.simple_spinner_item, listspMoneda)
        sp_filtroMoneda.adapter = AdaptadorMoneda
        sp_filtroMoneda.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSelectMoneda = listspMoneda[position]

                Toast.makeText(this@EditCabezera,"Lista $itemSelectMoneda", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }


    //************ CLIENTE ******************
    private fun listarClientes() {
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

            binding.tvCliente.text = "Nombre Cliente ${data.nombre}"
            binding.tvRuc.text = "RUC: ${data.ruc}"

            dialog.hide()
        }

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

}


