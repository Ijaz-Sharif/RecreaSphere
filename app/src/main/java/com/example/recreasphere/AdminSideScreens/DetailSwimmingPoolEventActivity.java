package com.example.recreasphere.AdminSideScreens;

import static com.example.recreasphere.Utils.Constant.EVENTID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recreasphere.Model.EventMember;
import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class DetailSwimmingPoolEventActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<EventMember> eventMemberArrayList=new ArrayList<EventMember>();

    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_swimming_pool_event);
        recyclerView=findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                getData();
    }

    public void getData(){
        eventMemberArrayList.clear();
        loadingDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.GAMETYPE)
                .child(Constant.EVENTTYPE)
                .child(EVENTID)
                .child("Team Members");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    eventMemberArrayList.add(new EventMember(
                            dataSnapshot1.child("MemberName").getValue(String.class)
                            ,dataSnapshot1.child("MemberId").getValue(String.class)
                    ));

                }
                recyclerView.setAdapter(new MemberAdapter());
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ImageViewHoler> {

        public MemberAdapter(){

        }
        @NonNull
        @Override
        public MemberAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(DetailSwimmingPoolEventActivity.this).inflate(R.layout.item_member,parent,false);
            return  new MemberAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final MemberAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.member_name.setText("Title : "+eventMemberArrayList.get(position).getMemberName());

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(eventMemberArrayList.get(position).getMemberId());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                            .getReference(Constant.GAMETYPE)
                            .child(Constant.EVENTTYPE).
                            child(EVENTID).child("Team Members").
                            child(eventMemberArrayList.get(position).getMemberId());
                    databaseReference.removeValue();
                    getData();
                }
            });




        }

        @Override
        public int getItemCount() {
            return eventMemberArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView member_name;
            ImageView btn_delete;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                member_name=itemView.findViewById(R.id.member_name);
                btn_delete=itemView.findViewById(R.id.btn_delete);

            }
        }
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