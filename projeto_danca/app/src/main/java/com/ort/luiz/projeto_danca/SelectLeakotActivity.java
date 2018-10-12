package com.ort.luiz.projeto_danca;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectLeakotActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef;

    private ListView listaItens;
    String[]itens = {"Chazitão", "Dalia", "Habonito", "Betar", "Shalom (Hebraica SP)"};

    Button btnVoltarLeakot;

    TextView scrollingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_leakot);

        btnVoltarLeakot = findViewById(R.id.btnVoltarLeakotId);
        btnVoltarLeakot.setOnClickListener((V)->{
            btnVoltarLeakot.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, MainActivity.class));
        });

        listaItens = findViewById(R.id.listViewLeakotId);

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                getApplicationContext(), // contexto da aplicação
                android.R.layout.simple_list_item_1, // layout
                android.R.id.text1, // id do layout
                itens
        );

        listaItens.setAdapter(adaptador);

        listaItens.setOnItemClickListener((parent, view, position, id) -> {
            String valorClicado;
            //valorClicado = listaItens.getItemAtPosition(position).toString();

            //Toast.makeText(getApplicationContext(), valorClicado, Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, LeakotExemploActivity.class));

        });

        database = FirebaseDatabase.getInstance();
        acontecendoRef = database.getReference("Acontecendo_agora");
        acontecendoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scrollingText = findViewById(R.id.scrollingTextId3);
                String valor = dataSnapshot.getValue().toString();
                scrollingText.setText(valor);
                scrollingText.setSelected(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }});


    }
}
