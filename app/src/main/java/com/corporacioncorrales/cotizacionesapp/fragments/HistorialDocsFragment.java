package com.corporacioncorrales.cotizacionesapp.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.adapters.DocumentsAdapter;
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
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

    public HistorialDocsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_historial_docs, container, false);
        ButterKnife.bind(this, view);

        Common.setActionBarTitle(getActivity(), Constants.fragmentTagHistorial);
        Singleton sg = Singleton.getInstance();
        mainProgressBar = ((MainActivity) getActivity()).mProgressBar;
        documentsArrayList = new ArrayList<>();
        formatToShow = new SimpleDateFormat("dd/MM/yyyy");
        formatToSend = new SimpleDateFormat("yyyyMMdd"); //20161015

        initDates();
        initSpinnerEstadosDoc();
        initSpinnerRubroDoc();

        getDocumentsHistory(sg.getUserCode(),
                "0",  //sg.getIdclientSelected()
                Constants.rubro_todos,
                Constants.estadoDoc_todos,
                "20160701",
                "20161015");

        /*getDocumentsHistory("5",
                "0",
                Constants.rubro_todos,
                Constants.estadoDoc_todos,
                "20160701",
                "20160931");*/

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
                        Log.d(Constants.log_arrow, getActivity().getString(R.string.no_se_encontraron_docs));
                        Common.showToastMessage(getActivity(), getActivity().getString(R.string.no_se_encontraron_docs));
                    }
                    mainProgressBar.setVisibility(View.GONE);
                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DocumentsResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                mainProgressBar.setVisibility(View.GONE);
            }
        });

    }

    @OnClick(R.id.btnFiltrarDocumentos)
    public void OnClickFiltrarDocumentos() {
        Log.d(Constants.log_arrow, String.format("Fecha inicial: %s, Fecha final: %s, Estado documento: %s, Rubro: %s", fechaInicial, fechaFinal, estadoDocSeleccionado, rubroSeleccionado));
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
                fechaInicial = formatToSend.format(new Date(year, monthOfYear, dayOfMonth));
                tvFechaInicial.setText(formatToShow.format(new Date(year, monthOfYear, dayOfMonth)));
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
                //tvFechaFinal.setText(fechaFinal);

                //fechaFinal = formatToSend.format(new Date(year, monthOfYear, dayOfMonth));
                //tvFechaFinal.setText(formatToShow.format(new Date(year, monthOfYear, dayOfMonth)));

                tvFechaFinal.setText(formatDate(year, monthOfYear, dayOfMonth));
            }
        }, mYearFinal, mMonthFinal, mDayFinal);

        datePickerFechaFinalDialog.setCancelable(false);
        datePickerFechaFinalDialog.show();
    }

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(date);
    }

    private String getCurrentDateToSend() {
        String currentDate = Constants.Empty;
        try {
            Date curDate = new Date();
            currentDate = formatToSend.format(curDate);

        } catch (Exception e) {
            Log.e(Constants.log_arrow_error, e.toString());
        }
        return currentDate;
    }

    private String getCurrentDateToShow() {
        String currentDate = Constants.Empty;
        try {
            Date curDate = new Date();
            currentDate = formatToShow.format(curDate);

        } catch (Exception e) {
            Log.e(Constants.log_arrow_error, e.toString());
        }
        return currentDate;
    }

    private String getCurrentDate1() {
        int day, month, year;
        String currentDate = Constants.Empty;
        try {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            currentDate = day + "/" + (month + 1) + "/" + year;

        } catch (Exception e) {
            Log.e(Constants.log_arrow_error, e.toString());
        }
        return currentDate;
    }

    private void initDates() {
        fechaInicial = getCurrentDateToShow();
        fechaFinal = getCurrentDateToShow();
        tvFechaInicial.setText(fechaInicial);
        tvFechaFinal.setText(fechaFinal);
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
