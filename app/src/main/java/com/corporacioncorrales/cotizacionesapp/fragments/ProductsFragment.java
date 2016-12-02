package com.corporacioncorrales.cotizacionesapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.adapters.ProductsAdapter;
import com.corporacioncorrales.cotizacionesapp.adapters.QuotationAdapter;
import com.corporacioncorrales.cotizacionesapp.model.DocumentDetailResponse;
import com.corporacioncorrales.cotizacionesapp.model.PaymentsResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.model.QuotationProductRequest;
import com.corporacioncorrales.cotizacionesapp.networking.DocumentsApi;
import com.corporacioncorrales.cotizacionesapp.networking.ProductsApi;
import com.corporacioncorrales.cotizacionesapp.networking.QuotationApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    @BindView(R.id.tvCliente)
    TextView tvCliente;
    @BindView(R.id.recyclerViewProductos)
    RecyclerView recyclerViewProductos;
    @BindView(R.id.rvQuotation)
    RecyclerView rvQuotation;
    @BindView(R.id.btnEnviarDocumento)
    Button btnEnviarDocumento;
    @BindView(R.id.tvTotalProductos)
    TextView tvTotalProductos;
    @BindView(R.id.spTipoDoc)
    Spinner spTipoDoc;
    @BindView(R.id.svFilterProduct)
    SearchView svFilterProduct;
    @BindView(R.id.edtGhost)
    EditText edtGhost;
    @BindView(R.id.tvMontoTotal)
    TextView tvMontoTotal;
    @BindView(R.id.tvIndicadorSaldoDisponible)
    TextView tvIndicadorSaldoDisponible;
    @BindView(R.id.productsMainLayout)
    LinearLayout productsMainLayout;
    NavigationView nvMainActivity;
    @BindView(R.id.spFormaPago)
    Spinner spFormaPago;
    @BindView(R.id.btnSelectNumberOfDays)
    Button btnSelectNumberOfDays;
    @BindView(R.id.tvLineaDeCreditoTotalCliente)
    TextView tvLineaDeCreditoTotalCliente;
    @BindView(R.id.tvLineaDeCreditoDisponibleCliente)
    TextView tvLineaDeCreditoDisponibleCliente;


    private String TAG = getClass().getCanonicalName();
    private ProgressBar mainProgressBar;
    private Boolean fromOnCreate;
    private String client_id;
    private String client_razonSocial;
    private String cliente_saldoTotal;
    private String cliente_saldoDisponible;
    private String rubroSeleccionado;
    private String maxDaysFromClient;
    private String daysSelectedFromHistory = Constants.Empty;
    private String idDocumento;
    private String tipoDocumento;
    private String idFormaDePago;
    private String nombreFormaDePago;
    private ArrayList<ProductsResponse> productsArrayList;
    private ArrayList<ProductsResponse> originalProductsArrayList;
    private ArrayList<DocumentDetailResponse> productsFromDocument;
    public static ProductsAdapter productsAdapter;
    private QuotationAdapter quotationAdapter;
    private boolean comeFromHistorial;
    private String numberOfDaysToSend = "1";

    public static ArrayList<ProductsResponse> productsSelectedList;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comeFromHistorial = false;
        fromOnCreate = true;
        mainProgressBar = ((MainActivity) getActivity()).mProgressBar;
        nvMainActivity = ((MainActivity) getActivity()).navigationView;

        productsArrayList = new ArrayList<>();
        productsSelectedList = new ArrayList<>();
        productsFromDocument = new ArrayList<>();

        //get the values only here, not in onResume
        Bundle args = getArguments();
        if (args != null && args.containsKey("cliente_id")
                && args.containsKey("cliente_razonSocial")
                && args.containsKey("cliente_saldoTotal")
                && args.containsKey("cliente_saldoDisponible")
                && args.containsKey("rubroSeleccionado")
                && args.containsKey("maxdias")) {

            //tvCliente.setText(args.getString("cliente_razonSocial")); //butterKnife load in onCreateView
            client_id = args.getString("cliente_id");
            Singleton.getInstance().setIdclientSelected(client_id);
            client_razonSocial = args.getString("cliente_razonSocial");
            cliente_saldoTotal = args.getString("cliente_saldoTotal");
            cliente_saldoDisponible = args.getString("cliente_saldoDisponible");
            rubroSeleccionado = args.getString("rubroSeleccionado");
            maxDaysFromClient = args.getString("maxdias");

            //when coming from HistorialDocsFragment...
            if (args.containsKey("tipoDocumento") && args.containsKey("idDocumento")) {
                tipoDocumento = args.getString("tipoDocumento");
                idDocumento = args.getString("idDocumento");
                idFormaDePago = args.getString("idFormaDePago");
                nombreFormaDePago = args.getString("nombreFormaDePago");
                daysSelectedFromHistory = args.getString("daysSelectedFromHistory");
                comeFromHistorial = true;
            }

            Singleton.getInstance().setSaldoDisponibleCliente(cliente_saldoDisponible);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);
        Common.setActionBarTitle(getActivity(), "Productos");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tvCliente.setText(client_razonSocial);
        tvLineaDeCreditoTotalCliente.setText(cliente_saldoTotal);
        tvLineaDeCreditoDisponibleCliente.setText(cliente_saldoDisponible);
        svFilterProduct.setOnQueryTextListener(productsFilterListener);
        Common.hideKeyboard(getActivity(), edtGhost);

        Common.selectProductOnNavigationView(getActivity(), 0);

        if (fromOnCreate) {
            if (comeFromHistorial) {
                if (Common.isOnline(getActivity())) {
                    initSpinnerFormaPago(idFormaDePago);
                    initSpinnerDocType(tipoDocumento);
                    //rebuildFromQuotation();
                    loadProductsPerClient(client_id, rubroSeleccionado);
                }
            } else {
                if (Common.isOnline(getActivity())) {
                    initSpinnerFormaPago(Constants.idTipoDePagoDeposito);   //SEGUN ESPECIFICACION, 'DEPOSITO' ES EL TIPO DE PAGO POR DEFECTO
                    initSpinnerDocType(Constants.tipoDoc_proforma);
                    createNewQuotation();
                    loadProductsPerClient(client_id, rubroSeleccionado);
                }
            }

            if(!daysSelectedFromHistory.isEmpty()) {  //come from History
                btnSelectNumberOfDays.setText(daysSelectedFromHistory);
            } else {
                btnSelectNumberOfDays.setText(maxDaysFromClient);
            }


            fromOnCreate = false;
        }
    }

    private void getProductsFromDocumentDetail(String idDocumento) {
        final ArrayList<ProductsResponse> productsToSetInQuotation = new ArrayList<>();
        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.url_server).addConverterFactory(GsonConverterFactory.create()).build();
        DocumentsApi request = retrofit.create(DocumentsApi.class);
        Call<ArrayList<DocumentDetailResponse>> call = request.getProductsFromDocumentDetail(idDocumento);

        call.enqueue(new Callback<ArrayList<DocumentDetailResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DocumentDetailResponse>> call, Response<ArrayList<DocumentDetailResponse>> response) {

                if (response != null) {
                    productsFromDocument.clear();
                    productsFromDocument = response.body();

                    if (productsFromDocument != null) {
                        if (productsFromDocument.size() > 0) {  // el detalle siempre contiene productos
                            for (int i = 0; i < productsFromDocument.size(); i++) {
                                DocumentDetailResponse productFromDocument = productsFromDocument.get(i);

                                for (int j = 0; j < originalProductsArrayList.size(); j++) {
                                    ProductsResponse actualProduct = originalProductsArrayList.get(j);

                                    if (actualProduct.getId().trim().equals(productFromDocument.getIdProducto().trim())) {
                                        //actualProduct.setPrecio(productFromDocument.getPrecioListaActual());  //no es necesario, siempre muestra el actual

                                        //verificar si el precio ingresado por el vendedor es menor al precio inferior actual
                                        String cp = Common.comparePrices(Double.valueOf(productFromDocument.getPrecioIngresadoPorVendedor()), Double.valueOf(actualProduct.getPre_inferior()));
                                        if (cp.equals(Constants.comparar_esMayor) || cp.equals(Constants.comparar_esIgual)) {
                                            actualProduct.setEsPrecioMenorAlLimite(false);
                                        } else if (cp.equals(Constants.comparar_esMenor)) {
                                            actualProduct.setEsPrecioMenorAlLimite(true);
                                        }

                                        //Precio recalculado
                                        actualProduct.setPrecioRecalculado(productFromDocument.getPrecioPorUnidad());

                                        //verificar si cambio el precio de lista actual con el que se uso al momento de generar el documento
                                        if (!productFromDocument.getPrecioListaActual().equals(productFromDocument.getPrecioListaAnterior())) {
                                            //mostrar icono que indica esta diferencia
                                        } else {
                                            //ocultar icono que indica esta diferencia
                                        }

                                        //unidad
                                        if (productFromDocument.getIdUnidad() != null && !productFromDocument.getIdUnidad().isEmpty()) {
                                            actualProduct.setNuevaUnidad(productFromDocument.getIdUnidad());
                                        } else {
                                            //actualProduct.setNuevaUnidad(actualProduct.getIdUnidad());
                                        }

                                        if (productFromDocument.getNombreUnidad() != null && !productFromDocument.getNombreUnidad().isEmpty()) {
                                            actualProduct.setNuevaPresentacion(productFromDocument.getNombreUnidad());
                                        }

                                        if (productFromDocument.getStockUnidad() != null && !productFromDocument.getStockUnidad().isEmpty()) {
                                            actualProduct.setNuevaCantidad(productFromDocument.getStockUnidad());
                                        }

                                        //Cantidad Solicitada
                                        actualProduct.setCantidadSolicitada(productFromDocument.getCantidadSolicitada());

                                        actualProduct.setSelected(true);
                                        productsToSetInQuotation.add(actualProduct);
                                        //break;
                                    }
                                }

                            }

                            if (productsToSetInQuotation.size() > 0) {
                                //1.
                                rebuildFromQuotation(productsToSetInQuotation);
                                //2.
                                productsAdapter = new ProductsAdapter(getActivity(), productsArrayList, quotationAdapter, mainProgressBar);
                                recyclerViewProductos.setAdapter(productsAdapter);
                                StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                                recyclerViewProductos.setLayoutManager(sgm);
                                //3.
                                quotationAdapter.refreshItems();
                            }

                        }

                    } else {
                        Common.showToastMessage(getActivity(), "Error en el servidor");
                        mainProgressBar.setVisibility(View.GONE);
                    }

                } else {
                    Log.e(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(getActivity(), "Error en el servidor");
                    mainProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<DocumentDetailResponse>> call, Throwable t) {
                Log.e(Constants.log_arrow_failure, t.toString());
                mainProgressBar.setVisibility(View.GONE);
                Common.showToastMessage(getActivity(), "Error en el servidor");
            }
        });
    }

    private void createNewQuotation() {
        rvQuotation.setHasFixedSize(true);
        quotationAdapter = new QuotationAdapter(getActivity(), new ArrayList<ProductsResponse>(), tvTotalProductos, tvMontoTotal, tvIndicadorSaldoDisponible, mainProgressBar);
        rvQuotation.setAdapter(quotationAdapter);
        LinearLayoutManager sgm = new LinearLayoutManager(getActivity());
        rvQuotation.setLayoutManager(sgm);
    }

    private void rebuildFromQuotation(ArrayList<ProductsResponse> quotationList) {
        rvQuotation.setHasFixedSize(true);
        quotationAdapter = new QuotationAdapter(getActivity(), quotationList, tvTotalProductos, tvMontoTotal, tvIndicadorSaldoDisponible, mainProgressBar);
        rvQuotation.setAdapter(quotationAdapter);
        LinearLayoutManager sgm = new LinearLayoutManager(getActivity());
        rvQuotation.setLayoutManager(sgm);
    }

    private void loadProductsPerClient(String idClient, String rubroSeleccionado) {
        recyclerViewProductos.setHasFixedSize(true);
        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.url_server).addConverterFactory(GsonConverterFactory.create()).build();

        ProductsApi request = retrofit.create(ProductsApi.class);
        Call<ArrayList<ProductsResponse>> call = request.getProductsPerClient(idClient, rubroSeleccionado);

        call.enqueue(new Callback<ArrayList<ProductsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductsResponse>> call, Response<ArrayList<ProductsResponse>> response) {

                if (response != null) {
                    productsArrayList.clear();
                    productsArrayList = response.body();

                    if(productsArrayList!= null && productsArrayList.size()>0) {
                        if (productsArrayList.size() > 0) {
                            //guardando primera descarga de productos
                            originalProductsArrayList = productsArrayList;

                            if (comeFromHistorial) {
                                if (Common.isOnline(getActivity())) {
                                    getProductsFromDocumentDetail(idDocumento.trim());
                                }
                            } else {
                                productsAdapter = new ProductsAdapter(getActivity(), productsArrayList, quotationAdapter, mainProgressBar);
                                recyclerViewProductos.setAdapter(productsAdapter);
                                StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                                recyclerViewProductos.setLayoutManager(sgm);
                            }

                        } else {
                            Log.d(Constants.log_arrow_response, "No se encontraron productos para este cliente");
                            Common.showToastMessage(getActivity(), "No se encontraron productos para este cliente");
                        }
                    } else {
                        Log.d(Constants.log_arrow, "loadProductsPerClient - Error al consultar la data.");
                        Common.showToastMessage(getActivity(), "Error al consultar la data.");
                    }

                    mainProgressBar.setVisibility(View.GONE);
                } else {
                    Log.d(Constants.log_arrow_response, "loadProductsPerClient - response null");
                    Common.showToastMessage(getActivity(), "Error en el servidor");
                    mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductsResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                mainProgressBar.setVisibility(View.GONE);
                Common.showToastMessage(getActivity(), "Error en el servidor");
            }
        });
    }

    int nroDiasIngresado = -1;
    @OnClick(R.id.btnSelectNumberOfDays)
    public void onClick() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_number_of_days);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnClose);
        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final EditText edtNumberOfDays = (EditText) dialog.findViewById(R.id.edtNumberOfDays);
        final TextView tvMsgMaxDias = (TextView) dialog.findViewById(R.id.tvMsgMaxDias);
        final TextView tvMaxOfDays = (TextView) dialog.findViewById(R.id.tvMaxOfDays);

        edtNumberOfDays.setText(numberOfDaysToSend);
        //tvMaxOfDays.setText(getActivity().getResources().getString(R.string.nro_dias_dentro_del_rango));
        //tvMaxOfDays.setTextColor(ContextCompat.getColor(getActivity(), R.color.verde));

        tvMsgMaxDias.setText(String.format("%s %s.", getActivity().getResources().getString(R.string.nro_dias_maximo), maxDaysFromClient));
        ////tvMaxOfDays.setText("");

        edtNumberOfDays.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String daysInserted = s.toString();

                // Remove spacing char
                /*if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }*/

                if(!daysInserted.isEmpty() && daysInserted.length() > 0 && !daysInserted.equals(".") && !daysInserted.equals(",")) {
                    if(Integer.valueOf(daysInserted) <= Integer.valueOf(maxDaysFromClient)) {
                        tvMaxOfDays.setText(getActivity().getResources().getString(R.string.nro_dias_dentro_del_rango));
                        tvMaxOfDays.setTextColor(ContextCompat.getColor(getActivity(), R.color.verde));
                        btnAcceptDialog.setEnabled(true);
                        nroDiasIngresado = Integer.valueOf(daysInserted);
                    } else {
                        tvMaxOfDays.setText(getActivity().getResources().getString(R.string.nro_dias_fuera_del_rango));
                        tvMaxOfDays.setTextColor(ContextCompat.getColor(getActivity(), R.color.rojo));
                        btnAcceptDialog.setEnabled(false);
                    }
                } else {
                    tvMaxOfDays.setText(getActivity().getResources().getString(R.string.nro_dias_ingresado_vacio));
                    tvMaxOfDays.setTextColor(ContextCompat.getColor(getActivity(), R.color.rojo));
                    btnAcceptDialog.setEnabled(false);
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfDaysToSend = String.valueOf(nroDiasIngresado);
                btnSelectNumberOfDays.setText(numberOfDaysToSend);
                dialog.dismiss();
            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @OnClick(R.id.btnEnviarDocumento)
    public void OnClick() {

        if (quotationAdapter != null && quotationAdapter.getItemCount() > 0) {

            ArrayList<ProductsResponse> productsSelected = quotationAdapter.getQuotationProductsList();
            ArrayList<QuotationProductRequest> dataToSend = new ArrayList<>();
            String tipoDocumento = Singleton.getInstance().getTipoDocumento();
            String labelTipoDocumento = Constants.Empty;
            Boolean rebasaSaldoDisponible = isUpToCreditLine(tvMontoTotal.getText().toString(), cliente_saldoDisponible) ? true : false;

            if (tipoDocumento.equals(Constants.tipoDoc_factura)) {
                labelTipoDocumento = Constants.tipoDoc_factura_label;
            } else if (tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                labelTipoDocumento = Constants.tipoDoc_proforma_label;
            } else if (tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                labelTipoDocumento = Constants.tipoDoc_preventa_label;
            }

            if (tipoDocumento.equals(Constants.tipoDoc_factura)) {
                if (rebasaSaldoDisponible) {
                    Common.showAlertDialogMessage(
                            labelTipoDocumento,
                            String.format("%s", "El saldo disponible no es suficiente para el monto total solicitado."),
                            getActivity());
                    return;
                }
            }

            for (int i = 0; i < productsSelected.size(); i++) {
                ProductsResponse productSelected = productsSelected.get(i);
                if (tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                    if (Integer.valueOf(productSelected.getNuevaCantidad()) > 0) {   //cantidad por unidad
                        if (Integer.valueOf(productSelected.getCantidadSolicitada()) > 0) {
                            if (Integer.valueOf(productSelected.getNuevaCantidad()) >= Integer.valueOf(productSelected.getCantidadSolicitada())) {
                                QuotationProductRequest productToSend = new QuotationProductRequest();
                                productToSend.setArticulo(productSelected.getId());
                                productToSend.setCantidad(productSelected.getCantidadSolicitada());
                                productToSend.setPrecio_real(productSelected.getPrecioProductResponse());
                                productToSend.setPrecio(productSelected.getNuevoPrecio());
                                productToSend.setMas_bajo_que_limite(productSelected.getEsPrecioMenorAlLimite() ? "1" : "0");
                                productToSend.setIdUnidad(productSelected.getNuevaUnidad());
                                dataToSend.add(productToSend);
                            } else {
                                Common.showAlertDialogMessage(
                                        labelTipoDocumento,
                                        String.format("%s %s %s.", "La cantidad solicitada del producto", productSelected.getId().trim(), "debe ser menor o igual al stock disponible"),
                                        getActivity()
                                );
                            }
                        } else {
                            Common.showAlertDialogMessage(
                                    labelTipoDocumento,
                                    String.format("%s %s.", "Por favor ingrese la cantidad a solicitar del producto con código", productSelected.getId().trim()),
                                    getActivity());
                            return;
                        }
                    } else {
                        Common.showAlertDialogMessage(
                                labelTipoDocumento,
                                String.format("%s %s, %s", "Por favor retire el producto con código", productSelected.getId().trim(), "porque no cuenta con stock."),
                                getActivity());
                        return;
                    }

                } else if (tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                    if (Integer.valueOf(productSelected.getCantidadSolicitada()) > 0) {
                        QuotationProductRequest productToSend = new QuotationProductRequest();
                        productToSend.setArticulo(productSelected.getId());
                        productToSend.setCantidad(productSelected.getCantidadSolicitada());
                        productToSend.setPrecio_real(productSelected.getPrecioProductResponse());
                        productToSend.setPrecio(productSelected.getNuevoPrecio());
                        productToSend.setMas_bajo_que_limite(productSelected.getEsPrecioMenorAlLimite() ? "1" : "0");
                        productToSend.setIdUnidad(productSelected.getNuevaUnidad());
                        dataToSend.add(productToSend);
                    } else {
                        Common.showAlertDialogMessage(
                                labelTipoDocumento,
                                String.format("%s %s.", "Por favor ingrese la cantidad a solicitar del producto con código", productSelected.getId().trim()),
                                getActivity());
                        return;
                    }
                }
            }

            if (dataToSend.size() > 0) {
                showConfirmationToSendDocumentAlertDialog(getActivity().getString(R.string.app_name),
                        String.format("%s %s %s", "¿Desea enviar la", labelTipoDocumento, "?"),
                        "Enviar",
                        "Cancelar",
                        dataToSend);
            }

        } else {
            Common.showAlertDialogMessage1(String.format("Por favor, agregue algún producto."), getActivity());
        }
    }

    SearchView.OnQueryTextListener productsFilterListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {

            ArrayList<ProductsResponse> filteredProductsList = new ArrayList<>();

            if (!query.isEmpty()) {
                if (originalProductsArrayList != null && originalProductsArrayList.size() > 0) {
                    for (int i = 0; i < originalProductsArrayList.size(); i++) {
                        final String productId = originalProductsArrayList.get(i).getId().toLowerCase();
                        final String productName = originalProductsArrayList.get(i).getNombre().toLowerCase();
                        // Filter using id or product name
                        if (productId.contains(query.toLowerCase()) || productName.contains(query.toLowerCase())) {
                            filteredProductsList.add(originalProductsArrayList.get(i));
                        }
                    }
                }
            } else {
                filteredProductsList = originalProductsArrayList;
            }

            productsAdapter = new ProductsAdapter(getActivity(), filteredProductsList, quotationAdapter, mainProgressBar);
            recyclerViewProductos.setAdapter(productsAdapter);
            StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerViewProductos.setLayoutManager(sgm);
            productsAdapter.notifyDataSetChanged();
            return false;
        }
    };

    private void showConfirmationToSendDocumentAlertDialog(final String title, final String message, final String textBtnOk, String textBtnCancelar, final ArrayList<QuotationProductRequest> dataToSend) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(textBtnOk,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if (Common.isOnline(getActivity())) {
                            sendQuotation(client_id.trim(),
                                    Singleton.getInstance().getRubroSelected().trim(),
                                    Singleton.getInstance().getUserCode().trim(),
                                    isUpToCreditLine(tvMontoTotal.getText().toString(), cliente_saldoDisponible) ? Constants.montoTotalMayorALineaDeCredito : Constants.montoTotalMenorOIgualALineaDeCredito,
                                    Singleton.getInstance().getTipoDocumento(),
                                    Singleton.getInstance().getIdPaymentTypeSelected(),
                                    tvMontoTotal.getText().toString().trim(),
                                    numberOfDaysToSend,
                                    dataToSend);
                        }
                    }
                });
        builder.setNegativeButton(textBtnCancelar,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sendQuotation(String idCliente, String idRubro, String idUsuario, String sobregiro, String tipoDocumento, String idFormaPago, String montoTotal, String numberOfDays, ArrayList<QuotationProductRequest> data) {
        mainProgressBar.setVisibility(View.VISIBLE);
        productsMainLayout.setEnabled(false);
        btnEnviarDocumento.setEnabled(false);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.url_server).addConverterFactory(GsonConverterFactory.create()).build();
        QuotationApi request = retrofit.create(QuotationApi.class);

        Call<String> call = request.sendQuotation(idCliente, idRubro, idUsuario, sobregiro, tipoDocumento, idFormaPago, montoTotal, numberOfDays, data);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response != null) {
                    String rp = response.body();

                    if (rp != null) {
                        Log.d(Constants.log_arrow_response, rp);
                        Common.showToastMessage(getActivity(), rp);
                        // go to Clients view
                        getFragmentManager().popBackStackImmediate();
                        Common.selectProductOnNavigationView2(1, nvMainActivity);
                    } else {
                        Log.d(Constants.log_arrow_response, "body null:" + response.raw().message());
                        Common.showToastMessage(getActivity(), "Error en el servidor");
                    }

                    mainProgressBar.setVisibility(View.GONE);
                    btnEnviarDocumento.setEnabled(true);
                    productsMainLayout.setEnabled(true);

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(getActivity(), "Error en el servidor");
                    mainProgressBar.setVisibility(View.GONE);
                    btnEnviarDocumento.setEnabled(true);
                    productsMainLayout.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                mainProgressBar.setVisibility(View.GONE);
                btnEnviarDocumento.setEnabled(true);
                productsMainLayout.setEnabled(true);
                Common.showToastMessage(getActivity(), "Error en el servidor");
            }
        });
    }

    private void initSpinnerFormaPago(String idInitialPaymentType) {
        boolean existsIdInitialPaymentType = false;
        int positionInitialPaymentType = -1;
        ArrayList<PaymentsResponse> paymentTypes = Singleton.getInstance().getPaymentTypes();  //para grabar, se valido que tenga al menos un tipo de pago

        if (paymentTypes != null) {
            final ArrayList<String> idPaymentsType = new ArrayList<String>();
            final ArrayList<String> namePaymentsType = new ArrayList<String>();

            for (int i = 0; i < paymentTypes.size(); i++) {
                idPaymentsType.add(paymentTypes.get(i).getIdPaymentType());
                namePaymentsType.add(paymentTypes.get(i).getPaymentType());

                if (paymentTypes.get(i).getIdPaymentType().equals(idInitialPaymentType)) {
                    existsIdInitialPaymentType = true;
                    positionInitialPaymentType = i;
                }
            }

            ArrayAdapter adapterFormaPago = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, namePaymentsType);
            spFormaPago.setAdapter(adapterFormaPago);

            if (existsIdInitialPaymentType && positionInitialPaymentType > 0) {
                spFormaPago.setSelection(positionInitialPaymentType);  //VERIFICAR QUE EXISTA 'DEPOSITO' EN LO QUE TRAJO EL SERVER, DE LO CONTRARIO MOSTRAR EL PRIMERO QUE VENGA
            } else {
                spFormaPago.setSelection(0); //sino encuentra el id inicial en la lista selecciona el primero por defecto
            }

            spFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    for (int j = 0; j < namePaymentsType.size(); j++) {
                        if (namePaymentsType.get(j).equals(adapterView.getItemAtPosition(i).toString())) {

                            if(idPaymentsType.get(j).equals(Constants.idTipoDePagoEfectivo)) {
                                numberOfDaysToSend = "0";
                                btnSelectNumberOfDays.setText(numberOfDaysToSend);
                                btnSelectNumberOfDays.setEnabled(false);

                            } else if(idPaymentsType.get(j).equals(Constants.idTipoDePagoDeposito)) {
                                numberOfDaysToSend = "1";
                                btnSelectNumberOfDays.setText(numberOfDaysToSend);
                                btnSelectNumberOfDays.setEnabled(false);

                            } else {
                                //Luego de la carga del historial, muestro el valor de dias.... si cambio de tipo de pago
                                //debo seguir mostrando ese numero que vino del historial o resetear a 1?
                                if(!daysSelectedFromHistory.isEmpty()) {  //come from History
                                    numberOfDaysToSend = daysSelectedFromHistory;
                                    btnSelectNumberOfDays.setText(daysSelectedFromHistory);
                                    btnSelectNumberOfDays.setEnabled(true);
                                } else {
                                    numberOfDaysToSend = "1";
                                    btnSelectNumberOfDays.setText(numberOfDaysToSend);
                                    btnSelectNumberOfDays.setEnabled(true);
                                }

                            }

                            Singleton.getInstance().setIdPaymentTypeSelected(idPaymentsType.get(i));
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private void initSpinnerDocType(String initialValue) {
        ArrayAdapter adapterRubroType = ArrayAdapter.createFromResource(getActivity(), R.array.array_tipos_doc, android.R.layout.simple_list_item_1);
        spTipoDoc.setAdapter(adapterRubroType);

        if (comeFromHistorial) {
            //set the spinner value only when come from Historial
            if (initialValue != null && !initialValue.isEmpty()) {
                if (initialValue.equals(Constants.tipoDoc_factura)) {
                    spTipoDoc.setSelection(0);
                } else if (initialValue.equals(Constants.tipoDoc_proforma)) {
                    spTipoDoc.setSelection(1);
                } else if (initialValue.equals(Constants.tipoDoc_preventa)) {
                    spTipoDoc.setSelection(2);
                }
            }
        } else {
            //selecciona por defecto "Proforma"
            spTipoDoc.setSelection(1);
        }

        spTipoDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals(Constants.tipoDoc_factura_label)) {
                    Singleton.getInstance().setTipoDocumento(Constants.tipoDoc_factura);
                } else if (item.equals(Constants.tipoDoc_proforma_label)) {
                    Singleton.getInstance().setTipoDocumento(Constants.tipoDoc_proforma);
                } else if (item.equals(Constants.tipoDoc_preventa_label)) {
                    Singleton.getInstance().setTipoDocumento(Constants.tipoDoc_preventa);
                }

                //quotationAdapter is not null after load products
                if (quotationAdapter != null) {
                    //quotationAdapter.resetProducts();
                    quotationAdapter.refreshItems();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Boolean isUpToCreditLine(String montoTotal, String saldoDisponible) {
        Boolean upToCreditLine = false;
        try {
            Double difference = Double.valueOf(saldoDisponible) - Double.valueOf(montoTotal);
            if (difference < 0) {
                upToCreditLine = true;
            }
        } catch (Exception ex) {
            Log.e(Constants.log_arrow, ex.toString());
        }
        return upToCreditLine;
    }

    boolean containsProduct(ArrayList<ProductsResponse> list, String idProductoFromDocument) {
        for (ProductsResponse item : list) {
            if (item.getId().equals(idProductoFromDocument)) {
                return true;
            }
        }
        return false;
    }


}
