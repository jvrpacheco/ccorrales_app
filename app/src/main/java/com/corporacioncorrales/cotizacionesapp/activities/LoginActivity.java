package com.corporacioncorrales.cotizacionesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.corporacioncorrales.cotizacionesapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        etLoginUser.setText("admin");
        etLoginClave.setText("admin");
    }

    @OnClick(R.id.btnLoginIngresar)
    public void onClick() {
        String user = etLoginUser.getText().toString();
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
        }
    }

}
