package com.caffeine.covid_19stats.UIInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.caffeine.covid_19stats.Adapter.CountryAdapter;
import com.caffeine.covid_19stats.Model.CountryModel;
import com.caffeine.covid_19stats.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private final static String url = "https://coronavirus-19-api.herokuapp.com/countries?fbclid=IwAR35djmwbxYkGXwokot7e99TMYlgi-SdHza-rOXpXKL9kXlUyUvcXpUxywU";
    private TextView country_name, name;
    private TextView tc, tr, td, nc, nd, active, critical, cpom, dpom, ttest, tpom;
    private ImageView back;
    private ScrollView scrollView;
    private ShimmerFrameLayout shimmer;
    private String COUNTRY;

    private CountryModel thisCountry;
    private RequestQueue mQueue;
    private ArrayList<CountryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        COUNTRY = i.getStringExtra("name");

        gettingLayoutIDs();
        country_name.setText(COUNTRY);
        name.setText(COUNTRY);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        shimmer.startShimmer();

        retrieveData();
    }

    private void setData(){

        String TC = Integer.toString(thisCountry.getTotal_confirmed());
        String TR = Integer.toString(thisCountry.getTotal_recovered());
        String TD = Integer.toString(thisCountry.getTotal_death());
        String NC = Integer.toString(thisCountry.getNewConfirmed());
        String ND = Integer.toString(thisCountry.getNewDeaths());
        String AC = Integer.toString(thisCountry.getActive());
        String CR = Integer.toString(thisCountry.getCritical());
        String CPOM = Integer.toString(thisCountry.getCpom());
        String DPOM = Integer.toString(thisCountry.getDpom());
        String TT = Integer.toString(thisCountry.getTests());
        String TPOM = Integer.toString(thisCountry.getTpom());

        tc.setText(TC);
        tr.setText(TR);
        td.setText(TD);
        nc.setText(NC);
        nd.setText(ND);
        active.setText(AC);
        critical.setText(CR);
        cpom.setText(CPOM);
        dpom.setText(DPOM);
        ttest.setText(TT);
        tpom.setText(TPOM);

        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmer();
        scrollView.setVisibility(View.VISIBLE);
    }

    private void retrieveData(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                CountryModel model = new CountryModel();

                                model.setCountry(object.getString("country"));
                                model.setTotal_confirmed(object.getInt("cases"));
                                model.setNewConfirmed(object.getInt("todayCases"));
                                model.setTotal_death(object.getInt("deaths"));
                                model.setNewDeaths(object.getInt("todayDeaths"));
                                model.setTotal_recovered(object.getInt("recovered"));
                                model.setActive(object.getInt("active"));
                                model.setCritical(object.getInt("critical"));
                                model.setCpom(object.getInt("casesPerOneMillion"));
                                model.setDpom(object.getInt("deathsPerOneMillion"));
                                model.setTests(object.getInt("totalTests"));
                                model.setTpom(object.getInt("testsPerOneMillion"));

                                if (model.getCountry().equals(COUNTRY)){
                                    thisCountry = model;
                                }
                            }
                            catch (JSONException e){}
                        }

                        setData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError){
                            retrieveData();
                        }
                    }
                });
        mQueue.add(request);
    }

    private void gettingLayoutIDs(){
        country_name = findViewById(R.id.country);
        name = findViewById(R.id.country_name);
        back = findViewById(R.id.back);
        scrollView = findViewById(R.id.scrollViewDetails);
        shimmer = findViewById(R.id.details_shimmer);

        tc = findViewById(R.id.total_confirmed);
        tr = findViewById(R.id.recovered);
        td = findViewById(R.id.total_deaths);
        nc = findViewById(R.id.today_confirmed);
        nd = findViewById(R.id.todays_deaths);
        active = findViewById(R.id.active);
        critical = findViewById(R.id.critical);
        cpom = findViewById(R.id.cpom);
        dpom = findViewById(R.id.dpom);
        ttest = findViewById(R.id.total_tests);
        tpom = findViewById(R.id.tpom);

        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
