package com.example.photostudio.activity;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.photostudio.R;
import com.example.photostudio.controllers.ClientManager;

import com.example.photostudio.fragment.ClientsPageFragment;
import com.example.photostudio.fragment.RoomBookingFragment;
import com.example.photostudio.fragment.RoomsListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog bottomSheetDialog;
    CollectionReference userRef;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);
        userRef= FirebaseFirestore.getInstance().collection("users");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        String clientID = getIntent().getStringExtra("client_id");
        Log.d(TAG,"home activity started");

        if (getSupportFragmentManager().beginTransaction().isEmpty()){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ClientsPageFragment.getInstance())
                    .commit();
        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d(TAG,"choose");
                if (menuItem.getItemId() == R.id.action_home)
                    fragment = new ClientsPageFragment();
                else if (menuItem.getItemId() == R.id.action_rooms)
                    fragment = new RoomsListFragment();

                return loadFragment(fragment);
            }
        });

      ///  Client cLient = clientManager.getClientFromDB(db, clientID); добавить поиск клиента с файрбэйс

    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }
}