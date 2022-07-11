package com.unosoft.ecomercialapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.unosoft.ecomercialapp.Activity.inicio.InicioActivity
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.api.APIClient.client
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import com.unosoft.ecomercialapp.entity.Login.Login
import com.unosoft.ecomercialapp.entity.Login.LoginComercialResponse
import com.unosoft.ecomercialapp.entity.Login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var apiInterface: LoginApi? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ingresar = findViewById<Button>(R.id.ingresar)
        val user = findViewById<EditText>(R.id.user)
        val pass = findViewById<EditText>(R.id.pass)
        apiInterface = client!!.create(LoginApi::class.java)
        val pd = ProgressDialog(this)
        pd.setMessage("Validando usuario....")
        pd.setCancelable(false)
        pd.create()
        ingresar.setOnClickListener(View.OnClickListener {
            if (user.text.toString().length == 0 || pass.text.toString().length == 0) {
                AlertMessage("Datos inválidos")
                return@OnClickListener
            } else {
                pd.show()
                val _user = DCLoginUser(user.text.toString(), pass.text.toString())
                val call1 = apiInterface!!.login(_user)
                call1.enqueue(object : Callback<LoginComercialResponse> {
                    override fun onResponse(
                        call: Call<LoginComercialResponse>,
                        response: Response<LoginComercialResponse>
                    ) {
                        if (response.code() == 400) {
                            AlertMessage("Usuario y/o Contraseña incorrecta")
                            pd.cancel()
                        } else {
                            val user1 = response.body()!!
                            prefs.save_CdgVendedor(user1.cdG_VENDEDOR)
                            prefs.save_TipoCambio(user1.tipocambio)
                            val i = Intent(applicationContext, InicioActivity::class.java)
                            startActivity(i)

                            // Toast.makeText(getApplicationContext(), user1.nombreusuario + " " + user1.jwtToken + " " + user1.poR_IGV + " " + user1.refreshToken, Toast.LENGTH_SHORT).show();
                            pd.cancel()
                        }
                    }

                    override fun onFailure(call: Call<LoginComercialResponse>, t: Throwable) {
                        pd.cancel()
                        AlertMessage("Error de Conexión: " + t.message)
                        call.cancel()
                    }
                })
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.inicio, menu)
        return true
    }
    fun AlertMessage(mensaje: String?) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Información")
        builder.setMessage(mensaje)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
        val dialogMessage = builder.create()
        dialogMessage.show()
    }
}