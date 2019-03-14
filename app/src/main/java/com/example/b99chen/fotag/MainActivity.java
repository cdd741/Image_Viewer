package com.example.b99chen.fotag;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {
    LinearLayout view;
    TableLayout tb;
    ImageButton loadButton, clearButton;
    RatingBar rtb;
    float stars[] = new float[10];
    float filtered = 0;
    float oldFil = 0;
    Boolean created = false;
    final String imgUrl[] = new String[10];
    int fullScreen = -1;


    // data pack
    public class ConfigWrapper{
        public float[] Cstars;
        public Boolean Ccreated;
        public float Cfiltered;
        public float ColdFil;

        protected void loadMyData(){
            stars = Cstars;
            created = Ccreated;
            filtered = Cfiltered;
            oldFil = ColdFil;
        }
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState)
    {

    }

    // save last data
    @Override
    public ConfigWrapper onRetainCustomNonConfigurationInstance(){
        ConfigWrapper config = new ConfigWrapper();
        config.Cstars = stars;
        config.Ccreated = created;
        config.Cfiltered = filtered;
        config.ColdFil = oldFil;
        return config;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get last data
        final ConfigWrapper data = (ConfigWrapper) getLastCustomNonConfigurationInstance();
        if (data != null) {
//            data.loadMyData();
            created = data.Ccreated;
            stars = data.Cstars;
            filtered = data.Cfiltered;
            oldFil = data.ColdFil;
        }
        // check orientation and set buttons
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            loadButton = findViewById(R.id.load_land);
            clearButton = findViewById(R.id.clear_land);
            rtb = findViewById(R.id.ratingBar_land);
        }
        else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            loadButton = findViewById(R.id.load_port);
            clearButton = findViewById(R.id.clear_port);
            rtb = findViewById(R.id.ratingBar_port);
        }
        // create two view version
        view = findViewById(R.id.linear);
        tb = findViewById(R.id.table);
        rtb.setRating(filtered);


        imgUrl[0] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/bunny.jpg";
        imgUrl[1] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/chinchilla.jpg";
        imgUrl[2] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/doggo.jpg";
        imgUrl[3] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/hamster.jpg";
        imgUrl[4] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/husky.jpg";
        imgUrl[5] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/kitten.png";
        imgUrl[6] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/loris.jpg";
        imgUrl[7] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/puppy.jpg";
        imgUrl[8] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/redpanda.jpg";
        imgUrl[9] = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/sleepy.png";

        if(created) createView(this, imgUrl);

        final Context context = this;
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                created = true;
                for(int i = 0; i < 10;  ++i){
                    stars[i] = 0;
                }
                createView(context, imgUrl);
                filtered = 0;
                rtb.setRating(filtered);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 10; ++i){
                    stars[i] = 0;
                }
                clearView();
                created = false;
                filtered = 0;
                rtb.setRating(filtered);
            }
        });

        rtb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
//                if(filtered == rating){
//                    filtered = 0;
//                    rtb.setRating(0);
//                }else
                filtered = rating;
                clearView();
                createView(context, imgUrl);
            }
        });


    }

    public void rank(int idx, float star){
//        Intent n = new Intent();
//        n.putExtra("FUCK", 5);
//        n.getIntExtra("FUCK", -1);

        stars[idx] = star;
        clearView();
        createView(this, imgUrl);
    }

    void createView(Context context, String[] imgUrl) {
        if(!created) return;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tb.removeAllViews();
            TableRow tbRows[] = new TableRow[5];
            int col = 0;
            int row = 0;
            for (int i = 0; i < 10; ++i) {
                if(filtered <= stars[i]){
                    if(col == 0) {
                        tbRows[row] = new TableRow(context);
                        tb.addView(tbRows[row]);
                    }
                    tbRows[row].addView(new imagelayout(context, imgUrl[i], stars[i], i, this));
                    ++col;
                    if(col > 1){
                        col = 0;
                        ++row;
                    }
                }
            }
        }
        else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view.removeAllViews();
            for (int i = 0; i < 10; ++i) {
                if(filtered <= stars[i])
                    view.addView(new imagelayout(context, imgUrl[i], stars[i], i, this));
            }
        }
    }

    void clearView(){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            (tb).removeAllViews();
        }
        else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            (view).removeAllViews();
        }
    }

    public void open(int idx){
        Intent intent = new Intent(MainActivity.this, fullscreen.class);
        String url = imgUrl[idx];
        float star = stars[idx];
        intent.putExtra("addr", url);
        intent.putExtra("star", star);
        startActivityForResult(intent, 1);

        fullScreen = idx;
    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode ,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            float star = data.getFloatExtra("returnStar", -1);
            if(star != -1){
                rank(fullScreen, star);
            }

            fullScreen = -1;
        }
    }
}
