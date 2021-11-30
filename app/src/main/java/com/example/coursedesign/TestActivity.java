package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvGuess;
    ImageView ivRandom1;
    ImageView ivRandom2;
    ImageView ivRandom3;
    ImageView ivRandom4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();

    }

    private void init() {
        tvGuess = findViewById(R.id.tv_guess);
        ivRandom1 = findViewById(R.id.iv_random1);
        ivRandom2 = findViewById(R.id.iv_random2);
        ivRandom3 = findViewById(R.id.iv_random3);
        ivRandom4 = findViewById(R.id.iv_random4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_random1:
                break;
            case R.id.iv_random2:
                break;
            case R.id.iv_random3:
                break;
            case R.id.iv_random4:
                break;
        }
    }
}