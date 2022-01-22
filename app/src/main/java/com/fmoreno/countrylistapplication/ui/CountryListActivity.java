package com.fmoreno.countrylistapplication.ui;

import static com.fmoreno.countrylistapplication.internet.WebServicesConstant.BASE_URL_APPLICATION;
import static com.fmoreno.countrylistapplication.internet.WebServicesConstant.TIMEOUT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.Collection;
import java.util.List;

import utils.Utils;

public class CountryListActivity extends AppCompatActivity implements RecyclerViewInterface {
    public static final String TAG = "CountryListActivity";


    RelativeLayout rlCountriesList;
    private ProgressBar progressBar;
    RecyclerView rv_countries;
    RecyclerViewCountriesAdapter recyclerViewCountriesAdapter;

    LinearLayoutManager layoutManager;

    //For Load more functionality
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 2;
    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pageNumber = 1;



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
            rv_countries = findViewById(R.id.rv_countries);
            progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            progressBar.setVisibility(View.GONE);
            rlCountriesList.addView(progressBar, params);

            layoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rv_countries.setLayoutManager(layoutManager);
            rv_countries.setHasFixedSize(true);
            rv_countries.clearOnScrollListeners(); //clear scrolllisteners
            recyclerViewCountriesAdapter = new RecyclerViewCountriesAdapter(this, this);

        }catch (Exception ex){
            Log.e("initView", ex.toString());
        }
    }

    private void initOperation() {
        try{
            rv_countries.setAdapter(recyclerViewCountriesAdapter);

            rv_countries.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        visibleItemCount = recyclerView.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if (totalItemCount > previousTotal) {
                                loading = false;
                                previousTotal = totalItemCount;
                            }
                        }
                        if (!loading && (totalItemCount - visibleItemCount)
                                <= (firstVisibleItem + visibleThreshold)) {
                            // End has been reached
                            Log.i("InfiniteScrollListener", "End reached");

                            callGetCountriesApi();
                            loading = true;
                        }
                    }
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
            return;
        }

        showProgress();


        //constructing api url
        String ws_url = BASE_URL_APPLICATION;

        //Using Volley to call api

        WebApiRequest webApiRequest = new WebApiRequest(Request.Method.GET,
                ws_url, ReqSuccessListener(), ReqErrorListener());
        /*webApiRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

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
                    pageNumber++;

                    //countriesList =  (Countrie[])Utils.jsonToPojo(response, Countrie[].class);

                    Type collectionType = new TypeToken<List<Countrie>>(){}.getType();
                    countriesList = (List<Countrie>) new Gson()
                            .fromJson( response , collectionType);

                    if (countriesList != null &&
                            countriesList.size() > 0) {
                            recyclerViewCountriesAdapter.addMovies(countriesList);
                    } else {
                        Log.e(TAG, "list empty==");
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
            }
        };
    }

    @Override
    public void onItemClick(Countrie result, View view) {

    }
}