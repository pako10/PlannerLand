package com.example.pakoandrade.plannerland.main.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;



import com.example.pakoandrade.plannerland.R;

/**
 * Created by AhskaBay on 04/04/2016.
 */
public class layout_configuracion extends Fragment {
    Spinner sptiempo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sevedInstanceState){

        final View view = inflater.inflate(R.layout.layout_configuracion, container, false);

        this.sptiempo = (Spinner) view.findViewById(R.id.minutos);
        


        return view;
    }


}
