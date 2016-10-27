package com.example.pakoandrade.plannerland.main.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.pakoandrade.plannerland.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by AhskaBay on 04/04/2016.
 */
public class layout_habilidades extends Fragment {
    Button btHabilities;
    EditText etHabilities;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sevedInstanceState){

        final View view = inflater.inflate(R.layout.layout_habilidades, container, false);
        etHabilities = (EditText) view.findViewById(R.id.etHabilidades);
        btHabilities = (Button) view.findViewById(R.id.btHabilidades);

        btHabilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerHabilities();
            }
        });

        /**doctor =(RelativeLayout)view.findViewById(R.id.doctoruno);
         doctor.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent gto = new Intent(getActivity(), DoctorScrolling.class);
        startActivity(gto);

        }
        });*/

        return view;
    }

    public void registerHabilities(){
        AsyncHttpClient client = new AsyncHttpClient();
        String habilities = etHabilities.getText().toString();
        final RequestParams params = new RequestParams();
        params.put("pHabilidades",habilities);

        client.get("http://wsplannerregistro.cloudapp.net/wsRegistrro.svc/GetHabilidades", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                //JSONObject jsObj = new JSONObject(response);
                //JSONArray arr = jsObj.optJSONArray("d");
                etHabilities.setText(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
