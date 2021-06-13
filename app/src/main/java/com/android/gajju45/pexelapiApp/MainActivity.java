package com.android.gajju45.pexelapiApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewClickListenerInterface {
    ImageView menuIcon;
    LinearLayout contentView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView, topMostRecyclerView;
    RecyclerView.Adapter adapter;
    wallpaperAdapter wallpaperAdapter;
    List<wallpaperModel> wallpaperModelList;
    ArrayList<SuggestedModel> suggestedModels = new ArrayList<>();
    TextView replaceTitle;

    int pageNumber = 1;

    ProgressBar progressBar;
    String url = "https://api.pexels.com/v1/curated/?page=" + pageNumber + "&per_page=80";

    EditText searchEt;
    ImageView searchIV;
    NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content_view);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        nestedScrollView = findViewById(R.id.nestedSV);


        navigationDrawer();
        //profile nav drawer



        //recycler View
        recyclerView = findViewById(R.id.recyclerView);
        topMostRecyclerView = findViewById(R.id.suggestedRecyclerView);

        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new wallpaperAdapter(this, wallpaperModelList);

        //Connection
        if (!isConnected(this)) {
            showCustomDialog();
        }


        //Nested Recycler View
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    url = "https://api.pexels.com/v1/curated/?page=" + pageNumber + "per_page=80";
                    fetchWallpaper();
                    //code to fetch more data for endless scrolling
                }
            }
        });


        recyclerView.setAdapter(wallpaperAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        fetchWallpaper();
        suggestedItems();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        replaceTitle = findViewById(R.id.topMostTitle);


        //search Et and IV
        searchEt = (EditText) findViewById(R.id.searchEV);
        searchIV = findViewById(R.id.search_img);


        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String query = searchEt.getText().toString().toLowerCase();
                progressBar.setVisibility(View.INVISIBLE);
                url = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=" + query;
                wallpaperModelList.clear();
                fetchWallpaper();
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this, "search Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }




//Check Internet Connection

    private boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnecton = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifiConnection != null && wifiConnection.isConnected() || (mobileConnecton != null && mobileConnecton.isConnected())) {
            return true;

        } else {
            return false;
        }

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.back_button));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.back_button));
        });
        alert.show();
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);

                }
            }
        });
        //animation
        animationNavigationDrawer();

    }

    private void animationNavigationDrawer() {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return true;
    }

    //Suggested Items
    private void suggestedItems() {
        topMostRecyclerView.setHasFixedSize(true);
        topMostRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "Trending", getResources().getColor(R.color.red)));
        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "Nature ",getResources().getColor(R.color.lightGreen)));
        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "Dark ",getResources().getColor(R.color.login_background)));
        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "Desktop ",getResources().getColor(R.color.teal_700)));
        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "Coding ",getResources().getColor(R.color.gray)));
        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "Beautiful ",getResources().getColor(R.color.yellow)));
        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "4K ",getResources().getColor(R.color.skyblue)));
        suggestedModels.add(new SuggestedModel(R.drawable.desktop, "Lifestyle ",getResources().getColor(R.color.pink)));

        adapter = new SuggestedAdapter(suggestedModels, MainActivity.this);
        topMostRecyclerView.setAdapter(adapter);
    }

    //fetch image and name from pexel Api
    private void fetchWallpaper() {
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");
                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                String photographerName = object.getString("photographer");

                                JSONObject objectImage = object.getJSONObject("src");
                                String originalUrl = objectImage.getString("original");
                                String mediumUrl = objectImage.getString("medium");
                                wallpaperModel wallpaperModel = new wallpaperModel(id, originalUrl, mediumUrl, photographerName);
                                wallpaperModelList.add(wallpaperModel);
                            }

                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f91700001000001e7e1e222b23a412194e13ce4d3dd35f2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public void onItemClick(int adapterPosition) {
        progressBar.setVisibility(View.VISIBLE);
        if (adapterPosition == 0) {
            replaceTitle.setText("Trending");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=trending";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (adapterPosition == 1) {
            replaceTitle.setText("Nature wallpaper");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=nature%20wallpaper";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (adapterPosition == 2) {
            replaceTitle.setText("Dark Wallpaper");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=dark";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (adapterPosition == 3) {
            replaceTitle.setText("Desktop Wallpaper");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=desktop%20backgrounds";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (adapterPosition == 4) {
            replaceTitle.setText("Coding Wallpaper");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=coding";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (adapterPosition == 5) {
            replaceTitle.setText("Beautiful Wallpaper");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=beautiful";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (adapterPosition == 6) {
            replaceTitle.setText("4K Wallpaper");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=4k%20wallpaper";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (adapterPosition == 7) {
            replaceTitle.setText("Life Style Wallpaper");
            url = "https://api.pexels.com/v1/search?page=" + pageNumber + "&per_page=80&query=lifestyle";
            wallpaperModelList.clear();//clear wallpaper list previous
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        }

    }

    @SuppressWarnings("serial")
    public class ClientError extends ServerError {
        public ClientError(NetworkResponse networkResponse) {
            super(networkResponse);
        }

        public ClientError() {
            super();
        }
    }

}