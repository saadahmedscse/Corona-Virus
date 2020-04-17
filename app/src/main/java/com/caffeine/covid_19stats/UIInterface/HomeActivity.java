package com.caffeine.covid_19stats.UIInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import java.util.Collections;
import java.util.Comparator;

public class HomeActivity extends AppCompatActivity {

    private TextView bdDeath, bdConfirmed, w_confirmed, w_death;
    private LinearLayout _world, _bangladesh;
    private Dialog dialog, searchDialog;
    private int count = 1;
    private ImageView searchBtn, filterBtn;
    private ShimmerFrameLayout shimmer, recShimmer;
    private NestedScrollView scrollView;
    private RecyclerView recyclerView;
    private final static String url = "https://coronavirus-19-api.herokuapp.com/countries?fbclid=IwAR35djmwbxYkGXwokot7e99TMYlgi-SdHza-rOXpXKL9kXlUyUvcXpUxywU";
    private RequestQueue mQueue;
    private ArrayList<CountryModel> list = new ArrayList<>();
    private CountryModel bangladesh, world;
    private Activity activity = HomeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gettingLayoutIDs();

        shimmer.startShimmer();

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAction();
            }
        });

        getDataFromJson();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDialogAction();
            }
        });

        _bangladesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                intent.putExtra("name", "Bangladesh");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        _world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                intent.putExtra("name", "World");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void getDataFromJson(){
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

                                list.add(model);

                                if (model.getCountry().equals("") || model.getCountry().equals("World") || model.getCountry().equals("Total:")){
                                    list.remove(model);
                                }

                                if (model.getCountry().equals("Bangladesh")){
                                    bangladesh = model;
                                }

                                if (model.getCountry().equals("World")){
                                    world = model;
                                }
                            }
                            catch (JSONException e){}
                        }

                        String bc = "Confirmed: " + bangladesh.getNewConfirmed(), bd = "Deaths: " + bangladesh.getNewDeaths();
                        String wc = "Confirmed: " + world.getNewConfirmed(), wd = "Deaths: " + world.getNewDeaths();

                        bdConfirmed.setText(bc);
                        bdDeath.setText(bd);
                        w_confirmed.setText(wc);
                        w_death.setText(wd);

                        if (list.size() > 0){
                            Collections.sort(list, new Comparator<CountryModel>() {
                                @Override
                                public int compare(CountryModel o1, CountryModel o2) {
                                    return o1.getCountry().compareTo(o2.getCountry());
                                }
                            });
                        }

                        CountryAdapter adapter = new CountryAdapter(list, activity);
                        recyclerView.setAdapter(adapter);

                        if (adapter.getItemCount() > 0){
                            shimmer.setVisibility(View.GONE);
                            shimmer.stopShimmer();
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError){
                            getDataFromJson();
                        }
                    }
                });
        mQueue.add(request);
    }

    private void dialogAction(){
        dialog.setContentView(R.layout.filter_by);

        final RadioButton country = dialog.findViewById(R.id.sort_country);
        final RadioButton con = dialog.findViewById(R.id.sort_confirmed);
        final RadioButton dea = dialog.findViewById(R.id.most_death);

        switch (count){
            case 1:
                country.setChecked(true);
                break;
            case 2:
                con.setChecked(true);
                break;
            case 3:
                dea.setChecked(true);
                break;
        }

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recShimmer.startShimmer();
                recShimmer.setVisibility(View.VISIBLE);

                if (list.size() > 0) {
                    Collections.sort(list, new Comparator<CountryModel>() {
                        @Override
                        public int compare(CountryModel o1, CountryModel o2) {
                            return o1.getCountry().compareTo(o2.getCountry());
                        }
                    });

                    CountryAdapter adapter = new CountryAdapter(list, activity);
                    recyclerView.setAdapter(adapter);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recShimmer.setVisibility(View.INVISIBLE);
                            recShimmer.stopShimmer();
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }, 1800);
                }

                count = 1;
                dialog.dismiss();
            }
        });

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recShimmer.startShimmer();
                recShimmer.setVisibility(View.VISIBLE);

                if (list.size() > 0){
                    Collections.sort(list, new Comparator<CountryModel>() {
                        @Override
                        public int compare(CountryModel o1, CountryModel o2) {
                            return Integer.compare(o2.getTotal_confirmed(), o1.getTotal_confirmed());
                        }
                    });

                    CountryAdapter adapter = new CountryAdapter(list, activity);
                    recyclerView.setAdapter(adapter);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recShimmer.setVisibility(View.INVISIBLE);
                            recShimmer.stopShimmer();
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }, 1800);
                }

                count = 2;
                dialog.dismiss();
            }
        });

        dea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recShimmer.startShimmer();
                recShimmer.setVisibility(View.VISIBLE);

                if (list.size() > 0){
                    Collections.sort(list, new Comparator<CountryModel>() {
                        @Override
                        public int compare(CountryModel o1, CountryModel o2) {
                            return Integer.compare(o2.getTotal_death(), o1.getTotal_death());
                        }
                    });

                    CountryAdapter adapter = new CountryAdapter(list, activity);
                    recyclerView.setAdapter(adapter);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recShimmer.setVisibility(View.INVISIBLE);
                            recShimmer.stopShimmer();
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }, 1800);
                }

                count = 3;
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void searchDialogAction(){
        searchDialog.setContentView(R.layout.search_layout);

        final EditText search;
        RelativeLayout search_btn;
        LinearLayout layout;
        final TextView search_tv;
        final ProgressBar bar;

        search = searchDialog.findViewById(R.id.search_txt);
        search_btn = searchDialog.findViewById(R.id.search);
        search_tv = searchDialog.findViewById(R.id.search_tv);
        bar = searchDialog.findViewById(R.id.search_progressBar);
        layout = searchDialog.findViewById(R.id.layout);

        float w = getWindowManager().getDefaultDisplay().getWidth();
        double width = w - 40;
        layout.getLayoutParams().width = (int) width;

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = search.getText().toString().toLowerCase();
                if (!txt.isEmpty()){
                    if (txt.length() > 2){
                        search_tv.setVisibility(View.GONE);
                        bar.setVisibility(View.VISIBLE);

                        for (CountryModel model : list){
                            if (model.getCountry().toLowerCase().contains(txt)){
                                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                                intent.putExtra("name", model.getCountry());
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                searchDialog.dismiss();
                            }

                            else {
                                bar.setVisibility(View.GONE);
                                search_tv.setVisibility(View.VISIBLE);
                                search.setError("Country not found");
                                search.requestFocus();
                            }
                        }
                    }
                    else {
                        search.setError("Invalid country name");
                        search.requestFocus();
                    }
                }
                else {
                    search.setError("Country name required");
                    search.requestFocus();
                }
            }
        });

        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchDialog.show();
    }

    private void gettingLayoutIDs(){
        bdConfirmed = findViewById(R.id.bd_confirmed);
        bdDeath = findViewById(R.id.bd_death);
        _world = findViewById(R.id.world);
        _bangladesh = findViewById(R.id.bangladesh);
        w_confirmed = findViewById(R.id.world_confirmed);
        shimmer = findViewById(R.id.home_shimmer);
        recShimmer = findViewById(R.id.recShimmer);
        searchBtn = findViewById(R.id.search_btn);
        filterBtn = findViewById(R.id.filter_btn);
        dialog = new Dialog(this);
        searchDialog = new Dialog(this);
        scrollView = findViewById(R.id.scrollView);
        w_death = findViewById(R.id.world_death);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mQueue = Volley.newRequestQueue(this);
    }
}
