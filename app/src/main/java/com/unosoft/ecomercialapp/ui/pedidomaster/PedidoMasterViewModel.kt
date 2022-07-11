package com.unosoft.ecomercialapp.ui.pedidomaster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PedidoMasterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Bienvenidos al PedidoMaster"
    }
    val text: LiveData<String> = _text
}