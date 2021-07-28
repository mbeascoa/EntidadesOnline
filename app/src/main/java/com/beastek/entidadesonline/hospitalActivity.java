package com.beastek.entidadesonline;

import android.annotation.SuppressLint;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.beastek.entidadesonline.models.doctorInfo;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class hospitalActivity extends AppCompatActivity {

    private Toolbar hospitalToolbar;
    private ListView hospitalSearchList;
    private TabLayout hospitalTabLayout;
    private ViewPager hospitalPager;
    private MaterialSearchBar hospitalSearchBar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public hospitalListAdapter hospitalListAdapter;

    private ConnectivityManager connectivityManager;

    private MaterialDialog mMaterialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        hospitalTabLayout=(TabLayout) findViewById(R.id.hospitalTabLayout);
        hospitalPager=  findViewById(R.id.hospitalPager);
        hospitalSearchBar = findViewById(R.id.hospitalSearchBar);

        com.beastek.entidadesonline.doctorCategoryAdapter adapter=new com.beastek.entidadesonline.doctorCategoryAdapter(this,getSupportFragmentManager());
        hospitalPager.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hospitalTabLayout.setBackgroundColor(getColor(R.color.actionBar));
        }
        hospitalTabLayout.setupWithViewPager(hospitalPager);

        hospitalToolbar = findViewById(R.id.hospitalToolbar);
        hospitalSearchList = findViewById(R.id.hospitalSearchList);
        hospitalSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] tags = (String[]) view.getTag();
                Intent intent = new Intent(hospitalActivity.this , com.beastek.entidadesonline.hospitalDetailActivity.class);
                intent.putExtra("tag", tags);
                startActivity(intent);
            }
        });

        showAboutUsDialog();

        /////////////////////////////////////
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        ///////////////

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        List<doctorInfo> hospitalInfo = new ArrayList<>();
        hospitalListAdapter = new hospitalListAdapter(this, R.layout.patients_list_item, hospitalInfo);
        hospitalSearchList.setAdapter(hospitalListAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hospitalToolbar.setElevation(0);
        }
        setSupportActionBar(hospitalToolbar);

        createSearchBar();

    }

    @SuppressLint("NewApi")
    private void createSearchBar() {

        hospitalSearchBar.setHint("Search Hospital Name");
        hospitalSearchBar.setElevation(10);
        hospitalSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        hospitalSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    hospitalListAdapter.clear();
                    hospitalToolbar.setVisibility(View.VISIBLE);
                    hospitalPager.setVisibility(View.VISIBLE);
                    hospitalTabLayout.setVisibility(View.VISIBLE);
                    hospitalSearchBar.setVisibility(View.GONE);
                    hospitalSearchList.setVisibility(View.GONE);

                }else {
                    hospitalPager.setVisibility(View.GONE);
                    hospitalTabLayout.setVisibility(View.GONE);
                    hospitalSearchList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null){
                    Toast.makeText(hospitalActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }


                hospitalListAdapter.clear();
                startSearching(text.toString().toLowerCase());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearching(final String s) {
        final boolean[] hasData = {false};
        // TODO: Send messages on click
        Query hekkQuery = databaseReference;
        hekkQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if (snapshot.hasChild("appointments")){

                    }else {
                        for (DataSnapshot snapshot1: snapshot.getChildren()){
                            snapshot1.getRef().orderByChild("instituteName")
                                    .startAt(s)
                                    .endAt(s+"\uf8ff").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    if(snapshot2.child("doctorInfo").getValue() == null){
                                    }else {
                                        hasData[0] = true;
                                        doctorInfo info = snapshot2.child("doctorInfo").getValue(doctorInfo.class);
                                        hospitalListAdapter.add(info);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if(hasData[0]){

        }else {
            Toast.makeText(hospitalActivity.this, "No Results Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patientmenu, menu);

        if(com.beastek.entidadesonline.doctorPreference.getIsTapTargetShown(hospitalActivity.this)){

        }else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    final View view = findViewById(R.id.searchBar);

                    new MaterialTapTargetPrompt.Builder(hospitalActivity.this)
                            .setTarget(view)
                            .setBackgroundColour(getResources().getColor(R.color.actionBar))
                            .setPrimaryText("Search Hospitals")
                            .setSecondaryText("You can Search Hospitals Registered with us.")
                            /* .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                            {
                                @Override
                                public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                                {
                                    //TODO: Store in SharedPrefs so you don't show this prompt again.
                                    com.beastek.entidadesonline.doctorPreference.saveIsTapTargetShown(hospitalActivity.this, true);
                                }

                                @Override
                                public void onHidePromptComplete()
                                {
                                    com.beastek.entidadesonline.doctorPreference.saveIsTapTargetShown(hospitalActivity.this, true);
                                }
                            })  */
                            .show();

                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.logout1:
                logoutPatient();
                break;
            case R.id.searchBar:
                hospitalToolbar.setVisibility(View.GONE);
                hospitalSearchBar.setVisibility(View.VISIBLE);
                hospitalSearchBar.enableSearch();
                hospitalSearchBar.hideSuggestionsList();
                break;
            case R.id.aboutUs:
                mMaterialDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutUsDialog() {
        TextView textView = new TextView(this);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "Doctorave is a Complete app for Doctors and Patients. Developed by Bhavya Arora. More Features Coming soon... <br><br>&emsp;<a href='https://github.com/bhavya-arora'> Github </a> &emsp;" +
                "  <a href='https://in.linkedin.com/in/bhavya-arora-716b37145'> Linkedin </a> &emsp;  <a href='http://bhavya-arora.me/'> Website </a>" +
                "<br><br><strong>Privacy and Policy / Open Source Licenses:</strong> <a href='http://bhavya-arora.me/doctorave-privacy-policy'> Check Here. </a><br><br><Strong>Fork us on Github: </Strong><a href='https://github.com/bhavya-arora/Doctorave'>Here. </a><br><br> If any Issue/Bug contact us here: <a href=\"mailto:gobhavyaarora15@gmail.com?Subject=Hello%20again\" target=\"_top\">here</a>";
        textView.setText(Html.fromHtml(text));


        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Doctorave")
                .setMessage("Doctorave is a Complete App for Doctors and Patient. Developed by Bhavya Arora.")
                //.setContentView(R.layout.aboutusdialog)
                .setContentView(textView)
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mMaterialDialog.dismiss();
                    }
                });
    }

    private void logoutPatient() {
        com.beastek.entidadesonline.doctorPreference.savePhoneNumberInSP(this, null);
        com.beastek.entidadesonline.doctorPreference.saveBooleanInSP(this, false);
        com.beastek.entidadesonline.doctorPreference.saveIsTapTargetShown(this, false);
        FirebaseAuth.getInstance().getCurrentUser().delete();
        finish();
    }

}
