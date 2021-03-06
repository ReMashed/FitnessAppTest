package com.remashed.databasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;
    private ListView listViewUser;

    private EditText editTextName, editTextAddress;
    private Button buttonSave;

    private List<UserInformation> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        firebaseAuth = FirebaseAuth.getInstance();

        //if condition is true, user is not logged in
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSave =  (Button) findViewById(R.id.saveButton);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        listViewUser = (ListView) findViewById(R.id.listviewUser);
        userList = new ArrayList<>();

        textViewUserEmail = (TextView) findViewById(R.id.userEmailText);
        textViewUserEmail.setText("Welcome " + user.getEmail());

        buttonLogout = (Button) findViewById(R.id.logoutButton);

        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //method executed everytime we change something in the database
                userList.clear(); //clear list, to store the most current data in the database

                for(DataSnapshot userSnapShot: dataSnapshot.getChildren()) {
                    UserInformation user = userSnapShot.getValue(UserInformation.class);

                    userList.add(user);
                }

                 UserList adapter = new UserList(ProfileActivity.this, userList);
                listViewUser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation() {
        String name  = editTextName.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();

        //define object for the class we created
        UserInformation userInformation = new UserInformation(name, add);

        //to store the user information in firebase database, we can use the unique id of the logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //Every user has a unique id in the database, and we can use that id to create a node
        //Inside the node we will store information for the particular user
        //create a new child with the id of the logged in user, and use setValue
        //setValue takes an object and maps all the variables in it to the database
        databaseReference.child(user.getUid()).setValue(userInformation);
        //upload

    }

    @Override
    public void onClick(View view) {
        //if logout button is clicked
        if(view == buttonLogout){
            //call the method signout using firebase object
            firebaseAuth.signOut();
            //finish current activity
            finish();
            //launch the login page
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == buttonSave) {
            saveUserInformation();
            Toast.makeText(this, "Information Saved...", Toast.LENGTH_SHORT).show();
        }
    }
}
