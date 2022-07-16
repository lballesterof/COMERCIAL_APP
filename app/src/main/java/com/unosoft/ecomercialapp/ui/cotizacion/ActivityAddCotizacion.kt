package com.unosoft.ecomercialapp.ui.cotizacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ActivityAddCotizacionBinding
import com.unosoft.ecomercialapp.databinding.ActivityEditCotizacionBinding

class ActivityAddCotizacion : AppCompatActivity() {

    private lateinit var binding: ActivityAddCotizacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        eventsHandlers()
    }

    private fun eventsHandlers() {

    }


}