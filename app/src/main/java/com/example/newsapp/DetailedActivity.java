package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {

    TextView tvTitle,tvSource,tvTime,tvDescription;
    WebView webView;
    ImageView imageView;
    ProgressBar loader;
    CardView cardView,cardView2;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        tvTitle=findViewById(R.id.tvId);
        tvSource=findViewById(R.id.tvSource);
        tvTime=findViewById(R.id.tvDate);
        tvDescription=findViewById(R.id.tvDesc);
        cardView=findViewById(R.id.cardView);
        cardView2=findViewById(R.id.cardView2);

        linearLayout=findViewById(R.id.linearLayout);

        imageView=findViewById(R.id.imageView);

        webView=findViewById(R.id.webView);

        loader=findViewById(R.id.webViewLoader);
        loader.setVisibility(View.VISIBLE);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String source=intent.getStringExtra("source");
        String time=intent.getStringExtra("time");
        String imageUrl=intent.getStringExtra("imageUrl");
        String url=intent.getStringExtra("url");
        String description=intent.getStringExtra("description");

        tvTitle.setText(title);
        tvSource.setText(source);
        tvTime.setText(time);
        tvDescription.setText(description);

        Picasso.with(DetailedActivity.this).load(imageUrl).into(imageView);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if(webView.isShown())
        {
            loader.setVisibility(View.INVISIBLE);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String dark_mode = prefs.getString("mode", "no id");


        //Toast.makeText(DetailedActivity.this,""+dark_mode,Toast.LENGTH_SHORT).show();
        if(dark_mode.equals("false")) {
            cardView.setCardBackgroundColor(Color.WHITE);
            tvTitle.setTextColor(Color.BLACK);
            tvDescription.setTextColor(Color.GRAY);
            cardView2.setCardBackgroundColor(Color.WHITE);
            webView.setBackgroundColor(Color.WHITE);
        }

        else if(dark_mode.equals("true"))
        {
            linearLayout.setBackgroundColor(Color.BLACK);
            cardView.setCardBackgroundColor(Color.BLACK);
            tvTitle.setTextColor(Color.WHITE);
            tvDescription.setTextColor(Color.WHITE);
            cardView2.setCardBackgroundColor(Color.BLACK);
            webView.setBackgroundColor(Color.GRAY);
        }
    }

}