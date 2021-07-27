package com.beastek.entidadesonline;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class secondTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_tab);

        getSupportFragmentManager().beginTransaction().replace(R.id.container1, new secondTabFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
