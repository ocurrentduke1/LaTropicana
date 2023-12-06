package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Producto[] wines, food, snaks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                if (employeeRegistry()) {
                    intent = new Intent(MainActivity.this, MenuLateralActivity.class);
                } else if (userRegistry()) {
                    intent = new Intent(MainActivity.this, MenuCliente.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000);
    }

    private boolean employeeRegistry() {
        SharedPreferences preferences = getSharedPreferences(
                "employee.dat", MODE_PRIVATE
        );
        return preferences.getBoolean("exists", false);
    }

    private boolean userRegistry() {
        SharedPreferences preferences = getSharedPreferences(
                "user.dat", MODE_PRIVATE
        );
        return preferences.getBoolean("exists", false);
    }
}
