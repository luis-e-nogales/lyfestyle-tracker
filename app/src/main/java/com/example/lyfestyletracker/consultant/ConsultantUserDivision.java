package com.example.lyfestyletracker.consultant;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lyfestyletracker.R;
import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultantUserDivision extends Fragment {

    private String username;
    private View thisView;

    public ConsultantUserDivision() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username username.
     * @return A new instance of fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultantUserDivision newInstance(String username) {
        ConsultantUserDivision fragment = new ConsultantUserDivision();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_consultant_user_division, container, false);
        populateTable();
        return thisView;
    }

    public void populateTable() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select u.username, p.email, uhc.contractNumber From UserPerson u, UserHiresConsultant uhc, People p Where uhc.userUsername = u.username AND p.username = u.username AND uhc.consultantUsername = '" + username + "' AND NOT EXISTS ((Select planId FROM Plan WHERE createdByUsername = uhc.consultantUsername) MINUS (Select csp.planId FROM ConsultantSuggestsPlan csp Where csp.userUsername = u.username AND csp.consultantUsername = uhc.consultantUsername))");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        System.out.println(ans);

        TableLayout mainTable = thisView.findViewById(R.id.user_log_main_table);
        mainTable.removeAllViews();

        if (ans == null) {
            return;
        }

        for (int i = 0; i < ans.length(); i++) {
            try {
                JSONObject o = ans.getJSONObject(i);

                TableRow row = new TableRow(getContext());
                row.setWeightSum(1.0f);
                row.setPadding(0, 10, 0, 10);
                if (i % 2 == 0) {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light1));
                } else {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light2));
                }

                TableRow.LayoutParams paramsusername = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.28f);
                TableRow.LayoutParams paramsEmail = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.42f);
                TableRow.LayoutParams paramsContract = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.15f);

                TextView usernameText = new TextView(getContext());
                usernameText.setText(o.getString("USERNAME"));
                usernameText.setLayoutParams(paramsusername);
                usernameText.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView emailText = new TextView(getContext());
                emailText.setText(o.getString("EMAIL"));
                emailText.setLayoutParams(paramsEmail);

                TextView contractText = new TextView(getContext());
                contractText.setText(o.getString("CONTRACTNUMBER"));
                contractText.setLayoutParams(paramsContract);
                contractText.setGravity(Gravity.CENTER_HORIZONTAL);

                row.addView(usernameText);
                row.addView(emailText);
                row.addView(contractText);
                mainTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTable();
    }
}
