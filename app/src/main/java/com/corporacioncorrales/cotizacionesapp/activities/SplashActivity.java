package com.corporacioncorrales.cotizacionesapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.ImeiResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ImeiApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////setContentView(R.layout.activity_splash);

        useVerifyImei(true);
    }

    private void useVerifyImei(boolean useImei) {
        if(useImei) {
            /*String deviceId = Common.getDeviceId(SplashActivity.this);
            if(!deviceId.isEmpty()) {
                checkDeviceOnServer(deviceId);
            }*/
            //String x = Build.SERIAL;

            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            if(!android_id.isEmpty()) {
                checkDeviceOnServer(android_id);
            }

        } else {
            goToLogin();
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkDeviceOnServer(String deviceId) {

        //progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImeiApi request = retrofit.create(ImeiApi.class);
        Call<ArrayList<ImeiResponse>> call = request.checkDeviceOnServer(deviceId);

        call.enqueue(new Callback<ArrayList<ImeiResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ImeiResponse>> call, Response<ArrayList<ImeiResponse>> response) {
                if(response != null) {

                    ArrayList<ImeiResponse> imeiResponseList = response.body();
                    if(imeiResponseList!=null) {
                        if(imeiResponseList.size() > 0) {
                            if(imeiResponseList.get(0).getSerie().equals(Constants.imeiResultOK)) {
                                goToLogin();
                            } else {
                                showDenyPermissionMessage(SplashActivity.this, "Lo sentimos, Ud. no tiene permiso para usar CorralesApp. Hasta luego.");  //http://stackoverflow.com/questions/5796611/dialog-throwing-unable-to-add-window-token-null-is-not-for-an-application-wi
                            }
                        }
                    }
                    //progressBar.setVisibility(View.GONE);
                } else {
                    Log.e(Constants.log_arrow_response, "response null");
                    //progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImeiResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                //progressBar.setVisibility(View.GONE);
                Common.showToastMessage(getApplicationContext(), t.toString());
            }
        });
    }

    private void showDenyPermissionMessage(Context context, final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getText(R.string.accept),
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                        //finish();
                        finishAffinity();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
