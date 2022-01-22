package com.fmoreno.countrylistapplication.interfaces;

import android.view.View;

import com.fmoreno.countrylistapplication.model.Countrie;

public interface RecyclerViewInterface {
    void onItemClick(Countrie result, View view);
}
