package com.example.recreasphere.UserSideScreens;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ReservJoinActivity extends AppCompatActivity {
    Spinner spinner;
    String selectedTeam;
    private Dialog loadingDialog;
    ArrayAdapter<String> spinnerAdapter;
    int teamAMembers,teamBMembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserv_join);
        teamAMembers= Integer.parseInt(Constant.event.getTeamAMember());
        teamBMembers=Integer.parseInt(Constant.event.getTeamBMember());

        spinner=findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.item_array));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
// Set a listener to handle item selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the spinner
                selectedTeam = (String) parent.getItemAtPosition(position);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });


    }

    public void saveRecord(){
        String id = null;
        try {
            id = createFavId().substring(0,7);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                    getReference().child("UserData")
                    .child(Constant.getUserId(this))
                    .child("History").child(id);
            String message="Booking for "+Constant.GAMETYPE+" game is confirmed for "+ Constant.event.getEventDate() +" and time is " +Constant.event.getEventStartTime()+" at "+ Constant.event.getEventVenu();
            databaseReference.child("Message").setValue(message);
            loadingDialog.show();
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                    .child(Constant.EVENTTYPE)
                    .child(Constant.event.getEventId())
                    .child(selectedTeam)
                    .child(Constant.getUserId(this));
            databaseReference1.child("MemberName").setValue(Constant.getUsername(this));
            databaseReference1.child("MemberId").setValue(Constant.getUserId(this));
            loadingDialog.dismiss();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    public void addMember(View view){
        if(!selectedTeam.equals("Select Team")){
            if(Constant.GAMETYPE.equals("BasketBallEvent")){
                if(selectedTeam.equals("Team A")&&teamAMembers<3){
                    saveRecord();
                }else if(selectedTeam.equals("Team B")&&teamBMembers<3){
                    saveRecord();
                }else {
                    Toast.makeText(ReservJoinActivity.this,"This Team is full you can not join them",Toast.LENGTH_LONG).show();
                }
            }else if(Constant.GAMETYPE.equals("PadelEvent")){
                if(selectedTeam.equals("Team A")&&teamAMembers<2){
                    saveRecord();
                }else if(selectedTeam.equals("Team B")&&teamBMembers<2){
                    saveRecord();
                }else {
                    Toast.makeText(ReservJoinActivity.this,"This Team is full you can not join them",Toast.LENGTH_LONG).show();
                }
            }else if(Constant.GAMETYPE.equals("FootBallEvent")){
                if(selectedTeam.equals("Team A")&&teamAMembers<5){
                    saveRecord();
                }else if(selectedTeam.equals("Team B")&&teamBMembers<5){
                    saveRecord();
                }else {
                    Toast.makeText(ReservJoinActivity.this,"This Team is full you can not join them",Toast.LENGTH_LONG).show();
                }
            }



        }else {
            Toast.makeText(ReservJoinActivity.this,"Please Select team",Toast.LENGTH_LONG).show();

        }
    }




}