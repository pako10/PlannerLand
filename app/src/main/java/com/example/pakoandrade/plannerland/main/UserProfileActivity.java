package com.example.pakoandrade.plannerland.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.registro.LoginActivity;
import com.example.pakoandrade.plannerland.registro.RegistroActivity;
import com.example.pakoandrade.plannerland.utils.Constants;
import com.example.pakoandrade.plannerland.utils.Errors;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class UserProfileActivity extends FragmentActivity implements OnMapReadyCallback {

    boolean showFAB = true;
    AlertDialog alert = null;
    Float lon;
    Float lat;
    String name;
    String userM;
    TextView tvAdress;
    ProgressDialog progressDialog;

    EditText etPass;
    EditText etEmail;
    TextView tvPass;
    EditText edtRecover;

    private GoogleMap mMap;

    private SupportMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);


        Bundle extra = getIntent().getExtras();
        name = extra.getString("nombre");
        String habilidades = extra.getString("habilidades");
        lat = Float.valueOf(extra.getString("latitud"));
        lon = Float.valueOf(extra.getString("longitud"));
        userM = extra.getString("member");


        TextView tvUserName = (TextView) findViewById(R.id.userName);
        TextView tvUserHabilities = (TextView) findViewById(R.id.tvHabilidades);
        tvAdress = (TextView) findViewById(R.id.tvAdress);
        final FloatingActionButton telFab = (FloatingActionButton) findViewById(R.id.tel_fab);
        final FloatingActionButton calFab = (FloatingActionButton) findViewById(R.id.cal_fab);


        if (userM.equals("false")){
            mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.maps));
            mMapFragment.getView().setVisibility(View.INVISIBLE);
        }


        telFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userM.equals("false")) {
                    createLoginDialogo();
                } else {

                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + "4737385437"));

                    if (ActivityCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);


                }
            }
        });

        calFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userM.equals("false")) {
                    createLoginDialogo();
                }else{
                    //mandamos llamar el activity con los calendarios

                }
            }
        });

        //tvUserHabilities.setText(habilidades);
        tvUserName.setText(name);
        getAdress();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);*/


        /**
         * Bottom Sheet
         */

        // To handle FAB animation upon entrance and exit
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_shrink);




        telFab.setVisibility(View.VISIBLE);
        telFab.startAnimation(growAnimation);
        calFab.setVisibility(View.VISIBLE);
        calFab.startAnimation(growAnimation);


        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                telFab.setVisibility(View.GONE);
                calFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.user_coordinator);
        View bottomSheet = coordinatorLayout.findViewById(R.id.gmail_bottom_sheet);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (showFAB)
                            telFab.startAnimation(shrinkAnimation);
                        calFab.startAnimation(shrinkAnimation);
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        showFAB = true;
                        telFab.setVisibility(View.VISIBLE);
                        telFab.startAnimation(growAnimation);
                        calFab.setVisibility(View.VISIBLE);
                        calFab.startAnimation(growAnimation);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        showFAB = false;
                        break;


                }

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog createLoginDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_sign, null);

        builder.setView(v);

        Button signup = (Button) v.findViewById(R.id.crear_boton);
        Button signin = (Button) v.findViewById(R.id.entrar_boton);
        etEmail = (EditText)  v.findViewById(R.id.name_input);
        etPass = (EditText) v.findViewById(R.id.pass_input);
        tvPass = (TextView) v.findViewById(R.id.olvidar_pass);

        tvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();
            }
        });

        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...
                        Intent i = new Intent(UserProfileActivity.this, RegistroActivity.class);
                        startActivity(i);
                    }
                }
        );

        signin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...

                        progressDialog = new ProgressDialog(UserProfileActivity.this);
                        progressDialog.setMessage(getResources().getString(R.string.entering));


                        final String username = etEmail.getText().toString();
                        final String password = etPass.getText().toString();
                        if (!isEmailValid(username)){
                            etEmail.setError(getResources().getString(R.string.emailInvalidFormat));
                            return;
                        }
                        if (password.length()<6){
                            etPass.setError(getResources().getString(R.string.passInvalidFormat));
                            return;
                        }else {
                            progressDialog.show();
                            Login(username, password);
                        }

                    }
                }

        );

        alert = builder.create();
        alert.show();
        return alert;
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
        if (lat != null && lon != null) {
            LatLng sydney = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(sydney).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f));
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }else{}
    }


    public void getAdress(){
        Geocoder geocoder;
        List<Address> yourAddresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        if (lat != null && lon != null) {
            try {
                yourAddresses = geocoder.getFromLocation(lat, lon, 1);
                //yourAddresses = geocoder.getFromLocationName("los alcaldes gregorio ferron 17",1);
                if (yourAddresses.size() > 0) {
                    Double ghu = yourAddresses.get(0).getLatitude();
                    Double longitud = yourAddresses.get(0).getLongitude();
                    String yourAddress = yourAddresses.get(0).getAddressLine(0);
                    String yourCity = yourAddresses.get(0).getAddressLine(1);
                    String yourCountry = yourAddresses.get(0).getAddressLine(2);
                    yourCountry = yourCountry.replace("[0-9]","");
                    tvAdress.setText("Col. " + yourCity + ", " + yourAddress + ", " + yourCountry);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {

        }
    }

    public void Login(final String username, String password){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constants.emailParam,username);
        params.add(Constants.passwordParam,password);

        client.get(Constants.wsLogin, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONParser parser = new JSONParser();
                try {

                    int countRes = responseString.length();

                    String newRes = responseString.substring(5,countRes-1);

                    Object obj = parser.parse(newRes);

                    JSONObject jsonObject = new JSONObject(obj.toString());

                    System.out.println(jsonObject.getString("Nombre"));

                    String name = String.valueOf(jsonObject.get("Nombre"));
                    String lastName = String.valueOf(jsonObject.get("Apellidos"));
                    if (name.equals(Errors.errorUser)){
                        etEmail.setError(getResources().getString(R.string.userNoExist));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(Errors.errorActivation)){
                        etEmail.setError(getResources().getString(R.string.userNotActive));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(Errors.accountBlocked)){
                        etEmail.setError(getResources().getString(R.string.userBlocked));
                        progressDialog.dismiss();
                        return;
                    }if (name.equals(String.valueOf(1))||name.equals(String.valueOf(2))){
                        etPass.setError(getResources().getString(R.string.wrongPassword));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(String.valueOf(3))){
                        etPass.setError(getResources().getString(R.string.wrongPass)+ " " + String.valueOf(2) + " " + getResources().getString(R.string.numberIntents));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(String.valueOf(4))){
                        etPass.setError(getResources().getString(R.string.wrongPass)+ " " + String.valueOf(1) + " " + getResources().getString(R.string.numberIntents));
                        progressDialog.dismiss();
                        return;
                    }
                    else {


                        SharedPreferences usuario = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = usuario.edit();
                        String userName = String.valueOf(jsonObject.get("Nombre"));
                        String userLastName = String.valueOf(jsonObject.get("Apellidos"));
                        String userPID = String.valueOf(jsonObject.get("PID"));
                        String userHabilities = String.valueOf(jsonObject.get("Habilidades"));
                        String userLat = String.valueOf(jsonObject.get("Latitud"));
                        String userLong = String.valueOf(jsonObject.get("Longitud"));
                        String userIdPid = String.valueOf(jsonObject.get("idPID"));
                        String userIdCiudad = String.valueOf(jsonObject.get("idCiudad"));
                        String userIdPais = String.valueOf(jsonObject.get("idPais"));
                        editor.putString("nombre",userName);
                        editor.putString("apellidos",userLastName);
                        editor.putString("PID",userPID);
                        editor.putString("habilidades",userHabilities);
                        editor.putString("latitud",userLat);
                        editor.putString("longitud",userLong);
                        editor.putString("idPID",userIdPid);
                        editor.putString("idCiudad",userIdCiudad);
                        editor.putString("idPais",userIdPais);
                        editor.commit();
                        Intent o = new Intent(UserProfileActivity.this, PlannerMainMenuActivity.class);
                        startActivity(o);
                        finish();

                        progressDialog.dismiss();
                    }


                }catch (Exception e){
                    System.out.println(e.toString());
                }

            }
        });

    }

    public boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_recover_password, null);
        dialogBuilder.setView(dialogView);

        edtRecover = (EditText) dialogView.findViewById(R.id.edit1);


        dialogBuilder.setTitle(getResources().getString(R.string.recoverPassword));
        dialogBuilder.setMessage(getResources().getString(R.string.enterEmail));
        dialogBuilder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                if (!isEmailValid(edtRecover.getText().toString())){
                    showChangeLangDialog();
                    edtRecover.setError(getResources().getString(R.string.emailInvalidFormat));
                }else {
                    recoverPass(edtRecover.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void recoverPass(String email){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constants.emailParam,email);

        client.get(Constants.wsRecoverPass, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String name = String.valueOf(jsonObject.get("d"));
                    if (name.equals(Errors.errorUser)){
                        showChangeLangDialog();
                        edtRecover.setError(getResources().getString(R.string.userNoExist));
                    }else if (name.equals(Errors.passRecoverSucess)){
                        alertSucess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void alertSucess() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.emailSend))
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                });
        alert = builder.create();
        alert.show();

    }
}
