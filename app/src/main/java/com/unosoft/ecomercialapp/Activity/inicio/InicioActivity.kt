package com.unosoft.ecomercialapp.Activity.inicio

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.LogoApi
import com.unosoft.ecomercialapp.databinding.ActivityInicioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import java.net.URL

class InicioActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityInicioBinding

    var apiInterface: LogoApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiInterface = APIClient.client?.create(LogoApi::class.java) as LogoApi

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarInicio.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView


        iniciarDrawerLayout()



        val navController = findNavController(R.id.nav_host_fragment_content_inicio)
        appBarConfiguration = AppBarConfiguration(setOf( R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun iniciarDrawerLayout() {
        val navView: NavigationView = binding.navView

        val headerView = navView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tv_usuarioDra)
        val tvNameEmpresa = headerView.findViewById<TextView>(R.id.tv_nameEmpresaDra)
        val ivlogo = headerView.findViewById<ImageView>(R.id.iv_logoDra)

        CoroutineScope(Dispatchers.IO).launch {

            tvUserName.text = prefs.getUser()
            tvNameEmpresa.text = prefs.getCompany()


            runOnUiThread {

                    tvUserName.text = prefs.getUser()
                    tvNameEmpresa.text = prefs.getCompany()

                    val str = "${prefs.getURLBase()}api/Company/LogoEmpresa"
                    Picasso.get().load(str)
                        .resize(80, 80)
                        .error(R.drawable.image_not_found)
                        .into(ivlogo)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.inicio, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_inicio)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




}