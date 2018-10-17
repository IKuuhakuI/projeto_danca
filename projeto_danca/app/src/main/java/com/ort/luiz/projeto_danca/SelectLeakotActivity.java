package com.ort.luiz.projeto_danca;

import android.content.Intent;
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

import java.util.ArrayList;

public class SelectLeakotActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef, lehakotRef;

    private ListView listaItens;
    private ArrayAdapter<String> adaptador;

    private ArrayList<String> nomes = new ArrayList<>();

    Button btnVoltarLeakot;

    TextView scrollingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_leakot);
        database = FirebaseDatabase.getInstance();
        listaItens = findViewById(R.id.listViewLeakotId);

        lehakotRef = database.getReference("Lehakot");

        lehakotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (int i = 1; i < 21; i++) {
                        String nomeAtual;

                        String valor = Integer.toString(i);

                        nomeAtual = dataSnapshot.child(valor).child("name").getValue().toString();

                        nomes.add(nomeAtual);
                    }

                adaptador = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        nomes);

                listaItens.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String valorClicado;
                //valorClicado = listaItens.getItemAtPosition(position).toString();

                if((position + 1) == 1){
                    startActivity(new Intent(SelectLeakotActivity.this, Lehakot1Activity.class));
                }

            }
        });

        btnVoltarLeakot = findViewById(R.id.btnVoltarLeakotId);
        btnVoltarLeakot.setOnClickListener((V)->{
            btnVoltarLeakot.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, MainActivity.class));
        });

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


    private void alert(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
