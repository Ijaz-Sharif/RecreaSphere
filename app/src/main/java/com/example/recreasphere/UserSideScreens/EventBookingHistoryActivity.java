package com.example.recreasphere.UserSideScreens;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recreasphere.AdminSideScreens.UserListActivity;
import com.example.recreasphere.Model.EventMember;
import com.example.recreasphere.R;
import com.example.recreasphere.Screens.ChatActivity;
import com.example.recreasphere.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventBookingHistoryActivity extends AppCompatActivity {
    ArrayList<String> messagesList=new ArrayList<String>();
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking_history);
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
        messagesList.clear();
        loadingDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference().child("UserData")
                .child(Constant.getUserId(this))
                .child("History");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    messagesList.add(dataSnapshot1.child("Message").getValue(String.class));

                }
               recyclerView.setAdapter(new MessageAdapter());
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ImageViewHoler> {

        public MessageAdapter(){

        }
        @NonNull
        @Override
        public MessageAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(EventBookingHistoryActivity.this).inflate(R.layout.item_history_message,parent,false);
            return  new MessageAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final MessageAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {



            holder.message_user.setText(messagesList.get(position));




        }

        @Override
        public int getItemCount() {
            return messagesList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView message_user;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                message_user=itemView.findViewById(R.id.message_user);

            }
        }
    }
    public void finish(View view) {
        finish();
    }
}