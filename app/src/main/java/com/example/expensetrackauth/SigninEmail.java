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

import android.view.View;

public class SigninEmail extends AppCompatActivity {

    EditText name,email,pass;
    Button btnSigninEmail;
    //Button btnSignout;
    Button btnSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_email);


        mAuth = FirebaseAuth.getInstance();


        email = (EditText)findViewById(R.id.email);
        pass= (EditText)findViewById(R.id.pass);
        btnSigninEmail = (Button)findViewById(R.id.btnSigninEmail);
        btnSignup = (Button)findViewById(R.id.btnSignup);



        btnSigninEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String e = email.getText().toString();
                    String p = pass.getText().toString();
                    //e = "waqqas@gmail.com";
                    //p = "927727";

                    mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SigninEmail.this,MainActivity.class);
                                        intent.putExtra("btnSignIn",true);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                        //startActivity(new Intent(SigninEmail.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SigninEmail.this, "Sign in Not successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                catch (Exception e){
                    Toast.makeText(SigninEmail.this,"Both fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninEmail.this, SignupActivity.class));

            }
        });
    }

}
