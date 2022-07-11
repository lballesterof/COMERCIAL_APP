package com.unosoft.ecomercialapp.ui.pedidomaster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto

class PedidoMaster : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pedido_master, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iniciarData()
    }

    private fun iniciarData() {

        val dataRecuver = arguments
        val datos:pedidosDto = dataRecuver?.getSerializable("DATOSPEDIDOS") as pedidosDto

        val tv_numeroPedido = view?.findViewById<TextView>(R.id.tv_numeroPedido)
        val tv_fec_orden = view?.findViewById<TextView>(R.id.tv_fec_orden)
        val tv_cliente = view?.findViewById<TextView>(R.id.tv_cliente)
        val tv_ruc = view?.findViewById<TextView>(R.id.tv_ruc)
        val tv_moneda = view?.findViewById<TextView>(R.id.tv_moneda)
        val TV_STD_DOC = view?.findViewById<TextView>(R.id.TV_STD_DOC)

        tv_numeroPedido?.text = datos.numero_Pedido
        tv_fec_orden?.text = datos.fecha_pedido
        tv_cliente?.text = datos.persona
        tv_ruc?.text = datos.ruc
        tv_moneda?.text = datos.nom_moneda
        TV_STD_DOC?.text = datos.documento






    }
}