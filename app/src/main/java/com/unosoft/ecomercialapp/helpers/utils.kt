package com.unosoft.ecomercialapp.helpers

class utils {


    fun pricetostringformat(valuenumeric: Double): String {
        return String.format("%,.2f", valuenumeric)
    }


    fun calculateigvbypriceunit(price: Double, igv: Double): Double {
        return price.minus(price.div(1 + (igv.div(100))))
    }

    fun calculatepriceunitsubtractigv(price: Double, igv: Double): Double {
        return (price.div(1 + (igv.div(100))))
    }
}