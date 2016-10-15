package com.corporacioncorrales.cotizacionesapp.utils;

/**
 * Created by victor on 8/10/16.
 */
public class Constants {

    public static final String Empty = "";

    public static final String allowEnterToApp = "True";
    public static final String denyEnterToApp = "False";


    public static final String log_arrow = "--------> ";
    public static final String log_arrow_error = "--------> ERROR";
    public static final String log_arrow_response = "--------> RESPONSE";
    public static final String log_arrow_failure = "--------> FAILURE";


    public static final String url_server = "http://190.81.34.42:8080/";
    public static final String url_user_login = "api/usuario/{user}";
    public static final String url_clients_per_user = "api/clientes";
    public static final String url_products_per_user = "api/articulos";
    public static final String url_send_quotation = "api/grabarproforma/createproducts";
    public static final String url_image_zoom = "api/products";
    public static final String url_prices_history = "api/preciohistorico/prehistory";
    public static final String url_documents_history = "api/consultaproforma/historialproforma";
    public static final String url_documents_history_detail = "api/consultaproformadetalle";
    public static final String url_enviar_datos_correo = "api/correo/enviarcorreo";
    public static final String url_obtener_url_documento = "api/Whatsapp/enviowhatsapp";
    public static final String url_get_virtual_stock = "api/Stock/stockVirtual";
    public static final String url_check_imei_on_server = "api/IMEI/consultarIMEI";


    public static final String rubro_aluminio_label = "Aluminio";
    public static final String rubro_aluminio = "00";
    public static final String rubro_vidrio_label = "Vidrio";
    public static final String rubro_vidrio = "01";
    public static final String rubro_accesorio_label = "Accesorio";
    public static final String rubro_accesorio = "03";
    public static final String rubro_plastico_label = "Plastico";
    public static final String rubro_plastico = "04";
    public static final String rubro_todos_label = "Todos";
    public static final String rubro_todos = "99";


    public static final String tipoDoc_factura_label = "Factura";
    //public static final String tipoDoc_factura = "00";
    public static final String tipoDoc_factura = "1";
    public static final String tipoDoc_proforma_label = "Proforma";
    public static final String tipoDoc_proforma = "2";
    public static final String tipoDoc_preventa_label = "Preventa";
    public static final String tipoDoc_preventa = "3";


    public static final String estadoDoc_todos_label = "Todos";
    public static final String estadoDoc_todos = "0";
    public static final String estadoDoc_aceptados_label = "Aceptados";
    public static final String estadoDoc_aceptados = "1";
    public static final String estadoDoc_pendientes_label = "Pendientes";
    public static final String estadoDoc_pendientes = "2";
    public static final String estadoDoc_rechazados_label = "Rechazados";
    public static final String estadoDoc_rechazados = "3";


    //para comparar los precios
    public static final String comparar_esMayor = "esMayor";
    public static final String comparar_esMenor = "esMenor";
    public static final String comparar_esIgual = "esIgual";


    public static final String idClienteHeader = "idCliente";
    public static final String idRubroHeader = "idRubro";
    public static final String idUsuarioHeader = "idUsuario";
    public static final String idSobregiroHeader = "Sobregiro";
    public static final String idTipoDocHeader = "TipoDoc";
    public static final String idArticuloHeader = "idArticulo";
    public static final String idRubroDocHeader = "rubro";
    public static final String idEstadoDocHeader = "estado_doc";
    public static final String idFechaInicioDocHeader = "fecini";
    public static final String idFechaFinDocHeader = "fecfin";
    public static final String correosDestinatariosHeader = "idCorreoDestinatarios";
    public static final String correoAsuntoHeader = "Asunto";
    public static final String correoCuerpoHeader = "Cuerpo";
    public static final String correoAdjuntoHeader = "Adjunto";
    public static final String idDocumentoHeader = "idproforma";
    public static final String imeiHeader = "serie";

    public static final String montoTotalMayorALineaDeCredito = "1";
    public static final String montoTotalMenorOIgualALineaDeCredito = "0";
    public static final String dentroDeLineaDeCredito = "Dentro de la Linea de Credito";
    public static final String superaLineaDeCredito = "Supera Linea de Credito";


    public static final String fragmentTagClientes = "Clientes";
    public static final String fragmentTagProductos = "Productos";
    public static final String fragmentTagHistorial = "Historial";


    public static final String round_two_decimals = "%.2f";
    public static final String round_three_decimals = "%.3f";

    public static final String todosLosClientes = "Todos";
    public static final String todosLosClientesId = "0";

    // en cuanto incrementara o disminuira el precio en el dialogo de cambio de precios
    public static final Double baseDouble = 0.001;
    // en cuanto incrementara o disminuira la cantidad en el dialogo de cambio de cantidad de productos
    public static final int baseInteger = 1;

    public static final String imeiResultOK = "1";
}
