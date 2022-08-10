package com.unosoft.ecomercialapp.helpers

import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class utils {


    fun pricetostringformat(valuenumeric: Double): String {
        return String.format("%,.2f", valuenumeric)
    }

    fun priceSubTotal(price: Double): Double {
        val igv = prefs.getIGV().toDouble()
        return (price.div(1 + (igv.div(100))))
    }

    fun priceIGV(price: Double): Double {
        val igv = prefs.getIGV().toDouble()
        return price.minus(price.div(1 + (igv.div(100))))
    }

    fun getFecha(): String {
        return SimpleDateFormat("dd/MM/yyyy").format(LocalDateTime.now())
    }

    fun converFechaString(date:LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = date.format(formatter)
        return formattedDate.toString()
    }



}