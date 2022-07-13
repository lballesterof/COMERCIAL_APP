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
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityDetalleCotizacion : AppCompatActivity() {

    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var productlistcotadarte: productlistcotadarte

    private val listaProductoCotizacion = ArrayList<productocomercial>()
    private val listaProductoListados = ArrayList<productocomercial>()


    var apiInterface2: ProductoComercial? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cotizacion)
        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)


        productosListado()
        abrirListProductos()
    }

    private fun productosListado() {
        val rv_detallecotizacion = findViewById<RecyclerView>(R.id.rv_detallecotizacion)
        rv_detallecotizacion?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productlistcotadarte = productlistcotadarte(listaProductoListados) { data -> onItemDatosProductList(data) }
        rv_detallecotizacion?.adapter = productlistcotadarte
    }

    private fun onItemDatosProductList(data: productocomercial) {

    }


    private fun abrirListProductos() {
        val icon_agregarProductosCotizacion = findViewById<FloatingActionButton>(R.id.icon_agregarProductosCotizacion)
        icon_agregarProductosCotizacion.setOnClickListener {

            //***********  Alerta de Dialogo  ***********
            val builder = AlertDialog.Builder(this)
            val vista = layoutInflater.inflate(R.layout.dialogue_detalle_cotizacion_productos,null)
            vista.setBackgroundResource(R.color.transparent)

            builder.setView(vista)

            val dialog = builder.create()
            dialog.window!!.setGravity(Gravity.TOP)
            dialog.show()

            val rv_productos = vista.findViewById<RecyclerView>(R.id.rv_productos)
            val sv_consultasproductos = vista.findViewById<SearchView>(R.id.sv_consultasproductos)

            rv_productos.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            adapterProductoComercial = productocomercialadapter(listaProductoCotizacion) { data -> onItemDatosProductos(data) }
            rv_productos.adapter = adapterProductoComercial

            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getProductoComercial("LPR0000001","0001","4.00")
                runOnUiThread {
                    if(response.isSuccessful){
                        listaProductoCotizacion.clear()
                        listaProductoCotizacion.addAll(response.body()!!)
                        adapterProductoComercial.notifyDataSetChanged()
                    }
                }
            }

            sv_consultasproductos?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    println("$newText")
                    filter(newText.toString())
                    return false
                }
            })
        }
    }
    fun filter(text: String) {
        val filterdNameProducto: ArrayList<productocomercial> = ArrayList()
        for (i in listaProductoCotizacion.indices) {
            if (listaProductoCotizacion[i].nombre.lowercase().contains(text.lowercase())) {
                filterdNameProducto.add(listaProductoCotizacion[i])
            }
        }
        adapterProductoComercial.filterList(filterdNameProducto)
    }
    private fun onItemDatosProductos(data: productocomercial) {

        //***********  Alerta de Dialogo  ***********
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.item_productocomericaldetallado,null)
        vista.setBackgroundResource(R.color.transparent)

        builder.setView(vista)

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.TOP)
        dialog.show()
        //*********************************************

        val tv_nameProducto = vista.findViewById<TextView>(R.id.tv_nameProducto)
        val tv_codProducto = vista.findViewById<TextView>(R.id.tv_codProducto)
        val tv_precioUnidad = vista.findViewById<TextView>(R.id.tv_precioUnidad)
        val tv_precioTotal = vista.findViewById<TextView>(R.id.tv_precioTotal)
        val tv_cantidad = vista.findViewById<TextView>(R.id.tv_cantidad)
        val iv_btnAutementar = vista.findViewById<ImageView>(R.id.iv_btnAutementar)
        val iv_btnDisminuir = vista.findViewById<ImageView>(R.id.iv_btnDisminuir)

        tv_nameProducto.text = data.nombre
        tv_codProducto.text = data.codigo
        tv_precioUnidad.text = "${data.mon} ${data.precio_Venta}"
        tv_precioTotal.text = "${data.mon} ${data.precio_Venta}"

        var cantidad = 0
        tv_cantidad.text = cantidad.toString()

        iv_btnAutementar.setOnClickListener {
            if (cantidad>=0){
                cantidad += 1
                tv_cantidad.text = cantidad.toString()
            }
            listaProductoListados.add(data)
            productlistcotadarte.notifyDataSetChanged()
        }

        iv_btnDisminuir.setOnClickListener {
            if (cantidad>0){
                cantidad -= 1
                tv_cantidad.text = cantidad.toString()
            }else if (cantidad==0){
                listaProductoListados.remove(data)
                productlistcotadarte.notifyDataSetChanged()
            }
        }
    }


}