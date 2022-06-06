package com.example.photostudio.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.photostudio.R;
import com.example.photostudio.common.Common;
import com.example.photostudio.controllers.ClientManager;
import com.example.photostudio.databaseHandler.CommonUtils;

import com.example.photostudio.databaseHandler.FirebaseHandler;
import com.example.photostudio.models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    ClientManager clientManager = new ClientManager();
    CommonUtils commonUtils = new CommonUtils();
    FirebaseFirestore db;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_client_login2);

         db = FirebaseFirestore.getInstance();
        Button submitBtn= findViewById(R.id.loginBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLoginButtonClicked();
                //
                // startActivity(new Intent(getBaseContext(),HomeActivity.class));
            }
        });

    }

    private void submitLoginButtonClicked() {

        EditText usernameField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);

        String username =usernameField.getText().toString();
        String password = passwordField.getText().toString();
        Client loggingInClient = new Client(username,password);


       showAdmin(username,password);


       if (clientManager.isClientInFirebase(username)){
            Toast.makeText(getApplicationContext(), "Пайдаланушы тіркелмеген!", Toast.LENGTH_LONG).show();
        } else
            if (clientManager.isClientInFirebase(username,password)) {
           Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

           CollectionReference usersCollection = db.collection("users");
           Query query = usersCollection.whereEqualTo("email",username).whereEqualTo("password",password);
           query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   for (DocumentSnapshot documentSnapshot: task.getResult())
                       if(task.isSuccessful()){
                           Toast.makeText(getApplicationContext(),"Қош келдіңіз!",Toast.LENGTH_LONG).show();
                           Client client = documentSnapshot.toObject(Client.class);
                           Common.currentClient=client;
                           Log.d(TAG, "client manager   " + Common.currentClient.getName());
                           startActivityForResult(intent, 1);
                       }
               }

           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Құпиясөз немесе почта қате!", Toast.LENGTH_LONG).show();

               }
           });

       }
    }

    public void showAdmin(String user, String pass){
        if (user.equals("admin") && pass.equals("admin")) {
            //start Activity Admin
        }
    }

}