package com.ort.luiz.projeto_danca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KapaimActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef, kapaimRef;

    private ListView listaItens;
    private ArrayAdapter<String> adaptador;

    private ArrayList<String> nomes = new ArrayList<>();
    private ArrayList<Integer> posicoes = new ArrayList<>();

    Button btnVoltarKapaim;

    TextView scrollingText;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kapaim);

        database = FirebaseDatabase.getInstance();

        acontecendoRef = database.getReference("Acontecendo_agora");
        acontecendoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scrollingText = findViewById(R.id.scrollingTextId5);
                String valor = dataSnapshot.getValue().toString();
                scrollingText.setText(valor);
                scrollingText.setSelected(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaItens = findViewById(R.id.listViewKapaimId);

        kapaimRef = database.getReference("Lehakot");

        kapaimRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 1; i < 62; i++) {
                    if(i == 26 || i == 25 || dataSnapshot.child(Integer.toString(i)).child("isVotable").getValue().toString() == "false"){
                        continue;
                    }

                    String nomeAtual;

                    String valor = Integer.toString(i);

                    nomeAtual = dataSnapshot.child(valor).child("name").getValue().toString();

                    nomes.add(nomeAtual);
                    posicoes.add(i);
                }

                if(i == 0) {
                    adaptador = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            nomes);

                    listaItens.setAdapter(adaptador);

                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaItens.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(KapaimActivity.this, LehakaActivity.class);

            intent.putExtra("id", Integer.toString(posicoes.get(position)));
            intent.putExtra("lastPage", "kapaim");

            startActivity(intent);
        });

        btnVoltarKapaim = findViewById(R.id.btnVoltarKapaimId);
        btnVoltarKapaim.setOnClickListener((V)->{
            btnVoltarKapaim.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
