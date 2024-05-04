package com.example.recreasphere.AdminSideScreens;

import static com.example.recreasphere.Utils.Constant.EVENTID;
import static com.example.recreasphere.Utils.Constant.GAMETYPE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recreasphere.MainActivity;
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

public class UserListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<EventMember> eventMemberArrayList=new ArrayList<EventMember>();
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
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
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().
                getReference().child("UserData");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    eventMemberArrayList.add(new EventMember(
                            dataSnapshot1.child("Name").getValue(String.class)
                            ,dataSnapshot1.child("UserId").getValue(String.class)
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
            View v= LayoutInflater.from(UserListActivity.this).inflate(R.layout.item_member,parent,false);
            return  new MemberAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final MemberAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {



            holder.member_name.setText("User Name : "+eventMemberArrayList.get(position).getMemberName());
        holder.btn_delete.setVisibility(View.GONE);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(UserListActivity.this, ChatActivity.class)
                            .putExtra("userId",eventMemberArrayList.get(position).getMemberId()));
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
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                member_name=itemView.findViewById(R.id.member_name);
                btn_delete=itemView.findViewById(R.id.btn_delete);
                cardView=itemView.findViewById(R.id.cardView);

            }
        }
    }
    public void finish(View view) {
        finish();
    }
}