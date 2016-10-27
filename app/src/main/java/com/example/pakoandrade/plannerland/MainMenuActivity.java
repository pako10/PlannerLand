package com.example.pakoandrade.plannerland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pakoandrade.plannerland.main.fragments.layout_about;
import com.example.pakoandrade.plannerland.main.fragments.layout_calendario;
import com.example.pakoandrade.plannerland.main.fragments.layout_chat;
import com.example.pakoandrade.plannerland.main.fragments.layout_configuracion;
import com.example.pakoandrade.plannerland.main.fragments.layout_habilidades;
import com.example.pakoandrade.plannerland.main.fragments.layout_membresia;
import com.example.pakoandrade.plannerland.main.fragments.layout_publicidad;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvUser;
    TextView tvEmail;

    private EditText edtSeach;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //setContentView(R.layout.nav_header_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //tvEmail = (TextView) findViewById(R.id.tvEmail);
        //tvUser = (TextView) findViewById(R.id.tvUser);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        tvUser = (TextView) hView.findViewById(R.id.tvEmail);
        tvEmail = (TextView) hView.findViewById(R.id.tvUser);

        setFragment(0);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra!=null){
            String user = (String) extra.get("user");
            String email = (String) extra.get("correo");
            Integer tipo = (Integer) extra.get("tipo");
            if (tipo.equals(1)){
                setFragment(7);
            }else{
                setFragment(0);
            }
            tvUser.setText(email);
            tvEmail.setText(user);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
        //Use the void handleMenuSearch for make a search
            case R.id.accion_busqueda:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.item_navigation_drawer_calendar:
                item.setChecked(true);
                setFragment(0);
                break;

            case R.id.item_navigation_drawer_chat:
                item.setChecked(true);
                setFragment(1);
                break;
            case R.id.item_navigation_drawer_membership:
                item.setChecked(true);
                setFragment(2);
                break;
            case R.id.item_navigation_drawer_habilidades:
                item.setChecked(true);
                setFragment(3);
                break;
            case R.id.item_navigation_drawer_publicidad:
                item.setChecked(true);
                setFragment(4);
                break;
            case R.id.item_navigation_drawer_conditionterms:
                item.setChecked(true);
               // setFragment(5);
                break;
            case R.id.item_navigation_drawer_settings:
                item.setChecked(true);
                setFragment(6);
                break;
            case R.id.item_navigation_drawer_aboutme:
                item.setChecked(true);
                setFragment(7);
                break;

            case R.id.item_navigation_drawer_logout:
                item.setChecked(true);
                setFragment(8);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_24dp));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //START THE SEARCH
                        return true;
                    }

                    return true;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_clear_black_24dp));

            isSearchOpened = true;
        }
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
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_membresia membresia = new layout_membresia();
                fragmentTransaction.replace(R.id.ejemplo_fragment, membresia);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_habilidades habilidades = new layout_habilidades();
                fragmentTransaction.replace(R.id.ejemplo_fragment, habilidades);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_publicidad publicidad = new layout_publicidad();
                fragmentTransaction.replace(R.id.ejemplo_fragment, publicidad);
                fragmentTransaction.commit();
                break;
         /*   case 5:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_terminos terminos = new layout_terminos();
                fragmentTransaction.replace(R.id.ejemplo_fragment, terminos);
                fragmentTransaction.commit();
                break;*/
            case 6:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_configuracion configuracion = new layout_configuracion();
                fragmentTransaction.replace(R.id.ejemplo_fragment, configuracion);
                fragmentTransaction.commit();
                break;
            case 7:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                layout_about about = new layout_about();
                fragmentTransaction.replace(R.id.ejemplo_fragment, about);
                fragmentTransaction.commit();
                break;

        }
    }

}
