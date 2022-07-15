package com.unosoft.ecomercialapp.ui.cotizacion

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.unosoft.ecomercialapp.Adapter.Cotizaciones.listcotizacionesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.PedidoMaster
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityEditCotizacion : AppCompatActivity() {

    private lateinit var adapterCotizaciones: listcotizacionesadapter
    private val listacotizaciones = ArrayList<cotizacionesDto>()
    private val listaTipoMoneda = ArrayList<MonedaResponse>()
    var apiInterface: PedidoMaster? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cotizacion)
        apiInterface = APIClient.client?.create(PedidoMaster::class.java) as PedidoMaster
        iniciarData()
    }

    private fun iniciarData() {

        val datos = intent.getSerializableExtra("DATOSCOTIZACION") as cotizacionesDto

        val tv_idCotizacion = findViewById<TextView>(R.id.tv_idCotizacion)
        val tv_nameClientCot = findViewById<TextView>(R.id.tv_nameClientCot)
        val tv_rucCot = findViewById<TextView>(R.id.tv_rucCot)
        val tv_tipoMonedaCot = findViewById<TextView>(R.id.tv_tipoMonedaCot)
        val tv_CondPagoCot = findViewById<TextView>(R.id.tv_CondPagoCot)
        val tv_subtotalCot = findViewById<TextView>(R.id.tv_subtotalCot)
        val tv_valorventaCot = findViewById<TextView>(R.id.tv_valorventaCot)
        val tv_igvCot = findViewById<TextView>(R.id.tv_igvCot)
        val tv_importe = findViewById<TextView>(R.id.tv_importe)

        val iv_person_Cot = findViewById<ImageView>(R.id.iv_person_Cot)
        val iv_productosCot = findViewById<ImageView>(R.id.iv_productosCot)

        tv_idCotizacion?.text = "NUMERO: ${datos.id_cotizacion}"
        //tv_fechaCreacionCot?.text = "Fecha Creacion: ${LocalDateTime.now()}"
        tv_nameClientCot?.text = "Nombre Cliente: ${datos.persona}"
        tv_rucCot?.text = "RUC: ${datos.ruc}"
        tv_tipoMonedaCot?.text = "Moneda ${datos.mon}"
        //database.daoTblBasica().findnamecategoriapagowithnumero(datos.pa)
        tv_CondPagoCot?.text = "Consicion Pago: ------ "
        tv_subtotalCot?.text = "S./ ${datos.importe_total - datos.importe_igv}"
        tv_valorventaCot?.text = "S./ ${datos.importe_total - datos.importe_igv}"
        tv_igvCot?.text = "S./ ${datos.importe_igv}"
        tv_importe?.text = "S./ ${datos.importe_total}"

        iv_person_Cot?.setOnClickListener {

            //******************** EXTRAR DATA BASE ********************//
            val listaspinner = ArrayList<String>()
            CoroutineScope(Dispatchers.IO).launch {
                val size = database.daoTblBasica().getSizeMoneda()
                listaTipoMoneda.clear()
                for (i in 1..size) {
                    listaTipoMoneda.add(database.daoTblBasica().getTipoMoneda(i))
                }
            }

            val dialogue = Dialog(this)
            dialogue.setContentView(R.layout.dialogue_editarinformacion)
            dialogue.show()


            //**************** SPINNER *****************
            listaTipoMoneda.forEach { listaspinner.add(it.Nombre) }

            val sp_filtroMoneda = dialogue.findViewById<Spinner>(R.id.sp_filtroMoneda)
            val Adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaspinner)
            sp_filtroMoneda?.adapter = Adaptador
            sp_filtroMoneda.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSelect = listaspinner[position]
                    Toast.makeText(
                        this@ActivityEditCotizacion,
                        "Lista $itemSelect",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            //*******************************************
            dialogue.show()

        }
        iv_productosCot?.setOnClickListener {
            val intent = Intent(this, ActivityDetalleCotizacion::class.java)

            /*
            //ENVIAR DATOS
            val bundle = Bundle()
            bundle.putSerializable("DATOSCOTIZACION", dataclassCotizacion)
            intent.putExtras(bundle)*/

            startActivity(intent)

        }
    }

    private fun iniciarDataProductos() {


    }

    private fun getData(IDPEDIDO: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface!!.getbyIdPedidoCab("$IDPEDIDO")

            if (response.isSuccessful) {

            }
        }
    }

}