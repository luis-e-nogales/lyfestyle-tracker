package com.example.lyfestyletracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SleepMonthly#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepMonthly extends Fragment {

    private String username;
    private View thisView;

    public SleepMonthly() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username username.
     * @return A new instance of fragment.
     */
    public static SleepMonthly newInstance(String username) {
        SleepMonthly fragment = new SleepMonthly();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_sleep_monthly, container, false);
        updateMonthlyGraph(thisView);
        return thisView;
    }

    protected void updateMonthlyGraph(View v) {
        BarChart barChart2 = v.findViewById(R.id.monthly_bar_chart_avg);
        barChart2.clear();
        BarChart barChart3 = v.findViewById(R.id.monthly_bar_chart_sum);
        barChart3.clear();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "select distinct trunc(sleepdate, 'IW') AS weekDate, AVG(sleepTime) AS average, SUM(sleepTime) as sum from UserSleepEntry where username = '" + username + "' AND sleepdate >= TRUNC(SYSDATE,'mm') GROUP BY trunc(sleepdate, 'IW') ORDER BY trunc(sleepdate, 'IW')");

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        ArrayList<BarEntry> barEntries2 = new ArrayList<>();

        String[] dates = new String[res.length()];

        for (int i = 0; i < res.length(); i++) {
            try {
                barEntries1.add(new BarEntry(i, Float.parseFloat(res.getJSONObject(i).getString("AVERAGE"))));
                barEntries2.add(new BarEntry(i, Integer.parseInt(res.getJSONObject(i).getString("SUM"))));
                dates[i] = "week" + i;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries1, "avg");
        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "sum");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet2.setColors(ColorTemplate.JOYFUL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);
        BarData data2 = new BarData(barDataSet2);
        data2.setBarWidth(0.9f);

        barChart2.setData(data);
        barChart3.setData(data2);

        barChart2.getDescription().setEnabled(false);
        barChart3.getDescription().setEnabled(false);
        barChart2.getAxisLeft().setAxisMinimum(0);
        barChart3.getAxisLeft().setAxisMinimum(0);
        barChart2.getAxisRight().setAxisMinimum(0);
        barChart3.getAxisRight().setAxisMinimum(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMonthlyGraph(thisView);
    }
}