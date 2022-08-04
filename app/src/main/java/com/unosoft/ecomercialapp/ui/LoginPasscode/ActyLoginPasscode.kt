package com.unosoft.ecomercialapp.ui.LoginPasscode

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.unosoft.ecomercialapp.Activity.inicio.InicioActivity
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.databinding.ActivityAddPedidoBinding
import com.unosoft.ecomercialapp.databinding.ActivityLoginPasscodeBinding
import com.unosoft.ecomercialapp.db.EntityDataLogin
import com.unosoft.ecomercialapp.db.EntityEmpresa
import com.unosoft.ecomercialapp.entity.Empresa.dcEmpresa
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import com.unosoft.ecomercialapp.entity.Login.LoginComercialResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActyLoginPasscode : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPasscodeBinding

    var apiInterface: LoginApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPasscodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface = APIClient.client?.create(LoginApi::class.java)


        binding.btnIngresar.setOnClickListener(View.OnClickListener {

            if (binding.etPass.text.toString().isEmpty()) {
                AlertMessage("Datos inválidos")
                return@OnClickListener
            } else {

                println(prefs.getUser())
                println(prefs.getURLBase())

                //***********************  MANTENER   ****************************
                val _user = DCLoginUser(prefs.getUser(), binding.etPass.text.toString())

                println(_user)

                val call1 = apiInterface!!.login(_user)
                call1.enqueue(object : Callback<LoginComercialResponse> {
                    override fun onResponse(call: Call<LoginComercialResponse>, response: Response<LoginComercialResponse>) {
                        if (response.code() == 400) {
                            AlertMessage("Usuario y/o Contraseña incorrecta")
                        } else {

                            val user1 = response.body()!!

                            CoroutineScope(Dispatchers.IO).launch {
                                database.daoTblBasica().deleteTableDataLogin()
                                database.daoTblBasica().clearPrimaryKeyDataLogin()

                                database.daoTblBasica().insertDataLogin(
                                    EntityDataLogin(
                                    0,
                                    user1.nombreusuario,
                                    user1.codigO_EMPRESA,
                                    user1.iD_CLIENTE,
                                    user1.poR_IGV,
                                    user1.cdgmoneda,
                                    user1.validez,
                                    user1.cdgpago,
                                    user1.sucursal,
                                    user1.usuarioautoriza,
                                    user1.usuariocreacion,
                                    user1.descuento,
                                    user1.seriepedido,
                                    user1.estadopedido,
                                    user1.tipocambio,
                                    user1.jwtToken,
                                    user1.facturA_ADELANTADA,
                                    user1.iD_COTIZACION,
                                    user1.puntO_VENTA,
                                    user1.redondeo,
                                    user1.cdG_VENDEDOR
                                    )
                                )

                                println("***************  IMPRIMIENDO DATOS USUARIO  *****************")
                                println(database.daoTblBasica().getAllDataLogin())

                                println("***************  LIMPIAR DATOS LISTA PRODUCTO Y CABEZERA  *****************")
                                database.daoTblBasica().deleteTableListProct()
                                database.daoTblBasica().clearPrimaryKeyListProct()
                                database.daoTblBasica().deleteTableDataCabezera()
                                database.daoTblBasica().clearPrimaryKeyDataCabezera()

                                runOnUiThread {

                                    println(user1.cdG_VENDEDOR)
                                    println(user1.tipocambio)

                                    prefs.save_CdgVendedor(user1.cdG_VENDEDOR)
                                    prefs.save_TipoCambio(user1.tipocambio.toString())
                                    prefs.save_IGV(user1.poR_IGV)

                                    val i = Intent(applicationContext, InicioActivity::class.java)
                                    startActivity(i)
                                    finish()

                                    binding.etPass.setText("")

                                    // Toast.makeText(getApplicationContext(), user1.nombreusuario + " " + user1.jwtToken + " " + user1.poR_IGV + " " + user1.refreshToken, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginComercialResponse>, t: Throwable) {
                        AlertMessage("Error de Conexión: " + t.message)
                        call.cancel()
                    }
                })
                //*******  MANTENER ********
            }
        })
    }

    fun AlertMessage(mensaje: String?) {
        val builder = AlertDialog.Builder(this@ActyLoginPasscode)
        builder.setTitle("Información")
        builder.setMessage(mensaje)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
        val dialogMessage = builder.create()
        dialogMessage.show()
    }
}