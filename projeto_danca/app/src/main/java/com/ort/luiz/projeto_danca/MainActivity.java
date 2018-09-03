package com.ort.luiz.projeto_danca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView scrollingText;
    Button btnEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEventos = findViewById(R.id.btnEventosId);
        btnEventos.setOnClickListener((V)->{
            startActivity(new Intent(MainActivity.this, EventosActivity.class));
        });

        scrollingText = findViewById(R.id.scrollingTextId);
        scrollingText.setSelected(true);
    }
}
