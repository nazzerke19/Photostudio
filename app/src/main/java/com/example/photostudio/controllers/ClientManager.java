package com.example.photostudio.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.photostudio.activity.LoginActivity;
import com.example.photostudio.databaseHandler.FirebaseHandler;
import com.example.photostudio.models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClientManager {
    private static final String TAG = "CustomerController";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int exists, existsPass;
    FirebaseHandler firebaseHandler = new FirebaseHandler();

    // Reference to the Bookings collection in Firebase
    private CollectionReference usersCollection = db.collection("users");


    public Client createClient(String id,String name,String email, String password, String phone){
        Client client = new Client();
        client.setId(id);
        client.setPhone(phone);
        client.setName(name);
        client.setEmail(email);
        client.setPassword(password);


        return client;
    }
//
//    public void addClientToDB(DatabaseHandler db, Client client){
//        ContentValues clientValues = new ContentValues();
//
//        clientValues.put("name",client.getName());
//        clientValues.put("email",client.getEmail());
//        clientValues.put("phone",client.getPhone());
//        clientValues.put("password",client.getPassword());
//
//        db.write("client",clientValues);
//    }
//
//    public boolean isClientinDB(DatabaseHandler db, Client client){
//        Log.d(TAG, "Тұтынушының келесі деректемелері ДБ-да сақталғанын тексеру:");
//
//        ArrayList<Client> allClientsInDB = db.readAllClients();
//
//        for (int i=0;i<=allClientsInDB.size()-1;i++){
//            if (allClientsInDB.get(i).getEmail().equals(client.getEmail())){
//                if (allClientsInDB.get(i).getPassword().equals(client.getPassword())){
//                    Log.d(TAG,"Тұтынушы ДБ-да бар");
//                    return  true;
//                }
//            }
//        }
//            Log.d(TAG,"Тұтынушы ДБ-да жоқ");
//
//        return false;
//    }
//
//    public Client getClientFromDB(DatabaseHandler db, Client client) {
//        ArrayList<Client> allClientsInDB = db.readAllClients();
//
//        for (int i = 0; i <= allClientsInDB.size() - 1; i++) {
//                if (allClientsInDB.get(i).getEmail().equals(client.getEmail())) {
//                    if (allClientsInDB.get(i).getPassword().equals(client.getPassword())) {
//                        return allClientsInDB.get(i);
//                    }
//                }
//            }
//
//        return client;
//    }




// public Client getClientFromFirebase(Client client){
//
//     Query query = usersCollection.whereEqualTo("email",client.getEmail());
//     query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//         @Override
//         public void onComplete(@NonNull Task<QuerySnapshot> task) {
//             for (DocumentSnapshot documentSnapshot: task.getResult())
//                 if(task.isSuccessful()){
//
//                     String emailofUser = documentSnapshot.getString("email");
//                     if(emailofUser.equals(client.getEmail())){
//                      Client client = documentSnapshot.toObject(Client.class);
//                      Common.currentClient = client;
//                         Log.d(TAG, "client manager   " + client.getName());
//                     }
//
//                 }
//         }
//
//     });
//
//return client;
//
// }


    public void addClientToFirebase(Client client,DocumentReference documentReference ){
        documentReference.set(client);
    }

    public Boolean isClientInFirebase( String email){

        Query query = usersCollection.whereEqualTo("email",email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot: task.getResult())
                if(task.isSuccessful()){
                    String emailofUser = documentSnapshot.getString("email");
                    if(emailofUser.equals(email)){
                        Log.d(TAG,"User Exists");
                        return;
                       //  exists=1;
                    }
                }
            }

        });
    //if (exists==1) return true;
        return false;
    }

    public Boolean isClientInFirebase( String email,String password){

        Query query = usersCollection.whereEqualTo("email",email).whereEqualTo("password",password);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot: task.getResult())
                    if(task.isSuccessful()){
                        String emailofUser = documentSnapshot.getString("email");
                        String passOfUser = documentSnapshot.getString("password");
                        if(emailofUser.equals(email)&&passOfUser.equals(passOfUser)){
                            Log.d(TAG,"User Exists pass");
                            return;
                            //existsPass=1;
                        } else{ Log.d(TAG,"User NOT Exists"); }
                    }
            }

        });
       // if (existsPass==1) return true;
        return true;
    }
}
