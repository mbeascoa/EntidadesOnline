package com.beastek.entidadesonline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.concurrent.TimeUnit;

public class verifyPatient extends AppCompatActivity {

    private EditText countryCodeView;
    private EditText phoneNumberView;
    private Button sendCode;

    private ConnectivityManager connectivityManager;

    private static String fullPhoneNo;

    private SmsVerifyCatcher smsVerifyCatcher;

    private static ProgressDialog progressDialog;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_patient);
        //leemos el número de teléfono de las preferencias compartidas, en el caso de que existe el
        // número de telefóno nos vamos a hospitalActivity
        if (com.beastek.entidadesonline.doctorPreference.getPhoneNumberFromSP(this) != null){
            Intent intent = new Intent(this, com.beastek.entidadesonline.hospitalActivity.class);
            startActivity(intent);
        }

        //en el caso de que no tengamos el número de movil registrado en Firebase auth, lanzamos la verificación por SMS
        progressDialog=new ProgressDialog(this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Por favor, espere..");
        progressDialog.setCancelable(false);

        countryCodeView = findViewById(R.id.countryCodeView);
        phoneNumberView = findViewById(R.id.phoneNumberView);
        sendCode = findViewById(R.id.sendCode);

        firebasePhoneVerification();

        //Verificamos si tenemos conexión a internet
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //esperamos presione el botón de enviar código SMS, verificamos si está conectado a Internet
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null){
                    Toast.makeText(verifyPatient.this, "Verifique que está conectado a Internet", Toast.LENGTH_LONG).show();
                }else {
                    if(TextUtils.isEmpty(countryCodeView.getText().toString().trim())){
                        countryCodeView.setError("El campo código de País no puede estar vacío");
                    }else {
                        if(TextUtils.isEmpty(phoneNumberView.getText().toString().trim())){
                            phoneNumberView.setError("El campo telefono móvil no puede estar vacío");
                        }else{
                            progressDialog.show();
                            fullPhoneNo = countryCodeView.getText().toString().trim().concat(phoneNumberView.getText().toString().trim());
                            String numberToVerify = fullPhoneNo; //es la concatenación del codigo de país más el número de telefono
                            // que introduce el usuarios

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    fullPhoneNo,        // Phone number to verify
                                    60,                 // Timeout duration
                                    TimeUnit.SECONDS,   // Unit of timeout
                                    verifyPatient.this,               // Activity (for callback binding)
                                    mCallbacks);
                        }
                    }
                }
            }
        });

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
            }
        });

    }

    private void firebasePhoneVerification() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(verifyPatient.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressDialog.dismiss();
                Toast.makeText(verifyPatient.this, "Código Enviado ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(verifyPatient.this, com.beastek.entidadesonline.verifyPatient2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("verificationId", s);
                intent.putExtra("phone", fullPhoneNo);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
