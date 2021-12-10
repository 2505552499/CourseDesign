package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Date;
import java.util.Random;

import com.example.coursedesign.Bean.Picture;
import com.example.coursedesign.Bean.User;
import com.example.coursedesign.sqlite.MyHelper;

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
    MediaPlayer mediaPlayer;
    User user;
    MyHelper helper;
    SQLiteDatabase db;
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
        user = (User) bundle.getSerializable("user");
        Pictures = (List<Picture>) bundle.getSerializable("Pictures");
        getImage(Pictures, Pictures.size());
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        },600);
    }

    private void init() {
        helper = new MyHelper(this);
        db = helper.getWritableDatabase();
        tvGuess = findViewById(R.id.tv_guess);
        ivRandom1 = findViewById(R.id.iv_random1);
        ivRandom1.setOnClickListener(this);
        ivRandom2 = findViewById(R.id.iv_random2);
        ivRandom2.setOnClickListener(this);
        ivRandom3 = findViewById(R.id.iv_random3);
        ivRandom3.setOnClickListener(this);
        ivRandom4 = findViewById(R.id.iv_random4);
        ivRandom4.setOnClickListener(this);
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
        random_arr.add(randomtv);
        tvGuess.setText(Pictures.get(Pictureid).getName());
        ivRandom1.setImageBitmap(bitmap1);
        ivRandom2.setImageBitmap(bitmap2);
        ivRandom3.setImageBitmap(bitmap3);
        ivRandom4.setImageBitmap(bitmap4);
        return random_arr;
    }

    public void play(String path) {
        mediaPlayer = MediaPlayer.create(this, R.raw.xiamian);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
                play2(path);
            }
        });
    }

    public void play2(String path) {
        mediaPlayer = new MediaPlayer();
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
                            mediaPlayer = null;
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void play3(){
        mediaPlayer = MediaPlayer.create(this, R.raw.zhengque);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }
    public void play4(){
        mediaPlayer = MediaPlayer.create(this, R.raw.cuowu);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }

    public void change(int id){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        switch (init_arr.get(5)){
            case 0:
                ivRandom2.setImageResource(R.drawable.error);
                ivRandom3.setImageResource(R.drawable.error);
                ivRandom4.setImageResource(R.drawable.error);
                break;
            case 1:
                ivRandom1.setImageResource(R.drawable.error);
                ivRandom3.setImageResource(R.drawable.error);
                ivRandom4.setImageResource(R.drawable.error);
                break;
            case 2:
                ivRandom1.setImageResource(R.drawable.error);
                ivRandom2.setImageResource(R.drawable.error);
                ivRandom4.setImageResource(R.drawable.error);
                break;
            case 3:
                ivRandom1.setImageResource(R.drawable.error);
                ivRandom2.setImageResource(R.drawable.error);
                ivRandom3.setImageResource(R.drawable.error);
                break;
            default:
                break;
        }
        if(Pictures.get(init_arr.get(4)).getName().equals(Pictures.get(init_arr.get(id)).getName())){
            mediaPlayer = MediaPlayer.create(this, R.raw.zhengque);
        }else{
            if(user != null){
                int wid = user.getWid();
                int pid = Pictures.get(init_arr.get(4)).getId();
                Cursor cursor = db.rawQuery("select * from wrongbook where wid=? and pid=?", new String[]{String.valueOf(wid), String.valueOf(pid)});
                if(cursor.getCount() == 0){
                    db.execSQL("insert into wrongbook (wid, pid) values (?, ?)", new Object[]{wid, pid});
                }
            }
            mediaPlayer = MediaPlayer.create(this, R.raw.cuowu);
        }
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                init_arr = getRandom();
                String path = Pictures.get(init_arr.get(4)).getAudio_cn();
                play(path);
            }
        });
    }
    @Override
    public void onClick(View v) {
        MediaPlayer mediaPlayer;
        switch (v.getId()) {
            case R.id.iv_random1:
                change(0);
                break;
            case R.id.iv_random2:
                change(1);
                break;
            case R.id.iv_random3:
                change(2);
                break;
            case R.id.iv_random4:
                change(3);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}