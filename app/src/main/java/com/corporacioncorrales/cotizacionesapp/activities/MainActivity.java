package com.corporacioncorrales.cotizacionesapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ClientsFragment;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = getClass().getCanonicalName();

    @BindView(R.id.progressBar) public ProgressBar mProgressBar;
    //public Dialog mOverlayDialog;
    //private String userFromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mProgressBar.setScaleY(.2f);
        mProgressBar.setScaleX(.2f);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //select default fragment
        selectItemFromMenu(navigationView, 0);
    }

    @Override
    public void onBackPressed() {
        Log.d("----", "onBackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //Singleton.getInstance().setUser(Constants.Empty);
        }
        Log.d(Constants.log_arrow + TAG + " User", Singleton.getInstance().getUser());
    }

    private void selectItemFromMenu(NavigationView nv, int itemId) {
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("userFromLogin") != null) {
                Bundle bundle=new Bundle();
                bundle.putString("userFromLogin", extras.getString("userFromLogin"));
                fragment.setArguments(bundle);
            }
        }*/

        //Seleccionar por defecto el item indicado del menu
        nv.getMenu().getItem(itemId).setChecked(true);
        //Mostrar la vista del primer item seleccionado del menu
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ClientsFragment()).commit();
        //setActionBarTitle(getString(R.string.category_clients));
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        int id = item.getItemId();
        String title = getString(R.string.app_name);

        switch (id) {
            case R.id.nav_clients:
                fragment = new ClientsFragment();
                title = getString(R.string.category_clients);
                break;
            /*case R.id.nav_products:
                fragment = new ProductsFragment();
                title  = getString(R.string.category_products);
                break;
            case R.id.nav_slideshow:
                //fragmentClass = ThirdFragment.class;
                break;*/
            case R.id.nav_manage:
                //fragmentClass = ThirdFragment.class;
                break;
            case R.id.nav_share:
                //fragmentClass = ThirdFragment.class;
                break;
            case R.id.nav_send:
                //fragmentClass = ThirdFragment.class;
                break;
            default:
                fragment = new ClientsFragment();
                title = "Clientes - Default";
                break;
        }

        try {
            // Insert the fragment by replacing any existing fragment
            if (fragment != null) {
                /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, fragment);
                ft.commit();*/

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack("asd");
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setActionBarTitle(title);
        Common.setActionBarTitle(this, title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*private void setActionBarTitle(String title) {
        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }*/
}
