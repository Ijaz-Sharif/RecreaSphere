package com.example.recreasphere;

import static com.example.recreasphere.Utils.Constant.GAMETYPE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.AdminSideScreens.SwimmingPoolActivity;
import com.example.recreasphere.Screens.AccountActivity;
import com.example.recreasphere.Screens.ChatActivity;
import com.example.recreasphere.UserSideScreens.EventActivity;
import com.example.recreasphere.UserSideScreens.EventBookingHistoryActivity;
import com.example.recreasphere.UserSideScreens.SwimmingPoolMainActivity;
import com.example.recreasphere.Utils.Constant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void openFootBallRecord(View view)
    {
        GAMETYPE="FootBallEvent";
        startActivity(new Intent(this, EventActivity.class));
    }
    public void openPadelRecord(View view)
    {
        GAMETYPE="PadelEvent";
        startActivity(new Intent(this, EventActivity.class));
    }
    public void openBasketBallRecord(View view)
    {
        GAMETYPE="BasketBallEvent";
        startActivity(new Intent(this, EventActivity.class));
    }
    public void openSwimmingPoolRecord(View view)
    {
        GAMETYPE="SwimmingPool";
        startActivity(new Intent(this, SwimmingPoolMainActivity.class));
    }



    public void showLogoutAlert(View view){
        final CharSequence[] options = {"History","Chat With Admin","LogOut", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("LogOut")) {
                    Constant.setAdminLoginStatus(MainActivity.this,false);
                    Constant.setUserLoginStatus(MainActivity.this,false);
                    startActivity(new Intent(MainActivity.this, AccountActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    dialog.dismiss();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("History")) {
                    startActivity(new Intent(MainActivity.this, EventBookingHistoryActivity.class));
                    dialog.dismiss();
                }else if (options[item].equals("Chat With Admin")) {
                    startActivity(new Intent(MainActivity.this, ChatActivity.class)
                            .putExtra("userId",Constant.getUserId(MainActivity.this)));
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }
}