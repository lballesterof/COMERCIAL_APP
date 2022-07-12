package com.unosoft.ecomercialapp.ui.cotizacion

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unosoft.ecomercialapp.R

class ActivityDetalleCotizacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cotizacion)

        abrirListProductos()
    }

    private fun abrirListProductos() {
        val icon_agregarProductosCotizacion = findViewById<FloatingActionButton>(R.id.icon_agregarProductosCotizacion)
        icon_agregarProductosCotizacion.setOnClickListener {
            val dialogue = Dialog(this)
            dialogue.setContentView(R.layout.dialogue_detalle_cotizacion_productos)
            dialogue.show()
        }
    }


}