package com.unosoft.ecomercialapp.ui.slideshow

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.PedidoMaster
import com.unosoft.ecomercialapp.api.TablaBasicaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityEditCotizacion : AppCompatActivity() {

    var apiInterface: PedidoMaster? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_cotizacion_generar)
        getData(DATAGLOBAL.prefs.getIdPedido())
    }

    private fun getData(IDPEDIDO:String){
        CoroutineScope(Dispatchers.IO).launch {
            val cotizacion = apiInterface!!.getbyIdPedidoCab("$IDPEDIDO")

                if(cotizacion.isSuccessful){
                    var response = cotizacion.body()!!
                }

        }
    }


}