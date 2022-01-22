package com.fmoreno.countrylistapplication.adapter.filters;

import android.widget.Filter;

import com.fmoreno.countrylistapplication.adapter.RecyclerViewCountriesAdapter;
import com.fmoreno.countrylistapplication.model.Countrie;

import java.util.ArrayList;
import java.util.List;

public class CountrieFilter extends Filter {
    private final RecyclerViewCountriesAdapter recyclerViewCountriesAdapter;
    private final List<Countrie> originalList;
    private final List<Countrie> filteredList;

    public CountrieFilter(RecyclerViewCountriesAdapter myAdapter, List<Countrie> originalList) {
        this.recyclerViewCountriesAdapter = myAdapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<Countrie>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        filteredList.clear();
        final FilterResults results = new FilterResults();
        if (charSequence.length() == 0){
            filteredList.addAll(originalList);
        }else {
            final String filterPattern = charSequence.toString().toLowerCase().trim();
            for (Countrie movie : originalList){
                if (movie.getName().toLowerCase().contains(filterPattern)){
                    filteredList.add(movie);
                }
            }
        }

        results.values = filteredList;
        results.count = filteredList.size();
        return results;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        recyclerViewCountriesAdapter.countriesList.clear();
        recyclerViewCountriesAdapter.countriesList.addAll((ArrayList<Countrie>)filterResults.values);
        recyclerViewCountriesAdapter.notifyDataSetChanged();

    }
}
