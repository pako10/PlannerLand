package com.example.pakoandrade.plannerland.registro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pakoandrade.plannerland.MainMenuActivity;
import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.main.PlannerMainMenuActivity;
import com.example.pakoandrade.plannerland.main.SearchActivity;

public class SplashActivity extends AppCompatActivity {
    public  static final int Segundos = 5;
    public  static final int delay = 2;
    public  static final int milisegundos = Segundos * 1000;
    private ProgressBar progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /** Programacion de ProgressBar*/
        progreso = (ProgressBar) findViewById(R.id.ProgressSplash);
        progreso.setMax(maximo_progreso());


        SharedPreferences usuario = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String correo = usuario.getString("nombre","");
        if(correo != ""){
            Intent i = new Intent(SplashActivity.this, PlannerMainMenuActivity.class);
            startActivity(i);
            finish();
        }else {
            empezaranima();
        }

    }

    public void empezaranima() {
        new CountDownTimer(milisegundos, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                progreso.setProgress(establecer_progreso(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent op;
                op = new Intent(SplashActivity.this, SearchActivity.class);
                startActivity(op);
                finish();

            }
        }.start();
    }

    public int establecer_progreso(long miliseconds) {
        return (int) ((milisegundos - miliseconds) / 1000);
    }

    public int maximo_progreso() {
        return Segundos - delay;
    }



}
