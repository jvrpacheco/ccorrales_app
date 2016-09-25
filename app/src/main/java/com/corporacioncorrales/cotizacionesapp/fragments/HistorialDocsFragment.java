package com.corporacioncorrales.cotizacionesapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corporacioncorrales.cotizacionesapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistorialDocsFragment extends Fragment {


    public HistorialDocsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial_docs, container, false);
    }

}
