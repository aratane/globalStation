package info.appteve.radioelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;

import info.appteve.radioelectro.constants.Constants;

public class OfflinePodcastActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constants {

    final ArrayList<OfflinePodcastItem> offpodcastItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_podcast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listRaw();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        AdView mAdView = (AdView) findViewById(R.id.adViewPodcastOff);
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
        getMenuInflater().inflate(R.menu.offline_podcast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

            Intent searchIntent = new Intent(OfflinePodcastActivity.this, RadioActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }   else if (id == R.id.nav_manage) {

            Intent searchIntent = new Intent(OfflinePodcastActivity.this, NewsActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_share) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MENU_SITE_URL));
            startActivity(browserIntent);

        } else if (id == R.id.nav_send) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MENU_APPLICATIONS_URL));
            startActivity(browserIntent);

        } else if(id == R.id.nav_podcast){

            Intent searchIntent = new Intent(OfflinePodcastActivity.this, PodcastActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        } else if(id == R.id.nav_podcast_offline){

            Intent searchIntent = new Intent(OfflinePodcastActivity.this, OfflinePodcastActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void listRaw(){


        File rirr = new File(Environment.getExternalStorageDirectory() + SDCARD_FOLDER );

        offpodcastItems.clear();

        for (File f : rirr.listFiles()) {
            if (f.isFile())
            {
                String namez = f.getName();
                String filedir = rirr + File.separator+ namez;
                offpodcastItems.add(new OfflinePodcastItem(namez,filedir));
            }

            makeDraw();

        }
    }

    public void makeDraw(){

        try {


            final ListView offPodcList = (ListView) findViewById(R.id.offpodcastlistView);

            OfflinePodcastAdapter adapter = new OfflinePodcastAdapter(getApplicationContext(), offpodcastItems);

            offPodcList.setAdapter(adapter);

            offPodcList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               final int pos, long id) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(OfflinePodcastActivity.this);
                    builder.setTitle("Warning!")
                            .setMessage("Do you want to delete the track: " + offpodcastItems.get(pos).offtrack_name + "? If YES, please click  DELETE button")
                            .setIcon(R.drawable.defimage)
                            .setCancelable(true)
                            .setNegativeButton("ОК, DELETE",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                                String path = offpodcastItems.get(pos).offtrack_file;
                                                new File(path).delete();
                                                dialog.cancel();
                                                listRaw();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });

            offPodcList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OfflinePodcastActivity.this, OfflinePlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra("track_file", offpodcastItems.get(position).offtrack_file);
                    intent.putExtra("track_name", offpodcastItems.get(position).offtrack_name);
                    intent.putExtra("offline", 0);

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
