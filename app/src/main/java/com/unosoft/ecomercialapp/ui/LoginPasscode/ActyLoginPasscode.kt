package com.unosoft.ecomercialapp.ui.LoginPasscode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unosoft.ecomercialapp.databinding.ActivityAddPedidoBinding
import com.unosoft.ecomercialapp.databinding.ActivityLoginPasscodeBinding

class ActyLoginPasscode : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPasscodeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPasscodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}