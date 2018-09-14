package com.ort.luiz.projeto_danca;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EventosActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogo;
    private Button botao;

    private ListView listaItens;
    String[]itens = {"Evento 1", "Evento 2", "Evento 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        listaItens = findViewById(R.id.listViewId);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                itens
        );

        listaItens.setAdapter(adaptador);

        listaItens.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String valorClicado;
            valorClicado = listaItens.getItemAtPosition(position).toString();
            dialogo = new AlertDialog.Builder(EventosActivity.this);
            dialogo.setTitle("Agenda");
            dialogo.setMessage("Deseja adicionar " + valorClicado + " a sua agenda?");
            dialogo.setCancelable(false);
            dialogo.setNegativeButton("Não", (dialog, which) ->
                    Toast.makeText(getApplicationContext(), "Você disse Não", Toast.LENGTH_SHORT).show()
            );
            dialogo.setPositiveButton("Sim", (dialog, which) ->
                    Toast.makeText(getApplicationContext(), "Você disse Sim", Toast.LENGTH_SHORT).show());
            dialogo.create();
            dialogo.show();
        });
    }
}
