package com.corporacioncorrales.cotizacionesapp.utils;

/**
 * Created by victor on 8/10/16.
 */
public class Constants {

    public static final String allowEnterToApp = "True";
    public static final String denyEnterToApp = "False";

    public static final String log_arrow = "--------> ";
    public static final String log_arrow_response = "--------> RESPONSE";
    public static final String log_arrow_failure = "--------> FAILURE";

    public static final String url_server = "http://190.81.34.42:8080/";
    public static final String url_user_login = "api/usuario/{user}";
    public static final String url_clients_per_user = "api/clientes";
    public static final String url_products_per_user = "api/articulos";
    //public static final String url_send_quotation = "api/products/createproducts";
    public static final String url_send_quotation = "api/grabarproforma/createproducts";
    public static final String url_image_zoom = "api/products";
    public static final String url_prices_history = "api/preciohistorico/prehistory";

    public static final String rubro_aluminio_label = "Aluminio";
    public static final String rubro_aluminio = "00";
    public static final String rubro_vidrio_label = "Vidrio";
    public static final String rubro_vidrio = "01";
    public static final String rubro_accesorio_label = "Accesorio";
    public static final String rubro_accesorio = "03";
    public static final String rubro_plastico_label = "Plastico";
    public static final String rubro_plastico = "04";

    public static final String tipoDoc_proforma_label = "Proforma";
    public static final String tipoDoc_proforma = "00";
    public static final String tipoDoc_factura_label = "Factura";
    public static final String tipoDoc_factura = "01";

    //para comparar los precios
    public static final String comparar_esMayor = "esMayor";
    public static final String comparar_esMenor = "esMenor";
    public static final String comparar_esIgual = "esIgual";

    public static final String idClienteHeader = "idCliente";
    public static final String idRubroHeader = "idRubro";
    public static final String idUsuarioHeader = "idUsuario";
    public static final String idArticuloHeader = "idArticulo";

    public static final String Empty = "";
}
