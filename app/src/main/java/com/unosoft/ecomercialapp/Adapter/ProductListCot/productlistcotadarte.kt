package com.unosoft.ecomercialapp.Adapter.ProductListCot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial

class productlistcotadarte (var datos: ArrayList<productocomercial>, private val onClickListener: (productocomercial) -> Unit) : RecyclerView.Adapter<productlistcotadarte.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_productocomericaldetallado,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position],onClickListener)
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun render (datos: productocomercial, onClickListener: (productocomercial) -> Unit){

            val tv_nameProducto = view.findViewById<TextView>(R.id.tv_nameProducto)
            val tv_codProducto = view.findViewById<TextView>(R.id.tv_codProducto)
            val tv_precioUnidad = view.findViewById<TextView>(R.id.tv_precioUnidad)
            val tv_precioTotal = view.findViewById<TextView>(R.id.tv_precioTotal)
            val iv_btnAutementar = view.findViewById<ImageView>(R.id.iv_btnAutementar)
            val iv_btnDisminuir = view.findViewById<ImageView>(R.id.iv_btnDisminuir)
            val tv_cantidad = view.findViewById<TextView>(R.id.tv_cantidad)



            tv_nameProducto.text = datos.nombre
            tv_codProducto.text = datos.codigo
            tv_precioUnidad.text = "${datos.mon} ${datos.precio_Venta}"
            tv_precioTotal.text = "${datos.mon} ${datos.precio_Venta}"

            itemView.setOnClickListener { onClickListener(datos) }

        }
    }
}