package com.unosoft.ecomercialapp.ui.cotizacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ActivityEditCabezeraBinding
import com.unosoft.ecomercialapp.databinding.ActivityVisorPdfcotizacionBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.helpers.VisorPDF

class VisorPDFCotizacion : AppCompatActivity() {
    private lateinit var binding: ActivityVisorPdfcotizacionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisorPdfcotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //******************************************

        val datos = intent.getStringExtra("ID")

        val pdfView = findViewById<PDFView>(R.id.pdfView);

        val urlPdf = "http://181.224.236.167:6969/api/Cotizacion/pdf/$datos";

        VisorPDF(pdfView).execute(urlPdf)

    }

}