package com.example.recreasphere.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recreasphere.Model.Event;
import com.example.recreasphere.R;
import com.example.recreasphere.UserSideScreens.ReservBookEvent;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookEventFragment extends Fragment {


    RecyclerView recyclerView;
    private Dialog loadingDialog;
    ArrayList<Event> eventArrayList=new ArrayList<Event>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_book_event, container, false);
        recyclerView=view.findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        return view;
    }

    @Override
    public void onStart() {
        getData();
        super.onStart();
    }

    public void getData(){

        loadingDialog.show();
        eventArrayList.clear();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                .child("BOOK");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    eventArrayList.add(new Event(
                            dataSnapshot1.child("EventTitle").getValue(String.class)
                            ,dataSnapshot1.child("EventDate").getValue(String.class)
                            ,dataSnapshot1.child("EventStartTime").getValue(String.class)
                            , dataSnapshot1.child("EventVenue").getValue(String.class)
                            , dataSnapshot1.child("EventId").getValue(String.class),
                            dataSnapshot1.child("EventStatus").getValue(String.class)
                    ));


                }

                recyclerView.setAdapter(new ArrayAdapter());
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getContext()).inflate(R.layout.item_book_event,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {



            holder.event_title.setText("Title : "+eventArrayList.get(position).getEventTitle());
            holder.event_venu.setText("Venue : "+eventArrayList.get(position).getEventVenu());
            holder.event_date.setText("Date : "+eventArrayList.get(position).getEventDate());
            holder.event_status.setText("Event Status : "+eventArrayList.get(position).getEventStatus());
            holder.event_time.setText("Time : "+eventArrayList.get(position).getEventStartTime());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] options = {"Book Event", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Book Event")) {
                                Constant.EVENTTYPE = "BOOK";
                                Constant.event=new Event(
                                        eventArrayList.get(position).getEventTitle(),
                                        eventArrayList.get(position).getEventDate(),
                                        eventArrayList.get(position).getEventStartTime(),
                                        eventArrayList.get(position).getEventVenu(),
                                        eventArrayList.get(position).getEventId()
                                );
                                if(eventArrayList.get(position).getEventStatus().equals("Pending")){
                                startActivity(new Intent(getContext(), ReservBookEvent.class));
                                }else {
                                    Toast.makeText(getContext(),"This is booked",Toast.LENGTH_LONG).show();

                                }
                                dialog.dismiss();
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }

                        }
                    });
                    builder.show();
                }
            });




        }

        @Override
        public int getItemCount() {
            return eventArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView event_title,event_venu,event_date,event_time
                    ,event_status;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                event_title=itemView.findViewById(R.id.event_title);
                event_venu=itemView.findViewById(R.id.event_venu);
                event_date=itemView.findViewById(R.id.event_date);
                event_time=itemView.findViewById(R.id.event_time);
                event_status=itemView.findViewById(R.id.event_status);
                cardView=itemView.findViewById(R.id.cardView);

            }
        }
    }
}