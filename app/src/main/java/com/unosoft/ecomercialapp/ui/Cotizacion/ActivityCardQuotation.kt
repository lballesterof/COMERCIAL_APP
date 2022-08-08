package com.unosoft.ecomercialapp.ui.Cotizacion

import android.content.Intent
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
import com.unosoft.ecomercialapp.Adapter.ProductListCot.productlistcotadarte
import com.unosoft.ecomercialapp.Adapter.ProductoComercial.productocomercialadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.databinding.ActivityCardQuotationBinding
import com.unosoft.ecomercialapp.db.EntityListProct
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityCardQuotation : AppCompatActivity() {

    private lateinit var binding: ActivityCardQuotationBinding

    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var productlistcotadarte: productlistcotadarte

    private val listaProductoCotizacion = ArrayList<productocomercial>()
    private val listaProductoListados = ArrayList<productlistcot>()


    var apiInterface2: ProductoComercial? = null

    var montoTotal:Double = 0.0
    var igvTotal:Double = 0.0
    var subtotal:Double = 0.0

    var tipomoneda = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardQuotationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tipomoneda = intent.getStringExtra("TIPOMONEDA").toString()

        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)

        //iniciarLista()
        getData()

        productosListado()
        abrirListProductos()
        eventsHandlers()
    }

    private fun eventsHandlers() {
        binding.btnGuardarCartCotizacion.setOnClickListener { guardarDatos() }
        binding.btnCancelarCartCotizacion.setOnClickListener { cancelarPedido() }
    }

    private fun cancelarPedido() {
        val intent = Intent(this, ActivityAddCotizacion::class.java)
        startActivity(intent)
        finish()
    }

    private fun guardarDatos() {
        guardarListRoom()

        val intent = Intent(this, ActivityAddCotizacion::class.java)
        startActivity(intent)
        finish()
    }


    private fun iniciarLista() {
        /*
        CoroutineScope(Dispatchers.IO).launch {
            if (database.daoTblBasica().isExistsEntityListProctCot())
            {
                database.daoTblBasica().deleteTableListProctCot()
                database.daoTblBasica().clearPrimaryKeyListProctCot()
            }

        }
        */

    }

    //********* INICIA DATOS  **************
    fun getData() {
        CoroutineScope(Dispatchers.IO).launch {

            if (database.daoTblBasica().isExistsEntityProductList()){

                database.daoTblBasica().getAllListProct().forEach {
                    listaProductoListados.add(
                        productlistcot(
                            it.id_Producto,it.codigo,it.codigo_Barra,it.nombre,tipomoneda,it.precio_Venta,it.factor_Conversion,
                            it.cdg_Unidad,it.unidad,it.moneda_Lp,it.cantidad,it.precioUnidad,it.precioTotal
                        )
                    )
                }

                database.daoTblBasica().deleteTableListProct()
                database.daoTblBasica().clearPrimaryKeyListProct()
            }

            CoroutineScope(Dispatchers.IO).launch {
                calcularMontoTotal()
            }
        }

    }

    //************  PRODUCTOS LISTADOS *************
    fun productosListado() {
        val rv_listproductcot = binding.rvListProdut
        rv_listproductcot.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productlistcotadarte = productlistcotadarte(listaProductoListados) { data -> onItemDatosProductList(data) }
        rv_listproductcot.adapter = productlistcotadarte
    }
    fun onItemDatosProductList(data: productlistcot) {
        //***********  Alerta de Dialogo  ***********
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.item_productocomericaldetallado, null)
        vista.setBackgroundResource(R.color.transparent)

        builder.setView(vista)

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.TOP)
        dialog.show()
        //*********************************************

        //********************    DECLARA LOS COMPONENTES   ******************
        val tv_nameProducto = vista.findViewById<TextView>(R.id.tv_nameProducto)
        val tv_codProducto = vista.findViewById<TextView>(R.id.tv_codProducto)
        val tv_precioUnidad = vista.findViewById<TextView>(R.id.tv_precioUnidad)
        val tv_precioTotal = vista.findViewById<TextView>(R.id.tv_precioTotal)
        val tv_cantidad = vista.findViewById<TextView>(R.id.tv_cantidad)
        val iv_btnAutementar = vista.findViewById<ImageView>(R.id.iv_btnAutementar)
        val iv_btnDisminuir = vista.findViewById<ImageView>(R.id.iv_btnDisminuir)

        //********   SETEA VALORES INICIALES
        val evalua = buscaCoincidencia(data.codigo!!)
        val action = evalua[0]
        val pos = evalua[1]

        tv_nameProducto.text = data.nombre
        tv_codProducto.text = data.codigo
        tv_precioUnidad.text = "${data.mon} ${pricetostringformat(data.precio_Venta)}"
        tv_precioTotal.text = "${data.mon} ${pricetostringformat(calculatepricebyqty(tv_cantidad.text.toString().toInt(), data.precio_Venta))}"

        if (action == 0) {
            tv_cantidad.text = "0"
            tv_precioTotal.text = "0"
        } else {
            tv_cantidad.text = listaProductoListados[pos].cantidad.toString()
            tv_precioTotal.text = "${data.mon} ${pricetostringformat(calculatepricebyqty(tv_cantidad.text.toString().toInt(), data.precio_Venta))}"
        }

        //********   AUMENTA PRODUCTOS O AGREGA    *************
        iv_btnAutementar.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]
            var cantidad = 0

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action == 0) {
                cantidad += 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${pricetostringformat(precioTotal)}"
                listaProductoListados.add(
                    productlistcot(
                        data.id_Producto,
                        data.codigo,
                        data.codigo_Barra,
                        data.nombre,
                        data.mon,
                        data.precio_Venta,
                        data.factor_Conversion,
                        data.cdg_Unidad,
                        data.unidad,
                        data.moneda_Lp,
                        cantidad,
                        data.precio_Venta,
                        precioTotal
                    )
                )
                rv_listproductcot.adapter?.notifyDataSetChanged()
                rv_listproductcot?.scrollToPosition(listaProductoListados.size - 1)
            } else {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad + 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${pricetostringformat(precioTotal)}"
                listaProductoListados[pos] = productlistcot(
                    data.id_Producto,
                    data.codigo,
                    data.codigo_Barra,
                    data.nombre,
                    data.mon,
                    data.precio_Venta,
                    data.factor_Conversion,
                    data.cdg_Unidad,
                    data.unidad,
                    data.moneda_Lp,
                    cantidad,
                    data.precio_Venta,
                    precioTotal
                )
                rv_listproductcot?.adapter?.notifyDataSetChanged()
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()

        }
        //********   DISMINUYE PRODUCTOS O ELIMINA    *************
        iv_btnDisminuir.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action != 0) {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad - 1
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                listaProductoListados[pos] = productlistcot(
                    data.id_Producto,
                    data.codigo,
                    data.codigo_Barra,
                    data.nombre,
                    data.mon,
                    data.precio_Venta,
                    data.factor_Conversion,
                    data.cdg_Unidad,
                    data.unidad,
                    data.moneda_Lp,
                    cantidad,
                    data.precio_Venta,
                    precioTotal)
                tv_cantidad.text = cantidad.toString()
                tv_precioTotal.text = "${data.mon} ${pricetostringformat(precioTotal)}"
                rv_listproductcot?.adapter?.notifyDataSetChanged()
                if (cantidad == 0) {
                    listaProductoListados.removeAt(pos)
                    rv_listproductcot?.adapter?.notifyDataSetChanged()
                }
            } else {
                println("no hay productos que conincidan")
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()
        }
    }
    fun abrirListProductos() {

        binding.iconAddMoreProductList.setOnClickListener {

            //***********  Alerta de Dialogo  ***********
            val builder = AlertDialog.Builder(this)
            val vista = layoutInflater.inflate(R.layout.dialogue_detalle_cotizacion_productos, null)
            vista.setBackgroundResource(R.color.transparent)

            builder.setView(vista)

            val dialog = builder.create()
            dialog.window!!.setGravity(Gravity.TOP)
            dialog.show()

            val rv_productos = vista.findViewById<RecyclerView>(R.id.rv_productos)
            val sv_consultasproductos = vista.findViewById<SearchView>(R.id.sv_consultasproductos)

            rv_productos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapterProductoComercial = productocomercialadapter(listaProductoCotizacion) { data ->
                onItemDatosProductos(data)
            }
            rv_productos.adapter = adapterProductoComercial

            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getProductoComercial("${database.daoTblBasica().getAllDataCabezera()[0].codListPrecio}", "${database.daoTblBasica().getAllDataCabezera()[0].codMoneda}", "${prefs.getTipoCambio()}")
                runOnUiThread {
                    if (response.isSuccessful) {
                        listaProductoCotizacion.clear()
                        listaProductoCotizacion.addAll(response.body()!!)
                        adapterProductoComercial.notifyDataSetChanged()
                    }
                }
            }

            sv_consultasproductos?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
    fun onItemDatosProductos(data: productocomercial) {
        //***********  Alerta de Dialogo  ************
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.item_productocomericaldetallado, null)
        vista.setBackgroundResource(R.color.transparent)

        builder.setView(vista)

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.TOP)
        dialog.show()
        //*********************************************

        //********************    DECLARA LOS COMPONENTES   ******************
        val tv_nameProducto = vista.findViewById<TextView>(R.id.tv_nameProducto)
        val tv_codProducto = vista.findViewById<TextView>(R.id.tv_codProducto)
        val tv_precioUnidad = vista.findViewById<TextView>(R.id.tv_precioUnidad)
        val tv_precioTotal = vista.findViewById<TextView>(R.id.tv_precioTotal)
        val tv_cantidad = vista.findViewById<TextView>(R.id.tv_cantidad)
        val iv_btnAutementar = vista.findViewById<ImageView>(R.id.iv_btnAutementar)
        val iv_btnDisminuir = vista.findViewById<ImageView>(R.id.iv_btnDisminuir)

        //********   SETEA VALORES INICIALES
        val evalua = buscaCoincidencia(data.codigo)
        val action = evalua[0]
        val pos = evalua[1]

        tv_nameProducto.text = data.nombre
        tv_codProducto.text = data.codigo
        tv_precioUnidad.text = "${data.mon} ${pricetostringformat(data.precio_Venta!!)}"
        tv_precioTotal.text = "${data.mon} ${ pricetostringformat(calculatepricebyqty(tv_cantidad.text.toString().toInt(), data.precio_Venta))}"

        if (action == 0) {
            tv_cantidad.text = "0"
            tv_precioTotal.text = "0"
        } else {
            tv_cantidad.text = listaProductoListados[pos].cantidad.toString()
            tv_precioTotal.text = "${data.mon} ${pricetostringformat(calculatepricebyqty(tv_cantidad.text.toString().toInt(), data.precio_Venta))}"
        }

        //********   AUMENTA PRODUCTOS O AGREGA    *************
        iv_btnAutementar.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]
            var cantidad = 0

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action == 0) {
                cantidad += 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${pricetostringformat(precioTotal)}"
                listaProductoListados.add(
                    productlistcot(
                        data.id_Producto,
                        data.codigo,
                        data.codigo_Barra,
                        data.nombre,
                        data.mon,
                        data.precio_Venta,
                        data.factor_Conversion,
                        data.cdg_Unidad,
                        data.unidad,
                        data.moneda_Lp,
                        cantidad,
                        data.precio_Venta,
                        precioTotal
                    )
                )
                Toast.makeText(this, "Se agrego ${data.nombre}", Toast.LENGTH_SHORT).show()
                rv_listproductcot.adapter?.notifyDataSetChanged()
                rv_listproductcot?.scrollToPosition(listaProductoListados.size - 1)
            } else {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad + 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${pricetostringformat(precioTotal)}"
                listaProductoListados.set(
                    pos,
                    productlistcot(
                        data.id_Producto,
                        data.codigo,
                        data.codigo_Barra,
                        data.nombre,
                        data.mon,
                        data.precio_Venta,
                        data.factor_Conversion,
                        data.cdg_Unidad,
                        data.unidad,
                        data.moneda_Lp,
                        cantidad,
                        data.precio_Venta,
                        precioTotal
                    )
                )
                rv_listproductcot?.adapter?.notifyDataSetChanged()
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()

        }

        //********   DISMINUYE PRODUCTOS O ELIMINA    *************
        iv_btnDisminuir.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action != 0) {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad - 1
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                listaProductoListados[pos] = productlistcot(
                    data.id_Producto,
                    data.codigo,
                    data.codigo_Barra,
                    data.nombre,
                    data.mon,
                    data.precio_Venta,
                    data.factor_Conversion,
                    data.cdg_Unidad,
                    data.unidad,
                    data.moneda_Lp,
                    cantidad,
                    data.precio_Venta,
                    precioTotal
                )
                tv_cantidad.text = cantidad.toString()
                tv_precioTotal.text = "${data.mon} ${pricetostringformat(precioTotal)}"
                rv_listproductcot?.adapter?.notifyDataSetChanged()
                if (cantidad == 0) {
                    listaProductoListados.removeAt(pos)
                    rv_listproductcot?.adapter?.notifyDataSetChanged()
                }
            } else {
                println("no hay productos que conincidan")
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()
        }
    }

    //************* FUNCIONES ADICIONALES  ****************
    fun calcularMontoTotal(){

        montoTotal = listaProductoListados.sumOf { it.precioTotal }
        igvTotal = utils().priceIGV(montoTotal)
        subtotal = utils().priceSubTotal(montoTotal)

        val tv_subtotalCot = binding.tvSubTotalAddCart
        val tv_igvCot = binding.tvIgvCotAddCart
        val tv_totalCot = binding.tvTotalCotAddCart

        tv_totalCot.text = "${tipomoneda} ${utils().pricetostringformat(montoTotal)}"
        tv_igvCot.text = "${tipomoneda} ${utils().pricetostringformat(igvTotal)}"
        tv_subtotalCot.text = "${tipomoneda} ${utils().pricetostringformat(subtotal)}"
    }

    private fun buscaCoincidencia(dataCodigo:String): List<Int> {
        //-------------Evalua POSICION Y ACCION DE AGREGAR-------------------
        //println("------- Evalua POSICION Y ACCION DE AGREGAR-------------")
        var action = 0
        var pos = -1

        for (i in listaProductoListados.indices) {
            println("Cod: ${listaProductoListados[i].codigo}")
            if (listaProductoListados[i].codigo == dataCodigo) {
                action += 1
            }
            if (action == 1) {
                pos = i
                println("posicion: $pos")
                break
            }
        }

        return listOf(action, pos)
    }
    private fun calculatepricebyqty(Qty: Int, Price: Double): Double {
        return (Price * Qty.toDouble())
    }
    private fun pricetostringformat(valuenumeric: Double): String {
        return String.format("%,.2f", valuenumeric)
    }

    //************* GUARDAR ROOM  ****************
    fun guardarListRoom(){

        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().deleteTableListProct()
            database.daoTblBasica().clearPrimaryKeyListProct()

            if(listaProductoListados.size>0){
                listaProductoListados.forEach {
                    database.daoTblBasica().insertListProct(
                        EntityListProct(
                            0, it.id_Producto, it.codigo!!, it.codigo_Barra,
                            it.nombre,it.mon,it.precio_Venta,it.factor_Conversion,it.cdg_Unidad,it.unidad,
                            it.moneda_Lp,it.cantidad,it.precioUnidad,it.precioTotal,subtotal,igvTotal,montoTotal
                        )
                    )
                }
            }

            println(database.daoTblBasica().getAllListProct())
        }

    }
    override fun onBackPressed() {
        guardarListRoom()

        val intent = Intent(this, ActivityAddCotizacion::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}