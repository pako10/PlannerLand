package com.example.pakoandrade.plannerland.registro;

import android.accessibilityservice.GestureDescription;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakoandrade.plannerland.MainMenuActivity;
import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.main.PlannerMainMenuActivity;
import com.example.pakoandrade.plannerland.main.SearchActivity;
import com.example.pakoandrade.plannerland.utils.Constants;
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
import com.example.pakoandrade.plannerland.utils.Errors;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    TextView tvRegister;
    EditText etUserLogin;
    EditText etPassLogin;
    Button btnOk;
    TextView etPostLogin;
    ProgressDialog progressDialog;
    TextView tvRecover;
    AlertDialog alert = null;
    EditText edtRecover;

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
        tvRecover = (TextView) findViewById(R.id.tvRecoverPass);

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
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.entering));


                final String username = etUserLogin.getText().toString();
                final String password = etPassLogin.getText().toString();
                if (!isEmailValid(username)){
                    etUserLogin.setError(getResources().getString(R.string.emailInvalidFormat));
                    return;
                }
                if (password.length()<6){
                    etPassLogin.setError(getResources().getString(R.string.passInvalidFormat));
                    return;
                }else {
                    progressDialog.show();
                    Login(username, password);
                }
            }
        });

        tvRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLangDialog();
            }
        });


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
                        etUserLogin.setError(getResources().getString(R.string.userNoExist));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(Errors.errorActivation)){
                        etUserLogin.setError(getResources().getString(R.string.userNotActive));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(Errors.accountBlocked)){
                        etUserLogin.setError(getResources().getString(R.string.userBlocked));
                        progressDialog.dismiss();
                        return;
                    }if (name.equals(String.valueOf(1))||name.equals(String.valueOf(2))){
                        etPassLogin.setError(getResources().getString(R.string.wrongPassword));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(String.valueOf(3))){
                        etPassLogin.setError(getResources().getString(R.string.wrongPass)+ " " + String.valueOf(2) + " " + getResources().getString(R.string.numberIntents));
                        progressDialog.dismiss();
                        return;
                    }
                    if (name.equals(String.valueOf(4))){
                        etPassLogin.setError(getResources().getString(R.string.wrongPass)+ " " + String.valueOf(1) + " " + getResources().getString(R.string.numberIntents));
                        progressDialog.dismiss();
                        return;
                    }
                    else {
                        etPostLogin.setText(String.valueOf(jsonObject.get("Nombre")) + " " + String.valueOf(jsonObject.get("Apellidos")));

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
                        Intent o = new Intent(LoginActivity.this, PlannerMainMenuActivity.class);
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

    @Override
    public void onBackPressed(){
        Intent i = new Intent(LoginActivity.this, SearchActivity.class);
        startActivity(i);
        finish();
    }

}
