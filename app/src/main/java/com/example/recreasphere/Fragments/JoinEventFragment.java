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
import com.example.recreasphere.UserSideScreens.ReservJoinActivity;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class JoinEventFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Event> eventArrayList=new ArrayList<Event>();
    private Dialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_join_event, container, false);
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
                .child("JOIN");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    long teamACount=0;
                    DataSnapshot teamASnapshot = dataSnapshot1.child("Team A");
                    if (teamASnapshot.exists()) {
                        teamACount = teamASnapshot.getChildrenCount();
                        // Use teamACount as needed (e.g., display it, store it, etc.)
                    }
                    long teamBCount =0;
                    DataSnapshot teamBSnapshot = dataSnapshot1.child("Team B");
                    if (teamBSnapshot.exists()) {
                        teamBCount = teamBSnapshot.getChildrenCount();
                        // Use teamACount as needed (e.g., display it, store it, etc.)
                    }

                    eventArrayList.add(new Event(
                            dataSnapshot1.child("EventTitle").getValue(String.class)
                            ,dataSnapshot1.child("EventDate").getValue(String.class)
                            ,dataSnapshot1.child("EventStartTime").getValue(String.class)
                            , dataSnapshot1.child("EventVenue").getValue(String.class)
                            , dataSnapshot1.child("EventId").getValue(String.class),
                            teamACount+"",teamBCount+""
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
            View v= LayoutInflater.from(getContext()).inflate(R.layout.item_event,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {



            holder.event_title.setText("Title : "+eventArrayList.get(position).getEventTitle());
            if(Constant.GAMETYPE.equals("PadelEvent")){
                holder.team_a_member.setText("Team A \n "+eventArrayList.get(position).getTeamAMember()+"/2");
                holder.team_b_member.setText("Team B \n "+eventArrayList.get(position).getTeamBMember()+"/2");
            }else if(Constant.GAMETYPE.equals("BasketBallEvent")){
                holder.team_a_member.setText("Team A \n "+eventArrayList.get(position).getTeamAMember()+"/3");
                holder.team_b_member.setText("Team B \n "+eventArrayList.get(position).getTeamBMember()+"/3");
            }else {
                holder.team_a_member.setText("Team A \n "+eventArrayList.get(position).getTeamAMember()+"/5");
                holder.team_b_member.setText("Team B \n "+eventArrayList.get(position).getTeamBMember()+"/5");
            }
            holder.event_venu.setText("Venue : "+eventArrayList.get(position).getEventVenu());
            holder.event_date.setText("Date : "+eventArrayList.get(position).getEventDate());
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
                                if(Constant.GAMETYPE.equals("PadelEvent")){
                                    if(eventArrayList.get(position).getTeamAMember().equals("2")
                                            &&eventArrayList.get(position).getTeamBMember().equals("2")){
                                        Toast.makeText(getContext(),"This Team is full you can not join them",Toast.LENGTH_LONG).show();

                                    }else {

                                        Constant.EVENTTYPE="JOIN";
                                        Constant.event=new Event(
                                                eventArrayList.get(position).getEventTitle(),
                                                eventArrayList.get(position).getEventDate(),
                                                eventArrayList.get(position).getEventStartTime(),
                                                eventArrayList.get(position).getEventVenu(),
                                                eventArrayList.get(position).getEventId(),
                                                eventArrayList.get(position).getTeamAMember()
                                                ,eventArrayList.get(position).getTeamBMember()
                                        );
                                        startActivity(new Intent(getContext(), ReservJoinActivity.class));

                                    }
                                }else if(Constant.GAMETYPE.equals("BasketBallEvent")){
                                    if(eventArrayList.get(position).getTeamAMember().equals("3")
                                            &&eventArrayList.get(position).getTeamBMember().equals("3")){
                                        Toast.makeText(getContext(),"This Team is full you can not join them",Toast.LENGTH_LONG).show();

                                    }else {

                                        Constant.EVENTTYPE="JOIN";
                                        Constant.event=new Event(
                                                eventArrayList.get(position).getEventTitle(),
                                                eventArrayList.get(position).getEventDate(),
                                                eventArrayList.get(position).getEventStartTime(),
                                                eventArrayList.get(position).getEventVenu(),
                                                eventArrayList.get(position).getEventId(),
                                                eventArrayList.get(position).getTeamAMember()
                                                ,eventArrayList.get(position).getTeamBMember()
                                        );
                                        startActivity(new Intent(getContext(), ReservJoinActivity.class));

                                    }
                                }
                                else {
                                    if(eventArrayList.get(position).getTeamAMember().equals("5")
                                            &&eventArrayList.get(position).getTeamBMember().equals("5")){
                                        Toast.makeText(getContext(),"This Team is full you can not join them",Toast.LENGTH_LONG).show();

                                    }else {

                                        Constant.EVENTTYPE="JOIN";
                                        Constant.event=new Event(
                                                eventArrayList.get(position).getEventTitle(),
                                                eventArrayList.get(position).getEventDate(),
                                                eventArrayList.get(position).getEventStartTime(),
                                                eventArrayList.get(position).getEventVenu(),
                                                eventArrayList.get(position).getEventId(),
                                                eventArrayList.get(position).getTeamAMember()
                                                ,eventArrayList.get(position).getTeamBMember()
                                        );
                                        startActivity(new Intent(getContext(), ReservJoinActivity.class));

                                    }
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
                    ,team_a_member,team_b_member;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                event_title=itemView.findViewById(R.id.event_title);
                event_venu=itemView.findViewById(R.id.event_venu);
                event_date=itemView.findViewById(R.id.event_date);
                event_time=itemView.findViewById(R.id.event_time);
                team_a_member=itemView.findViewById(R.id.team_a_member);
                team_b_member=itemView.findViewById(R.id.team_b_member);
                cardView=itemView.findViewById(R.id.cardView);

            }
        }
    }

}