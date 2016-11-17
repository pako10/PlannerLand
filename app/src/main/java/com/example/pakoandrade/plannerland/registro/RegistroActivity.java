package com.example.pakoandrade.plannerland.registro;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceActivity;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.main.SearchActivity;
import com.example.pakoandrade.plannerland.volley.BaseVolley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
    ProgressBar pbRegistrando;
    AutoCompleteTextView atCity;
    public  static final int Segundos = 5;
    public  static final int delay = 2;
    public  static final int milisegundos = Segundos * 1000;
    public int establecer_progreso(long miliseconds) {
        return (int) ((milisegundos - miliseconds) / 1000);
    }

    public int maximo_progreso() {
        return Segundos - delay;
    }

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
        pbRegistrando = (ProgressBar) findViewById(R.id.progressBar);
        pbRegistrando.setVisibility(View.INVISIBLE);
        pbRegistrando.setMax(maximo_progreso());

        atCity = (AutoCompleteTextView) findViewById(R.id.autoCompleteCity);

        this.spPaises =(Spinner) findViewById(R.id.paises);
        this.spCiudades =(Spinner) findViewById(R.id.ciudades);
        makeRequest();

        spPaises.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        getValue(position);

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        BtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().trim().length()==0){
                    etName.setError("Introduce un nombre");
                    return;
                }
                if (isValidt(etName.getText().toString())){
                    etName.setError("Introduce un nombre valido");
                    return;
                }
                if (isValidt(etLastName.getText().toString())){
                    etLastName.setError("Introduce un apellidos validos");
                    return;
                }
                if(etLastName.getText().toString().trim().length()==0){
                    etLastName.setError("Introduce tus apellidos");
                    return;
                }
                if(!isEmailValid(etEmail.getText().toString())){
                    etEmail.setError("Introduce un correo electronico valido");
                    return;
                }
                if(!contra(etPassword.getText().toString())){
                    etPassword.setError("La contraseña debe contener letras mayusculas, minusculas y números");
                    return;
                }
                if(etPassword.getText().toString().trim().length()<6){
                    etPassword.setError("Introduce un password de minimo 6 letras");
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
                if(spTerminos.isChecked()){
                    spTerminos.setError("Revisa los terminos y concidiones");
                }
                if (!VerificaTerminos()) {
                    Toast.makeText(getBaseContext(),
                            "Porfavor revisa los terminos y condiciones", Toast.LENGTH_SHORT)
                            .show();
                }

                else {
                    RegisterUser();
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
                    JSONArray jsonArray = jsonObject.optJSONArray("d");
                    String data = "";


                    List<String> list = new ArrayList<String>(data.length());
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObjeto = jsonArray.getJSONObject(i);

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

    private void getValue(final int value){
        String url = "http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/ListPaisBusqueda";

        final JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try{
                    JSONArray jsonArray = jsonObject.optJSONArray("d");
                    String data = "";


                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObjeto = jsonArray.getJSONObject(i);

                        if(value == i){
                            String ciudad = jsonObjeto.optString("Value");
                            Nuevo(ciudad);
                        }
                        final String value = jsonObjeto.optString("Value");



                        data +=   value + "\n" ;
                    }
                }catch (JSONException e) {e.printStackTrace();}
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


    public void Nuevo(String value){
        final String KEY_USERNAME = "pID";
        RequestParams params = new RequestParams();
        params.put(KEY_USERNAME,value);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/ListCiudad", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    byte[] bytes = responseBody;
                    String city = new String(bytes);
                    JSONObject jsObj = new JSONObject(city);
                    int size = responseBody.length;
                    parseResponseAmount(jsObj,size);

                }catch (Exception e){
                    Toast.makeText(RegistroActivity.this, "No entra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegistroActivity.this, statusCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private  void parseResponseAmount (JSONObject response, int amount) {
        final ArrayAdapter<CharSequence> adapterC =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item );
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            JSONArray jsonArray = response.optJSONArray("d");

            for (int i = 0; i < amount; i++) {
                JSONObject userObject = (JSONObject) jsonArray.get(i);
                String id = userObject.optString("Text");
                spCiudades.setAdapter(adapterC);
                atCity.setAdapter(adapterC);
                adapterC.add(id);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void RegisterUser(){
        // final String cityValueR = label2.getText().toString().trim();
        final String cityValue = "2964adba-8a58-4c89-9d43-ee65e8f322a4";
        final String KEY_USERNAME = "pID";
        final String keyCity = "ciudad";
        final String keyCountry = "pais";
        final String keyUserName = "nombre";
        final String keyUserLastName = "apellido";
        final String keyPassword = "password";
        final String keyEmail = "email";
        final String keyLatitud = "lat";
        final String keyLongitud = "long";
        final String username = etName.getText().toString().trim();
        final String userLastName = etLastName.getText().toString();
        final String password = etPassword.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        //final String cityValue = etValue.getText().toString().trim();



        RequestParams params = new RequestParams();
        //params.put(KEY_USERNAME,value);
        params.put(keyUserName, username);
        params.put(keyUserLastName, userLastName);
        params.put(keyEmail, email);
        params.put(keyPassword, password);
        //params.put(keyCityValue, cityValue);
        params.put(keyCountry, cityValue);
        params.put(keyCity,"2964adba-8a58-4c89-9d43-ee65e8f322a4");
        params.put(keyLatitud, "a");
        params.put(keyLongitud, "a");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/InsertPID", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    byte[] bytes = responseBody;
                    String str = new String(bytes);
                    Toast.makeText(RegistroActivity.this, "Registrando....", Toast.LENGTH_LONG).show();
                    startProgressBar();

                }catch (Exception e){
                    Toast.makeText(RegistroActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegistroActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void startProgressBar() {
        new CountDownTimer(milisegundos, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pbRegistrando.setVisibility(View.VISIBLE);
                pbRegistrando.setProgress(establecer_progreso(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(RegistroActivity.this, RegisteredActivity.class);
                startActivity(i);
                finish();

            }
        }.start();
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


}
