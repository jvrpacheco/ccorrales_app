package com.corporacioncorrales.cotizacionesapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by victor on 8/10/16.
 */
public class Common {

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
