package com.example.recreasphere.AdminSideScreens;

import static com.example.recreasphere.Utils.Constant.EVENTID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recreasphere.Fragments.JoinFragment;
import com.example.recreasphere.Model.Event;
import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SwimmingPoolActivity extends AppCompatActivity {
    FloatingActionButton add_btn;
    RecyclerView recyclerView;
    ArrayList<Event> eventArrayList=new ArrayList<Event>();
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimming_pool);
        add_btn=findViewById(R.id.add_btn);
        recyclerView=findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.EVENTTYPE="JOIN";
                startActivity(new Intent(SwimmingPoolActivity.this, CreatedEventActivity.class));
            }
        });
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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
                    long teamMembers=0;
                    DataSnapshot teamSnapshot = dataSnapshot1.child("Team Members");
                    if (teamSnapshot.exists()) {
                        teamMembers = teamSnapshot.getChildrenCount();
                        // Use teamACount as needed (e.g., display it, store it, etc.)
                    }

                    eventArrayList.add(new Event(
                            dataSnapshot1.child("EventTitle").getValue(String.class)
                            ,dataSnapshot1.child("EventDate").getValue(String.class)
                            ,dataSnapshot1.child("EventStartTime").getValue(String.class)
                            , dataSnapshot1.child("EventVenue").getValue(String.class)
                            , dataSnapshot1.child("EventId").getValue(String.class),
                            teamMembers+"",""
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
            View v= LayoutInflater.from(SwimmingPoolActivity.this).inflate(R.layout.item_swimming_pool,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {



            holder.event_title.setText("Title : "+eventArrayList.get(position).getEventTitle());
            holder.team_a_member.setText("Team Members \n "+eventArrayList.get(position).getTeamAMember()+"/10");
            holder.event_venu.setText("Venue : "+eventArrayList.get(position).getEventVenu());
            holder.event_date.setText("Date : "+eventArrayList.get(position).getEventDate());
            holder.event_time.setText("Time : "+eventArrayList.get(position).getEventStartTime());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] options = {"Detail","Update","Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(SwimmingPoolActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Update")) {
                                Constant.EVENTTYPE="JOIN";
                                Constant.event=new Event(
                                        eventArrayList.get(position).getEventTitle(),
                                        eventArrayList.get(position).getEventDate(),
                                        eventArrayList.get(position).getEventStartTime(),
                                        eventArrayList.get(position).getEventVenu(),
                                        eventArrayList.get(position).getEventId()
                                );
                                startActivity(new Intent(SwimmingPoolActivity.this, UpdateEventActivity.class));
                                dialog.dismiss();
                            }else if (options[item].equals("Detail")) {
                                Constant.EVENTTYPE="JOIN";
                                Constant.event=new Event(
                                        eventArrayList.get(position).getEventTitle(),
                                        eventArrayList.get(position).getEventDate(),
                                        eventArrayList.get(position).getEventStartTime(),
                                        eventArrayList.get(position).getEventVenu(),
                                        eventArrayList.get(position).getEventId()
                                );
                                EVENTID=eventArrayList.get(position).getEventId();
                                startActivity(new Intent(SwimmingPoolActivity.this, DetailSwimmingPoolEventActivity.class));
                                dialog.dismiss();
                            }else if (options[item].equals("Delete")) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                                        .child("JOIN").child(eventArrayList.get(position).getEventId());
                                databaseReference.removeValue();
                                getData();
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
                    ,team_a_member;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                event_title=itemView.findViewById(R.id.event_title);
                event_venu=itemView.findViewById(R.id.event_venu);
                event_date=itemView.findViewById(R.id.event_date);
                event_time=itemView.findViewById(R.id.event_time);
                team_a_member=itemView.findViewById(R.id.team_a_member);
                cardView=itemView.findViewById(R.id.cardView);

            }
        }
    }
}