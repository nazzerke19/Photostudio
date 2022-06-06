package com.example.photostudio.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.photostudio.R;
import com.example.photostudio.controllers.RoomsManager;
import com.example.photostudio.models.IAllRoomsLoadListener;
import com.example.photostudio.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RoomsListFragment extends Fragment implements IAllRoomsLoadListener{


    IAllRoomsLoadListener iAllRoomsLoadListener;
    static RoomsListFragment instance;
    CollectionReference roomsRef;
    Unbinder unbinder;
    RecyclerView recyclerView;

    public RoomsListFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.rooms_recyclerView)
    RecyclerView recyclerRooms;

    public static RoomsListFragment getInstance() {
        if(instance == null)
            instance = new RoomsListFragment();
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        roomsRef = FirebaseFirestore.getInstance().collection("rooms");
        iAllRoomsLoadListener = this;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_rooms_list,container,false);
        unbinder= ButterKnife.bind(this,view);
       initRecyclerView(view);
        loadAllRooms();

       return  view;
        }

        private  void initRecyclerView(View view){
//              recyclerView = view.findViewById((R.id.rooms_recyclerView));
//             LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//
//             recyclerView.setLayoutManager(layoutManager);



         //  loadAllRooms();
         //  RoomManager adapter = new RoomManager(getActivity(), rooms_List);
        Log.d(TAG,"nop");
          //  recyclerView.setAdapter(adapter);
    recyclerRooms.setHasFixedSize(true);
    recyclerRooms.setLayoutManager(new LinearLayoutManager(getContext()));

        }

    public void loadAllRooms(){
        roomsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               List<Room> roomList = new ArrayList<>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Room room = documentSnapshot.toObject(Room.class);
                        room.setRoom_id(documentSnapshot.getId());

                        roomList.add(room);
                    }
                    iAllRoomsLoadListener.onAllRoomsLoadSucces(roomList);
                    Log.d(TAG, roomList.toString());
                }
            }
        });



     /*  roomsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               if(queryDocumentSnapshots.isEmpty()) {
                   Log.d(TAG, "List Is EMpty");
               }else {
                       List roomList = queryDocumentSnapshots.toObjects(Room.class);
                   Log.d(TAG, "List Is not EMpty");

                     rooms_List.addAll(roomList);
                   Log.d(TAG, String.valueOf(roomList));
                   }
               }
       });*/

    }

    @Override
    public void onAllRoomsLoadSucces(List<Room> areaNameList) {
        RoomsManager adapter = new RoomsManager(getActivity(), areaNameList);

        recyclerRooms.setAdapter(adapter);

    }
}