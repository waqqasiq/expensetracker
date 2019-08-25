package com.example.expensetrackauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email,pass,confirmpass;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.pass);
        confirmpass = (EditText)findViewById(R.id.confirmpass);
        btnSignup = (Button)findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String e = email.getText().toString();
                    String p = pass.getText().toString();
                    String confirmp = confirmpass.getText().toString();
                    if(p.equals(confirmp)) {
                        mAuth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this, "SignUp Not successful", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(SignupActivity.this,"All fields must be filled", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}
