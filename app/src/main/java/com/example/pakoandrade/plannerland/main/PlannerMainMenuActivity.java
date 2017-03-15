package com.example.pakoandrade.plannerland.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.main.fragments.PedaFragment;
import com.example.pakoandrade.plannerland.main.fragments.layout_about;
import com.example.pakoandrade.plannerland.main.fragments.layout_calendario;
import com.example.pakoandrade.plannerland.main.fragments.layout_chat;
import com.example.pakoandrade.plannerland.main.fragments.layout_configuracion;
import com.example.pakoandrade.plannerland.main.fragments.layout_habilidades;
import com.example.pakoandrade.plannerland.main.fragments.layout_membresia;
import com.example.pakoandrade.plannerland.main.fragments.layout_publicidad;
import com.example.pakoandrade.plannerland.registro.LoginActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class PlannerMainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MaterialSearchView materialSearchView;
    TextView tvUserName,tvUserEmail;
    AlertDialog alert = null;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view2);
        materialSearchView.setVoiceSearch(true); //or false

        setFragment(0);


        // MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.search_view);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Intent i = new Intent(PlannerMainMenuActivity.this,SearchMapActivity.class);
                i.putExtra("busqueda",query);
                startActivity(i);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        tvUserName = (TextView) hView.findViewById(R.id.tvUserName);
        tvUserEmail = (TextView) hView.findViewById(R.id.tvUserEmail);

        SharedPreferences usuario = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String name = usuario.getString("nombre","");
        String email = usuario.getString("PID","");
        String habilities = usuario.getString("habilidades","");
        if (habilities != ""){
            alertSucess();
        }
        tvUserName.setText(name);
        tvUserEmail.setText(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (materialSearchView.isSearchOpen()){
            materialSearchView.closeSearch();
            return;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backCount = fragmentManager.getBackStackEntryCount();

        if (backCount > 0){
           // navigationView.setCheckedItem(R.id.item_navigation_drawer_calendar);
            navigationView.getMenu().getItem(0).setChecked(true);
            setFragment(0);

        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.planner_main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_navigation_drawer_calendar) {
            // Handle the camera action
            setFragment(0);
        } else if (id == R.id.item_navigation_drawer_chat) {
            setFragment(1);
        } else if (id == R.id.item_navigation_drawer_membership) {
            setFragment(2);
        } else if (id == R.id.item_navigation_drawer_habilidades) {
            setFragment(3);
        } else if (id == R.id.item_navigation_drawer_publicidad) {
            setFragment(4);
        } else if (id == R.id.item_navigation_drawer_conditionterms) {
            //setFragment(5);
        }else if (id == R.id.item_navigation_drawer_settings) {
            setFragment(6);
        }else if (id == R.id.item_navigation_drawer_aboutme) {
            setFragment(7);
        }else if (id == R.id.item_navigation_drawer_logout) {
            SharedPreferences usuario = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = usuario.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(PlannerMainMenuActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Metodo por el medio del cual extraemos los fragmest de sus clases java
     */
    public void setFragment(int position) {

        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_calendario calendario = new layout_calendario();
                fragmentTransaction.replace(R.id.ejemplo_fragment, calendario);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_chat chat = new layout_chat();
                fragmentTransaction.replace(R.id.ejemplo_fragment, chat);
                fragmentTransaction.addToBackStack(String.valueOf(1));
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_membresia membresia = new layout_membresia();
                fragmentTransaction.replace(R.id.ejemplo_fragment, membresia);
                fragmentTransaction.addToBackStack(String.valueOf(2));
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_habilidades habilidades = new layout_habilidades();
                fragmentTransaction.replace(R.id.ejemplo_fragment, habilidades);
                fragmentTransaction.addToBackStack(String.valueOf(3));
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_publicidad publicidad = new layout_publicidad();
                fragmentTransaction.replace(R.id.ejemplo_fragment, publicidad);
                fragmentTransaction.addToBackStack(String.valueOf(4));
                fragmentTransaction.commit();
                break;
          /*  case 5:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_terminos terminos = new layout_terminos();
                fragmentTransaction.replace(R.id.ejemplo_fragment, terminos);
                fragmentTransaction.addToBackStack(String.valueOf(5));
                fragmentTransaction.commit();
                break;*/
            case 5:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PedaFragment peda = new PedaFragment();
                fragmentTransaction.replace(R.id.ejemplo_fragment, peda);
                fragmentTransaction.addToBackStack(String.valueOf(5));
                fragmentTransaction.commit();
                break;
            case 6:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_configuracion configuracion = new layout_configuracion();
                fragmentTransaction.replace(R.id.ejemplo_fragment, configuracion);
                fragmentTransaction.addToBackStack(String.valueOf(6));
                fragmentTransaction.commit();
                break;
            case 7:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_about about = new layout_about();
                fragmentTransaction.replace(R.id.ejemplo_fragment, about);
                fragmentTransaction.addToBackStack(String.valueOf(7));
                fragmentTransaction.commit();
                break;

        }
    }

    private void alertSucess() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aun no llenas tus habilidades")
                .setMessage("Recuerda que con tus habilidades llenas, vendes mas")
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Llenar Ahora", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        navigationView.getMenu().getItem(3).setChecked(true);
                        setFragment(3);
                    }
                }).setNegativeButton("Llenar Despues", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert = builder.create();
        alert.show();

    }
}
