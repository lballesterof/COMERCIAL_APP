package com.unosoft.ecomercialapp.ui.pedidos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.PedidoMaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityEditPedido : AppCompatActivity() {

    var apiInterface: PedidoMaster? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiInterface = APIClient.client?.create(PedidoMaster::class.java) as PedidoMaster
        setContentView(R.layout.activity_pedido_editar)
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