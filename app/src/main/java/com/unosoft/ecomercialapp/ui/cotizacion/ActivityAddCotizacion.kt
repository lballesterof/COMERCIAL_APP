package com.unosoft.ecomercialapp.ui.cotizacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ActivityAddCotizacionBinding

class ActivityAddCotizacion : AppCompatActivity() {

    private lateinit var binding: ActivityAddCotizacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventsHandlers()
    }



    private fun editDateClient() {

    }

    private fun addressCartQuotation() {
        val intent = Intent(this, ActivityCardQuotation::class.java)
        startActivity(intent)
    }

    private fun eventsHandlers() {
        binding.ivDatosClientAddCot.setOnClickListener { editDateClient() }
        binding.ivProductoAddCot.setOnClickListener { addressCartQuotation() }
    }
}