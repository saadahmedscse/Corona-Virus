package com.caffeine.covid_19stats.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caffeine.covid_19stats.Model.CountryModel;
import com.caffeine.covid_19stats.R;
import com.caffeine.covid_19stats.UIInterface.DetailsActivity;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder>{

    ArrayList<CountryModel> list;
    Activity activity;

    public CountryAdapter(ArrayList<CountryModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.c_name.setText(list.get(position).getCountry());

        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("name", holder.c_name.getText().toString());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView c_name;
        RelativeLayout next;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            c_name = itemView.findViewById(R.id.item_country_name);
            next = itemView.findViewById(R.id.next);
        }
    }
}
