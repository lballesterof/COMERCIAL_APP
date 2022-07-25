package com.unosoft.ecomercialapp.ui.pedidos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.PedidoMaster
import com.unosoft.ecomercialapp.databinding.ActivityPedidoEditarBinding
import com.unosoft.ecomercialapp.db.EntityPedidoMaster
import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto
import com.unosoft.ecomercialapp.helpers.utils
import com.unosoft.ecomercialapp.ui.Cotizacion.ActivityAddCotizacion
import com.unosoft.ecomercialapp.ui.Cotizacion.VisorPDFCotizacion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityEditPedido : AppCompatActivity() {

    var apiInterface: PedidoMaster? = null
    var iv_productosCot :ImageView? = null

    private lateinit var binding : ActivityPedidoEditarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoEditarBinding .inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface = APIClient.client?.create(PedidoMaster::class.java) as PedidoMaster
        getData(DATAGLOBAL.prefs.getIdPedido())
        InitializeUI()
        eventsHandlers()
    }
    private fun getData(IDPEDIDO: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val pedidocabresponse = apiInterface!!.getbyIdPedidoCab("$IDPEDIDO")
            val pedidodetailresponse = apiInterface!!.getbyIdPedidoDetail("$IDPEDIDO")
            if (pedidocabresponse.isSuccessful) {
                var t = pedidocabresponse.body()!!

                //Insert Cabecera ROOM
                with(database) {
                    //Insert Cabecera ROOM
                    daoTblBasica().deleteTablePedidoMaster()
                    daoTblBasica().insertPedidoMaster(
                        EntityPedidoMaster
                            (
                            0,
                            t.iD_PEDIDO,
                            t.numerO_PEDIDO,
                            t.noM_MON,
                            t.smB_MON,
                            t.conD_PAGO,
                            t.persona,
                            t.ruc,
                            t.freC_DIAS.toString(),
                            t.codigO_VENDEDOR,
                            t.codigO_CPAGO,
                            t.codigO_MONEDA,
                            t.fechA_PEDIDO,
                            t.numerO_OCLIENTE,
                            t.importE_STOT,
                            t.importE_IGV,
                            t.importE_DESCUENTO.toDouble(),
                            t.importE_TOTAL,
                            t.porcentajE_DESCUENTO,
                            t.porcentajE_IGV,
                            t.observacion,
                            t.serie.toString(),
                            t.estado,
                            t.iD_CLIENTE,
                            t.importE_ISC,
                            t.usuariO_CREACION,
                            t.usuariO_AUTORIZA.toString(),
                            t.fechA_CREACION,
                            t.fechA_MODIFICACION,
                            t.codigO_EMPRESA,
                            t.sucursal,
                            t.valoR_VENTA,
                            t.iD_CLIENTE_FACTURA,
                            t.codigO_VENDEDOR_ASIGNADO,
                            t.fechA_PROGRAMADA,
                            t.facturA_ADELANTADA,
                            t.contacto,
                            t.emaiL_CONTACTO,
                            t.lugaR_ENTREGA,
                            t.iD_COTIZACION,
                            t.comision,
                            t.puntO_VENTA,
                            t.redondeo,
                            t.validez,
                            t.motivo,
                            t.correlativo,
                            t.centrO_COSTO,
                            t.tipO_CAMBIO,
                            t.sucursal
                        )
                    )
                }

                if (pedidodetailresponse.isSuccessful) {
                    var d = pedidodetailresponse.body()!!
                    //Insert Detalle ROOM
                    with(database)
                    {
                        daoTblBasica().deleteTableEntityEditPedidoDetail()
                        d.forEach {
                            daoTblBasica().insertEditPedidoDetail(
                                EntityEditPedidoDetail(
                                    0,
                                    it.iD_PEDIDO,
                                    it.iD_PRODUCTO,
                                    it.cantidad,
                                    it.nombre,
                                    it.precio,
                                    it.descuento,
                                    it.igv,
                                    it.importe,
                                    it.canT_DESPACHADA,
                                    it.canT_FACTURADA,
                                    it.observacion,
                                    it.secuencia,
                                    it.preciO_ORIGINAL,
                                    it.tipo,
                                    it.importE_DSCTO,
                                    it.afectO_IGV,
                                    it.comision,
                                    it.iD_PRESUPUESTO,
                                    it.cdG_SERV,
                                    it.flaG_C,
                                    it.flaG_P,
                                    it.flaG_C,
                                    it.noM_UNIDAD,
                                    it.comanda,
                                    it.mozo,
                                    it.unidad,
                                    it.codigO_BARRA,
                                    it.poR_PERCEPCION,
                                    it.percepcion,
                                    it.valoR_VEN,
                                    it.uniD_VEN,
                                    it.fechA_VEN,
                                    it.factoR_CONVERSION,
                                    it.cdG_KIT,
                                    it.swT_PIGV,
                                    it.swT_PROM,
                                    it.canT_KIT,
                                    it.swT_DCOM,
                                    it.swT_SABOR,
                                    it.swT_FREE,
                                    it.noM_IMP,
                                    it.seC_PROD,
                                    it.poR_DETRACCION,
                                    it.detraccion,
                                    it.usuariO_ANULA,
                                    it.fechA_ANULA,
                                    it.margen,
                                    it.importE_MARGEN,
                                    it.costO_ADIC
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun InitializeUI() {
        val datos = intent.getSerializableExtra("DATOSPEDIDOS") as pedidosDto

        binding.tvNumPedido.text = "Numero: "+datos.numero_Pedido
        binding.tvFechaCreacionPedido.text = "Fecha de creacion "+datos.fecha_pedido
        binding.tvNomClientePedido.text = "Cliente: "+datos.persona
        binding.tvRucPedido.text = "RUC: "+datos.ruc
        binding.tvTipoMonedaPedido.text = "Moneda: ${datos.nom_moneda}"
        binding.tvCondPagoPedido.text = "Condicion Pago: "

        binding.tvSubTotalPedido.text = "${datos.mon} ${utils().pricetostringformat(datos.importe_Total-datos.importe_igv)}"
        binding.tvValorVentaPedido.text = "${datos.mon} ${utils().pricetostringformat(datos.importe_Total-datos.importe_igv)}"
        binding.tvIgvPedido.text = "${datos.mon} ${datos.importe_igv}"
        binding.tvImporteTotalPedido.text = "${datos.mon} ${datos.importe_Total}"

    }

    private fun eventsHandlers()
    {
        binding.icProducts.setOnClickListener {showactivitydetail()}
        val fa_pdfPedido = findViewById<com.getbase.floatingactionbutton.FloatingActionButton>(R.id.fa_pdfPedido)
        fa_pdfPedido.setOnClickListener { verPDF() }
    }

    private fun verPDF() {
        val intent = Intent(this, VisorPDFCotizacion::class.java)
        //ENVIAR DATOS
        val bundle = Bundle()
        bundle.putString("ID", DATAGLOBAL.prefs.getIdPedido())
        intent.putExtras(bundle)
        startActivity(intent)
    }


    //Nuevo Activity
    private fun showactivitydetail()
    {
        val intent = Intent(this, ActivityEditDetallePedido::class.java)
        startActivity(intent)
    }



}