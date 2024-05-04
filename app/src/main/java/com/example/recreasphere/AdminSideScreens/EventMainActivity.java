package com.example.recreasphere.AdminSideScreens;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.recreasphere.Adapter.ViewPagerAdapter;
import com.example.recreasphere.Fragments.BookFragment;
import com.example.recreasphere.Fragments.JoinFragment;
import com.example.recreasphere.R;
import com.google.android.material.tabs.TabLayout;

public class EventMainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_ball_main);
        toolbar=findViewById(R.id.toolbar);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);
        setSupportActionBar(toolbar);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new JoinFragment(),"Join");
        viewPagerAdapter.addFragment(new BookFragment(),"Book");
        viewPager.setAdapter(viewPagerAdapter);
    }
}