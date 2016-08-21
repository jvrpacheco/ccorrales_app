package com.corporacioncorrales.cotizacionesapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.adapters.ProductsAdapter;
import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ProductsApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    private ProgressBar mainProgressBar;
    private String client_id;
    private String client_razonSocial;
    private ArrayList<ProductsResponse> productsArrayList = new ArrayList<ProductsResponse>();
    private ProductsAdapter productsAdapter;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainProgressBar = ((MainActivity) getActivity()).mProgressBar;

        //solo cambiara el valor cuando viene de clientes
        Bundle args = getArguments();
        if (args != null && args.containsKey("cliente_id") && args.containsKey("cliente_razonSocial")) {

            //tvCliente.setText(args.getString("cliente_razonSocial")); //butterKnife load in onCreateView
            client_id = args.getString("cliente_id");
            client_razonSocial = args.getString("cliente_razonSocial");

            //loadProductsPerClient(client_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);

        tvCliente.setText(client_razonSocial);
        //VALIDAR PARA QUE SOLO CARGUE EN ONCREATE Y CUANDO EL CLIENTID EXISTA
        loadProductsPerClient(client_id);

        return view;
    }

    private void loadProductsPerClient(String idClient) {

        recyclerViewProductos.setHasFixedSize(true);
        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi request = retrofit.create(ProductsApi.class);
        Call<ArrayList<ProductsResponse>> call = request.getProductsPerClient("124896", "00");

        call.enqueue(new Callback<ArrayList<ProductsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductsResponse>> call, Response<ArrayList<ProductsResponse>> response) {

                if(response != null) {
                    productsArrayList.clear();
                    productsArrayList = response.body();

                    if(productsArrayList.size()>0) {

                        productsAdapter = new ProductsAdapter(getActivity(), productsArrayList);
                        //productsAdapter.notifyDataSetChanged();
                        recyclerViewProductos.setAdapter(productsAdapter);

                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        recyclerViewProductos.setLayoutManager(llm);

                    } else {
                        Log.d(getString(R.string.log_arrow_response), "No se encontraron productos para este cliente");
                        Common.showToastMessage(getActivity(), "No se encontraron productos para este cliente");
                    }
                    mainProgressBar.setVisibility(View.GONE);

                } else {
                    Log.d(getString(R.string.log_arrow_response), "response null");
                    mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductsResponse>> call, Throwable t) {
                Log.d(getString(R.string.log_arrow_failure), t.getMessage());
                mainProgressBar.setVisibility(View.GONE);
            }
        });

    }

}
