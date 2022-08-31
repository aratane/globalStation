package info.appteve.radioelectro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import info.appteve.radioelectro.constants.Constants;

public class OfflinePlayerActivity extends AppCompatActivity  implements Constants{


    final ArrayList<OfflinePodcastItem> offpodcastItems = new ArrayList<>();
    private ProgressDialog pDialog;
    InterstitialAd mInterstitialAd;

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    Context context;
    ProgressBar progressBar;
    ProgressDialog dialogz;
    String files;
    String names;
    String urls;
    Uri uurls;

    int position = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_player);

        position = getIntent().getExtras().getInt("position");
        generateLisPodcast();

        files = getIntent().getExtras().getString("track_file");
        names  = getIntent().getExtras().getString("track_name");

        createPlayer(files,names);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                    mediaPlayer.pause();
                } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                    // mediaPlayer.start();

                } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

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

    private  void  createPlayer(String url, String trackname){

        urls = Environment.getExternalStorageDirectory()+ SDCARD_FOLDER + trackname;

        mediaPlayer = new MediaPlayer();

        // try to load data and play
        try {

            // give data to mediaPlayer
            mediaPlayer.setDataSource(urls);

            // media player asynchronous preparation
           // mediaPlayer.prepareAsync();
            mediaPlayer.prepare();

            //   progressBar = (new ProgressBar(R.id.progressBar));
//
            // create a progress dialog (waiting media player preparation)
            final ProgressDialog dialog = new ProgressDialog(OfflinePlayerActivity.this);

            // set message of the dialog
            dialog.setMessage("Prepare to play. Please wait.");

            // prevent dialog to be canceled by back button press
            dialog.setCancelable(false);

            // show dialog at the bottom
            dialog.getWindow().setGravity(Gravity.CENTER);

            // show dialog
            dialog.show();


            // inflate layout
            //  setContentView(R.layout.activity_player_cast);

            // display title

            String str_track = trackname.replaceFirst("\\.mp3$", "");

            ((TextView)findViewById(R.id.now_playing_text)).setText(str_track);



            // execute this code at the end of asynchronous media player preparation
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(final MediaPlayer mp) {


                    //start media player
                    mp.start();

                    // link seekbar to bar view
                    seekBar = (SeekBar) findViewById(R.id.seekBar);

                    //update seekbar
                    mRunnable.run();

                    //dismiss dialog
                    dialog.dismiss();
                    showInterstitial();

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            continiousPlay();
                        }
                    });
                }
            });







        } catch (IOException e) {
            Activity a = this;
            a.finish();
            Toast.makeText(this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
        }



    }


    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if(mediaPlayer != null) {

                //set max value
                int mDuration = mediaPlayer.getDuration();
                seekBar.setMax(mDuration);

                //update total time text view
                TextView totalTime = (TextView) findViewById(R.id.totalTime);
                totalTime.setText(getTimeString(mDuration));

                //set progress to current position
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);

                //update current time text view
                TextView currentTime = (TextView) findViewById(R.id.currentTime);
                currentTime.setText(getTimeString(mCurrentPosition));

                //handle drag on seekbar
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(mediaPlayer != null && fromUser){
                            mediaPlayer.seekTo(progress);
                        }
                    }
                });


            }

            //repeat above code every second
            mHandler.postDelayed(this, 10);
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public void playOff(View view){

        mediaPlayer.start();
    }


    public void pauseOff(View view){

        mediaPlayer.pause();

    }

    public void stop(View view){

        mediaPlayer.seekTo(0);
        mediaPlayer.pause();

    }


    public void seekForward(View view){

        //set seek time
        int seekForwardTime = 5000;

        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekForward time is lesser than song duration
        if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
            // forward song
            mediaPlayer.seekTo(currentPosition + seekForwardTime);
        }else{
            // forward to end position
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }

    }

    public void seekBackward(View view){

        //set seek time
        int seekBackwardTime = 5000;

        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekBackward time is greater than 0 sec
        if(currentPosition - seekBackwardTime >= 0){
            // forward song
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        }else{
            // backward to starting position
            mediaPlayer.seekTo(0);
        }

    }




    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(OfflinePlayerActivity.this, OfflinePodcastActivity.class);
        startActivity(intent);


        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000*60*60);
        long minutes = ( millis % (1000*60*60) ) / (1000*60);
        long seconds = ( ( millis % (1000*60*60) ) % (1000*60) ) / 1000;

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }



    public void generateLisPodcast(){


        File rirr = new File(Environment.getExternalStorageDirectory() +
                File.separator + SDCARD_FOLDER_W +"/" );

        offpodcastItems.clear();

        for (File f : rirr.listFiles()) {
            if (f.isFile())
            {
                String namez = f.getName();
                Log.d("filez", namez);
                String filedir = rirr + File.separator+ namez;
                Log.d("filez", filedir);

                offpodcastItems.add(new OfflinePodcastItem(namez,filedir));

            }



        }
    }

    public void nextTrackOff(View view){


        if (position == offpodcastItems.size() - 1){

            position = 0;
        } else {

            position = position + 1;
        }

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }



        names = offpodcastItems.get(position).offtrack_name;
        files = offpodcastItems.get(position).offtrack_file;

        createPlayer(files,names);
        showInterstitial();


    }

    public void forwardOff (View view){



        if (position == 0){

            position = offpodcastItems.size() - 1;
        } else {

            position = position - 1;
        }



        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }



        names = offpodcastItems.get(position).offtrack_name;
        files = offpodcastItems.get(position).offtrack_file;

        createPlayer(files,names);

    }

    public void continiousPlay(){

        if (position == offpodcastItems.size() - 1){

            position = 0;
        } else {

            position = position + 1;
        }

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }



        names = offpodcastItems.get(position).offtrack_name;
        files = offpodcastItems.get(position).offtrack_file;

        createPlayer(files,names);
    }


}
