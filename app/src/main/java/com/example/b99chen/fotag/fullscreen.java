package com.example.b99chen.fotag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.io.IOException;
import java.net.URL;

public class fullscreen extends AppCompatActivity {
    private ImageView img;
    private RatingBar rb;
    int idx;
    float star;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen);

        Intent intent = getIntent();
        final String url = intent.getStringExtra("addr");
        star = intent.getFloatExtra("star", -1);
        rb = findViewById(R.id.ratingBar3);

        rb.setRating(star);
        img = findViewById(R.id.fullscreen);
        context = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL web = new URL(url);
                    final Bitmap image = BitmapFactory.decodeStream(web.openConnection().getInputStream());
                    ((fullscreen)context).runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            ((ImageView)findViewById(R.id.fullscreen)).setImageBitmap(image);
                        }
                    });
                } catch(IOException e) {
                    System.out.println(e);
                }
            }
        }).start();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("returnStar", star);
                setResult(2, data);
                finish();
            }
        });

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                star = rating;
            }
        });
    }
}
