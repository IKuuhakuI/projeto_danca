package com.ort.luiz.projeto_danca;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class LeakotExemploActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef, leakotExemploRef;

    TextView scrollingText;
    Button btnVoltarLeakotExemplo, btnFacebook, btnInstagram, btnInternet;

    private ListView horarioLeakotExemplo, integrantesLeakotExemplo;

    String[]horarios = {"13"};
    String[]integrantes = {"Exemplo"};

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



        leakotExemploRef = database.getReference("Grupos").child("Grupo1").child("Apresentacoes");
        leakotExemploRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                horarioLeakotExemplo = findViewById(R.id.listViewLeakotExemploHorariosId);

                String getApresentacoes = dataSnapshot.toString().replace("DataSnapshot { key = Apresentacoes, value = {", "");
                String temp = getApresentacoes.replace("} }", "");
                getApresentacoes = temp.replace("=", " - ");

                horarios = getApresentacoes.split(", ");

                ArrayAdapter<String> adaptadorHorario = new ArrayAdapter<>(
                        getApplicationContext(), // contexto da aplicação
                        android.R.layout.simple_list_item_1, // layout
                        android.R.id.text1, // id do layout
                        horarios
                );
                horarioLeakotExemplo.setAdapter(adaptadorHorario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }});

        Context context = getApplicationContext();

        btnFacebook =  findViewById(R.id.btnFacebookId);
        btnFacebook.setOnClickListener((V)->{
            Intent facebook = getOpenFacebookIntent(context);
            startActivity(facebook);

        });

        btnInstagram = findViewById(R.id.btnInstagramId);
        btnInstagram.setOnClickListener((V) ->{
            Intent instagram  = newInstagramProfileIntent(context.getPackageManager(), "http://instagram.com/jaredrummler");
            startActivity(instagram);
            Toast.makeText(getApplicationContext(),"Clicou",Toast.LENGTH_SHORT).show();
        });

        btnInternet = findViewById(R.id.btnInternetId);
        btnInternet.setOnClickListener((V)->{
            String valorClicado;
            valorClicado = "";

            String url = "https://www.google.com.br/search?q=" + valorClicado;
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(browserIntent);
        });
    }

    public static Intent newInstagramProfileIntent(PackageManager pm, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);

                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/hebraicario"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/hebraicario"));
        }
    }
}
