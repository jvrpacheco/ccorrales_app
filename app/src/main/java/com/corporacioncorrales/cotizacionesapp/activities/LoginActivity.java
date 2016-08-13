package com.corporacioncorrales.cotizacionesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;
import com.corporacioncorrales.cotizacionesapp.networking.LoginApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.etLoginUser)
    EditText etLoginUser;
    @BindView(R.id.etLoginClave)
    EditText etLoginClave;
    @BindView(R.id.btnLoginIngresar)
    Button btnLoginIngresar;
    @BindView(R.id.progressBarLogin)
    ProgressBar progressBarLogin;

    private String TAG = getClass().getCanonicalName();
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressBarLogin.setScaleY(.2f);
        progressBarLogin.setScaleX(.2f);
        progressBarLogin.setVisibility(View.GONE);

        /*progressBarLogin = (ProgressBar)findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.VISIBLE);
        progressBarLogin.setScaleY(.2f);
        progressBarLogin.setScaleX(.2f);*/

        etLoginUser.setText("jsalazar");
        etLoginClave.setText("123");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setUser(Constants.Empty);
        Log.d(getString(R.string.log_arrow) + TAG, "\"" + Singleton.getInstance().getUser() + "\"");
    }

    @OnClick(R.id.btnLoginIngresar)
    public void onClick() {



        user = etLoginUser.getText().toString().trim();
        String password = etLoginClave.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi request = retrofit.create(LoginApi.class);

        if (!user.isEmpty()) {
            progressBarLogin.setVisibility(View.VISIBLE);
            Call<LoginResponse> call = request.getUserAccess(user);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response != null) {
                        Log.d(getString(R.string.log_arrow_response), response.body().getCodUsu());

                        if(response.body().getCodUsu().equals(Constants.allowToEnterToApp)) {

                            Singleton.getInstance().setUser(user);
                            Log.d(getString(R.string.log_arrow) + TAG + " User admitido", Singleton.getInstance().getUser());
                            Common.showToastMessage(getApplicationContext(), "Bienvenido " + user + "!");
                            enterToApp(user);
                            progressBarLogin.setVisibility(View.GONE);

                        } else {
                            Log.d(getString(R.string.log_arrow) + TAG + " User no admitido", Singleton.getInstance().getUser());
                            Common.showToastMessage(getApplicationContext(), user + " no admitido!");
                            progressBarLogin.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d(getString(R.string.log_arrow_response), "response null");
                        progressBarLogin.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d(getString(R.string.log_arrow_failure), t.toString());
                    progressBarLogin.setVisibility(View.GONE);
                }
            });

        } else {
            Common.showToastMessage(getApplicationContext(), getString(R.string.msg_enter_user));
        }

    }

    private void enterToApp(String user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userFromLogin",user);
        startActivity(intent);
    }

}
