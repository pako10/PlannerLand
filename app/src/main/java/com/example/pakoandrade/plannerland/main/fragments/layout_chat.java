package com.example.pakoandrade.plannerland.main.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.pakoandrade.plannerland.R;

/**
 * Created by AhskaBay on 04/04/2016.
 */
public class layout_chat extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sevedInstanceState){

        final View view = inflater.inflate(R.layout.layout_chat, container, false);

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
}
