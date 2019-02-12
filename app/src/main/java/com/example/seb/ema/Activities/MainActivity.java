package com.example.seb.ema.Activities;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seb.ema.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private   FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;

    Button loginButton;
    Button signUpButton;
    String email;
    String password;
    EditText editTextPassword;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        loginButton=findViewById(R.id.button_sign_in);
        signUpButton=findViewById(R.id.buttonSignUp);
        editTextPassword=findViewById(R.id.editText_password);
        editText=findViewById(R.id.editText_username);


        loginButton.setOnClickListener(v -> login());

        signUpButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(getBaseContext(),   SingUp.class);
            startActivity(myIntent);
        });

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
                finish();

            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    void login(){
        email=editText.getText().toString();
        password=editTextPassword.getText().toString();

        if(password.isEmpty()){
            Toast.makeText(MainActivity.this, "Enter your password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(email.isEmpty()){
            Toast.makeText(MainActivity.this, "Enter your email",
                    Toast.LENGTH_SHORT).show();
            return;
        }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Authentication Sucess.",
                                    Toast.LENGTH_SHORT).show();

                            Intent myIntent = new Intent(getBaseContext(),   Main2Activity.class);
                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}

