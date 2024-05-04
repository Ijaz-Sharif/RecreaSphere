package com.example.recreasphere.AdminSideScreens;

import static com.example.recreasphere.Utils.Constant.GAMETYPE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.MainActivity;
import com.example.recreasphere.R;
import com.example.recreasphere.Screens.AccountActivity;
import com.example.recreasphere.Screens.ChatActivity;
import com.example.recreasphere.Utils.Constant;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }
    public void openFootBallRecord(View view)
    {
                    GAMETYPE="FootBallEvent";
                   startActivity(new Intent(this, EventMainActivity.class));
    }
    public void openPadelRecord(View view)
    {
        GAMETYPE="PadelEvent";
        startActivity(new Intent(this, EventMainActivity.class));
    }
    public void openBasketBallRecord(View view)
    {
        GAMETYPE="BasketBallEvent";
        startActivity(new Intent(this, EventMainActivity.class));
    }

    public void openSwimmingPoolRecord(View view)
    {
        GAMETYPE="SwimmingPool";
        startActivity(new Intent(this, SwimmingPoolActivity.class));
    }



    public void showLogoutAlert(View view){
        final CharSequence[] options = {"Chat","LogOut", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle("Select option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("LogOut")) {
                    Constant.setAdminLoginStatus(AdminMainActivity.this,false);
                    Constant.setUserLoginStatus(AdminMainActivity.this,false);
                    startActivity(new Intent(AdminMainActivity.this, AccountActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    dialog.dismiss();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }else if (options[item].equals("Chat")) {
                    startActivity(new Intent(AdminMainActivity.this, UserListActivity.class));
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }
}