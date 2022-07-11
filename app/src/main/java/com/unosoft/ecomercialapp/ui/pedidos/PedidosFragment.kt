package com.unosoft.ecomercialapp.ui.pedidos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppedido.Prefs
import com.unosoft.ecomercialapp.Adapter.Cotizaciones.listcotizacionesadapter
import com.unosoft.ecomercialapp.Adapter.Pedidos.listpedidosadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.api.PedidoApi
import com.unosoft.ecomercialapp.api.PedidoMaster
import com.unosoft.ecomercialapp.databinding.FragmentPedidosBinding
import com.unosoft.ecomercialapp.databinding.FragmentSlideshowBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PedidosFragment : Fragment() {
    private lateinit var adapterPedidos: listpedidosadapter
    private val listapedidos = ArrayList<pedidosDto>()
    var apiInterface: PedidoApi? = null
    var apiInterface2: LoginApi? = null


    private var _binding: FragmentPedidosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentPedidosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface = APIClient.client?.create(PedidoApi::class.java) as PedidoApi
        apiInterface2 = APIClient.client?.create(LoginApi::class.java) as LoginApi

        initRecyclerView()
        buscarCotizacion()
        getDataPedido(prefs.getCdgVendedor())
    }

    private fun buscarCotizacion() {
        var sv_buscadorPedido = view?.findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_buscadorPedido)
        sv_buscadorPedido?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
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

    fun filter(text: String) {
        val filterdNamePedido: ArrayList<pedidosDto> = ArrayList()
        for (i in listapedidos.indices) {
            if (listapedidos[i].numero_Pedido.lowercase().contains(text.lowercase())) {
                filterdNamePedido.add(listapedidos[i])
            }
        }
        adapterPedidos.filterList(filterdNamePedido)
    }

    fun getDataLoginUser(usuarioMozo:String,passMozo:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface2!!.checkLoginComanda(DCLoginUser("$usuarioMozo","$passMozo"))
            activity?.runOnUiThread {
                if(response.isSuccessful){
                    val DatosUsuario = response.body()
                    println("*******  cdg_ vendedor *********")
                    println("${DatosUsuario!!.cdG_VENDEDOR}")
                }
            }
        }
    }

    fun initRecyclerView() {
        val rv_pedidos = view?.findViewById<RecyclerView>(R.id.rv_recyclerpedidos)
        rv_pedidos?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterPedidos = listpedidosadapter(listapedidos) { dataclassPedido -> onItemDatosPedidos(dataclassPedido) }
        rv_pedidos?.adapter = adapterPedidos
    }

    fun onItemDatosPedidos(dataclassPedido: pedidosDto) {

        prefs.save_IdPedido(dataclassPedido.id_pedido.toString())
        println("IDPEDIDO: ${prefs.getIdPedido()}")

        val enviarDatos = Bundle()
        val fragment = com.unosoft.ecomercialapp.ui.pedidomaster.PedidoMaster()
        val fragmentManager = activity?.supportFragmentManager
        val transaction = fragmentManager?.beginTransaction()


        enviarDatos.putSerializable("DATOSPEDIDOS",dataclassPedido)

        fragment.arguments = enviarDatos
        //CAMBIAR FRAMENT
        transaction!!.replace(R.id.nav_host_fragment_content_inicio, fragment ).commit()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataPedido(cdg_ven:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface!!.getPedido("$cdg_ven")
            activity?.runOnUiThread {
                if(response.isSuccessful){
                    listapedidos.clear()
                    listapedidos.addAll(response.body()!!)



                    adapterPedidos.notifyDataSetChanged()
                }
            }
        }
    }

}
