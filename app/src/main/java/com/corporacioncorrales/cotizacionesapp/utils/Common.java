package com.corporacioncorrales.cotizacionesapp.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.corporacioncorrales.cotizacionesapp.R;

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

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideKeyboardOnDialog(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    public static void showAlertDialogMessage(final String title, final String message, final Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(String.format("%s - %s", context.getResources().getString(R.string.app_name), title));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getText(R.string.accept),
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });


        AlertDialog alert = builder.create();
        //(((FragmentActivity)context)).attatchAlertDialog(alert);
        alert.show();
    }



    public static void showAlertDialogMessage1(final String message, final Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getText(R.string.accept),
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });


        AlertDialog alert = builder.create();
        //(((FragmentActivity)context)).attatchAlertDialog(alert);
        alert.show();
    }

    public static void setActionBarTitle(Activity activity, String title) {

        android.support.v7.app.ActionBar mActionBar=((AppCompatActivity)activity).getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }
}
