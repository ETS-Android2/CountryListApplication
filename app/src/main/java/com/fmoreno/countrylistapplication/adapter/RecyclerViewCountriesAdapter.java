package com.fmoreno.countrylistapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmoreno.countrylistapplication.R;
import com.fmoreno.countrylistapplication.adapter.filters.CountrieFilter;
import com.fmoreno.countrylistapplication.interfaces.RecyclerViewInterface;
import com.fmoreno.countrylistapplication.model.Countrie;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCountriesAdapter extends RecyclerView.Adapter<RecyclerViewCountriesAdapter.ViewHolder>
        implements Filterable {
    public static final String TAG = "RecyclerViewCountriesAdapter";

    RecyclerViewInterface recyclerViewInterface;
    public CountrieFilter mFilter;



    public static List<Countrie> countriesList = new ArrayList<>();
    public static List<Countrie> mFilteredCountriesList = new ArrayList<>();
    //private Context context;
    Context mContext;

    public RecyclerViewCountriesAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        //this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public Filter getFilter() {

        if (mFilter == null){
            mFilteredCountriesList.clear();
            mFilteredCountriesList.addAll(this.countriesList);
            mFilter = new CountrieFilter(RecyclerViewCountriesAdapter.this, this.mFilteredCountriesList);
        }
        return mFilter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_countrie, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Animation startAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_up);

        Countrie countrie = countriesList.get(position);

        holder.btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onItemClick(countrie, holder.btn_go);
            }
        });

        try {
            String name = new String(countrie.getName().getBytes("ISO-8859-1"),
                    "UTF-8");
            String encodedTextName = Html.fromHtml(name).toString();
            holder.tv_name.setText(encodedTextName);

            String capital = new String(countrie.getCapital().getBytes("ISO-8859-1"),
                    "UTF-8");
            String encodedTextCapital = Html.fromHtml(capital).toString();
            holder.tv_capital.setText(encodedTextCapital);

            String region = new String(countrie.getRegion().getBytes("ISO-8859-1"),
                    "UTF-8");
            String encodedTextRegion = Html.fromHtml(region).toString();
            holder.tv_region.setText(encodedTextRegion);

        }  catch (Exception ex){
            Log.e("adapter", ex.toString());
        }
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
