package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.Model.Articles;
import com.example.newsapp.Model.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton dark;
    TextView name;
    final String API_KEY="883eb413cfb44984a65eb670376086f1";
    Adapter adapter;
    ConstraintLayout constraintLayout;
    boolean dark_mode;
    EditText editText;
    Button button;
    List<Articles> articles=new ArrayList<>();
    String dark_mode1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dark_mode=false;
        dark_mode1="false";
        name=findViewById(R.id.news);
        constraintLayout=findViewById(R.id.cons_layout);
        swipeRefreshLayout=findViewById(R.id.swipeRefresh);
        dark=findViewById(R.id.mode);
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dark_mode==false) {
                    name.setTextColor(Color.WHITE);
                    constraintLayout.setBackgroundColor(Color.BLACK);
                    recyclerView.setBackgroundColor(Color.BLACK);
                    dark_mode1="true";
                    dark_mode = true;
                }

                else if(dark_mode==true)
                {
                    name.setTextColor(Color.BLACK);
                    constraintLayout.setBackgroundColor(Color.WHITE);
                    recyclerView.setBackgroundColor(Color.WHITE);
                   dark_mode1="false";
                    dark_mode = false;
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("mode", dark_mode1);
                editor.commit();
            }
        });


       final String country="in";

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("",country,API_KEY);
            }
        });

        retrieveJson("",country,API_KEY);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editText.getText().toString().equals(""))
                {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(editText.getText().toString(),country,API_KEY);
                        }
                    });
                    retrieveJson(editText.getText().toString(),country,API_KEY);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter a search keyword ; Showing top headlines.",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("",country,API_KEY);
                        }
                    });
                    retrieveJson("",country,API_KEY);
                }
            }
        });

    }

    public void retrieveJson(String query,String country,String apiKey)
    {
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if(!editText.getText().toString().equals(""))
        {
            call=ApiClient.getInstance().getApi().getSpecificData(query,apiKey);
        }
        else
        {
            call=ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles()!=null)
                {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles=response.body().getArticles();
                    adapter=new Adapter(MainActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  String getCountry()
    {
        Locale locale=Locale.getDefault();
        String country=locale.getCountry();
        return country.toLowerCase();
    }
}