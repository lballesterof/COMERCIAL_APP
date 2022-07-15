package com.unosoft.ecomercialapp.Adapter.Pedidos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.helpers.utils
import java.lang.StringBuilder

class productlisteditorderadapter(var datos: ArrayList<EntityEditPedidoDetail>) : RecyclerView.Adapter<productlisteditorderadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_productocomericaldetalladocot,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position])
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun render (datos: EntityEditPedidoDetail){

            val tv_nameProducto = view.findViewById<TextView>(R.id.tv_nameProducto)
            val tv_codProducto = view.findViewById<TextView>(R.id.tv_codProducto)
            val tv_precioUnidad = view.findViewById<TextView>(R.id.tv_precioUnidad)
            val tv_cantidad = view.findViewById<TextView>(R.id.tv_cantidad)
            val tv_preciototal = view.findViewById<TextView>(R.id.tv_precioTotal)
            tv_cantidad.text = "${datos.cantidad.toString()} ${datos.unidad}"
            tv_nameProducto.text = datos.nombre
            tv_codProducto.text = datos.iD_PRODUCTO.toString()
            tv_precioUnidad.text = "${utils().pricetostringformat(datos.precio!!.toDouble())}"
            tv_preciototal.text = StringBuilder().append(utils().pricetostringformat(datos.precio.toDouble()))
        }
    }
}