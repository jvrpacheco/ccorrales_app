package com.corporacioncorrales.cotizacionesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.LoginRequest;
import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;
import com.corporacioncorrales.cotizacionesapp.networking.LoginApi;
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
        /*String user = etLoginUser.getText().toString();
        String password = etLoginClave.getText().toString();

        if(!user.isEmpty() && !password.isEmpty()) {
            if(user.equals("admin") && password.equals("admin")) {
                Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Usuario y/o clave incorrectos!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Ingrese usuario y clave", Toast.LENGTH_LONG).show();
        }*/

        //http://190.81.34.42:8080/api/usuario/spacheco

        String user = etLoginUser.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.server_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi request = retrofit.create(LoginApi.class);

        if(!user.isEmpty()) {
            retrofit2.Call<LoginResponse> call = request.getUserAccess(user.trim());

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(retrofit2.Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d("--------> response", response.body().getCodUsu());

                }

                @Override
                public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                    Log.d("--------> failure", t.getLocalizedMessage());
                }
            });
        } else {
            Toast.makeText(this, "Ingrese usuario", Toast.LENGTH_LONG).show();
        }


    }

}
