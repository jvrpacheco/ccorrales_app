package com.corporacioncorrales.cotizacionesapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ClientsFragment;
import com.corporacioncorrales.cotizacionesapp.fragments.HistorialDocsFragment;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.QuotationProductRequest;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity
        /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private String TAG = getClass().getCanonicalName();
    private boolean viewIsAtHome;
    public NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    String titleClientes = "Clientes";
    String titleHistorial = "Historial";


    //@BindView(R.id.xxprecio)
   // Button xxprecio;
   // @OnClick(R.id.xxprecio)
   // public void onClick(){
     //   Common.showToastMessage(null,"Bienvenido!");
    //}


    @BindView(R.id.progressBar) public ProgressBar mProgressBar;
    //public Dialog mOverlayDialog;
    //private String userFromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(Constants.log_arrow + TAG, "ONCREATE");

        ButterKnife.bind(this);

        mProgressBar.setScaleY(.2f);
        mProgressBar.setScaleX(.2f);




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        fab.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        selectDrawerItem(navigationView.getMenu().getItem(0));
    }





    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @SuppressWarnings("StatementWithEmptyBody")
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //avoid to select "Cerrar Sesion" option if select not close session
                        if(((MenuItem) menuItem).getTitle().equals(getString(R.string.navigationview_cerrar_sesion))) {
                            selectDrawerItem(menuItem);
                            return false;
                        }

                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    @Override
    public void onBackPressed() {
        Log.d("----", "onBackPressed MainActivity1");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

            if(fragment instanceof ProductsFragment) {
                showConfirmationAlertDialog(fragment,
                        getString(R.string.app_name),
                        "Esta a punto de abandonar su orden.",
                        "Abandonar",
                        "Continuar");
            } else if(fragment instanceof ClientsFragment) {
                showCloseSessionAlertDialog(fragment,
                        getString(R.string.app_name),
                        "Desea cerrar sesion?",
                        "Continuar",
                        "Cancelar");
            } else if(fragment instanceof HistorialDocsFragment) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new ClientsFragment());
                ft.commit();
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
        Log.d(Constants.log_arrow + " Usuario", Singleton.getInstance().getUser());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        String fragmentTag = "";
        String title = getString(R.string.app_name);

        switch (menuItem.getItemId()) {
            case R.id.nav_clients:
                fragment = new ClientsFragment();
                title  = titleClientes;
                fragmentTag = Constants.fragmentTagClientes;
                menuItem.setChecked(true);
                break;

            case R.id.nav_manage:
                fragment = new HistorialDocsFragment();
                title = titleHistorial;
                fragmentTag = Constants.fragmentTagHistorial;
                menuItem.setChecked(true);
                break;

            case R.id.nav_close_session:
                showCloseSessionFromMenuAlertDialog(fragment,
                        getString(R.string.app_name),
                        "Desea cerrar sesion?",
                        "Continuar",
                        "Cancelar");
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(fragmentTag);
            ft.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    private void showConfirmationAlertDialog(final Fragment fragment, final String title, final String message, final String textBtnOk, String textBtnCancelar) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(textBtnOk,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        MainActivity.super.onBackPressed();
                        /*if(fragment instanceof ProductsFragment) {
                            navigationView.getMenu().getItem(0).setChecked(true);
                        }*/
                    }
                });
        builder.setNegativeButton(textBtnCancelar,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showCloseSessionAlertDialog(final Fragment fragment, final String title, final String message, final String textBtnOk, String textBtnCancelar) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(textBtnOk,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        finish();
                    }
                });
        builder.setNegativeButton(textBtnCancelar,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showCloseSessionFromMenuAlertDialog(final Fragment fragment, final String title, final String message, final String textBtnOk, String textBtnCancelar) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(textBtnOk,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        goToLogin();
                    }
                });
        builder.setNegativeButton(textBtnCancelar,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });

        //navigationView.getMenu().getItem(3).setChecked(true);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void goToLogin() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Constants.log_arrow + TAG, "onDestroy");
    }
}
