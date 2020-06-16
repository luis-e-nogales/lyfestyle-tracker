package com.example.lyfestyletracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TabHost;

import com.example.lyfestyletracker.ui.main.ExerciseSectionsPagerAdapter;

public class ExerciseDashboard extends AppCompatActivity {

    private String username;
    private boolean currentTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_exercise_dashboard);
        ExerciseSectionsPagerAdapter exerciseSectionsPagerAdapter = new ExerciseSectionsPagerAdapter(this, getSupportFragmentManager(), getIntent().getStringExtra("username"));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(exerciseSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab_diet);






        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabLayout tb = (TabLayout) findViewById(R.id.tabs);
                if (tb.getSelectedTabPosition() == 0){
                    Intent intent = new Intent(ExerciseDashboard.this, AddWorkout.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }else{

                }


            }
        });
    }




}