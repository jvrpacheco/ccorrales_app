package com.corporacioncorrales.cotizacionesapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    @BindView(R.id.btnEnviarCotizacion)
    Button btnEnviarCotizacion;
    @BindView(R.id.tvTotalProductos)
    TextView tvTotalProductos;
    @BindView(R.id.spTipoDoc)
    Spinner spTipoDoc;

    private String TAG = getClass().getCanonicalName();
    private ProgressBar mainProgressBar;
    private Boolean fromOnCreate;
    private String client_id;
    private String client_razonSocial;
    private ArrayList<ProductsResponse> productsArrayList;
    public static ProductsAdapter productsAdapter;
    private QuotationAdapter quotationAdapter;
    private String idCliente;
    private String tipoDocSelected;

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
        if (args != null && args.containsKey("cliente_id") && args.containsKey("cliente_razonSocial")) {
            //tvCliente.setText(args.getString("cliente_razonSocial")); //butterKnife load in onCreateView
            client_id = args.getString("cliente_id");
            client_razonSocial = args.getString("cliente_razonSocial");
        }

        idCliente = "124896";
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

        if (fromOnCreate) {
            initSpinnerDocType();
            createQuotation();
            loadProductsPerClient(client_id);
            fromOnCreate = false;
        }
    }

    private void createQuotation() {
        rvQuotation.setHasFixedSize(true);
        quotationAdapter = new QuotationAdapter(getActivity(), new ArrayList<ProductsResponse>());
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

    @OnClick(R.id.btnEnviarCotizacion)
    public void OnClick() {

        if (quotationAdapter != null && quotationAdapter.getItemCount() > 0) {

            ArrayList<ProductsResponse> productsSelected = quotationAdapter.getQuotationProductsList();
            ArrayList<QuotationProductRequest> dataToSend = new ArrayList<>();

            for (int i = 0; i < productsSelected.size(); i++) {
                ProductsResponse productSelected = productsSelected.get(i);
                QuotationProductRequest productToSend = new QuotationProductRequest();
                productToSend.setArticulo(productSelected.getId());
                productToSend.setCantidad(productSelected.getCantidadSolicitada());
                productToSend.setPrecio_real(productSelected.getPrecio());
                productToSend.setPrecio(productSelected.getNuevoPrecio());
                dataToSend.add(productToSend);
            }

            sendQuotation(client_id,
                    Singleton.getInstance().getRubroSelected(),
                    Singleton.getInstance().getUserCode(),
                    dataToSend);

        } else {
            Common.showToastMessage(getActivity(), "Por favor, agregue productos a la Cotizacion.");
        }
    }

    private void sendQuotation(String idCliente, String idRubro, String idUsuario, ArrayList<QuotationProductRequest> data) {

        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuotationApi request = retrofit.create(QuotationApi.class);

        Call<String> call = request.sendQuotation(idCliente, idRubro, idUsuario, data);
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
                if (item.equals(Constants.tipoDoc_proforma_label)) {
                    tipoDocSelected = Constants.tipoDoc_proforma;
                } else if (item.equals(Constants.tipoDoc_factura_label)) {
                    tipoDocSelected = Constants.tipoDoc_factura;
                }

                Singleton.getInstance().setRubroSelected(tipoDocSelected);
                Log.d(Constants.log_arrow + TAG, "tipoDocSelected: " + tipoDocSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
