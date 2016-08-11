package com.corporacioncorrales.cotizacionesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;
import com.corporacioncorrales.cotizacionesapp.networking.LoginApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.etLoginUser) EditText etLoginUser;
    @BindView(R.id.etLoginClave) EditText etLoginClave;
    @BindView(R.id.btnLoginIngresar) Button btnLoginIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        etLoginUser.setText("spacheco");
        etLoginClave.setText("admin");
    }

    @OnClick(R.id.btnLoginIngresar)
    public void onClick() {

        final String user = etLoginUser.getText().toString();
        String password = etLoginClave.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi request = retrofit.create(LoginApi.class);

        if(!user.isEmpty()) {

            retrofit2.Call<LoginResponse> call = request.getUserAccess(user.trim());

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(retrofit2.Call<LoginResponse> call, Response<LoginResponse> response) {

                    if(response != null) {
                        Log.d(getString(R.string.log_arrow_response), response.body().getCodUsu());
                        Common.showToastMessage(getApplicationContext(), "Bienvenido " + user.trim() + "!");
                        enterToApp();
                    } else {
                        Log.d(getString(R.string.log_arrow_response), response.body().getCodUsu());
                        Common.showToastMessage(getApplicationContext(), "Usuario incorrecto!");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                    Log.d(getString(R.string.log_arrow_failure), t.getLocalizedMessage());
                }
            });
        } else {
            Common.showToastMessage(getApplicationContext(), "Ingrese usuario!");
        }

    }

    private void enterToApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
