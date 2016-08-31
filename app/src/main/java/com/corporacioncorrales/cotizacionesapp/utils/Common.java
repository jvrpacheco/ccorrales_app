package com.corporacioncorrales.cotizacionesapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by victor on 8/10/16.
 */
public class Common {

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean isOnline(final Activity activity) {
        final ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    // el primero es... respecto al segundo
    public static String comparePrices(Double d1, Double d2) {

        String result;
        int retval = Double.compare(d1, d2);

        if(retval > 0) {
            Log.d(Constants.log_arrow, "d1 is greater than d2");
            result = Constants.comparar_esMayor;
        }
        else if(retval < 0) {
            Log.d(Constants.log_arrow, "d1 is less than d2");
            result = Constants.comparar_esMenor;
        }
        else {
            Log.d(Constants.log_arrow, "d1 is equal to d2");
            result = Constants.comparar_esIgual;
        }

        return result;
    }
}
