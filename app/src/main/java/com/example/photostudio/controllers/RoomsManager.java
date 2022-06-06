package com.example.photostudio.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photostudio.R;
import com.example.photostudio.activity.BookingsActivity;
import com.example.photostudio.common.Common;
import com.example.photostudio.models.IRecyclerItemSelectedListener;
import com.example.photostudio.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RoomsManager extends  RecyclerView.Adapter<RoomsManager.MyViewHolder> {

   Context context;
   List<Room> rooms;
   LocalBroadcastManager localBroadcastManager;

    CollectionReference roomRef;


   public RoomsManager(Context context, List<Room> rooms){
    this.context = context;
    this.rooms = rooms;

    localBroadcastManager = LocalBroadcastManager.getInstance(context);
   }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_room, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.name.setText(rooms.get(i).getName());
        holder.shirina.setText(rooms.get(i).getShirina());
        holder.dlina.setText(rooms.get(i).getDlina());
        holder.price.setText(Double.toString(rooms.get(i).getPrice()));
        Glide.with(holder.imageView.getContext())
                .load(rooms.get(i).getImage_url())
                .into(holder.imageView);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {


                holder.itemView.setBackgroundColor(context.getResources()
                .getColor(R.color.colorPrimary));

                Intent intent = new Intent(context, BookingsActivity.class);

                Common.currentRoom = rooms.get(pos);
                Log.d(TAG,rooms.get(pos).toString()+rooms.get(pos).getName()+"  RoomsManager getrooms position ");
                context.startActivity(intent);
            }
        });


    }



    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, dlina, shirina, price;
        ImageView imageView;
        Button bookBtn;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        private IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.item_room_name);
            dlina = view.findViewById(R.id.item_room_dlina);
            shirina = view.findViewById(R.id.item_room_shirina);
            price = view.findViewById(R.id.item_room_price);
            imageView = view.findViewById(R.id.item_room_img);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }

    private void loadBookingByRoom(int roomID) {


        roomRef = FirebaseFirestore.getInstance()

                .collection("rooms");

        roomRef.get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Room> rooms = new ArrayList<Room>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                            Room room = queryDocumentSnapshot.toObject(Room.class);

                            room.setRoom_id(String.valueOf(Integer.parseInt(queryDocumentSnapshot.getId())));

                            rooms.add(room);
                        }
                        Intent intent = new Intent(Common.KEY_ROOMS_LOAD_DONE);
                        intent.putParcelableArrayListExtra(Common.KEY_ROOMS_LOAD_DONE, rooms );
                        localBroadcastManager.sendBroadcast(intent);

                    }
                }
        );


    }

}
