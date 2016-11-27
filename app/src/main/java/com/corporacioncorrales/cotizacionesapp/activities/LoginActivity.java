package com.corporacioncorrales.cotizacionesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;
import com.corporacioncorrales.cotizacionesapp.model.PaymentsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.LoginApi;
import com.corporacioncorrales.cotizacionesapp.networking.PaymentsApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

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
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressBarLogin.setScaleY(.2f);
        progressBarLogin.setScaleX(.2f);
        progressBarLogin.setVisibility(View.GONE);

        etLoginUser.setText("jsalazar");
        etLoginClave.setText("123");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setUser(Constants.Empty);
        Log.d(Constants.log_arrow + TAG, "\"" + Singleton.getInstance().getUser() + "\"");
        enableLoginControls(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnClick(R.id.btnLoginIngresar)
    public void onClick() {

        user = etLoginUser.getText().toString().trim();
        password = etLoginClave.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi request = retrofit.create(LoginApi.class);

        if (!user.isEmpty() && !password.isEmpty()) {

            if(Common.isOnline(this)) {
                progressBarLogin.setVisibility(View.VISIBLE);
                enableLoginControls(false);

                Call<LoginResponse> call = request.getUserAccess(user,password);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response != null) {
                            Log.d(Constants.log_arrow_response, response.body().getCodUsu());

                            if(response.body().getFocoUsu().equals(Constants.allowEnterToApp)) {
                                Singleton.getInstance().setUser(user);
                                Singleton.getInstance().setUserCode(response.body().getCodUsu());
                                Log.d(Constants.log_arrow, response.body().getCodUsu());
                                Log.d(Constants.log_arrow + " admitido", Singleton.getInstance().getUser());

                                getPaymentTypes(progressBarLogin, user);

                            } else {
                                Log.d(Constants.log_arrow + " no admitido", Singleton.getInstance().getUser());
                                Common.showToastMessage(getApplicationContext(), user + " no admitido!");
                                enableLoginControls(true);
                                progressBarLogin.setVisibility(View.GONE);
                            }

                        } else {
                            Log.d(Constants.log_arrow_response, "response null");
                            Common.showToastMessage(LoginActivity.this, "Error en el servidor");
                            enableLoginControls(true);
                            progressBarLogin.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d(Constants.log_arrow_failure, t.toString());
                        Common.showToastMessage(LoginActivity.this, "Error en el servidor");
                        enableLoginControls(true);
                        progressBarLogin.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            Common.showToastMessage(getApplicationContext(), getString(R.string.msg_enter_user_and_password));
        }

    }

    private void getPaymentTypes(final ProgressBar progressBar, final String user) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.url_server).addConverterFactory(GsonConverterFactory.create()).build();
        PaymentsApi request = retrofit.create(PaymentsApi.class);

        Call<ArrayList<PaymentsResponse>> call = request.getPaymentOptions();
        call.enqueue(new Callback<ArrayList<PaymentsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<PaymentsResponse>> call, Response<ArrayList<PaymentsResponse>> response) {
                if(response!=null) {

                    if(response.body()!= null) {
                        if(response.body().size()>0) {
                            //saving Payment Types from server
                            ArrayList<PaymentsResponse> paymentTypes = new ArrayList<PaymentsResponse>(response.body());
                            Singleton.getInstance().setPaymentTypes(paymentTypes);

                            Common.showToastMessage(getApplicationContext(), "Bienvenido " + user + "!");
                            enterToApp(user);

                        } else {
                            Common.showToastMessage(LoginActivity.this, "Error, el servidor no ha enviado algún tipo de pago.");
                        }
                        progressBar.setVisibility(View.GONE);

                    } else {
                        Log.d(Constants.log_arrow, "Error al consultar la data.");
                        Common.showToastMessage(LoginActivity.this, "Error al consultar la data.");
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    Log.e(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(LoginActivity.this, "Error en el servidor");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PaymentsResponse>> call, Throwable t) {
                Log.e(Constants.log_arrow_failure, t.toString());
                Common.showToastMessage(LoginActivity.this, "Error en el servidor");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void savePaymentTypes() {
        HashMap hm = new HashMap();
        hm.put("", "");
    }

    private void enterToApp(String user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userFromLogin", user);
        startActivity(intent);
    }

    private void enableLoginControls(Boolean enable) {
        if(enable) {
            etLoginUser.setEnabled(true);
            etLoginClave.setEnabled(true);
            btnLoginIngresar.setBackgroundColor(ContextCompat.getColor(this, R.color.verde));
            btnLoginIngresar.setEnabled(true);
        } else {
            etLoginUser.setEnabled(false);
            etLoginClave.setEnabled(false);
            btnLoginIngresar.setBackgroundColor(ContextCompat.getColor(this, R.color.gris_fondo));
            btnLoginIngresar.setEnabled(false);
        }
    }

}
