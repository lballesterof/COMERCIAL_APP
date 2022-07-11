package com.unosoft.ecomercialapp.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Cotizaciones.listcotizacionesadapter
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.databinding.FragmentSlideshowBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SlideshowFragment : Fragment() {
    private lateinit var adapterCotizaciones: listcotizacionesadapter
    private val listacotizaciones = ArrayList<cotizacionesDto>()
    var apiInterface: ApiCotizacion? = null
    var apiInterface2: LoginApi? = null

    private var _binding: FragmentSlideshowBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface = APIClient.client?.create(ApiCotizacion::class.java) as ApiCotizacion
        apiInterface2 = APIClient.client?.create(LoginApi::class.java) as LoginApi

        initRecyclerView()
        getDataLoginUser("User1","123456")
        buscarCotizacion()
    }

    private fun buscarCotizacion() {
        var sv_buscadorCotizacion = view?.findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_buscadorCotizacion)

        sv_buscadorCotizacion?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
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
        val filterdNamePlato: ArrayList<cotizacionesDto> = ArrayList()
        for (i in listacotizaciones.indices) {
            if (listacotizaciones[i].numero_Cotizacion.lowercase().contains(text.lowercase())) {
                filterdNamePlato.add(listacotizaciones[i])
            }
        }
        adapterCotizaciones.filterList(filterdNamePlato)
    }



    fun getDataLoginUser(usuarioMozo:String,passMozo:String) {
        CoroutineScope(Dispatchers.IO).launch {
           val response = apiInterface2!!.checkLoginComanda(DCLoginUser("$usuarioMozo","$passMozo"))
              activity?.runOnUiThread {
                if(response.isSuccessful){
                    val DatosUsuario = response.body()
                    getData(DatosUsuario!!.cdG_VENDEDOR)
                }
           }
        }
    }


    fun initRecyclerView() {
        val rv_cotizaciones = view?.findViewById<RecyclerView>(R.id.recyclerCotizaciones)
        rv_cotizaciones?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterCotizaciones = listcotizacionesadapter(listacotizaciones)
        rv_cotizaciones?.adapter = adapterCotizaciones
    }

    private fun getData(cdg_ven:String){
        CoroutineScope(Dispatchers.IO).launch {
            val cotizaciones = apiInterface!!.fetchAllCotizaciones("$cdg_ven")
            activity?.runOnUiThread {
                if(cotizaciones.isSuccessful){
                    listacotizaciones.clear()
                    listacotizaciones.addAll(cotizaciones.body()!!)
                    adapterCotizaciones.notifyDataSetChanged()
                }
            }
        }
    }



}