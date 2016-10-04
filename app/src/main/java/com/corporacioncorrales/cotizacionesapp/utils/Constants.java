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

    public static final String rubro_aluminio_label = "Aluminio";
    public static final String rubro_aluminio = "00";
    public static final String rubro_vidrio_label = "Vidrio";
    public static final String rubro_vidrio = "01";
    public static final String rubro_accesorio_label = "Accesorio";
    public static final String rubro_accesorio = "03";
    public static final String rubro_plastico_label = "Plastico";
    public static final String rubro_plastico = "04";
    public static final String rubro_todos = "99";

    public static final String tipoDoc_factura_label = "Factura";
    //public static final String tipoDoc_factura = "00";
    public static final String tipoDoc_factura = "1";
    public static final String tipoDoc_proforma_label = "Proforma";
    public static final String tipoDoc_proforma = "2";
    public static final String tipoDoc_preventa_label = "Preventa";
    public static final String tipoDoc_preventa = "3";

    public static final String estadoDoc_pendientes = "1";
    public static final String estadoDoc_aprobados = "2";
    public static final String estadoDoc_rechazados = "3";
    public static final String estadoDoc_todos = "0";

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

    public static final String montoTotalMayorALineaDeCredito = "1";
    public static final String montoTotalMenorOIgualALineaDeCredito = "0";

    public static final String dentroDeLineaDeCredito = "Dentro de la Linea de Credito";
    public static final String superaLineaDeCredito = "Supera Linea de Credito";

    public static final String fragmentTagClientes = "Clientes";
    public static final String fragmentTagProductos = "Productos";
    public static final String fragmentTagHistorial = "Historial";

    public static final String round_two_decimals = "%.2f";
    public static final String round_three_decimals = "%.3f";

}
