package com.beastek.entidadesonline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button crearCuenta;
    private Button login;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFirebaseDatabase = MyDatabaseUtil.getDatabase();
        mDatabaseReference = mFirebaseDatabase.getReference().child("patientData");

        // si el usuario ya está logado, entonces salta directamente a patientsListActivity.class

        if(doctorPreference.getBooleanFromSP(this)){
            Intent intent = new Intent(this, patientsListActivity.class);
            startActivity(intent);
        }

        //si el usuario no está logado continúa a que cree una cuenta nueva o se logue

        Context context = myApp.getAppContext();

        crearCuenta = findViewById(R.id.createAccount);
        login = findViewById(R.id.loginButton);

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, createAccount.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });
    }

}
