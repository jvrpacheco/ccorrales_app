package com.corporacioncorrales.cotizacionesapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.adapters.ClientsAdapter;
import com.corporacioncorrales.cotizacionesapp.adapters.DocumentsAdapter;
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.DocumentsApi;
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
public class HistorialDocsFragment extends Fragment {

    @BindView(R.id.rvDocumentsHistory)
    RecyclerView rvDocumentsHistory;
    private ProgressBar mainProgressBar;
    private ArrayList<DocumentsResponse> documentsArrayList;
    private DocumentsAdapter documentsAdapter;

    public HistorialDocsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainProgressBar = ((MainActivity) getActivity()).mProgressBar;
        documentsArrayList = new ArrayList<>();

        //getDocumentsHistory("01", Constants.rubro_vidrio);
        getDocumentsHistory("224", "01");

        View view = inflater.inflate(R.layout.fragment_historial_docs, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    private void getDocumentsHistory(String id, String rubro) {
        mainProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DocumentsApi request = retrofit.create(DocumentsApi.class);
        Call<ArrayList<DocumentsResponse>> call = request.getDocumentsHistory(id, rubro);

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
}
