package info.appteve.radioelectro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.poliveira.apps.parallaxlistview.ParallaxScrollView;

import info.appteve.radioelectro.constants.Constants;

public class NewsDetailActivity extends AppCompatActivity implements Constants {

    RelativeLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mContainer = (RelativeLayout) findViewById(R.id.container);
        mContainer.removeAllViews();

        View v = getLayoutInflater().inflate(R.layout.inc_scroll, mContainer, true);
        ParallaxScrollView mScrollView = (ParallaxScrollView) v.findViewById(R.id.view);
        mScrollView.setParallaxView(getLayoutInflater().inflate(R.layout.view_header, mScrollView, false));


        String text = getIntent().getExtras().getString("text");
        String name = getIntent().getExtras().getString("name");
        String image = getIntent().getExtras().getString("image");


        TextView textNews = (TextView) findViewById(R.id.detailtextView);
        textNews.setText(text);

        ImageView textimage = (ImageView) findViewById(R.id.detailimageView);

        String imageFile = GENERAL_API_URL + UPLOADS_FOLDER + getIntent().getExtras().getString("imagge_file");

        Glide.with(this).load(imageFile).into(textimage);

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        AdView mAdView = (AdView) findViewById(R.id.adViewNewsD);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(NewsDetailActivity.this, NewsActivity.class);
        startActivity(intent);


       overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
