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
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.api.APIClient.client
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.api.TablaBasicaApi
import com.unosoft.ecomercialapp.db.EntityCondicionPago
import com.unosoft.ecomercialapp.db.EntityDepartamento
import com.unosoft.ecomercialapp.db.EntityDistrito
import com.unosoft.ecomercialapp.db.EntityDocIdentidad
import com.unosoft.ecomercialapp.db.EntityFrecuenciaDias
import com.unosoft.ecomercialapp.db.EntityMoneda
import com.unosoft.ecomercialapp.db.EntityProvincia
import com.unosoft.ecomercialapp.db.EntityUnidadMedida
import com.unosoft.ecomercialapp.db.TablaBasica
import com.unosoft.ecomercialapp.entity.Login.Login
import com.unosoft.ecomercialapp.entity.Login.LoginResponse
import com.unosoft.ecomercialapp.entity.TableBasic.CondicionPagoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DepartamentoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DistritoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DocIdentidadResponse
import com.unosoft.ecomercialapp.entity.TableBasic.FrecuenciaDiasResponse
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.ProvinciaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.UnidadMedidaResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var apiInterface: LoginApi? = null
var apiInterface2: TablaBasicaApi? = null

private val listCondicionPago = ArrayList<CondicionPagoResponse>()
private val listDepartamento = ArrayList<DepartamentoResponse>()
private val listDistrito = ArrayList<DistritoResponse>()
private val listDocIdentidad = ArrayList<DocIdentidadResponse>()
private val listFrecuenciaDias = ArrayList<FrecuenciaDiasResponse>()
private val listMoneda = ArrayList<MonedaResponse>()
private val listProvincia = ArrayList<ProvinciaResponse>()
private val listUnidadMedida = ArrayList<UnidadMedidaResponse>()


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ingresar = findViewById<Button>(R.id.ingresar)
        val user = findViewById<EditText>(R.id.user)
        val pass = findViewById<EditText>(R.id.pass)
        apiInterface = client!!.create(LoginApi::class.java)
        apiInterface2 = client!!.create(TablaBasicaApi::class.java)

        cargarTablaBasica()


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
                val _user = Login(user.text.toString(), pass.text.toString())
                val call1 = apiInterface!!.login(_user)
                call1!!.enqueue(object : Callback<LoginResponse?> {
                    override fun onResponse(
                        call: Call<LoginResponse?>,
                        response: Response<LoginResponse?>
                    ) {
                        if (response.code() == 400) {
                            AlertMessage("Usuario y/o Contraseña incorrecta")
                            pd.cancel()
                        } else {
                            val user1 = response.body()
                            pd.setMessage("Cargando Tablas Básicas....")

                            ///
                            //cargarTablaBasica()
                            ///

                            val i = Intent(applicationContext, InicioActivity::class.java)
                            startActivity(i)


                            // Toast.makeText(getApplicationContext(), user1.nombreusuario + " " + user1.jwtToken + " " + user1.poR_IGV + " " + user1.refreshToken, Toast.LENGTH_SHORT).show();
                            pd.cancel()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                        pd.cancel()
                        AlertMessage("Error de Conexión: " + t.message)
                        call.cancel()
                    }
                })
            }
        })
    }

    private fun cargarTablaBasica() {


        fun inyectarCondicionPago(lista: ArrayList<CondicionPagoResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableCondicionPago()
                database.daoTblBasica().clearPrimaryKeyCondicionPago()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertCondicionPago(
                        EntityCondicionPago(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1))
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllCondicionPago())
                }

            }
        }

        fun getDataCondicionPago(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getCondicionPago()
                runOnUiThread {
                    if(response.isSuccessful){
                        listCondicionPago.addAll(response.body()!!)
                        inyectarCondicionPago(listCondicionPago)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }


        fun inyectarDepartamento(lista: ArrayList<DepartamentoResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableDepartamento()
                database.daoTblBasica().clearPrimaryKeyDepartamento()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertDepartamento(
                        EntityDepartamento(0,it.Codigo,it.Nombre,it.Numero)
                    )
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllDepartamento())
                }

            }
        }


        fun getDataDepartamento(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getDepartamento()
                runOnUiThread {
                    if(response.isSuccessful){
                        listDepartamento.addAll(response.body()!!)
                        inyectarDepartamento(listDepartamento)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }

        fun inyectarDistrito(lista: ArrayList<DistritoResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableDistrito()
                database.daoTblBasica().clearPrimaryKeyDistrito()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertDistrito(
                        EntityDistrito(0,it.Codigo,it.Nombre,it.Numero,it.Referencia2,it.Referencia3)
                    )
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllDistrito())
                }

            }
        }

        fun getDistrito(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getDistrito()
                runOnUiThread {
                    if(response.isSuccessful){
                        listDistrito.addAll(response.body()!!)
                        inyectarDistrito(listDistrito)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }

        fun inyectarDocIdentidad(lista: ArrayList<DocIdentidadResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableDocIdentidad()
                database.daoTblBasica().clearPrimaryKeyDocIdentidad()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertDocIdentidad(
                        EntityDocIdentidad(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                    )
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllDocIdentidad())
                }

            }
        }

        fun getDocIdentidad(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getDocIdentidad()
                runOnUiThread {
                    if(response.isSuccessful){
                        listDocIdentidad.addAll(response.body()!!)
                        inyectarDocIdentidad(listDocIdentidad)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }

        fun inyectarFrecuenciaDias(lista: ArrayList<FrecuenciaDiasResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableFrecuenciaDias()
                database.daoTblBasica().clearPrimaryKeyFrecuenciaDias()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertFrecuenciaDias(
                        EntityFrecuenciaDias(0,it.Codigo,it.Nombre,it.Numero)
                    )
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllFrecuenciaDias())
                }

            }
        }

        fun getFrecuenciaDias(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getFrecuenciaDias()
                runOnUiThread {
                    if(response.isSuccessful){
                        listFrecuenciaDias.addAll(response.body()!!)
                        inyectarFrecuenciaDias(listFrecuenciaDias)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }

        fun inyectarMoneda(lista: ArrayList<MonedaResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableMoneda()
                database.daoTblBasica().clearPrimaryKeyMoneda()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertMoneda(
                        EntityMoneda(0,it.Nombre,it.Nombre,it.Referencia1)
                    )
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllMoneda())
                }

            }
        }

        fun getMoneda(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getMoneda()
                runOnUiThread {
                    if(response.isSuccessful){
                        listMoneda.addAll(response.body()!!)
                        inyectarMoneda(listMoneda)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }

        fun inyectarProvincia(lista: ArrayList<ProvinciaResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableProvincia()
                database.daoTblBasica().clearPrimaryKeyProvincia()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertProvincia(
                        EntityProvincia(0,it.Codigo,it.Nombre,it.Numero,it.Referencia2)
                    )
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllProvincia())
                }

            }
        }

        fun getProvincia(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getProvincia()
                runOnUiThread {
                    if(response.isSuccessful){
                        listProvincia.addAll(response.body()!!)
                        inyectarProvincia(listProvincia)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }

        fun inyectarUnidadMedida(lista: ArrayList<UnidadMedidaResponse>) {
            GlobalScope.launch(Dispatchers.Default) {
                database.daoTblBasica().deleteTableUnidadMedida()
                database.daoTblBasica().clearPrimaryKeyUnidadMedida()

                println("************ LISTA ***************")
                println(lista)

                lista.forEach {
                    database.daoTblBasica().insertUnidadMedida(
                        EntityUnidadMedida(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                    )
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllUnidadMedida())
                }
            }
        }

        fun getUnidadMedida(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getUnidadMedida()
                runOnUiThread {
                    if(response.isSuccessful){
                        listUnidadMedida.addAll(response.body()!!)
                        inyectarUnidadMedida(listUnidadMedida)
                    }else{
                        println("Error en la conaulta ")
                    }
                }
            }
        }

        getDataCondicionPago()
        getDataDepartamento()
        getDistrito()
        getDocIdentidad()
        getFrecuenciaDias()
        getMoneda()
        getProvincia()
        getUnidadMedida()

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