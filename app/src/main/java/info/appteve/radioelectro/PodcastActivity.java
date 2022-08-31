package info.appteve.radioelectro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.appteve.radioelectro.constants.Constants;


public class PodcastActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constants {

    final ArrayList<PodcastItem> podcastItems = new ArrayList<>();
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createLisPodcast();

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        AdView mAdView = (AdView) findViewById(R.id.adViewPodcast);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.podcast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {

            Intent searchIntent = new Intent(PodcastActivity.this, RadioActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }   else if (id == R.id.nav_manage) {

            Intent searchIntent = new Intent(PodcastActivity.this, NewsActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_share) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MENU_SITE_URL));
            startActivity(browserIntent);

        } else if (id == R.id.nav_send) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MENU_APPLICATIONS_URL));
            startActivity(browserIntent);

        } else if(id == R.id.nav_podcast){

            Intent searchIntent = new Intent(PodcastActivity.this, PodcastActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        } else if(id == R.id.nav_podcast_offline){

            Intent searchIntent = new Intent(PodcastActivity.this, OfflinePodcastActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createLisPodcast(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        String url = GENERAL_API_URL + URL_API_GET_PODCAST + API_KEY;

        JsonArrayRequest req = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);


                                String track_name = person.getString("track_name");
                                String track_file = person.getString("track_file");
                                String file = person.getString("file");

                                podcastItems.add(new PodcastItem(track_name,track_file,file));

                            }

                            pDialog.hide();
                            makeDraw();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VVV", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });




        AppController.getInstance().addToRequestQueue(req);


    }

    public void makeDraw(){

        try {

            final ListView stationList = (ListView) findViewById(R.id.podcastlistView);

            PodcastAdapter adapter = new PodcastAdapter(getApplicationContext(), podcastItems);
            stationList.setAdapter(adapter);

            stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PodcastActivity.this, PlayerCastActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    intent.putExtra("track_file", podcastItems.get(position).track_file);
                    intent.putExtra("track_name", podcastItems.get(position).track_name);
                    intent.putExtra("file", podcastItems.get(position).file);

                    intent.putExtra("position",position);

                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            });
        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "No File", Toast.LENGTH_LONG).show();
        }

    }
}
