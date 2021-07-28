package com.beastek.entidadesonline;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

public class patientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);


        getSupportFragmentManager().beginTransaction().replace(R.id.container1, new patientsFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
