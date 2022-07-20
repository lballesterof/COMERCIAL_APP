package com.unosoft.ecomercialapp.ui.cotizacion

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unosoft.ecomercialapp.Adapter.Cotizaciones.listcotizacionesadapter
import com.unosoft.ecomercialapp.Adapter.ProductListCot.productlistcotadarte
import com.unosoft.ecomercialapp.Adapter.ProductoComercial.productocomercialadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.db.EntityListProctCot
import com.unosoft.ecomercialapp.db.cotizacion.EntityEditQuotationDetail
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityDetalleCotizacion : AppCompatActivity() {

    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var productlistcotadarte: productlistcotadarte

    private val listaProductoCotizacion = ArrayList<productocomercial>()
    private val listaProductoListados = ArrayList<productlistcot>()

    var apiInterface2: ProductoComercial? = null

    var montoTotal:Double = 0.0
    var igvTotal:Double = 0.0
    var subtotal:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cotizacion)
        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)

        getData()
        productosListado()
    }

    fun productosListado() {
        val rv_listproductcot = findViewById<RecyclerView>(R.id.rv_listproductcot)
        rv_listproductcot?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productlistcotadarte = productlistcotadarte(listaProductoListados) { data -> onItemDatosProductList(data) }
        rv_listproductcot?.adapter = productlistcotadarte
    }

    private fun onItemDatosProductList(data: productlistcot) {

    }

    fun getData() {

        CoroutineScope(Dispatchers.IO).launch {

            println("Estado de valor "+database.daoTblBasica().isExistsEntityListEditQuotation())
            println("Datos de detalle "+database.daoTblBasica().getAllQuotationDetail())


            if (database.daoTblBasica().isExistsEntityListEditQuotation()) {
                    database.daoTblBasica().getAllQuotationDetail().forEach {
                        listaProductoListados.add(
                            productlistcot(
                            it.iD_PRODUCTO,it.codigo,it.codigO_BARRA,it.nombre,it.noM_UNIDAD,it.preciO_ORIGINAL,0.0,it.unidad,"","",it.cantidad,it.preciO_ORIGINAL,it.preciO_ORIGINAL
                            )
                        )
                    }
                }

                productlistcotadarte.notifyDataSetChanged()
                calcularMontoTotal()
            }

        }



    //************* FUNCIONES ADICIONALES  ****************

    fun calcularMontoTotal(){
        montoTotal = listaProductoListados.sumOf { it.precioTotal }
        igvTotal = montoTotal*0.18
        subtotal = montoTotal - igvTotal

        val tv_subtotalCot = findViewById<TextView>(R.id.tv_subtotalCotDetailQuotation)
        val tv_igvCot = findViewById<TextView>(R.id.tv_igvCotDetailQuotation)
        val tv_totalCot = findViewById<TextView>(R.id.tv_totalCotDetailQuotation)

        tv_totalCot.text = utils().pricetostringformat(montoTotal)
        tv_igvCot.text = utils().pricetostringformat(igvTotal)
        tv_subtotalCot.text = utils().pricetostringformat(subtotal)
    }


    private fun calculatepricebyqty(Qty: Int, Price: Double): Double {
        return (Price * Qty.toDouble())
    }
    private fun pricetostringformat(valuenumeric: Double): String {
        return String.format("%,.2f", valuenumeric)
    }

}