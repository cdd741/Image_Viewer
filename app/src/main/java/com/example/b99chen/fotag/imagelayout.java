package com.example.b99chen.fotag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.io.IOException;
import java.net.URL;

public class imagelayout extends ConstraintLayout {
    private float star = 0;
    RatingBar rb;
    ImageView imgV;

    public imagelayout(final Context context, final String url, float star, int idx, MainActivity ma)
    {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.imagelayout, this);
        rb = (RatingBar) findViewById(R.id.ratingBar);
        imgV = (ImageView)findViewById(R.id.imageView);
        this.star = star;
        rb.setRating(this.star);
        final int Idx = idx;
        final MainActivity Ma = ma;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL web = new URL(url);
                    final Bitmap image = BitmapFactory.decodeStream(web.openConnection().getInputStream());
                    ((MainActivity)context).runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(image);
                        }
                    });
                } catch(IOException e) {
                    System.out.println(e);
                }
            }
        }).start();

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Ma.rank(Idx, rating);
            }
        });


        imgV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Ma.open(Idx);
            }
        });
    }


    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }
}
