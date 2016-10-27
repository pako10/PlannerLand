package com.example.pakoandrade.plannerland.registro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakoandrade.plannerland.MainMenuActivity;
import com.example.pakoandrade.plannerland.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    TextView tvRegister;
    EditText etUserLogin;
    EditText etPassLogin;
    Button btnOk;
    TextView etPostLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvRegister = (TextView) findViewById(R.id.registerL);
        etUserLogin = (EditText) findViewById(R.id.et_user_login);
        etPassLogin = (EditText) findViewById(R.id.et_user_password);
        btnOk = (Button) findViewById(R.id.btn_user_value_ok);
        etPostLogin = (TextView) findViewById(R.id.textView);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(i);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String user = etUserLogin.getText().toString();
                if (etUserLogin.getText().toString().trim().length() == 0) {
                    etUserLogin.setError("Usuario no válido");
                    return;
                }
                else if (etPassLogin.getText().toString().trim().length() == 0) {
                    etPassLogin.setError("Contraseña no válida");
                    return;
                }else if ("pakoandrade10@gmail.com".equals(user)){
                        Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                        i.putExtra("user","Francisco Andrade");
                        i.putExtra("correo",etUserLogin.getText().toString().trim());
                        i.putExtra("tipo",1);
                        startActivity(i);
                        }
               else{
                    Toast.makeText(LoginActivity.this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                }*/
                LoginUser();
               // Intent o = new Intent(LoginActivity.this,MainMenuActivity.class);
            }
        });


    }

    public void LoginUser(){

        final String keyPassword = "mipass";
        final String keyUser = "correo";
        final String username = etUserLogin.getText().toString();
        final String password = etPassLogin.getText().toString();




        final RequestParams params = new RequestParams();

        params.put(keyUser, username);
        params.put(keyPassword, password);


        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://wsplannerlog.cloudapp.net/wsLogin.svc/GetLogin", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                JSONParser parser = new JSONParser();
                try {

                    int countRes = res.length();

                    String newRes = res.substring(5,countRes-1);

                    Object obj = parser.parse(newRes);

                    JSONObject jsonObject = new JSONObject(obj.toString());

                    System.out.println(jsonObject.getString("Nombre"));

                    etPostLogin.setText(String.valueOf(jsonObject.get("Nombre")) + " " + String.valueOf(jsonObject.get("Apellidos")));

                }catch (Exception e){
                    etPostLogin.setText(e.toString());
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });

    }

}
