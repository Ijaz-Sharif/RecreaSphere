package com.example.recreasphere.UserSideScreens;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.AdminSideScreens.CreatedEventActivity;
import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ReservBookEvent extends AppCompatActivity {
    TextView member_name;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserv_book_event);
        member_name=findViewById(R.id.member_name);
        member_name.setText(Constant.getUsername(this));
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    public void reserveEvent(View view){



        String id = null;
        try {
            id = createFavId().substring(0,7);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                    getReference().child("UserData")
                    .child(Constant.getUserId(this))
                    .child("History").child(id);
            String message="Booking for "+Constant.GAMETYPE+" game is confirmed for "+ Constant.event.getEventDate() +" and time is " +Constant.event.getEventStartTime()+" at "+ Constant.event.getEventVenu();
            databaseReference.child("Message").setValue(message);

            if(Constant.GAMETYPE.equals("SwimmingPool")){
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                        .child("JOIN")
                        .child(Constant.event.getEventId())
                        .child("Team Members")
                        .child(Constant.getUserId(this));
                myRef.child("MemberName").setValue(Constant.getUsername(this));
                myRef.child("MemberId").setValue(Constant.getUserId(this));
                finish();
            }else {
                DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                        .child(Constant.EVENTTYPE)
                        .child(Constant.event.getEventId());
                myRef1.child("EventStatus").setValue("Booked");
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                        .child("BOOK")
                        .child(Constant.event.getEventId())
                        .child("TeamMember");
                myRef.child("MemberName").setValue(Constant.getUsername(this));
                myRef.child("MemberId").setValue(Constant.getUserId(this));
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}