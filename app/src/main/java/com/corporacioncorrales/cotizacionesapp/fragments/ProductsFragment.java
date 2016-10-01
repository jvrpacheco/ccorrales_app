package com.corporacioncorrales.cotizacionesapp.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.adapters.ProductsAdapter;
import com.corporacioncorrales.cotizacionesapp.adapters.QuotationAdapter;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.model.QuotationProductRequest;
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
    @BindView(R.id.tvLineaDeCreditoCliente)
    TextView tvLineaDeCreditoCliente;
    @BindView(R.id.tvIndicadorSaldoDisponible)
    TextView tvIndicadorSaldoDisponible;

    private String TAG = getClass().getCanonicalName();
    private ProgressBar mainProgressBar;
    private Boolean fromOnCreate;
    private String client_id;
    private String client_razonSocial;
    private String cliente_lineaDeCredito;
    private ArrayList<ProductsResponse> productsArrayList;
    private ArrayList<ProductsResponse> originalProductsArrayList;
    public static ProductsAdapter productsAdapter;
    private QuotationAdapter quotationAdapter;
    private String idCliente;

    public static ArrayList<ProductsResponse> productsSelectedList;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fromOnCreate = true;
        mainProgressBar = ((MainActivity) getActivity()).mProgressBar;
        productsArrayList = new ArrayList<>();
        productsSelectedList = new ArrayList<>();

        //solo cambiara el valor cuando viene de clientes
        Bundle args = getArguments();
        if (args != null
                && args.containsKey("cliente_id")
                && args.containsKey("cliente_razonSocial")
                && args.containsKey("cliente_lineaDeCredito")) {

            //tvCliente.setText(args.getString("cliente_razonSocial")); //butterKnife load in onCreateView
            client_id = args.getString("cliente_id");
            client_razonSocial = args.getString("cliente_razonSocial");
            cliente_lineaDeCredito = args.getString("cliente_lineaDeCredito");
            Singleton.getInstance().setLineaDeCreditoCliente(cliente_lineaDeCredito);
        }

        //idCliente = "124896";
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
        tvLineaDeCreditoCliente.setText(cliente_lineaDeCredito);
        svFilterProduct.setOnQueryTextListener(productsFilterListener);

        Common.hideKeyboard(getActivity(), edtGhost);

        if (fromOnCreate) {
            initSpinnerDocType();
            createQuotation();
            loadProductsPerClient(client_id);
            fromOnCreate = false;
        }
    }

    private void createQuotation() {
        rvQuotation.setHasFixedSize(true);
        quotationAdapter = new QuotationAdapter(getActivity(), new ArrayList<ProductsResponse>(), tvTotalProductos, tvMontoTotal, tvIndicadorSaldoDisponible);
        rvQuotation.setAdapter(quotationAdapter);
        LinearLayoutManager sgm = new LinearLayoutManager(getActivity());
        rvQuotation.setLayoutManager(sgm);
    }

    private void loadProductsPerClient(String idClient) {

        recyclerViewProductos.setHasFixedSize(true);
        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi request = retrofit.create(ProductsApi.class);
        Call<ArrayList<ProductsResponse>> call = request.getProductsPerClient(idClient, Singleton.getInstance().getRubroSelected());

        call.enqueue(new Callback<ArrayList<ProductsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductsResponse>> call, Response<ArrayList<ProductsResponse>> response) {

                if (response != null) {
                    productsArrayList.clear();
                    productsArrayList = response.body();

                    if (productsArrayList.size() > 0) {

                        //guardando primera descarga de productos
                        originalProductsArrayList = productsArrayList;

                        productsAdapter = new ProductsAdapter(getActivity(), productsArrayList, quotationAdapter);
                        recyclerViewProductos.setAdapter(productsAdapter);
                        StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        recyclerViewProductos.setLayoutManager(sgm);
                    } else {

                        Log.d(Constants.log_arrow_response, "No se encontraron productos para este cliente");
                        Common.showToastMessage(getActivity(), "No se encontraron productos para este cliente");
                    }
                    mainProgressBar.setVisibility(View.GONE);

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductsResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                mainProgressBar.setVisibility(View.GONE);
                Common.showToastMessage(getActivity(), t.getMessage());
            }
        });

    }

    @OnClick(R.id.btnEnviarDocumento)
    public void OnClick() {

        if (quotationAdapter != null && quotationAdapter.getItemCount() > 0) {

            ArrayList<ProductsResponse> productsSelected = quotationAdapter.getQuotationProductsList();
            ArrayList<QuotationProductRequest> dataToSend = new ArrayList<>();
            String tipoDocumento = Singleton.getInstance().getTipoDocumento();
            String labelTipoDocumento = Constants.Empty;
            Boolean rebasaSaldoDisponible = isUpToCreditLine(tvMontoTotal.getText().toString(), cliente_lineaDeCredito) ? true : false;

            if(tipoDocumento.equals(Constants.tipoDoc_factura)) {
                labelTipoDocumento = Constants.tipoDoc_factura_label;
            } else if(tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                labelTipoDocumento = Constants.tipoDoc_proforma_label;
            } else if(tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                labelTipoDocumento = Constants.tipoDoc_preventa_label;
            }

            if(tipoDocumento.equals(Constants.tipoDoc_factura)) {
                if(rebasaSaldoDisponible) {
                    Common.showAlertDialogMessage(
                            labelTipoDocumento,
                            String.format("%s", "El saldo disponible no es suficiente para el monto total solicitado."),
                            getActivity());
                    return;
                }
            }

            for (int i = 0; i < productsSelected.size(); i++) {
                ProductsResponse productSelected = productsSelected.get(i);
                if(tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                    if(Integer.valueOf(productSelected.getCantidad())>0) {
                        if(Integer.valueOf(productSelected.getCantidadSolicitada())>0) {
                            QuotationProductRequest productToSend = new QuotationProductRequest();
                            productToSend.setArticulo(productSelected.getId());
                            productToSend.setCantidad(productSelected.getCantidadSolicitada());
                            productToSend.setPrecio_real(productSelected.getPrecio());
                            productToSend.setPrecio(productSelected.getNuevoPrecio());
                            dataToSend.add(productToSend);
                        } else {
                            Common.showAlertDialogMessage(
                                    labelTipoDocumento,
                                    String.format("%s %s%s",
                                            "Por favor ingrese la cantidad a solicitar del producto con codigo",
                                            productSelected.getId().trim(),
                                            "."),
                                    getActivity());
                            return;
                        }
                    } else {
                        Common.showAlertDialogMessage(
                                labelTipoDocumento,
                                String.format("%s %s, %s",
                                        "Por favor retire el producto con codigo",
                                        productSelected.getId().trim(),
                                        "porque no cuenta con stock."),
                                getActivity());
                        return;
                    }

                } else if(tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                    if(Integer.valueOf(productSelected.getCantidadSolicitada())>0) {
                        QuotationProductRequest productToSend = new QuotationProductRequest();
                        productToSend.setArticulo(productSelected.getId());
                        productToSend.setCantidad(productSelected.getCantidadSolicitada());
                        productToSend.setPrecio_real(productSelected.getPrecio());
                        productToSend.setPrecio(productSelected.getNuevoPrecio());
                        dataToSend.add(productToSend);
                    } else {
                        Common.showAlertDialogMessage(
                                labelTipoDocumento,
                                String.format("%s %s%s",
                                        "Por favor ingrese la cantidad a solicitar del producto con codigo",
                                        productSelected.getId().trim(),
                                        "."),
                                getActivity());
                        return;
                    }
                }
            }

            if(dataToSend.size()>0){
                showConfirmationToSendDocumentAlertDialog(getActivity().getString(R.string.app_name),
                        String.format("%s %s %s", "Desea enviar la", labelTipoDocumento, "?"),
                        "Enviar",
                        "Cancelar",
                        dataToSend);
            }

        } else {
            Common.showAlertDialogMessage1(
                    String.format("Por favor, agregue algun producto."),
                    getActivity());
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
                        // Filtro por id o por nombre de producto
                        if (productId.contains(query.toLowerCase()) || productName.contains(query.toLowerCase())) {
                            filteredProductsList.add(originalProductsArrayList.get(i));
                        }
                    }
                }
            } else {
                filteredProductsList = originalProductsArrayList;
            }

            productsAdapter = new ProductsAdapter(getActivity(), filteredProductsList, quotationAdapter);
            recyclerViewProductos.setAdapter(productsAdapter);
            StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerViewProductos.setLayoutManager(sgm);
            productsAdapter.notifyDataSetChanged();  // data set changed
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
                            sendQuotation(client_id,
                                    Singleton.getInstance().getRubroSelected(),
                                    Singleton.getInstance().getUserCode(),
                                    isUpToCreditLine(tvMontoTotal.getText().toString(), cliente_lineaDeCredito) ? Constants.montoTotalMayorALineaDeCredito : Constants.montoTotalMenorOIgualALineaDeCredito,
                                    dataToSend);
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

    private void sendQuotation(String idCliente, String idRubro, String idUsuario, String sobregiro, ArrayList<QuotationProductRequest> data) {

        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuotationApi request = retrofit.create(QuotationApi.class);

        Call<String> call = request.sendQuotation(idCliente, idRubro, idUsuario, sobregiro, data);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response != null) {
                    String rp = response.body();
                    Log.d(Constants.log_arrow_response, rp);
                    Common.showToastMessage(getActivity(), rp);
                    mainProgressBar.setVisibility(View.GONE);
                    // go to Clients view
                    getFragmentManager().popBackStackImmediate();
                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                mainProgressBar.setVisibility(View.GONE);
                Common.showToastMessage(getActivity(), t.getMessage());
            }
        });
    }

    private void initSpinnerDocType() {
        ArrayAdapter adapterRubroType = ArrayAdapter.createFromResource(getActivity(), R.array.array_tipos_doc, android.R.layout.simple_list_item_1);
        spTipoDoc.setAdapter(adapterRubroType);

        spTipoDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                if (item.equals(Constants.tipoDoc_factura_label)) {
                    Singleton.getInstance().setTipoDocumento(Constants.tipoDoc_factura);
                    /*quotationAdapter.resetProducts();
                    quotationAdapter.refreshItems();*/
                } else if (item.equals(Constants.tipoDoc_proforma_label)) {
                    Singleton.getInstance().setTipoDocumento(Constants.tipoDoc_proforma);
                    /*quotationAdapter.resetProducts();
                    quotationAdapter.refreshItems();*/
                } else if (item.equals(Constants.tipoDoc_preventa_label)) {
                    Singleton.getInstance().setTipoDocumento(Constants.tipoDoc_preventa);
                    /*quotationAdapter.resetProducts();
                    quotationAdapter.refreshItems();*/
                }
                quotationAdapter.resetProducts();
                quotationAdapter.refreshItems();
                Log.d(Constants.log_arrow + TAG, "Singleton.getInstance().getTipoDocumento(): " + Singleton.getInstance().getTipoDocumento());
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

}
