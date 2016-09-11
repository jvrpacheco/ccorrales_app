package com.corporacioncorrales.cotizacionesapp.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

    public static void hideKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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

    public static void setActionBarTitle(Activity activity, String title) {

        android.support.v7.app.ActionBar mActionBar=((AppCompatActivity)activity).getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }
}
