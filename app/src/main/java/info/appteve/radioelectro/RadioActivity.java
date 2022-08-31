package info.appteve.radioelectro;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.mobiwise.library.radio.RadioListener;
import co.mobiwise.library.radio.RadioManager;
import de.hdodenhof.circleimageview.CircleImageView;
import info.appteve.radioelectro.constants.Constants;



public class RadioActivity extends AppCompatActivity implements RadioListener,Constants, NavigationView.OnNavigationItemSelectedListener {


    RadioManager mRadioManager = RadioManager.with(this);
    List<MovieModel> movieModelList = new ArrayList<>();

    public String idradio;
    public String radioName;
    public String radioUrl;
    public String imageRadio;
    public String alldata;
    public String alldataNew;
    public String convert;
    public String itunesUrl;
    public String activity;
    public String urlimage;
    public String image_cover_radio_file;

    Button mButtonControlStart;
    TextView mTextViewControl;
    CircleImageView artistCover;
    ImageView backImage;

    InterstitialAd mInterstitialAd;

    Thread t;

    @Override
    public void onError() {

    }

    @Override
    public void onRadioLoading() {

    }

    @Override
    public void onAudioSessionId(int audioSessionId) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mButtonControlStart = (Button) findViewById(R.id.playBtn);
        mTextViewControl = (TextView) findViewById(R.id.radiotextField);
        artistCover = (CircleImageView) findViewById(R.id.imgCovers);
        backImage = (ImageView) findViewById(R.id.imageBack);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRadioManager.registerListener(this);
        mRadioManager.setLogging(true);
        mRadioManager.enableNotification(true);



        loadRadioApi();

        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if( alldata == null){
                                    alldata="Radio Global";
                                }


                                if (alldata == alldataNew){

                                } else{

                                    alldataNew = alldata;
                                    mTextViewControl.setText(alldataNew);


                                    try
                                    {

                                        convert = URLEncoder.encode(alldataNew, "UTF-8");

                                    }
                                    catch (UnsupportedEncodingException e)
                                    {

                                    }

                                    String mUrl = "https://itunes.apple.com/search?term="+ convert + "&limit=2";
                                    new JSONTask().execute(mUrl);


                                }


                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_banner_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    void showInterstitial(){

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
           // beginPlayingGame();
        }


    }





    public void changeCover(String url){

        showInterstitial();


        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap blurred = blurRenderScript(resource, 25);
                        backImage.setImageBitmap(blurred);
                        artistCover.setImageBitmap(resource);
                        mRadioManager.updateNotification(alldataNew,APP_NAME,R.drawable.defimage,resource);
                    }
                });

    }

    public void initializeUI() {


        mButtonControlStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying()) {
                    mRadioManager.startRadio(radioUrl);
                   // mButtonControlStart.setText("PAUSE");
                    mButtonControlStart.setBackgroundResource(R.drawable.pause_button_a);

                    mTextViewControl.setText(alldataNew);

                }else {
                    mRadioManager.stopRadio();
                  //  mButtonControlStart.setText("PLAY");
                    mButtonControlStart.setBackgroundResource(R.drawable.play_button_a);
                }
            }
        });

    }

    @Override
    protected void onResume () {
        super.onResume();
        mRadioManager.connect();
    }


    @Override
    protected void onDestroy () {
        super.onDestroy();

        mRadioManager.disconnect();

        t.interrupt();


    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onStart() {
        super.onStart();

       //  mRadioManager.stopRadio();

    }



    @Override
    public void onRadioConnected () {

    }

    @Override
    public void onRadioStarted () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO Do UI works here.
                mTextViewControl.setText("Radio loading...");
                mTextViewControl.setText(alldataNew);
            }
        });
    }

    @Override
    public void onRadioStopped () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO Do UI works here
                mTextViewControl.setText("Radio stop");
            }
        });
    }

    @Override
    public void onMetaDataReceived (String s, String s1){
        //TODO Check metadata values. Singer name, song name or whatever you have.

        alldata = s1;

    }

    public class JSONTask extends AsyncTask<String,String, List<MovieModel> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<MovieModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("results");

                movieModelList = new ArrayList<>();

                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    MovieModel movieModel = gson.fromJson(finalObject.toString(), MovieModel.class);
//
                    movieModelList.add(movieModel);
                }



                return movieModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(List<MovieModel> result) {
            super.onPostExecute(result);


            imageToGo();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

        int id = item.getItemId();


        if (id == R.id.nav_camera) {

            radiostoped();

            Intent searchIntent = new Intent(RadioActivity.this, RadioActivity.class);
            startActivity(searchIntent);

            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);



        }   else if (id == R.id.nav_manage) {

            radiostoped();

            Intent searchIntent = new Intent(RadioActivity.this, NewsActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_share) {

            radiostoped();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MENU_SITE_URL));
            startActivity(browserIntent);

        } else if (id == R.id.nav_send) {

            radiostoped();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MENU_APPLICATIONS_URL));
            startActivity(browserIntent);

        } else if(id == R.id.nav_podcast){

            radiostoped();

            Intent searchIntent = new Intent(RadioActivity.this, PodcastActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        } else if(id == R.id.nav_podcast_offline){

            radiostoped();

            Intent searchIntent = new Intent(RadioActivity.this, OfflinePodcastActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void radiostoped(){


            mRadioManager.stopRadio();
           // mButtonControlStart.setText("PLAY");
            mButtonControlStart.setBackgroundResource(R.drawable.play_button_a);

    }

    public void loadRadioApi(){

        String url = GENERAL_API_URL + URL_API_GET_LISTRADIO +  KEY_STRING + API_KEY;

        JsonArrayRequest req = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                System.out.println("arr: " + person);

                                idradio = "1";
                                radioName = person.getString("name");
                                radioUrl = person.getString("radio_url");
                                Log.d("DDD",radioUrl);


                                image_cover_radio_file = person.getString("image_file");

                                setTitle(radioName);

                                maker();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Errwor: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VVV", "Erwror: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        AppController.getInstance().addToRequestQueue(req);


    }

    public void maker(){

        setTitle(radioName);

        String urlImageFile = GENERAL_API_URL + UPLOADS_FOLDER + image_cover_radio_file;
        imageRadio = urlImageFile;


        Glide.with(this)
                .load(R.drawable.back_f)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap blurred = blurRenderScript(resource, 25);
                        backImage.setImageBitmap(blurred);
                        artistCover.setImageBitmap(resource);
                    }
                });

        initializeUI();
        LoginRequest();

        if (!isPlaying() ) {
            mRadioManager.startRadio(radioUrl);
           // mButtonControlStart.setText("PAUSE");
            mButtonControlStart.setBackgroundResource(R.drawable.pause_button_a);
            mTextViewControl.setText(alldataNew);

        }else {
            mRadioManager.stopRadio();
           // mButtonControlStart.setText("PLAY");
            mButtonControlStart.setBackgroundResource(R.drawable.play_button_a);
        }
    }

    public void imageToGo(){

        if (movieModelList.size() == 0){

            urlimage = imageRadio;
            changeCover(urlimage);
            itunesUrl="notfound";

        } else{

            urlimage = movieModelList.get(0).getUrlCover();
            itunesUrl = movieModelList.get(0).getItunesUrl();
            urlimage = urlimage.replace("100x100bb","800x800bb");
            changeCover(urlimage);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

            Log.d("DDD","Tape menu");



        } else {
            super.onBackPressed();
            Log.d("DDD","Tape back");

        }

    ;

        mRadioManager.stopRadio();
        //mButtonControlStart.setText("PLAY");
        mButtonControlStart.setBackgroundResource(R.drawable.play_button_a);
    }

    public void LoginRequest() {

        StringRequest sr = new StringRequest(Request.Method.POST,GENERAL_API_URL + URL_API_POST_TREND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("radio_id", idradio);
                params.put("X-API-KEY", API_KEY);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);

    }

    public void chat_Click (View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "I listen : "+alldataNew+" in" + APP_NAME + "application. Download app " + GOOGLE_APP_URL);
        startActivity(Intent.createChooser(intent, "Share with"));

    }

    /// blur

    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(RadioActivity.this);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    private boolean isPlaying() {
        return (null != mRadioManager && null != RadioManager.getService() && RadioManager.getService().isPlaying());
    }

}


