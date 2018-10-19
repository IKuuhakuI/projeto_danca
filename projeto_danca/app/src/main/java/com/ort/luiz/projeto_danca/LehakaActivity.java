package com.ort.luiz.projeto_danca;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LehakaActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef, lehakotExemploRef;

    TextView scrollingText;
    Button btnVoltarLeakotExemplo, btnFacebook, btnInstagram, btnInternet, btnVotar;

    ImageView imagemFundo;

    private ListView horarioLehakotExemplo;

    int palmas;

    String[]horarios = {"13"};
    boolean isVotable = false;

    TextView grupoId;

    String urlNet = "";
    String urlFace = "";
    String urlInsta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lehakot);

        Intent i = getIntent();

        String id = i.getStringExtra("id");
        String lastPage = i.getStringExtra("lastPage");

        //alert(lastPage);

        grupoId = findViewById(R.id.grupoId);

        scrollingText = findViewById(R.id.scrollingTextId4);
        imagemFundo = findViewById(R.id.imagemFundoId);

        btnVoltarLeakotExemplo = findViewById(R.id.btnVoltarLeakotExemploId);
        btnVoltarLeakotExemplo.setOnClickListener((V)->{
            btnVoltarLeakotExemplo.setBackgroundResource(R.color.White);
            if(lastPage == "kapaim"){
                startActivity(new Intent(this, KapaimActivity.class));
            } else if(lastPage == "lehaka"){
                startActivity(new Intent(this, SelectLeakotActivity.class));
            }
            finish();

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

        lehakotExemploRef = database.getReference("Lehakot").child(id);
        lehakotExemploRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("isVotable").getValue().toString() == "true"){
                    isVotable = true;
                } else {
                    isVotable = false;
                    btnVotar.setVisibility(View.GONE);
                }
                grupoId.setText(dataSnapshot.child("name").getValue().toString());

                String fundoLehaka = dataSnapshot.child("image").getValue().toString();

                imagemFundo.setImageResource(getResources().getIdentifier(fundoLehaka, "drawable", getPackageName()));

                if(dataSnapshot.child("website").getValue().toString().length() > 2) {
                    urlNet = dataSnapshot.child("website").getValue().toString();
                }
                urlFace = dataSnapshot.child("facebook").getValue().toString();
                urlInsta = dataSnapshot.child("instagram").getValue().toString();

                horarioLehakotExemplo = findViewById(R.id.listViewLeakotExemploHorariosId);

                String getApresentacoes = dataSnapshot.child("Apresentacoes").toString().replace("DataSnapshot { key = Apresentacoes, value = ", "");
                String temp = getApresentacoes.replace(" }", "");

                getApresentacoes = temp.replace("=", " - ");

                horarios = getApresentacoes.split(", ");

                palmas = Integer.parseInt(dataSnapshot.child("kapaim").getValue().toString());

                ArrayAdapter<String> adaptadorHorario = new ArrayAdapter<>(
                        getApplicationContext(), // contexto da aplicação
                        android.R.layout.simple_list_item_1, // layout
                        android.R.id.text1, // id do layout
                        horarios
                );
                horarioLehakotExemplo.setAdapter(adaptadorHorario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }});

        btnVotar = findViewById(R.id.btnVotarBetarId);

        btnVotar.setOnClickListener(v -> {
            if(isVotable == true) {
                palmas += 1;
                lehakotExemploRef.child("kapaim").setValue(palmas);
            }
        });

        Context context = getApplicationContext();

        btnFacebook =  findViewById(R.id.btnFacebookId);
        btnFacebook.setOnClickListener((V)->{
            Intent facebook = getOpenFacebookIntent(context, urlFace);
            startActivity(facebook);

        });

        btnInstagram = findViewById(R.id.btnInstagramId);
        btnInstagram.setOnClickListener((V) ->{
            if(urlInsta == ""){
                alert("Grupo não possui instagram");
            } else {
                Intent instagram = newInstagramProfileIntent(context.getPackageManager(), "http://instagram.com/" + urlInsta);
                startActivity(instagram);
            }
        });

        btnInternet = findViewById(R.id.btnInternetId);
        btnInternet.setOnClickListener((V)->{
            if(urlNet == ""){
                alert("Grupo não possui site");

            } else {
                String url = urlNet;
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(browserIntent);
            }
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

    public static Intent getOpenFacebookIntent(Context context, String urlFace) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + urlFace));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + urlFace));
        }
    }

    private void alert(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
