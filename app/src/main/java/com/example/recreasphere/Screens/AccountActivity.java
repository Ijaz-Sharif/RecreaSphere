package com.example.recreasphere.Screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recreasphere.Fragments.LoginFragment;
import com.example.recreasphere.Fragments.SignUpFragment;
import com.example.recreasphere.R;
import com.example.recreasphere.Utils.Constant;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        showLoginScreen();
    }
    // function to load login fragment in activity
    public void showLoginScreen(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new LoginFragment()).commit();
    }
    // function to call signup fragment in activity
    public void showSignUpScreen(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new SignUpFragment()).commit();
    }

}