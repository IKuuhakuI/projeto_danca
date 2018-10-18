package com.ort.luiz.projeto_danca;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventosActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef, eventosRef;

    TextView scrollingText;

    private AlertDialog.Builder dialogo;

    String selecionado = "1";

    private ListView listaItens;

    Button btnDia1, btnDia2, btnDia3, btnVoltarEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        database = FirebaseDatabase.getInstance();

        btnDia1 = findViewById(R.id.btnDia1Id);
        btnDia2 = findViewById(R.id.btnDia2Id);
        btnDia3 = findViewById(R.id.btnDia3Id);
        btnVoltarEventos = findViewById(R.id.btnVoltarEventosId);

        btnDia1.setOnClickListener((V)->{
            selecionado = "Dia1";
            agendaEventos(selecionado);

            btnDia1.setBackgroundResource(R.color.White);
            btnDia2.setBackgroundResource(R.drawable.button_border);
            btnDia3.setBackgroundResource(R.drawable.button_border);
        });

        btnDia2.setOnClickListener((V)->{
            selecionado = "Dia2";
            agendaEventos(selecionado);

            btnDia1.setBackgroundResource(R.drawable.button_border);
            btnDia2.setBackgroundResource(R.color.White);
            btnDia3.setBackgroundResource(R.drawable.button_border);
        });

        btnDia3.setOnClickListener((V)->{
            selecionado = "Dia3";
            agendaEventos(selecionado);

            btnDia1.setBackgroundResource(R.drawable.button_border);
            btnDia2.setBackgroundResource(R.drawable.button_border);
            btnDia3.setBackgroundResource(R.color.White);
        });

        btnVoltarEventos.setOnClickListener((V)->{
            btnVoltarEventos.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, MainActivity.class));
        });

        //Eventos
        eventosRef = database.getReference("Eventos");

        //Acontecendo agora
        acontecendoRef = database.getReference("Acontecendo_agora");
        acontecendoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scrollingText = findViewById(R.id.scrollingTextId2);
                String valor = dataSnapshot.getValue().toString();
                scrollingText.setText(valor);
                scrollingText.setSelected(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }});
    }

    private void agendaEventos (String selecionado) {
        eventosRef.child(selecionado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valor = dataSnapshot.getValue().toString();
                String valor1 = valor.replace("{", "");
                valor = valor1.replace("}", "");
                valor1 = valor.replace("=", " > ");
                valor = valor1.replace("[null, ", "");
                valor1 = valor.replace("]", "");

                String [] itens = valor1.split(", ");

                listaItens = findViewById(R.id.listViewId);
                ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        itens
                );

                //Lista de itens
                listaItens.setAdapter(adaptador);
                listaItens.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    String valorClicado, horaSelecionada, horaFinalSelecionada, local;

                    valorClicado = listaItens.getItemAtPosition(position).toString().substring(0, listaItens.getItemAtPosition(position).toString().indexOf(">") - 1);
                    horaSelecionada = listaItens.getItemAtPosition(position).toString().substring(listaItens.getItemAtPosition(position).toString().length() - 13, listaItens.getItemAtPosition(position).toString().length() - 8);

                    horaFinalSelecionada = listaItens.getItemAtPosition(position).toString().substring(listaItens.getItemAtPosition(position).toString().length() - 5 , listaItens.getItemAtPosition(position).toString().length());

                    local = listaItens.getItemAtPosition(position).toString().substring(listaItens.getItemAtPosition(position).toString().indexOf(">")+2, listaItens.getItemAtPosition(position).toString().length() - 16);

                    int horaInicial = Integer.parseInt(horaSelecionada.substring(0, 2));
                    int minutoInicial = Integer.parseInt(horaSelecionada.substring(3, horaSelecionada.length()));

                    int horaFinal = Integer.parseInt(horaFinalSelecionada.substring(0, 2));
                    int minutoFinal = Integer.parseInt(horaFinalSelecionada.substring(3, horaFinalSelecionada.length()));

                    String dia;

                    if(selecionado == "Dia1" && (horaInicial != 00 && horaInicial != 01 && horaInicial != 02 && horaInicial != 03 && horaInicial != 04)){
                        dia = "19/10";
                    } else if(selecionado == "Dia2" && (horaInicial != 00 && horaInicial != 01 && horaInicial != 02 && horaInicial != 03 && horaInicial != 04) || selecionado == "Dia1"){
                        dia = "20/10";
                    } else if(selecionado == "Dia3" && (horaInicial != 00 && horaInicial != 01 && horaInicial != 02 && horaInicial != 03 && horaInicial != 04) || selecionado == "Dia2"){
                        dia = "21/10";
                    } else {
                        dia = "22/10";
                    }

                    dialogo = new AlertDialog.Builder(EventosActivity.this);
                    dialogo.setTitle("Adicionar Evento");

                    dialogo.setMessage("Deseja adicionar o evento: " + valorClicado + " das " + horaSelecionada + " até " + horaFinalSelecionada + " no dia " + dia + " (" + local + ") ao seu calendário?");
                    dialogo.setCancelable(false);

                    dialogo.setNegativeButton("Não", (dialog, which) -> {
                            }
                    );

                    dialogo.setPositiveButton("Sim", (dialog, which) -> {
                        Calendar cal = Calendar.getInstance();
                        Intent intent = new Intent(Intent.ACTION_EDIT);

                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("title", valorClicado);
                        intent.putExtra("allDay", false);
                        intent.putExtra("eventLocation", local);

                        cal.set(Calendar.YEAR, 2018);
                        cal.set(Calendar.MONTH, 9);

                        if(selecionado == "Dia1") {
                            if(horaInicial == 00 || horaInicial == 01 || horaInicial == 02 || horaInicial == 03 || horaInicial == 04) {
                                cal.set(Calendar.DAY_OF_MONTH, 20);
                            } else {
                                cal.set(Calendar.DAY_OF_MONTH, 19);
                            }
                        } else if(selecionado == "Dia2") {
                            if(horaInicial == 00 || horaInicial == 01 || horaInicial == 02 || horaInicial == 03 || horaInicial == 04) {
                                cal.set(Calendar.DAY_OF_MONTH, 21);
                            } else {
                                cal.set(Calendar.DAY_OF_MONTH, 20);
                            }
                        } else if(selecionado == "Dia3") {
                            if(horaInicial == 00 || horaInicial == 01 || horaInicial == 02 || horaInicial == 03 || horaInicial == 04) {
                                cal.set(Calendar.DAY_OF_MONTH, 22);
                            } else {
                                cal.set(Calendar.DAY_OF_MONTH, 21);
                            }
                        }

                        cal.set(Calendar.HOUR_OF_DAY, horaInicial);
                        cal.set(Calendar.MINUTE, minutoInicial);
                        intent.putExtra("beginTime", cal.getTimeInMillis());

                        if(selecionado == "Dia1") {
                            if(horaFinal == 00 || horaFinal == 01 || horaFinal == 02 || horaFinal == 03 || horaFinal == 04) {
                                cal.set(Calendar.DAY_OF_MONTH, 20);
                            } else {
                                cal.set(Calendar.DAY_OF_MONTH, 19);
                            }
                        } else if(selecionado == "Dia2") {
                            if(horaFinal == 00 || horaFinal == 01 || horaFinal == 02 || horaFinal == 03 || horaFinal == 04) {
                                cal.set(Calendar.DAY_OF_MONTH, 21);
                            } else {
                                cal.set(Calendar.DAY_OF_MONTH, 20);
                            }
                        } else if(selecionado == "Dia3") {
                            if(horaFinal == 00 || horaFinal == 01 || horaFinal == 02 || horaFinal == 03 || horaFinal == 04) {
                                cal.set(Calendar.DAY_OF_MONTH, 22);
                            } else {
                                cal.set(Calendar.DAY_OF_MONTH, 21);
                            }
                        }

                        cal.set(Calendar.HOUR_OF_DAY, horaFinal);
                        cal.set(Calendar.MINUTE, minutoFinal);

                        intent.putExtra("endTime", cal.getTimeInMillis());

                        startActivity(intent);
                    });
                    dialogo.create();
                    dialogo.show();
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alert(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}