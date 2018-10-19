package com.ort.luiz.projeto_danca;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            if(verificaConexao() == true) {
                startActivity(new Intent(this, EventosActivity.class));
            } else  {
                alert("Não há conexão com a internet, por favor tente novamente");
            }
        });

        btnLeakot = findViewById(R.id.btnLeakotId);
        btnLeakot.setOnClickListener((V)->{
            btnLeakot.setBackgroundResource(R.color.White);
            if(verificaConexao() == true) {
                startActivity(new Intent(this, SelectLeakotActivity.class));
            }
            else{
                alert("Não há conexão com a internet, por favor tente novamente");
            }
        });

        btnKapaim = findViewById(R.id.btnKapaimId);
        btnKapaim.setOnClickListener(V->{
            btnKapaim.setBackgroundResource(R.color.White);
            if(verificaConexao() == true) {
                startActivity(new Intent(this, KapaimActivity.class));
            } else {
                alert("Não há conexão com a internet, por favor tente novamente");
            }
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

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    private void alert(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
