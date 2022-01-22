package com.fmoreno.countrylistapplication.ui;

import static com.fmoreno.countrylistapplication.internet.WebServicesConstant.BASE_URL_APPLICATION;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fmoreno.countrylistapplication.R;
import com.fmoreno.countrylistapplication.adapter.RecyclerViewCountriesAdapter;
import com.fmoreno.countrylistapplication.interfaces.RecyclerViewInterface;
import com.fmoreno.countrylistapplication.internet.WebApiRequest;
import com.fmoreno.countrylistapplication.model.Countrie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import utils.Utils;

public class CountryListActivity extends AppCompatActivity implements RecyclerViewInterface {
    public static final String TAG = "CountryListActivity";


    RelativeLayout rlCountriesList;
    private ProgressBar progressBar;
    SearchView search_bar;
    RecyclerView rv_countries;
    RecyclerViewCountriesAdapter recyclerViewCountriesAdapter;

    private View include;
    private ImageView iv_refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initOperation();
        callGetCountriesApi();
    }

    private void initView() {
        try{
            rlCountriesList = findViewById(R.id.rlCountriesList);
            search_bar = findViewById(R.id.search_bar);
            rv_countries = findViewById(R.id.rv_countries);
            progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            include = findViewById(R.id.include);
            include.setVisibility(View.GONE);
            iv_refresh = findViewById(R.id.iv_refresh);
            iv_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callGetCountriesApi();
                }
            });
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            progressBar.setVisibility(View.GONE);
            rlCountriesList.addView(progressBar, params);

            recyclerViewCountriesAdapter = new RecyclerViewCountriesAdapter(this, this);

        }catch (Exception ex){
            Log.e("initView", ex.toString());
        }
    }

    private void initOperation() {
        try{
            rv_countries.setAdapter(recyclerViewCountriesAdapter);
            rv_countries.setLayoutManager(new LinearLayoutManager(this));
            rv_countries.setHasFixedSize(true);
            rv_countries.clearOnScrollListeners();
            search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    recyclerViewCountriesAdapter.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    recyclerViewCountriesAdapter.getFilter().filter(newText);
                    return true;
                }
            });
        }catch (Exception ex){
            Log.e("initOperation", ex.toString());
        }
    }

    /**
     * Display Progress bar
     */

    private void showProgress() {
        try{
            progressBar.setVisibility(View.VISIBLE);
        }catch (Exception ex){
            Log.d(TAG, "showProgress: ex:"+ ex.toString());
        }

    }

    /**
     * Hide Progress bar
     */

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Call the api to fetch the TopRatedMovies list
     */

    private void callGetCountriesApi() {

        /**
         * Checking internet connection before api call.
         * Very important always take care.
         */

        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this,
                    getResources().getString(R.string.str_no_internet),
                    Toast.LENGTH_SHORT).show();
            include.setVisibility(View.VISIBLE);
            search_bar.setVisibility(View.GONE);
            return;
        }

        showProgress();


        //constructing api url
        String ws_url = BASE_URL_APPLICATION;

        //Using Volley to call api

        WebApiRequest webApiRequest = new WebApiRequest(Request.Method.GET,
                ws_url, ReqSuccessListener(), ReqErrorListener());
        webApiRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.e("Error", error.toString());
            } });

        Volley.newRequestQueue(this).add(webApiRequest);
    }

    /**
     * Success listener to handle the movie listing
     * process after api returns the movie list
     *
     * @return
     */

    private List<Countrie>  countriesList = null;

    private Response.Listener<String> ReqSuccessListener() {
        return new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("movie list_response", response);
                try {
                    hideProgress();

                    //countriesList =  (Countrie[])Utils.jsonToPojo(response, Countrie[].class);

                    Type collectionType = new TypeToken<List<Countrie>>(){}.getType();
                    countriesList = (List<Countrie>) new Gson()
                            .fromJson( response , collectionType);

                    if (countriesList != null &&
                            countriesList.size() > 0) {
                        include.setVisibility(View.GONE);
                        search_bar.setVisibility(View.VISIBLE);
                        recyclerViewCountriesAdapter.addMovies(countriesList);
                    } else {
                        Log.e(TAG, "list empty==");
                        include.setVisibility(View.VISIBLE);
                        search_bar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.e(TAG,"Exception=="+e.getLocalizedMessage());
                    hideProgress();
                }
            }
        };
    }

    /**
     * To Handle the error
     *
     * @return
     */

    private Response.ErrorListener ReqErrorListener() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Log.e("volley error", "volley error");
                Toast.makeText(getApplication(), getResources().getString(R.string.str_error_server), Toast.LENGTH_SHORT).show();
                include.setVisibility(View.VISIBLE);
                search_bar.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onItemClick(Countrie countrie, View view) {
        String coordinates = "geo:" +
                countrie.getLatlng().get(0) + "," +
                countrie.getLatlng().get(1) + "?q=" +
                countrie.getName();
        Uri gmmIntentUri = Uri.parse(coordinates);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    boolean onBack = false;
    @Override
    public void onBackPressed() {
        if(!onBack){
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_action_info)
                    .setTitle("Salir")
                    .setMessage(Utils.getStringFromResource(R.string.text_exit, this))
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBack = true;
                            exitApp();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBack = false;
                        }
                    }).show();
        } else {
            try{
                System.exit(0);
            }catch (Exception ex){
                Log.e("Dialog", ex.toString());
            }
            super.onBackPressed();
        }

    }

    private void exitApp(){
        onBackPressed();
    }
}