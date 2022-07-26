package com.unosoft.ecomercialapp.ui.CerrarSesion

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.MainActivity
import com.unosoft.ecomercialapp.databinding.ActivityMainBinding
import com.unosoft.ecomercialapp.databinding.FragmentCerrarSesionFramentBinding
import com.unosoft.ecomercialapp.ui.Cotizacion.ActivityAddCotizacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CerrarSesionFrament : Fragment() {

    private var _binding: FragmentCerrarSesionFramentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentCerrarSesionFramentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialogueCerrar()
    }

    private fun dialogueCerrar() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Cerrar Sesion")
        builder.setIcon(android.R.drawable.ic_input_delete)

        builder.setPositiveButton("Si") { dialogInterface, which ->
            cerrarSesion()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            requireActivity().onBackPressed()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun cerrarSesion() {
        GlobalScope.launch(Dispatchers.Default) {
            database.daoTblBasica().deleteTableCondicionPago()
            database.daoTblBasica().clearPrimaryKeyCondicionPago()
            database.daoTblBasica().deleteTableDepartamento()
            database.daoTblBasica().clearPrimaryKeyDepartamento()
            database.daoTblBasica().deleteTableDistrito()
            database.daoTblBasica().clearPrimaryKeyDistrito()
            database.daoTblBasica().deleteTableDocIdentidad()
            database.daoTblBasica().clearPrimaryKeyDocIdentidad()
            database.daoTblBasica().deleteTableFrecuenciaDias()
            database.daoTblBasica().clearPrimaryKeyFrecuenciaDias()
            database.daoTblBasica().deleteTableMoneda()
            database.daoTblBasica().clearPrimaryKeyMoneda()
            database.daoTblBasica().deleteTableProvincia()
            database.daoTblBasica().clearPrimaryKeyProvincia()
            database.daoTblBasica().deleteTableUnidadMedida()
            database.daoTblBasica().clearPrimaryKeyUnidadMedida()
            database.daoTblBasica().deleteTableListaPrecio()
            database.daoTblBasica().clearPrimaryKeyListaPrecio()
            database.daoTblBasica().deleteTableVendedor()
            database.daoTblBasica().clearPrimaryKeyVendedor()
            activity?.runOnUiThread {
                println("***************************************")
                println("*********** CERRAR SESION *************")
                println("***************************************")
                startActivity(
                    Intent(activity, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                )
                requireActivity().finish()
            }
        }
    }
}