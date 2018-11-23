package com.example.seb.ema;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class SingUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    Button signUpButton;
    String email;
    String password;
    EditText editText;
    EditText editTextPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        editText=findViewById(R.id.editText_email);
        editTextPassword=findViewById(R.id.editText_password);
       signUpButton=findViewById(R.id.buttonSignUp1);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });
    }

    public void createNewUser(){
        email=editText.getText().toString();
        password=editTextPassword.getText().toString();
        TextView textView= findViewById(R.id.textView);
        textView.setText(email+"\n"+password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SingUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            Toast.makeText(SingUp.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();



                            // updateUI(null);
                        }

                        // ...
                    }
                });

    }
}
