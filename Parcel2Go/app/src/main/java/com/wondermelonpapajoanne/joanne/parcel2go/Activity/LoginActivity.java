package com.wondermelonpapajoanne.joanne.parcel2go.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.wondermelonpapajoanne.joanne.parcel2go.Activity.Customer.MainActivity;
import com.wondermelonpapajoanne.joanne.parcel2go.R;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Sam on 7/29/2017.
 */

public class LoginActivity extends AppCompatActivity{
    private static final String TAG  = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText editTextEmailText;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView txtSignupLink;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextEmailText = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        buttonLogin = (Button) findViewById(R.id.btn_login);
        txtSignupLink = (TextView) findViewById(R.id.link_signup);

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                login();
            }
        });

        txtSignupLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                signUp();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void login()
    {
        if(!validate()){
            onLoginFailed();
            return;
        }

        buttonLogin.setEnabled(false);
        String email = editTextEmailText.getText().toString();
        String password = editTextPassword.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(LoginActivity.this, "loginUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private boolean validate()
    {
        boolean valid = true;
        String email = editTextEmailText.getText().toString();
        String password = editTextPassword.getText().toString();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailText.setError("enter a valid email address");
            valid = false;
        }
        else{
            editTextEmailText.setError(null);
        }

        if(password.isEmpty() || password.length() <4 || password.length() > 12){
            editTextPassword.setError("between 4 and 12 alphanumeric characters");
            valid = false;
        }
        else{
            editTextPassword.setError(null);
        }

        return valid;
    }

    private void onLoginFailed()
    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        buttonLogin.setEnabled(true);
    }

    private void signUp(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }

}