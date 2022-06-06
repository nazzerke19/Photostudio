package com.example.photostudio.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.photostudio.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button registbtn= findViewById(R.id.registerBtn);
        Button loginBtn = findViewById(R.id.loginButton);

         registbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 createAccountButtonClicked();
             }
         });

         loginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getBaseContext(), LoginActivity.class));
             }
         });


    }

    public void createAccountButtonClicked(){
        startActivity(new Intent(getBaseContext(),RegisterActivity.class));
    }
}