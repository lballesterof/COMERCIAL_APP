package com.unosoft.ecomercialapp.ui.slideshow

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
import com.unosoft.ecomercialapp.databinding.FragmentSlideshowBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
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
    var searchview: SearchView? = null
    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //  val textView: TextView = binding.textSlideshow
        //slideshowViewModel.text.observe(viewLifecycleOwner) {
        //  textView.text = it
        // }
        return root
    }


    fun initRecyclerView() {
        val rv_cotizaciones = view?.findViewById<RecyclerView>(R.id.recyclerCotizaciones)
        rv_cotizaciones?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterCotizaciones = listcotizacionesadapter(listacotizaciones)
        rv_cotizaciones?.adapter = adapterCotizaciones
    }

    private fun loadData(vendedor: String) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO)
            {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://181.224.236.167:6969")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(ApiCotizacion::class.java)
                val response = api.fetchAllCotizaciones()

                if (response.isSuccessful) {
                    listacotizaciones.clear()
                    listacotizaciones.addAll(response.body()!!)
                    adapterCotizaciones.notifyDataSetChanged()
                }
            }


        }
    }
    private fun getData(){
        CoroutineScope(Dispatchers.IO).launch {
            val cotizaciones = apiInterface!!.fetchAllCotizaciones()
            activity?.runOnUiThread {
                if(cotizaciones.isSuccessful){
                    listacotizaciones.clear()
                    listacotizaciones.addAll(cotizaciones.body()!!)
                    adapterCotizaciones.notifyDataSetChanged()
                }
            }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface = APIClient.client?.create(ApiCotizacion::class.java) as ApiCotizacion
        initRecyclerView()
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}