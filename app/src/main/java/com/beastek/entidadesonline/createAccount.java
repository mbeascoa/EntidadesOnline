package com.beastek.entidadesonline;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createAccount extends AppCompatActivity {

    private Button createAccountButton;
    private EditText firstName;
    private EditText lastName;
    private EditText userPhone;
    private EditText userEmail;
    private EditText passwordField;
    private EditText confirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("Crear Cuenta");

        if(doctorPreference.getBooleanFromSP(this)){
            Intent intent = new Intent(this, patientsListActivity.class);
            startActivity(intent);
        }

        createAccountButton = findViewById(R.id.createAccountButton);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDoctorAccount();
            }
        });
    }

    public void createDoctorAccount(){

        String firstname = firstName.getText().toString().trim();
        String lastname = lastName.getText().toString().trim();
        String userphone = userPhone.getText().toString().trim();
        String useremail = userEmail.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmpassword = passwordField.getText().toString().trim();

        if(TextUtils.isEmpty(firstname)){
            firstName.setError("El nombre no puede estar vac??o");
        }else {
            if(TextUtils.isEmpty(lastname)){
                lastName.setError("El apellido no puede estar vac??o");
            }else {
                if(TextUtils.isEmpty(userphone)){
                    userPhone.setError("El telefono no puede estar vac??o");
                }else {
                    if(TextUtils.isEmpty(useremail)){
                        userEmail.setError("El correo no puede estar vac??o");
                    }else {
                        if(TextUtils.isEmpty(password)){
                            passwordField.setError("La contrase??a no puede estar vac??a");
                        }else {
                            if(password.length() < 6){
                                passwordField.setError("La contrase??a debe tener m??s de 6 caracteres");
                            }else {
                                if(TextUtils.isEmpty(confirmpassword)){
                                    confirmPasswordField.setError("La confirmaci??n de la contrase??a no puede estar vac??a");
                                }else {
                                    if(passwordField.getText().toString().equals(confirmPasswordField.getText().toString())){
                                        Intent intent = new Intent(this, createAccount2Activity.class);
                                        intent.putExtra("firstname", firstname);
                                        intent.putExtra("lastname", lastname);
                                        intent.putExtra("userphone", userphone);
                                        intent.putExtra("useremail", useremail);
                                        intent.putExtra("password", password);
                                        startActivity(intent);
                                    }else {
                                        confirmPasswordField.setError("La contrase??a no coincide");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
