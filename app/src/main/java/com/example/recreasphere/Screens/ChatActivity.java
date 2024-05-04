package com.example.recreasphere.Screens;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recreasphere.Model.ChatMessage;
import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    ConstraintLayout message_text;
    private Dialog loadingDialog;
    ImageView sendMessage;
    EditText input;
    RecyclerView listOfMessages;
    String userId = "";
    static String userName = "";
    private FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
/////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        listOfMessages = findViewById(R.id.list_of_messages);
        listOfMessages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        message_text = findViewById(R.id.message_text);
        sendMessage = findViewById(R.id.send_message);
        input = findViewById(R.id.input);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().isEmpty()) {
                    input.setError("required");
                } else {

                    FirebaseDatabase.getInstance().getReference("OnlineChat")
                            .child(userId).child("Chat")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    Constant.getUsername(ChatActivity.this))
                            );

                    // Clear the input
                    input.setText("");



                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userName=Constant.getUsername(ChatActivity.this);
        userId = getIntent().getStringExtra("userId");
        loadChat();
    }




    public void loadChat() {
        loadingDialog.show();
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("OnlineChat")
                .child(userId).child("Chat");

        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(chatReference, ChatMessage.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessage model) {
                // Bind data to your ViewHolder
                holder.bind(model);
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new ViewHolder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                return new ChatViewHolder(view);
            }
        };

        listOfMessages.setAdapter(adapter);
        adapter.startListening();
        loadingDialog.dismiss();
    }



    // Other methods

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView messageUser;
        TextView messageTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            messageUser = itemView.findViewById(R.id.message_user);
            messageTime = itemView.findViewById(R.id.message_time);
        }

        public void bind(ChatMessage message) {
            messageText.setText(message.getMessageText());
            messageUser.setText(message.getMessageUser());
            messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));
            if(message.getMessageUser().equals(userName)){
                messageText.setTextColor(getResources().getColor(R.color.colorPrimary));
                messageUser.setTextColor(getResources().getColor(R.color.colorPrimary));
                messageTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            else
            {
                messageText.setTextColor(getResources().getColor(R.color.teal_200));
                messageUser.setTextColor(getResources().getColor(R.color.teal_200));
                messageTime.setTextColor(getResources().getColor(R.color.teal_200));
            }
        }
    }

    public void finish(View view) {
        finish();
    }
}