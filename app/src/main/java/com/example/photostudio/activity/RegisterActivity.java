package com.example.photostudio.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photostudio.R;
import com.example.photostudio.controllers.ClientManager;
import com.example.photostudio.databaseHandler.CommonUtils;

import com.example.photostudio.models.Client;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    EditText mName,mEmail,mPass,mPhone;
    Button mRegisterBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CommonUtils commonUtils = new CommonUtils();
    ClientManager clientManager = new ClientManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.editTextTextPersonName);
        mEmail = findViewById(R.id.editTextTextEmailAddress);
        mPhone = findViewById(R.id.editTextPhone);
        mPass= findViewById(R.id.editTextPassword);
        mRegisterBtn = findViewById(R.id.registerbutton);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String phone = mPhone.getText().toString();
                String pass = mPass.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Password is Required.");
                    return;
                }

                if(name.length() < 6){
                    mName.setError("Password Must be atleast 6 Characters");
                    return;
                }


                if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Phone No is Required.");
                    return;
                }

               // String id = db.collection("users").document().getId();
                String id = db.collection("users").document().getId();
                DocumentReference newUser = db.collection("users").document(id);
                Client newClient = clientManager.createClient(id,name,email,pass,phone);

                Client registeringClient = new Client(email,pass);


              if (!clientManager.isClientInFirebase(email))
              {

                  clientManager.addClientToFirebase(newClient,newUser);

                  Toast.makeText(getApplicationContext(), "Қолданушы аккаунты құрылды", Toast.LENGTH_LONG).show();


                  Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

//                  intent.putExtra("client_id", currentClient.getId());
//                  intent.putExtra("client_email", currentClient.getEmail());

                  startActivityForResult(intent, 1);
              } else  Toast.makeText(getApplicationContext(), "Қолданушы аккаунты құрылмады", Toast.LENGTH_LONG).show();



            }
        });
    }

}