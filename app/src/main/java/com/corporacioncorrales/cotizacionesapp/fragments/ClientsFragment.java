package com.corporacioncorrales.cotizacionesapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.adapters.ClientsAdapter;
import com.corporacioncorrales.cotizacionesapp.adapters.ProductsAdapter;
import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ClientsApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String TAG = getClass().getCanonicalName();
    private List<ClientsResponse> clientsList;
    private ArrayList<ClientsResponse> clientsArrayList = new ArrayList<ClientsResponse>();
    private ArrayList<ClientsResponse> originalClientsArrayList;
    private ClientsAdapter clientsAdapter;
    private Dialog mOverlayDialog;
    private String user;
    private Boolean fromOnCreate;

    @BindView(R.id.tvTotalClientes)
    TextView tvTotalClientes;
    @BindView(R.id.spinnerRubro)
    Spinner spRubro;
    @BindView(R.id.recyclerViewClients)
    RecyclerView recyclerViewClients;
    @BindView(R.id.svFilterClient)
    SearchView svFilterClient;
    /*@BindView(R.id.btnRefreshClients)
    Button btnRefreshClients;*/

    //private RecyclerView recyclerViewClients;

    private Dialog mainActivityDialog;
    private ProgressBar mainProgressBar;
    private String rubroSelected;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClientsFragment newInstance(String param1, String param2) {
        ClientsFragment fragment = new ClientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        //test 3
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rubroSelected = Constants.rubro_vidrio; // valor por defecto del rubro en la primera carga de la vista
        fromOnCreate = true;
        mainProgressBar = ((MainActivity) getActivity()).mProgressBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clients, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        Common.setActionBarTitle(getActivity(), "Clientes");
        svFilterClient.setOnQueryTextListener(clientsFilterListener);
        Log.d(Constants.log_arrow + TAG, "onResume, rubroSelected: " + rubroSelected);

        initSpinnerRubro();
        clearClientsFilter();

        if (fromOnCreate) {
            //initSpinnerRubro();
            initViews3();
        } else {
            //initSpinnerRubro();

            // Rebuild recyclerViewClients from first data downloaded from server in onCreate
            if (clientsAdapter != null && clientsArrayList != null && clientsArrayList.size() > 0) {
                updateTotalOfClients(clientsArrayList.size());
                recyclerViewClients.setAdapter(clientsAdapter);
                // Grid
                StaggeredGridLayoutManager mStaggeredGridManager3 = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
                recyclerViewClients.setLayoutManager(mStaggeredGridManager3);
            }
        }
    }

    private void initSpinnerRubro() {
        ArrayAdapter adapterRubroType = ArrayAdapter.createFromResource(getActivity(), R.array.array_rubros, android.R.layout.simple_list_item_1);
        spRubro.setAdapter(adapterRubroType);

        spRubro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (fromOnCreate) {
                    fromOnCreate = false;
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    if (item.equals(Constants.rubro_vidrio_label)) {
                        rubroSelected = Constants.rubro_vidrio;
                        //onResume permite actualizar para verificar si hay nuevos datos de acuerdo al rubro
                    } else if (item.equals(Constants.rubro_aluminio_label)) {
                        rubroSelected = Constants.rubro_aluminio;
                    } else if (item.equals(Constants.rubro_accesorio_label)) {
                        rubroSelected = Constants.rubro_accesorio;
                    } else if (item.equals(Constants.rubro_plastico_label)) {
                        rubroSelected = Constants.rubro_plastico;
                    }
                    initViews3();
                }
                //Common.showToastMessage(getActivity(), rubroSelected + "!");
                Singleton.getInstance().setRubroSelected(rubroSelected);
                Log.d(Constants.log_arrow + TAG, "onCreate, rubroSelected: " + rubroSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViews3() {
        recyclerViewClients.setHasFixedSize(true);
        if (!Singleton.getInstance().getUser().isEmpty()) {
            getClients(Singleton.getInstance().getUser(), rubroSelected);   //getClients("jsalazar", "00");   rubroSelected
            clearClientsFilter();
        }
    }

    private void getClients(String user, String rubro) {
        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClientsApi request = retrofit.create(ClientsApi.class);
        Call<ArrayList<ClientsResponse>> call = request.getClientsPerUser(user, rubro); //("jsalazar", "00");

        call.enqueue(new Callback<ArrayList<ClientsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ClientsResponse>> call, Response<ArrayList<ClientsResponse>> response) {

                if (response != null) {

                    clientsArrayList.clear();
                    clientsArrayList = response.body();

                    if (clientsArrayList.size() > 0) {

                        //guardando primera descarga de clientes
                        originalClientsArrayList = clientsArrayList;

                        updateTotalOfClients(clientsArrayList.size());

                        clientsAdapter = new ClientsAdapter(getActivity(), clientsArrayList);
                        clientsAdapter.notifyDataSetChanged();
                        recyclerViewClients.setAdapter(clientsAdapter);

                        /*LinearLayoutManager – Displays items in a vertical or horizontal scrolling list.
                        GridLayoutManager – Displays items in a grid.
                        StaggeredGridLayoutManager – Displays items in a staggered grid.*/

                        // ListView
                        //layoutManager2 = new LinearLayoutManager(getActivity());
                        //recyclerViewClients.setLayoutManager(layoutManager2);

                        // Grid
                        StaggeredGridLayoutManager mStaggeredGridManager3 = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
                        recyclerViewClients.setLayoutManager(mStaggeredGridManager3);

                        recyclerViewClients.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return false;
                            }

                            @Override
                            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                            }
                        });

                    } else {
                        Log.d(Constants.log_arrow, "No se encontraron clientes para este usuario");
                        Common.showToastMessage(getActivity(), "No se encontraron clientes para este usuario");
                        tvTotalClientes.setText("0");
                    }
                    mainProgressBar.setVisibility(View.GONE);

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    mainProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<ClientsResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                mainProgressBar.setVisibility(View.GONE);
            }
        });

    }

    SearchView.OnQueryTextListener clientsFilterListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {

            ArrayList<ClientsResponse> filteredClientsList = new ArrayList<>();

            if(originalClientsArrayList!= null && originalClientsArrayList.size()>0) {
                if(!query.isEmpty()) {
                    for(int i=0; i<originalClientsArrayList.size(); i++) {
                        final String text = originalClientsArrayList.get(i).getRazon_Social().toLowerCase();
                        if(text.contains(query.toLowerCase())) {
                            filteredClientsList.add(originalClientsArrayList.get(i));
                        }
                    }
                } else {
                    filteredClientsList = originalClientsArrayList;
                }
            }

            updateTotalOfClients(filteredClientsList.size());
            clientsAdapter = new ClientsAdapter(getActivity(), filteredClientsList);
            recyclerViewClients.setAdapter(clientsAdapter);
            StaggeredGridLayoutManager mStaggeredGridManager3 = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
            recyclerViewClients.setLayoutManager(mStaggeredGridManager3);
            clientsAdapter.notifyDataSetChanged();  // data set changed
            return false;
        }
    };

    private void showProgressLoading(Boolean show, ProgressBar mProgressBar) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void updateTotalOfClients(int numberOfClients) {
        tvTotalClientes.setText(Integer.toString(numberOfClients));
    }

    private void clearClientsFilter() {
        if(svFilterClient!=null) {
            svFilterClient.setQuery(Constants.Empty, false);
            svFilterClient.clearFocus();
        }
    }
}
