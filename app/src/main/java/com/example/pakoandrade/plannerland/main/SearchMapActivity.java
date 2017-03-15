package com.example.pakoandrade.plannerland.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.objects.UserPlanner;
import com.example.pakoandrade.plannerland.objects.UserPlannerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pakoAndrade on 24/01/17.
 */

public class SearchMapActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private Marker mMarker;


    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    List<UserPlanner> items;

    String nombre;
    String n;
    String habilidades;
    String lat;
    String lon;
    String latitud;
    String longitud;

    MaterialSearchView searchView;


    /**variables para geolocalizacion**/
    GoogleApiClient apiClient;
    private static final String LOGTAG = "android-localizacion";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    AlertDialog alert = null;
    LocationManager locationManager;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapS);
        mapFragment.getMapAsync(this);




        // Inicializar Animes
        items = new ArrayList<>();

/*
        items.add(new UserPlanner(R.drawable.pokemon, "Welcome to the NHK","","","",""));
        items.add(new UserPlanner(R.drawable.pokemon, "Welcome to the NHK","","","",""));
        items.add(new UserPlanner(R.drawable.pokemon, "Welcome to the NHK","","","",""));
        items.add(new UserPlanner(R.drawable.pokemon,"","","","", "Suzumiya Haruhi"));*/
        Bundle bundle = getIntent().getExtras();
        String query = bundle.getString("busqueda");


        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new UserPlannerAdapter(this,items);
        recycler.setAdapter(adapter);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true); //or false

        // MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        /** LOCALIZACION DEL USUARIO**/

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /****Mejora****/
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        }
        /********/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        search(query);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 15);
                        // mMap.animateCamera(zoom);
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                        mMap.animateCamera(zoom);
                    }
                }, 300);

                return false;
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void search(String query){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        /** CADA VEZ QUE MANDAMOS DATOS AL WS SE REVISA SI ESTA ACTIVADO EL GPS PARA MANDAR LAT Y LNG*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation =
                LocationServices.FusedLocationApi.getLastLocation(apiClient);
        updateUI(lastLocation);



        params.add("pBusqueda",query);
        params.add("pID","0");
        params.add("Lat", latitud);
        params.add("Lon", longitud);


        Toast.makeText(this, params.toString(), Toast.LENGTH_SHORT).show();

        //http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/BucarPID?pBusqueda="desarrollador"&pID="0"&Lat="0"&Lon="0"
        client.get("http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/BucarPID?pBusqueda=",params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                items.clear();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    nombre = String.valueOf(jsonObject.get("d"));
                    JSONArray jsonArray = new JSONArray(nombre);
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        n = jsonObject1.optString("Nombre");
                        habilidades = jsonObject1.optString("HabilidadesDescripcion");
                        lat = jsonObject1.optString("Latitud");
                        lon = jsonObject1.optString("Longitud");
                        items.add(new UserPlanner(R.drawable.logotipo, n, habilidades,lat,lon,"true"));// mandamos true porque ya es usuario registrado
                        Double lati = Double.valueOf(lat);
                        Double lng = Double.valueOf(lon);
                        agregarMarcador(lati,lng,n);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                adapter.notifyDataSetChanged();
            }
        });
    }


    public void agregarMarcador(double lat, double lon,String name) {
        LatLng coordenadas = new LatLng(lat, lon);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 10f);

        mMarker = mMap.addMarker(new MarkerOptions().position(coordenadas).title(name).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(miUbicacion);
    }
    @Override
    public void onBackPressed(){
        float zoom = mMap.getCameraPosition().zoom;

        if (zoom > 10f) {
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null);
        }else{
            super.onBackPressed();
        }
    }

    /**CONECTION FOR UBICATION **/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
//Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }


    //actualizar latitud y longitud
    private void updateUI(Location loc) {
        if (loc != null) {
            latitud = String.valueOf(loc.getLatitude());
            longitud =  String.valueOf(loc.getLongitude());
        } else {
            latitud = String.valueOf(0);
            longitud = String.valueOf(0);
        }
    }

    //dialogo para solicitar acceso al gps
    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema necesita su ubicacion GPS, ¿Desea activarlo?")
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();

    }
}
