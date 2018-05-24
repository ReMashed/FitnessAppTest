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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    //authorise firebase object
    //will use the object to register the user to the server
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initlise firebase object
        firebaseauth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        //If user is already logged in, navigate them to the mainpage (the profile page)
        if(firebaseauth.getCurrentUser() != null) {
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        //initialise views
        buttonRegister = (Button) findViewById(R.id.registerButton);
        editTextEmail = (EditText) findViewById(R.id.emailText);
        editTextPassword = (EditText) findViewById(R.id.passwordText);
        textViewSignIn = (TextView) findViewById(R.id.signText);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    private void registerUser() {
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
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        //this method creates a user on the firebase server with the email and password parameters
        //we can also attach a listener which will execute when the registration is complete
        firebaseauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Here we can check is the task is successful or not
                if (task.isSuccessful()) {
                    //user is sucessfully registered and logged in
                    //we will start the profile activty here
                        //profile activity here
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }else {
                    Toast.makeText(MainActivity.this, "Could not register.. please Try Again", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister){
            registerUser();
        }
        if (view == textViewSignIn) {
            //open login activity here
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
