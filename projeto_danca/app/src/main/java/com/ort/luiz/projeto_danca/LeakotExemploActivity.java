package com.ort.luiz.projeto_danca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LeakotExemploActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef;

    TextView scrollingText;
    Button btnVoltarLeakotExemplo, btnFacebook, btnInstagram, btnInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakot_exemplo);

        scrollingText = findViewById(R.id.scrollingTextId4);

        btnVoltarLeakotExemplo = findViewById(R.id.btnVoltarLeakotExemploId);
        btnVoltarLeakotExemplo.setOnClickListener((V)->{
            btnVoltarLeakotExemplo.setBackgroundResource(R.color.White);

            startActivity(new Intent(this, SelectLeakotActivity.class));
        });

        database = FirebaseDatabase.getInstance();
        acontecendoRef = database.getReference("Acontecendo_agora");
        acontecendoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scrollingText = findViewById(R.id.scrollingTextId4);
                String valor = dataSnapshot.getValue().toString();
                scrollingText.setText(valor);
                scrollingText.setSelected(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }});

        btnFacebook =  findViewById(R.id.btnFacebookId);
        btnFacebook.setOnClickListener((V)->{

        });

        btnInstagram = findViewById(R.id.btnInstagramId);
        btnInstagram.setOnClickListener((V)->{

        });

        btnInternet = findViewById(R.id.btnInternetId);
        btnInternet.setOnClickListener((V)->{
            
        });
    }
}
