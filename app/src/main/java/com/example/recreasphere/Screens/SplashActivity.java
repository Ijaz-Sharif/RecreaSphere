package com.example.recreasphere.Screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.AdminSideScreens.AdminMainActivity;
import com.example.recreasphere.MainActivity;
import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        startApp();
    }
    public void startApp(){


        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    if(Constant.getUserLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else  if(Constant.getAdminLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, AdminMainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, AccountActivity.class));
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}