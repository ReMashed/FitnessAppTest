package com.remashed.databasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        //Check if a user is already logged in
        if(firebaseAuth.getCurrentUser() != null) {
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        buttonSignIn = (Button) findViewById(R.id.signInButton);
        editTextEmail = (EditText) findViewById(R.id.emailText);
        editTextPassword = (EditText) findViewById(R.id.passwordText);
        textViewSignUp = (TextView) findViewById(R.id.signUpText);

        progressDialog = new ProgressDialog(this);

        //here we need to implement the onclick listener interface
        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further.
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution further.
            return;
        }
        //if validations are ok
        //will first show a progress bar
       // progressDialog.setMessage("Logging In ...");
       // progressDialog.show();

        //Inner oncomplete method wil be called when sign in is complete
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //start a profile activity
                            //since we're currently inside the onCompletelistener we can user "this"
                            //user getApplicationContext();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn) {
            userLogin();
        }

        if(view == textViewSignUp){
            finish(); //closes activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
