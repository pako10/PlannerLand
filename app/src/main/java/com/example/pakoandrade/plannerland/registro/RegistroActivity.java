package com.example.pakoandrade.plannerland.registro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceActivity;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.main.SearchActivity;
import com.example.pakoandrade.plannerland.utils.Errors;
import com.example.pakoandrade.plannerland.volley.BaseVolley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends BaseVolley {
    Button   BtRegistrate;
    EditText etName;
    EditText etLastName;
    EditText etEmail;
    EditText etPassword;
    EditText etRpassword;
    TextView etTerminos;
    CheckBox spTerminos;
    Spinner  spPaises;
    Spinner  spCiudades;
    AutoCompleteTextView atCity;
    String pais;
    JSONObject jsObj;
    String ciudad;
    JSONArray jsonArrayCity;
    String ciudadValue;
    JSONArray jsonArrayCountry;
    AlertDialog alert = null;
    ProgressDialog progressDialog;


    BottomSheetBehavior behavior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.Name);
        etLastName = (EditText) findViewById(R.id.LastName);
        etEmail    = (EditText) findViewById(R.id.Email);
        etPassword = (EditText) findViewById(R.id.Rpassword);
        etRpassword =(EditText) findViewById(R.id.ConfirmRpassword);
        BtRegistrate = (Button) findViewById(R.id.registrate);
        etTerminos = (TextView) findViewById(R.id.etTerminos);
        spTerminos = (CheckBox) findViewById(R.id.terminos_condiciones);
        atCity = (AutoCompleteTextView) findViewById(R.id.autoCompleteCity);
        this.spPaises =(Spinner) findViewById(R.id.paises);
        makeRequest();

        spPaises.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        getValueCountry(position);

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        atCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getValueCity(i);
            }
        });
        BtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().trim().length()==0){
                    etName.setError("El campo de nombre es requerido, favor de llenarlo.");
                    return;
                }
                if (isValidt(etName.getText().toString())){
                    etName.setError("El campo de nombre no recibe caracteres especiales, solo letras.");
                    return;
                }
                if(etLastName.getText().toString().trim().length()==0){
                    etLastName.setError("El campo de apellido es requerido, favor de llenarlo.");
                    return;
                }
                if (isValidt(etLastName.getText().toString())){
                    etLastName.setError("El campo de apellido no recibe caracteres especiales, solo letras.");
                    return;
                }
                if(!isEmailValid(etEmail.getText().toString())){
                    etEmail.setError("Formato de correo inválido.");
                    return;
                }
                if(!contra(etPassword.getText().toString())){
                    etPassword.setError("La contraseña debe contener letras mayusculas, minusculas y números");
                    return;
                }
                if(etPassword.getText().toString().trim().length()<6){
                    etPassword.setError("La contraseña debe contener al menos 6 caracteres.");
                    return;
                }

                if (!VerificaPass()) {
                    etRpassword.setError("Las contraseñas no coinciden");
                    return;
                }
                if(etRpassword.getText()== etPassword){
                    etRpassword.setError("Los password con coinciden");
                    return;
                }
                if (atCity.getText().toString().length()==0){
                    atCity.setError("Por favor seleccione su ciudad");
                    return;
                }
                if (!VerificaTerminos()) {
                    alertTerminos();
                    return;
                }
                else {
                    progressDialog = new ProgressDialog(RegistroActivity.this);
                    progressDialog.setMessage("Registrando");
                    progressDialog.show();
                    registerUser();
                }
            }


        });



        View bottomSheet = findViewById(R.id.design_bottom_sheet);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button btAcept = (Button) findViewById(R.id.btAceptar);
        btAcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                spTerminos.setChecked(true);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        behavior = BottomSheetBehavior.from(bottomSheet);

        etTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


    }

    private void makeRequest(){
        String url = "http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/ListPaisBusqueda";
        //String url = " http://wsplanner.cloudapp.net/wsPL.svc/GetPrueba";
        //String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=39.476245,-0.349448&sensor=true";
        final ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try{
                    jsonArrayCountry = jsonObject.optJSONArray("d");
                    String data = "";


                    List<String> list = new ArrayList<String>(data.length());
                    for(int i = 0; i < jsonArrayCountry.length(); i++){
                        JSONObject jsonObjeto = jsonArrayCountry.getJSONObject(i);

                        String id = jsonObjeto.optString("Text");
                        final String value = jsonObjeto.optString("Value");
                        spPaises.setAdapter(adapter);
                        adapter.add(id);

                        data +=   value + "\n" ;
                    }
                    //label2.setText(data);
                }catch (JSONException e) {e.printStackTrace();}

                //label.setText(jsonObject.toString());
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(volleyError.toString());
            }
        });
        addToQueue(request);
    }


    public void getValueCountry(final int value){
        for (int i = 0;i < jsonArrayCountry.length();i++){
            try {
                JSONObject jsonObj = jsonArrayCountry.getJSONObject(i);
                if(value == i){
                    pais = jsonObj.optString("Value");
                    //Nuevo(pais);
                    getCity(pais);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void getCity(String value){
        final String KEY_USERNAME = "pID";
        RequestParams params = new RequestParams();
        params.put(KEY_USERNAME,value);
        AsyncHttpClient client = new AsyncHttpClient();

        final ArrayAdapter<CharSequence> adapterC =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item );
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        client.get("http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/ListCiudad", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    jsonArrayCity = jsonObject.optJSONArray("d");
                    for (int i = 0;i < jsonArrayCity.length();i++){
                        JSONObject userObjetc = (JSONObject) jsonArrayCity.get(i);
                        String id = userObjetc.optString("Text");
                        atCity.setAdapter(adapterC);
                        adapterC.add(id);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getValueCity(int position){
        for (int i = 0;i < jsonArrayCity.length();i++){
            try {
                JSONObject jsonObj = jsonArrayCity.getJSONObject(i);
                if (position == i){
                    ciudadValue = jsonObj.getString("Value");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    public void registerUser(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        final String username = etName.getText().toString();
        final String userLastName = etLastName.getText().toString();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();



        params.add("nombre",username);
        params.add("apellido",userLastName);
        params.add("email",email);
        params.add("password",password);
        params.add("pais",pais);
        params.add("ciudad",ciudadValue);
        params.add("lat","24");
        params.add("lon","23");
        params.add("referido","o1RgBcP4OLvGOmWQ3D/FFQ4RADvU7Jgw9oRB/pX2qNgUP7ayjTrGbPigHSmJkeyA");
        //"http://wsars.cloudapp.net/wsARS.svc/InsertPID"
        client.get("http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/InsertPID",params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObj = new JSONObject(responseString);
                    String response = String.valueOf(jsonObj.get("d"));
                    progressDialog.dismiss();
                    if (response.equals(Errors.errorEmail)){
                        emailAlredyRegister();
                    }
                    if (response.equals(Errors.accountCreated)){
                        createSucessful();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public boolean isValidt(String s){
        String n = ".*[0-9].*";
        return s.matches(n)  ;
    }


    public boolean contra(String c){
        String contra = ".*[0-9].*";
        String letra = ".*[A-Z].*";
        String letramin = ".*[a-z].*";
        return c.matches(contra) && c.matches(letra) && c.matches(letramin);
    }

    public boolean VerificaTerminos(){
        boolean checado = false;
        if(spTerminos.isChecked()){
            checado = true;
        }
        return checado;
    }

    public boolean VerificaPass(){
        boolean verificado = false;
        if(etPassword.getText().toString().equals(etRpassword.getText().toString())){
            verificado = true;
        }
        return verificado;
    }

    public boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    public void onBackPressed(){
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void alertTerminos() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor revise términos y condiciones de uso.")
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Revisar Terminos", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

            }
        });
        alert = builder.create();
        alert.show();

    }

    private void createSucessful() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Se han registrado sus datos correctamente. Por favor Revise su correo electronico")
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent i = new Intent(RegistroActivity.this,SearchActivity.class);
                        startActivity(i);
                        finish();
                    }});
        alert = builder.create();
        alert.show();

    }

    private void emailAlredyRegister() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El correo electrónico proporcionado ya se encuentra registrado.")
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }});
        alert = builder.create();
        alert.show();

    }




}
