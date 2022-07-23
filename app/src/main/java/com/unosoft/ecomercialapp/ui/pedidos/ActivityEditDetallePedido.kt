package com.unosoft.ecomercialapp.ui.pedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Pedidos.productlisteditorderadapter
import com.unosoft.ecomercialapp.Adapter.ProductoComercial.productocomercialadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.databinding.ActivityDetallePedidoBinding
import com.unosoft.ecomercialapp.databinding.ActivityPedidoEditarBinding
import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityEditDetallePedido : AppCompatActivity() {
    private lateinit var binding: ActivityDetallePedidoBinding
    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var adapterdetailpedido: productlisteditorderadapter

    private val listaProductoPEDIDO = ArrayList<EntityEditPedidoDetail>()
    private val listaProductoListados = ArrayList<EntityEditPedidoDetail>()


    var apiInterface2: ProductoComercial? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)

        InitRecyclerview()
        getData()
    }

    fun InitRecyclerview() {
        binding.rvLtsorder.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapterdetailpedido = productlisteditorderadapter(listaProductoPEDIDO)
        binding.rvLtsorder.adapter = adapterdetailpedido
    }

    fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            if (database.daoTblBasica().isExistsEntityListEditPedido()) {
                listaProductoPEDIDO.addAll(database.daoTblBasica().getAllDetail())
                adapterdetailpedido.notifyDataSetChanged()
            }
        }
    }

}

