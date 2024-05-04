package com.example.recreasphere.AdminSideScreens;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreatedEventActivity extends AppCompatActivity {
        EditText et_event_date,et_event_time,et_event_title,et_event_venu;

    private Calendar calendar;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_event);
        // Initialize calendar with current date
        calendar = Calendar.getInstance();
        et_event_date=findViewById(R.id.et_event_date);
        et_event_title=findViewById(R.id.et_event_title);
        et_event_venu=findViewById(R.id.et_event_venu);
        et_event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        et_event_time=findViewById(R.id.et_event_time);
        et_event_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void addEvent(View view){
           if(et_event_title.getText().toString().isEmpty()){
               et_event_title.setError("required");
           }else if(et_event_date.getText().toString().isEmpty()){
               et_event_date.setError("required");
           }else if(et_event_time.getText().toString().isEmpty()){
               et_event_time.setError("required");
           }else if(et_event_venu.getText().toString().isEmpty()){
               et_event_venu.setError("required");
           }
           else {
                 loadingDialog.show();
               String id = null;
               try {
                   id = createFavId().substring(0,7);
                   DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                           .child(Constant.EVENTTYPE).child(id);
                   myRef.child("EventVenue").setValue(et_event_venu.getText().toString());
                   myRef.child("EventId").setValue(id);
                   myRef.child("EventTitle").setValue(et_event_title.getText().toString());
                   if(Constant.EVENTTYPE.equals("BOOK")){
                       myRef.child("EventStatus").setValue("Pending");
                   }
                   myRef.child("EventDate").setValue(et_event_date.getText().toString());
                   myRef.child("EventStartTime").setValue(et_event_time.getText().toString());
                   Toast.makeText(CreatedEventActivity.this,"Event added",Toast.LENGTH_LONG).show();
               loadingDialog.dismiss();
               finish();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }


    }
    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    public void showTime(){

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        et_event_time.setText(selectedTime);

                    }
                }, hour, minute, true); // true for 24-hour time format

        // Show the time picker dialog
        timePickerDialog.show();


    }


    private void showDatePickerDialog() {
        // Set minimum date to today
        Calendar minDate = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Update calendar with selected date
            calendar.set(year, month, dayOfMonth);

            // Update TextView with selected date
            updateTextView();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Set minimum date for DatePickerDialog
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void updateTextView() {
        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());

        // Set the formatted date to TextView
        et_event_date.setText(formattedDate);
    }
}