package com.example.recreasphere.AdminSideScreens;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.Fragments.BookFragment;
import com.example.recreasphere.Model.Event;
import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class BookEventDetailActivity extends AppCompatActivity {

    TextView member_name;
    private Dialog loadingDialog;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_event_detail);
        member_name=findViewById(R.id.member_name);
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
             getData();
    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                .child("BOOK")
                .child(Constant.EVENTID)
                .child("TeamMember");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                member_name.setText(dataSnapshot.child("MemberName").getValue(String.class));
                userId=dataSnapshot.child("MemberId").getValue(String.class);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void deleteMember(View view){
        sendMessage(userId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                .child("BOOK")
                .child(Constant.EVENTID)
                .child("TeamMember");
        databaseReference.removeValue();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                .child("BOOK")
                .child(Constant.EVENTID);
        myRef.child("EventStatus").setValue("Pending");
        finish();
    }

    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    public void sendMessage(String userId){
        String id = null;
        try {
            id = createFavId().substring(0,7);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                    getReference().child("UserData")
                    .child(userId)
                    .child("History").child(id);
            String message="Booking for "+Constant.GAMETYPE+" game is canceled at "+ Constant.event.getEventDate() +" and time is " +Constant.event.getEventStartTime()+" at "+ Constant.event.getEventVenu();
            databaseReference.child("Message").setValue(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}