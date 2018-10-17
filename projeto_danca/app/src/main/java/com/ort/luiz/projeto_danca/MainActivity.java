package com.ort.luiz.projeto_danca;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef;

    TextView scrollingText;
    Button btnEventos, btnLeakot, btnKapaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        acontecendoRef = database.getReference("Acontecendo_agora");

        btnEventos = findViewById(R.id.btnEventosId);
        btnEventos.setOnClickListener((V)->{
            btnEventos.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, EventosActivity.class));
        });

        btnLeakot = findViewById(R.id.btnLeakotId);
        btnLeakot.setOnClickListener((V)->{
            btnLeakot.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, SelectLeakotActivity.class));
        });

        btnKapaim = findViewById(R.id.btnKapaimId);
        btnKapaim.setOnClickListener(V->{
            btnKapaim.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, KapaimActivity.class));
        });

        acontecendoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scrollingText = findViewById(R.id.scrollingTextId);
                String valor = dataSnapshot.getValue().toString();
                scrollingText.setText(valor);
                scrollingText.setSelected(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
