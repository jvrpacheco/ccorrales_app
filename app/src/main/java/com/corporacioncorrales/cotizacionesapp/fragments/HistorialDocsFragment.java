package com.corporacioncorrales.cotizacionesapp.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.adapters.ClientsAdapter;
import com.corporacioncorrales.cotizacionesapp.adapters.ClientsDialogAdapter;
import com.corporacioncorrales.cotizacionesapp.adapters.DocumentsAdapter;
import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ClientsApi;
import com.corporacioncorrales.cotizacionesapp.networking.DocumentsApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
public class HistorialDocsFragment extends Fragment {

    @BindView(R.id.rvDocumentsHistory)
    RecyclerView rvDocumentsHistory;
    @BindView(R.id.rl_btnFechaInicial)
    RelativeLayout rlBtnFechaInicial;
    @BindView(R.id.rl_btnFechaFinal)
    RelativeLayout rlBtnFechaFinal;
    @BindView(R.id.tvFechaInicial)
    TextView tvFechaInicial;
    @BindView(R.id.tvFechaFinal)
    TextView tvFechaFinal;
    @BindView(R.id.spRubroDoc)
    Spinner spRubroDoc;
    @BindView(R.id.spEstadoDoc)
    Spinner spEstadoDoc;
    @BindView(R.id.btnFiltrarDocumentos)
    Button btnFiltrarDocumentos;
    @BindView(R.id.rl_btnListaClientes)
    RelativeLayout rlBtnListaClientes;
    @BindView(R.id.tvClienteSeleccionado)
    TextView tvClienteSeleccionado;
    private ProgressBar mainProgressBar;
    private ArrayList<DocumentsResponse> documentsArrayList;
    private DocumentsAdapter documentsAdapter;
    private int mYearInicial, mMonthInicial, mDayInicial, mYearFinal, mMonthFinal, mDayFinal;
    private String fechaInicial;
    private String fechaFinal;
    private String estadoDocSeleccionado;
    private String rubroSeleccionado;
    private SimpleDateFormat formatToShow;
    private SimpleDateFormat formatToSend;
    private Singleton sg;
    private ArrayList<ClientsResponse> clientsArrayList = new ArrayList<ClientsResponse>();
    private String selectedClientId = Constants.Empty; //
    private boolean existsClients = false;
    private boolean isClientSelectedFromList = false;
    private boolean todosIsChecked = false;
    private ClientsDialogAdapter okClientsAdapter;
    private ArrayList<ClientsResponse> originalClientsArrayList;


    public HistorialDocsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        //existsClients = false;
        //isClientSelected = false;

        Common.selectProductOnNavigationView(getActivity(), 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_historial_docs, container, false);
        ButterKnife.bind(this, view);

        Common.setActionBarTitle(getActivity(), Constants.fragmentTagHistorial);
        sg = Singleton.getInstance();
        mainProgressBar = ((MainActivity) getActivity()).mProgressBar;
        documentsArrayList = new ArrayList<>();
        formatToShow = new SimpleDateFormat("dd/MM/yyyy");
        formatToSend = new SimpleDateFormat("yyyyMMdd"); //20161015

        initDates2();
        initSpinnerEstadosDoc();
        initSpinnerRubroDoc();

        return view;
    }

    private void getDocumentsHistory(String idUsuario, String idCliente, String idRubro, String idEstadoDoc, String fechaInicio, String fechaFin) {
        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DocumentsApi request = retrofit.create(DocumentsApi.class);
        Call<ArrayList<DocumentsResponse>> call = request.getDocumentsHistory(idUsuario,
                idCliente,
                idRubro,
                idEstadoDoc,
                fechaInicio,
                fechaFin);

        call.enqueue(new Callback<ArrayList<DocumentsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DocumentsResponse>> call, Response<ArrayList<DocumentsResponse>> response) {

                if (response != null) {
                    documentsArrayList.clear();
                    documentsArrayList = response.body();
                    if (documentsArrayList.size() > 0) {
                        documentsAdapter = new DocumentsAdapter(getActivity(), documentsArrayList);
                        documentsAdapter.notifyDataSetChanged();
                        rvDocumentsHistory.setAdapter(documentsAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        rvDocumentsHistory.setLayoutManager(linearLayoutManager);
                    } else {
                        documentsAdapter = new DocumentsAdapter(getActivity(), documentsArrayList);
                        documentsAdapter.notifyDataSetChanged();
                        rvDocumentsHistory.setAdapter(documentsAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        rvDocumentsHistory.setLayoutManager(linearLayoutManager);

                        Log.d(Constants.log_arrow, getActivity().getString(R.string.no_se_encontraron_docs));
                        Common.showToastMessage(getActivity(), getActivity().getString(R.string.no_se_encontraron_docs));
                    }
                    mainProgressBar.setVisibility(View.GONE);
                } else {
                    Log.e(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(getActivity(), "Error en el servidor");
                    mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DocumentsResponse>> call, Throwable t) {
                Log.e(Constants.log_arrow_failure, t.toString());
                Common.showToastMessage(getActivity(), "Error en el servidor");
                mainProgressBar.setVisibility(View.GONE);
            }
        });

    }

    @OnClick(R.id.btnFiltrarDocumentos)
    public void OnClickFiltrarDocumentos() {
        Log.d(Constants.log_arrow,
                String.format("Fecha inicial: %s, Fecha final: %s, Estado documento: %s, Rubro: %s",
                        fechaInicial, fechaFinal, estadoDocSeleccionado, rubroSeleccionado));

        if(Common.isOnline(getActivity())) {

            Log.d(Constants.log_arrow, String.format("%s:%s, %s:%s, %s:%s, %s:%s, %s:%s, %s:%s,",
                    "idusuario", sg.getUserCode(),
                    "selectedClientId", selectedClientId.isEmpty() ? "0" : selectedClientId,
                    "rubroSeleccionado", rubroSeleccionado,
                    "estadoDocSeleccionado", estadoDocSeleccionado,
                    "fechaInicial", fechaInicial,
                    "fechaFinal", fechaFinal));

            getDocumentsHistory(sg.getUserCode(),
                    selectedClientId.isEmpty() ? "0" : selectedClientId,  //<-----popup para seleccionar cliente
                    rubroSeleccionado,
                    estadoDocSeleccionado,
                    fechaInicial,
                    fechaFinal);
        }
    }

    @OnClick(R.id.rl_btnFechaInicial)
    public void OnClickFechaInicial() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYearInicial = c.get(Calendar.YEAR);
        mMonthInicial = c.get(Calendar.MONTH);
        mDayInicial = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerFechaInicialDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fechaInicial = getCurrentDate(true, year, monthOfYear, dayOfMonth);
                tvFechaInicial.setText(getCurrentDate(false, year, monthOfYear, dayOfMonth));
            }
        }, mYearInicial, mMonthInicial, mDayInicial);

        datePickerFechaInicialDialog.setCancelable(false);
        datePickerFechaInicialDialog.show();
    }

    @OnClick(R.id.rl_btnFechaFinal)
    public void OnClickFechaFinal() {
        final Calendar c = Calendar.getInstance();
        mYearFinal = c.get(Calendar.YEAR);
        mMonthFinal = c.get(Calendar.MONTH);
        mDayFinal = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerFechaFinalDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //fechaFinal = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                fechaFinal = getCurrentDate(true, year, monthOfYear, dayOfMonth);
                tvFechaFinal.setText(getCurrentDate(false, year, monthOfYear, dayOfMonth));
            }
        }, mYearFinal, mMonthFinal, mDayFinal);

        datePickerFechaFinalDialog.setCancelable(false);
        datePickerFechaFinalDialog.show();
    }

    @OnClick(R.id.rl_btnListaClientes)
    public void OnClickDialogListaClientes() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_historial_clientes);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnClose);
        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final RecyclerView rvDialogClients = (RecyclerView) dialog.findViewById(R.id.rvDialogClients);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.newProgressBar);
        final TextView tvSelectedClient = (TextView) dialog.findViewById(R.id.tvSelectedClient);
        final CheckBox chkTodos = (CheckBox) dialog.findViewById(R.id.chkTodos);
        final SearchView svFilterClient = (SearchView) dialog.findViewById(R.id.svFilterClient);

        chkTodos.setChecked(true);
        todosIsChecked = true;
        svFilterClient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                okClientsAdapter = new ClientsDialogAdapter(getActivity(), filteredClientsList, tvSelectedClient, btnAcceptDialog, chkTodos);
                rvDialogClients.setAdapter(okClientsAdapter);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                rvDialogClients.setLayoutManager(llm);
                okClientsAdapter.notifyDataSetChanged();
                return false;
            }
        });
        tvSelectedClient.setText(Constants.todosLosClientes);

        chkTodos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                todosIsChecked = isChecked ? true : false;

                if(isChecked) {
                    isClientSelectedFromList = false;
                    btnAcceptDialog.setEnabled(true);
                    tvSelectedClient.setText(Constants.todosLosClientes);
                } else {
                    if(okClientsAdapter!=null && okClientsAdapter.getClientSelected()!=null) {
                        isClientSelectedFromList = true;
                        btnAcceptDialog.setEnabled(true);
                    } else {
                        isClientSelectedFromList = false;
                        btnAcceptDialog.setEnabled(false);
                    }
                }
            }
        });

        if(Common.isOnline(getActivity())) {
            getClients(sg.getUser(), Constants.rubro_todos, Constants.orden_nombre, rvDialogClients, progressBar, tvSelectedClient, btnAcceptDialog, chkTodos);
        }

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todosIsChecked) {
                    chkTodos.setChecked(true);
                    selectedClientId = Constants.todosLosClientesId;
                    tvSelectedClient.setText(Constants.todosLosClientes);
                    tvClienteSeleccionado.setText(Constants.todosLosClientes);
                    isClientSelectedFromList = false;
                    dialog.dismiss();
                } else {
                    chkTodos.setChecked(false);
                    if (isClientSelectedFromList) {
                        if (okClientsAdapter != null && okClientsAdapter.getClientSelected() != null) {
                            selectedClientId = okClientsAdapter.getClientSelected().getId();
                            tvClienteSeleccionado.setText(okClientsAdapter.getClientSelected().getRazon_Social());
                        }
                    } else {
                        btnAcceptDialog.setEnabled(false);
                        /*selectedClientId = Constants.todosLosClientesId;
                        tvSelectedClient.setText(Constants.todosLosClientes);
                        tvClienteSeleccionado.setText(Constants.todosLosClientes);*/
                    }
                    dialog.dismiss();
                }
            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Common.hideKeyboardOnDialog(dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getClients(String user, String rubro, String orden, final RecyclerView rvDialogClients, final ProgressBar progressBar, final TextView tvSelectedClient, final Button btnAccept, final CheckBox chkTodos) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClientsApi request = retrofit.create(ClientsApi.class);
        Call<ArrayList<ClientsResponse>> call = request.getClientsPerUser(user, rubro, orden); //("jsalazar", "00");

        call.enqueue(new Callback<ArrayList<ClientsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ClientsResponse>> call, Response<ArrayList<ClientsResponse>> response) {

                if (response != null) {
                    clientsArrayList.clear();
                    clientsArrayList = response.body();

                    if (clientsArrayList.size() > 0) {
                        //guardando primera descarga de clientes
                        originalClientsArrayList = clientsArrayList;

                        existsClients = true;
                        okClientsAdapter = new ClientsDialogAdapter(getActivity(), clientsArrayList, tvSelectedClient, btnAccept, chkTodos);
                        okClientsAdapter.notifyDataSetChanged();
                        rvDialogClients.setAdapter(okClientsAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        rvDialogClients.setLayoutManager(llm);
                        //isClientSelected = true;

                    } else {
                        ClientsDialogAdapter clientsAdapter = new ClientsDialogAdapter(getActivity(), clientsArrayList, tvSelectedClient, btnAccept, chkTodos);
                        clientsAdapter.notifyDataSetChanged();
                        rvDialogClients.setAdapter(clientsAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        rvDialogClients.setLayoutManager(llm);
                        isClientSelectedFromList = false;

                        Log.d(Constants.log_arrow, "No se encontraron clientes");
                        Common.showToastMessage(getActivity(), "No se encontraron clientes");
                        selectedClientId = "-";
                    }
                    progressBar.setVisibility(View.GONE);

                } else {
                    isClientSelectedFromList = false;
                    Log.e(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(getActivity(), "Error en el servidor");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ClientsResponse>> call, Throwable t) {
                isClientSelectedFromList = false;
                Log.e(Constants.log_arrow_failure, t.toString());
                Common.showToastMessage(getActivity(), "Error en el servidor");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private String getCurrentDate(boolean isDateToSend, int year, int month, int day) {
        String currentDate = Constants.Empty;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day);
            Date date = cal.getTime();
            if (isDateToSend) { // date to send
                currentDate = formatToSend.format(date);
            } else { //date to show
                currentDate = formatToShow.format(date);
            }
        } catch (Exception e) {
            Log.e(Constants.log_arrow_error, e.toString());
        }
        return currentDate;
    }

    private void initDates2() {
        //fecha actual
        int day, month, year;
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        fechaInicial = getCurrentDate(true, year, month, 01);
        tvFechaInicial.setText(getCurrentDate(false, year, month, 01));
        fechaFinal = getCurrentDate(true, year, month, day);
        tvFechaFinal.setText(getCurrentDate(false, year, month, day));

        if(Common.isOnline(getActivity())) {
            getDocumentsHistory(sg.getUserCode(),
                    "0",  //Todos los clientes
                    Constants.rubro_todos,
                    Constants.estadoDoc_todos,
                    fechaInicial,
                    fechaFinal);
        }
    }

    private void initSpinnerEstadosDoc() {
        ArrayAdapter adapterRubroType = ArrayAdapter.createFromResource(getActivity(), R.array.array_estados_doc, android.R.layout.simple_list_item_1);
        spEstadoDoc.setAdapter(adapterRubroType);
        spEstadoDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals(Constants.estadoDoc_todos_label)) {
                    estadoDocSeleccionado = Constants.estadoDoc_todos;
                } else if (item.equals(Constants.estadoDoc_aceptados_label)) {
                    estadoDocSeleccionado = Constants.estadoDoc_aceptados;
                } else if (item.equals(Constants.estadoDoc_pendientes_label)) {
                    estadoDocSeleccionado = Constants.estadoDoc_pendientes;
                } else if (item.equals(Constants.estadoDoc_rechazados_label)) {
                    estadoDocSeleccionado = Constants.estadoDoc_rechazados;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spEstadoDoc.setSelection(0, true);
        //spEstadoDoc.setSelection(0, false);

        spEstadoDoc.post(new Runnable() {
            public void run() {
                spEstadoDoc.setSelection(0);
            }
        });
    }

    private void initSpinnerRubroDoc() {
        ArrayAdapter adapterRubroType = ArrayAdapter.createFromResource(getActivity(), R.array.array_rubros_historial, android.R.layout.simple_list_item_1);
        spRubroDoc.setAdapter(adapterRubroType);
        spRubroDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals(Constants.rubro_todos_label)) {
                    rubroSeleccionado = Constants.rubro_todos;
                } else if (item.equals(Constants.rubro_vidrio_label)) {
                    rubroSeleccionado = Constants.rubro_vidrio;
                } else if (item.equals(Constants.rubro_aluminio_label)) {
                    rubroSeleccionado = Constants.rubro_aluminio;
                } else if (item.equals(Constants.rubro_accesorio_label)) {
                    rubroSeleccionado = Constants.rubro_accesorio;
                } else if (item.equals(Constants.rubro_plastico_label)) {
                    rubroSeleccionado = Constants.rubro_plastico;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
