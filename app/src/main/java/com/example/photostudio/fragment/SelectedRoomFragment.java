package com.example.photostudio.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.photostudio.R;
import com.example.photostudio.common.Common;
import com.example.photostudio.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectedRoomFragment extends Fragment {
    TextView name,dlina,shirina,description,price;
    TextView photographer1,photographer2,photographer3,photographer4;
    ImageView roomImage;
    CollectionReference roomReference;
    DocumentReference roomImagesReference;
    ImageSlider imageSlider;
    MaterialCardView card1,card2,card3,card4;
    static SelectedRoomFragment instance;



    public static Fragment getInstance() {
        if(instance == null)
            instance = new SelectedRoomFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activitity_room_details,container,false);
        name=view.findViewById(R.id.item_room_name);
        dlina = view.findViewById(R.id.item_room_dlina);
        shirina = view.findViewById(R.id.item_room_shirina);
        price = view.findViewById(R.id.item_room_price);
        roomImage = view.findViewById(R.id.item_room_img);
        description=view.findViewById(R.id.details_desc);
        imageSlider = view.findViewById(R.id.image_slider);

        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        card4 = view.findViewById(R.id.card4);

        photographer1 = view.findViewById(R.id.photographer1);
        photographer2 = view.findViewById(R.id.photographer2);
        photographer3 = view.findViewById(R.id.photographer3);
        photographer4 = view.findViewById(R.id.photographer4);

        loadRoomDetails();
        selectedPhootogtapherSite();
        photographerCardChecked();
        return view;

    }

    public void loadRoomDetails(){
        Intent intent = new Intent("");

        roomImagesReference = FirebaseFirestore.getInstance()
                .collection("rooms")
                .document(Common.currentRoom.getRoom_id())
                .collection("images")
                .document("image");
        roomImagesReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot!=null && documentSnapshot.exists()){
                        List <SlideModel> imageList = new ArrayList<SlideModel>();
                        imageList.add(new SlideModel(documentSnapshot.getString("image1"), "1", ScaleTypes.FIT));
                        imageList.add(new SlideModel(documentSnapshot.getString("image2"),"2",ScaleTypes.FIT));
                        imageList.add(new SlideModel(documentSnapshot.getString("image3"), "3",ScaleTypes.FIT));

                        imageSlider.setImageList(imageList);
                    }
                }
            }
        });

        roomReference = FirebaseFirestore.getInstance()

                .collection("rooms");
    roomReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            List<Room> roomList = new ArrayList<>();
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                    Room room = documentSnapshot.toObject(Room.class);
                    room.setRoom_id(documentSnapshot.getId());
                    roomList.add(room);
                }
                 Log.d(TAG, String.valueOf(Common.currentRoom.getRoom_id())+"  Selected room current user isss ");


                name.setText(Common.currentRoom.getName());
                dlina.setText(Common.currentRoom.getDlina());
                shirina.setText(Common.currentRoom.getShirina());
                price.setText(Double.toString(Common.currentRoom.getPrice()) );
                description.setText(Common.currentRoom.getDescription());
                Log.d(TAG, roomList.toString());
            }
        }
    });

}

    public void selectedPhootogtapherSite(){



        photographer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photographer1.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
        photographer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photographer2.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });

        photographer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photographer3.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
        photographer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photographer4.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });

    }

    public void photographerCardChecked(){
       card1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        card1.toggle();
                                        card2.setChecked(false);
                                        card3.setChecked(false);
                                        card4.setChecked(false);
                                        if (card1.isChecked()==true) {
                                            Common.photgrapher = photographer1.getText().toString();
                                        }
                                    }
                                }
       );

       card2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               card2.toggle();
               card1.setChecked(false);
               card3.setChecked(false);
               card4.setChecked(false);
               if (card2.isChecked()==true){
                   Common.photgrapher = photographer2.getText().toString();
           }
           }
       });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card3.toggle();
                card2.setChecked(false);
                card1.setChecked(false);
                card4.setChecked(false);
                if (card3.isChecked()==true){
                    Common.photgrapher = photographer3.getText().toString();
                }
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card4.toggle();
                card2.setChecked(false);
                card3.setChecked(false);
                card1.setChecked(false);
                if (card4.isChecked()==true){
                    Common.photgrapher = photographer4.getText().toString();
                }
            }
        });
}
}
