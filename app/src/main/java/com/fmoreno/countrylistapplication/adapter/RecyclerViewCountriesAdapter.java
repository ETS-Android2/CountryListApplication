package com.fmoreno.countrylistapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmoreno.countrylistapplication.R;
import com.fmoreno.countrylistapplication.interfaces.RecyclerViewInterface;
import com.fmoreno.countrylistapplication.model.Countrie;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCountriesAdapter extends RecyclerView.Adapter<RecyclerViewCountriesAdapter.ViewHolder>{
    public static final String TAG = "RecyclerViewCountriesAdapter";

    RecyclerViewInterface recyclerViewInterface;


    public static List<Countrie> countriesList = new ArrayList<>();
    public static List<Countrie> mFilteredCountriesList = new ArrayList<>();
    private Context context;

    public RecyclerViewCountriesAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_countrie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Animation startAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_up);

        holder.btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onItemClick(countriesList.get(position), holder.btn_go);
            }
        });

        holder.itemView.startAnimation(startAnimation);
    }

    /**
     * Add movies list when calling apis
     * @param movies
     */

    @SuppressLint("LongLogTag")
    public void addMovies(List<Countrie> movies) {
        countriesList.addAll(movies);
        Log.e(TAG, "size of movie list==" + countriesList.size());
        notifyDataSetChanged();
    }

    public void submitList(List<Countrie> movies) {
        countriesList = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return countriesList.size();
    }


    /**
     * View Holder for common row of movies
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button btn_go;
        private TextView tv_name;
        private TextView tv_capital;
        private TextView tv_region;
        public ViewHolder(View itemView) {
            super(itemView);
            btn_go = itemView.findViewById(R.id.btn_go);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_capital = itemView.findViewById(R.id.tv_capital);
            tv_region = itemView.findViewById(R.id.tv_region);
        }
    }
}
