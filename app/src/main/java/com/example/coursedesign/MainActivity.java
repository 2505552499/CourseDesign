package com.example.coursedesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursedesign.Bean.Picture;
import com.example.coursedesign.sqlite.MyHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    int language = 0;
    int position = 0;
    int type = 4;
    MyHelper myHelper;
    SQLiteDatabase db;
    List<Picture> Pictures = new ArrayList<>();
    HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    private ImageView ivtop;
    private TextView tvname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        createPicture(type);
        ivtop.setImageBitmap(bitmaps.get(position));
        tvname.setText("哈哈哈");
        Button button = findViewById(R.id.btn_bmp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = bitmaps.get(position);
                ivtop.setImageBitmap(bitmap);
                tvname.setText("名称:" + Pictures.get(position).getName());
            }
        });
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position += 1;
                if(position < Pictures.size()){
                    Bitmap bitmap = bitmaps.get(position);
                    ivtop.setImageBitmap(bitmap);
                    tvname.setText("名称：" + Pictures.get(position).getName());
                }else{
                    Toast.makeText(MainActivity.this, "没有下一张了", Toast.LENGTH_SHORT).show();
                    position -= 1;
                }
            }
        });
        Button btnPre = findViewById(R.id.btn_pre);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position -= 1;
                if(position >= 0){
                    Bitmap bitmap = bitmaps.get(position);
                    ivtop.setImageBitmap(bitmap);
                    tvname.setText("名称:" + Pictures.get(position).getName());
                }else{
                    Toast.makeText(MainActivity.this, "没有上一张了", Toast.LENGTH_SHORT).show();
                    position += 1;
                }
            }
        });
        Button btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    if(language == 0){
                        mediaPlayer.setDataSource(Pictures.get(position).getAudio_cn());
                    }else if (language == 1){
                        mediaPlayer.setDataSource(Pictures.get(position).getAudio_en());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }
        });
    }
    private void createPicture(int type) {
        Cursor pictures = db.query("picture", null, null, null, null, null, null);
        if(pictures.getCount() == 0){
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
        }else{
            pictures.moveToFirst();
            getImage(pictures, pictures.getPosition());
            Picture picture = Picture.getInstance(pictures.getInt(0), pictures.getString(1), pictures.getString(2), pictures.getString(3), pictures.getString(4), pictures.getString(5), pictures.getString(6), pictures.getString(7));
            Pictures.add(picture);

        }
        while(pictures.moveToNext()){
            getImage(pictures, pictures.getPosition());
//            Pictures.add(new Picture(pictures.getInt(0), pictures.getString(1), bitmaps.get(Pictures.size()), pictures.getString(3), pictures.getString(4), pictures.getString(5), pictures.getString(6), pictures.getString(7)));
            Picture picture = new Picture(pictures.getInt(0), pictures.getString(1), pictures.getString(2), pictures.getString(3), pictures.getString(4), pictures.getString(5), pictures.getString(6), pictures.getString(7));
            Pictures.add(picture);
        }
    }

    private void init() {
        ivtop = findViewById(R.id.iv_top);
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();
        tvname = (TextView) findViewById(R.id.tv_name);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.opt_number:
                type = 1;
                Toast.makeText(this, "数字", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_animal:
                type = 2;
                Toast.makeText(this, "动物", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_vehicle:
                type = 3;
                Toast.makeText(this, "交通工具", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_fruit:
                type = 4;
                Toast.makeText(this, "水果", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_color:
                type = 5;
                Toast.makeText(this, "颜色", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_shape:
                type = 6;
                Toast.makeText(this, "形状", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_cn:
                language = 0;
                Toast.makeText(MainActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_en:
                language = 1;
                Toast.makeText(MainActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_test:
                Intent intent = new Intent(this, TestActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getImage(Cursor pictures, int id){
        final String path = pictures.getString(2);
        if(TextUtils.isEmpty(path)){
            Log.d("getImage", "图片路径为空");
        }else{
            new Thread(){
                private HttpURLConnection conn;
                public Bitmap bitmap;
                public void run(){
                    try{
                        URL url = new URL(path);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        int code = conn.getResponseCode();
                        if(code == 200){
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
//                            Pictures.add(new Picture(pictures.getInt(0), pictures.getString(1), bitmap, pictures.getString(3), pictures.getString(4), pictures.getString(5), pictures.getString(6), pictures.getString(7)));
                            bitmaps.put(id ,bitmap);
                        }
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                    conn.disconnect();
                }
            }.start();
        }
    }
}