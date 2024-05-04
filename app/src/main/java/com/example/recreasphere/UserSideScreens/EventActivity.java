package com.example.recreasphere.UserSideScreens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.recreasphere.Adapter.ViewPagerAdapter;
import com.example.recreasphere.Fragments.BookEventFragment;
import com.example.recreasphere.Fragments.JoinEventFragment;
import com.example.recreasphere.R;
import com.google.android.material.tabs.TabLayout;

public class EventActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar=findViewById(R.id.toolbar);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);
        setSupportActionBar(toolbar);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new JoinEventFragment(),"Join");
        viewPagerAdapter.addFragment(new BookEventFragment(),"Book");
        viewPager.setAdapter(viewPagerAdapter);
    }
}