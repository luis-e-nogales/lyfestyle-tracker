package com.example.lyfestyletracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lyfestyletracker.ui.tabbed.FoodSectionsPagerAdapter;
import com.example.lyfestyletracker.utils.MaxPlan;
import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;

public class FoodDashboard extends AppCompatActivity {

    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_dashboard);
        FoodSectionsPagerAdapter foodSectionsPagerAdapter = new FoodSectionsPagerAdapter(this, getSupportFragmentManager(), getIntent().getStringExtra("username"));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(foodSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab_diet);

        username = getIntent().getStringExtra("username");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabLayout tb = (TabLayout) findViewById(R.id.tabs);
                if (tb.getSelectedTabPosition() == 0) {
                    Intent intent = new Intent(FoodDashboard.this, AddMeal.class);
                    intent.putExtra("username", getIntent().getStringExtra("username"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FoodDashboard.this, DietPlan.class);
                    intent.putExtra("username", username);

                    MaxPlan mp = new MaxPlan();
                    Integer maxInt = mp.getMaxPlan();
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into Plan Values(" + maxInt + ", '" + username + "')");
                    QueryExecutable qe = new QueryExecutable(map);
                    JSONArray ans = qe.run();

                    map.clear();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into Diet Values(" + maxInt + ", 0)");

                    qe = new QueryExecutable(map);
                    qe.run();

                    intent.putExtra("dietId", maxInt);
                    startActivity(intent);
                }
            }
        });
    }
}