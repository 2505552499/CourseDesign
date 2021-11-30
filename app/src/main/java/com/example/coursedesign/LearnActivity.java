package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LearnActivity extends AppCompatActivity implements View.OnClickListener{
    int language = 0;
    int position = 0;
    int type = 4;
    MyHelper myHelper;
    SQLiteDatabase db;
    List<Picture> demo = new ArrayList<>();
    Map<Integer, Bitmap> bitmaps = new HashMap<>();
//    List<Picture> Pictures = new ArrayList<>();
//    HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    private ImageView ivtop;
    private TextView tvname;
    private TextView tvpinyin;
    private TextView tvename;
    private Button btnnext;
    private Button btnpre;
    private Button btnplay;
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 1){
                Bitmap bitmap = (Bitmap) msg.obj;
                ivtop.setImageBitmap(bitmap);
            }else{
                Toast.makeText(LearnActivity.this, "加载图片失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        init();
        Bundle bundle = this.getIntent().getExtras();
        demo = (List<Picture>) bundle.getSerializable("Pictures");
        for(int i = 0; i < demo.size(); i++){
            getImage(demo, i);
        }
        tvpinyin.setText(demo.get(0).getText_cn());
        tvname.setText(demo.get(0).getName());
        tvename.setText(demo.get(0).getText_en());
        btnnext.setOnClickListener(this::onClick);
        btnpre.setOnClickListener(this::onClick);
        btnplay.setOnClickListener(this::onClick);
    }
    private void init() {
        ivtop = findViewById(R.id.iv_top);
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();
        tvname = (TextView) findViewById(R.id.tv_name);
        tvename = findViewById(R.id.tv_ename);
        tvpinyin = findViewById(R.id.tv_pinyin);
        btnnext = findViewById(R.id.btn_next);
        btnplay = findViewById(R.id.btn_play);
        btnpre = findViewById(R.id.btn_pre);
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
                Toast.makeText(LearnActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_en:
                language = 1;
                Toast.makeText(LearnActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_test:
                Intent intent = new Intent(this, TestActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getImage(List<Picture> pictures, int i){
        final String path = pictures.get(i).getPath();
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
                            bitmaps.put(i ,bitmap);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bitmaps.get(0);
                            handler.sendMessage(msg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                    conn.disconnect();
                }
            }.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                position += 1;
                if(position < demo.size()){
                    Bitmap bitmap = bitmaps.get(position);
                    ivtop.setImageBitmap(bitmap);
                    tvpinyin.setText(demo.get(position).getText_cn());
                    tvname.setText(demo.get(position).getName());
                    tvename.setText(demo.get(position).getText_en());
                }else{
                    Toast.makeText(LearnActivity.this, "没有下一张了", Toast.LENGTH_SHORT).show();
                    position -= 1;
                }
                break;
            case R.id.btn_pre:
                position -= 1;
                if(position >= 0){
                    Bitmap bitmap = bitmaps.get(position);
                    ivtop.setImageBitmap(bitmap);
                    tvpinyin.setText(demo.get(position).getText_cn());
                    tvname.setText(demo.get(position).getName());
                    tvename.setText(demo.get(position).getText_en());
                }else{
                    Toast.makeText(LearnActivity.this, "没有上一张了", Toast.LENGTH_SHORT).show();
                    position += 1;
                }
            case R.id.btn_play:
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    if(language == 0){
                        mediaPlayer.setDataSource(demo.get(position).getAudio_cn());
                    }else if (language == 1){
                        mediaPlayer.setDataSource(demo.get(position).getAudio_en());
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
            default:
                break;
        }
    }
}