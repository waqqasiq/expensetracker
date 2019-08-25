package com.example.expensetrackauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


//https://www.youtube.com/watch?v=SfV6sumayW0 watch this video for google sign in with firebase for android

public class SigninActivity extends AppCompatActivity {
    private static final String TAG = SigninActivity.class.getSimpleName();

    EditText name,email,pass;
    Button btnSignin;
    //Button btnSignout;
    Button btnSignup;
    SignInButton btnGoogleSignIn;


    private FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();
        btnSignin = (Button)findViewById(R.id.btnSignin);
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
//            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
/*
        email = (EditText)findViewById(R.id.email);
        pass= (EditText)findViewById(R.id.pass);
        btnSignin = (Button)findViewById(R.id.btnSignin);
        btnSignup = (Button)findViewById(R.id.btnSignup);*/

        btnGoogleSignIn = (SignInButton)findViewById(R.id.mygooglebutton);
        setGooglePlusButtonText(btnGoogleSignIn,"Sign in with Google"); //changing text of google sign in button

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent(); //need to declare this variable mGoogleSignInClient and then initialize inside onCreate
                startActivityForResult(signInIntent, 101); //101 was set according to a video

            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser()==null) {
                    Intent intent = new Intent(SigninActivity.this, SigninEmail.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else{
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


        /*btnSignin.setOnClickListener(new View.OnClickListener() {
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
                                        startActivity(new Intent(SigninActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SigninActivity.this, "Sign in Not successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                catch (Exception e){
                    Toast.makeText(SigninActivity.this,"Both fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
                finish();
            }
        });*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso); //initializing



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) { //101 was used above as well in SignIn method thats why 101
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){ // need to create this method

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                            intent.putExtra("btnSignIn",true);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SigninActivity.this, "Sign in successful with Google", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Could not login with Google", Toast.LENGTH_SHORT).show();

                            //Snackbar.make(findViewById(R.id.activity_signin), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }
    // this method is used to change the Google sign in button text
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

}
//waqqas@gmail.com pass: 927727

