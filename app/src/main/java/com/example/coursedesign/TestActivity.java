package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import com.example.coursedesign.Bean.Picture;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvGuess;
    ImageView ivRandom1;
    ImageView ivRandom2;
    ImageView ivRandom3;
    ImageView ivRandom4;
    List<Integer> init_arr;
    List<Picture> Pictures = new ArrayList<>();
    Random random = new Random();
    Map<Integer, Bitmap> bitmaps = new HashMap<>();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                init_arr = getRandom();
                String path = Pictures.get(init_arr.get(4)).getAudio_cn();
                play(path);
            } else {
                Toast.makeText(TestActivity.this, "加载图片失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
        Bundle bundle = this.getIntent().getExtras();
        Pictures = (List<Picture>) bundle.getSerializable("Pictures");
        getImage(Pictures, Pictures.size());
        Log.e("order: ", "oncreat");
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        },50);
    }

    private void init() {
        tvGuess = findViewById(R.id.tv_guess);
        ivRandom1 = findViewById(R.id.iv_random1);
        ivRandom1.setOnClickListener(this::onClick);
        ivRandom2 = findViewById(R.id.iv_random2);
        ivRandom2.setOnClickListener(this::onClick);
        ivRandom3 = findViewById(R.id.iv_random3);
        ivRandom3.setOnClickListener(this::onClick);
        ivRandom4 = findViewById(R.id.iv_random4);
        ivRandom4.setOnClickListener(this::onClick);
    }


    public List<Integer> getRandom() {
        List<Integer> random_arr = new ArrayList<>();
        while (random_arr.size() < 4) {
            int temp = random.nextInt(Pictures.size());
            if (!random_arr.contains(temp))
                random_arr.add(temp);
        }
        Bitmap bitmap1 = bitmaps.get(random_arr.get(0));
        Bitmap bitmap2 = bitmaps.get(random_arr.get(1));
        Bitmap bitmap3 = bitmaps.get(random_arr.get(2));
        Bitmap bitmap4 = bitmaps.get(random_arr.get(3));
        int randomtv = random.nextInt(4);
        int Pictureid = random_arr.get(randomtv);
        random_arr.add(Pictureid);
        tvGuess.setText(Pictures.get(Pictureid).getName());
        ivRandom1.setImageBitmap(bitmap1);
        Log.e("order", random_arr.get(0).toString());
        ivRandom2.setImageBitmap(bitmap2);
        Log.e("order", random_arr.get(1).toString());
        ivRandom3.setImageBitmap(bitmap3);
        Log.e("order", random_arr.get(2).toString());
        ivRandom4.setImageBitmap(bitmap4);
        Log.e("order", random_arr.get(3).toString());

        Log.e("orderd", "getRandom" );
        return random_arr;
    }

    public void play(String path) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.xiamian);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                play2(path);
            }
        });
        Log.e("order", "play ");
    }

    public void play2(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("order", "play2");
    }
    public void play3(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.zhengque);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }
    public void play4(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cuowu);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }

    @Override
    public void onClick(View v) { ;
        MediaPlayer mediaPlayer;
        switch (v.getId()) {
            case R.id.iv_random1:
                if(Pictures.get(init_arr.get(4)).getName().equals(Pictures.get(init_arr.get(0)).getName())){
                    mediaPlayer = MediaPlayer.create(this, R.raw.zhengque);
                }else{
                    mediaPlayer = MediaPlayer.create(this, R.raw.cuowu);
                }
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                        Log.e("random1", Pictures.get(init_arr.get(4)).getName() + Pictures.get(init_arr.get(0)).getName());
                        init_arr = getRandom();
                        String path = Pictures.get(init_arr.get(4)).getAudio_cn();
                        play(path);
                    }
                });
                break;
            case R.id.iv_random2:
                if(Pictures.get(init_arr.get(4)).getName().equals(Pictures.get(init_arr.get(1)).getName())){
                    mediaPlayer = MediaPlayer.create(this, R.raw.zhengque);
                }else{
                    mediaPlayer = MediaPlayer.create(this, R.raw.cuowu);
                }
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                        Log.e("random1", Pictures.get(init_arr.get(4)).getName() + Pictures.get(init_arr.get(0)).getName());
                        init_arr = getRandom();
                        String path = Pictures.get(init_arr.get(4)).getAudio_cn();
                        play(path);
                    }
                });
                break;
            case R.id.iv_random3:
                if(Pictures.get(init_arr.get(4)).getName().equals(Pictures.get(init_arr.get(2)).getName())){
                    mediaPlayer = MediaPlayer.create(this, R.raw.zhengque);
                }else{
                    mediaPlayer = MediaPlayer.create(this, R.raw.cuowu);
                }
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                        Log.e("random1", Pictures.get(init_arr.get(4)).getName() + Pictures.get(init_arr.get(0)).getName());
                        init_arr = getRandom();
                        String path = Pictures.get(init_arr.get(4)).getAudio_cn();
                        play(path);
                    }
                });
                break;
            case R.id.iv_random4:
                if(Pictures.get(init_arr.get(4)).getName().equals(Pictures.get(init_arr.get(3)).getName())){
                    mediaPlayer = MediaPlayer.create(this, R.raw.zhengque);
                }else{
                    mediaPlayer = MediaPlayer.create(this, R.raw.cuowu);
                }
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                        Log.e("random1", Pictures.get(init_arr.get(4)).getName() + Pictures.get(init_arr.get(0)).getName());
                        init_arr = getRandom();
                        String path = Pictures.get(init_arr.get(4)).getAudio_cn();
                        play(path);
                    }
                });
                break;
        }
    }

    public void getImage(List<Picture> pictures, int size) {
        for (int i = 0; i < size; i++) {
            String path = pictures.get(i).getPath();
            if (TextUtils.isEmpty(path)) {
                Log.d("getImage", "图片路径为空");
            } else {
                int finalI = i;
                new Thread() {
                    private HttpURLConnection conn;
                    public Bitmap bitmap;

                    public void run() {
                        try {
                            URL url = new URL(path);
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(is);
                                bitmaps.put(finalI, bitmap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        conn.disconnect();
                    }
                }.start();
            }
        }
        Log.e("order", "getImage" );
    }
}