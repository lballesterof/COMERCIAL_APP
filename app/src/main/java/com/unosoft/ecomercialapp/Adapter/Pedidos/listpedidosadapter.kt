package com.unosoft.ecomercialapp.Adapter.Pedidos

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.PedidoApi
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto

import kotlin.collections.ArrayList

class listpedidosadapter(var datos: ArrayList<pedidosDto>) : RecyclerView.Adapter<listpedidosadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.cardview_pedido,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position])
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        fun render (datos: pedidosDto){
            val tv_cliente = view.findViewById<TextView>(R.id.tv_cliente)
            val tv_numeropedido = view.findViewById<TextView>(R.id.tv_numeropedido)
            val ruc = view.findViewById<TextView>(R.id.ruc)
            val preciocotizacion = view.findViewById<TextView>(R.id.preciocotizacion)
            val fechapedido = view.findViewById<TextView>(R.id.fechapedido)

            tv_cliente.text = datos.persona
            tv_numeropedido.text = datos.numero_Pedido.toString()
            ruc.text = "RUC:  ${datos.ruc}"
            preciocotizacion.text = "${datos.mon}${datos.importe_Total}"
            fechapedido.text = "${datos.fecha_pedido}"
        }
    }

    fun filterList(namePedido: ArrayList<pedidosDto>) {
        datos = namePedido
        notifyDataSetChanged()
    }

}