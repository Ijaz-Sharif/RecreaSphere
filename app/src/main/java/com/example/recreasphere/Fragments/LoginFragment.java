package com.example.recreasphere.Fragments;

import static com.example.recreasphere.Utils.Constant.setAdminLoginStatus;
import static com.example.recreasphere.Utils.Constant.setUserId;
import static com.example.recreasphere.Utils.Constant.setUserLoginStatus;
import static com.example.recreasphere.Utils.Constant.setUsername;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recreasphere.AdminSideScreens.AdminMainActivity;
import com.example.recreasphere.MainActivity;
import com.example.recreasphere.R;
import com.example.recreasphere.Screens.AccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {
    private EditText etLoginEmail, etLoginPassword;
    TextView tv_new_register;
    private Dialog loadingDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        /////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // create the referances
        etLoginEmail =view.findViewById(R.id.et_login_email);
        etLoginPassword = view.findViewById(R.id.et_login_password);
        tv_new_register=view.findViewById(R.id.tv_new_register);
        Button btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();
                // call the validate function and then request
                if (validate(email, password)) requestLogin(email, password);
            }
        });


        tv_new_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AccountActivity)getActivity()).showSignUpScreen();
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String password) {
        if (email.isEmpty()) etLoginEmail.setError("Enter email!");
        else if (!email.contains("@")||!email.contains(".")) etLoginEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etLoginPassword.setError("Enter password!");
        else if (password.length()<6) etLoginPassword.setError("Password must be at least 6 characters!");
        else return true;
        return false;
    }
    private void requestLogin(String email, String password) {
        loadingDialog.show();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference().child("AdminData");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String adminEmail=dataSnapshot.child("adminEmail").getValue(String.class);
                    String adminPassword=dataSnapshot.child("adminPassword").getValue(String.class);
                    if(adminEmail.equals(email)&&adminPassword.equals(password)){
                        setAdminLoginStatus(getContext(), true);
                        setUserLoginStatus(getContext(), false);
                        setUsername(getContext(),"admin");
                        openAdminActivity();
                    }
                    else {
                        loginUser(email,password);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loginUser(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    loadingDialog.dismiss();
                    Toast.makeText(getContext(), "wrong mail or password" + task.getException(), Toast.LENGTH_LONG).show();
                } else if (task.isSuccessful()) {
                    getData();
                }
            }
        });
    }
    public void getData(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference().child("UserData")
                .child(firebaseUser.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    setUserId(getContext(),dataSnapshot.child("UserId").getValue(String.class));
                    setUsername(getContext(),dataSnapshot.child("Name").getValue(String.class));
                    setAdminLoginStatus(getContext(), false);

                    setUserLoginStatus(getContext(), true);
                    openHomeActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void openHomeActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
    private void openAdminActivity() {
        startActivity(new Intent(getActivity(), AdminMainActivity.class));
        getActivity().finish();
    }
}