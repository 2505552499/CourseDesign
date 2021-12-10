package com.example.coursedesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursedesign.Bean.Picture;
import com.example.coursedesign.Bean.User;
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
import java.util.Timer;
import java.util.TimerTask;


public class LearnActivity extends AppCompatActivity implements View.OnClickListener {
    int language;
    int position = 0;
    int autoplay = 0;
    User user;
    MyHelper myHelper;
    SQLiteDatabase db;
    MediaPlayer mediaPlayer;
    List<Picture> Pictures = new ArrayList<>();
    Map<Integer, Bitmap> bitmaps = new HashMap<>();
    private GestureDetector gestureDetector;
    int ScreenWidth;//获取屏幕的宽高，可以自定义手势在屏幕上滑动的距离
    int ScreenHeight;
    private ImageView ivtop;
    private ImageView ivbuttom1;
    private ImageView ivbuttom2;
    private ImageView ivbutotm3;
    private TextView tvname;
    private TextView tvpinyin;
    private TextView tvename;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int size = bitmaps.size();
                Bitmap bitmap1 = bitmaps.get(size - 1);
                Bitmap bitmap2 = bitmaps.get(0);
                Bitmap bitmap3 = bitmaps.get(1);
                ivtop.setImageBitmap(bitmap2);
                ivbuttom1.setImageBitmap(bitmap1);
                ivbuttom2.setImageBitmap(bitmap2);
                ivbutotm3.setImageBitmap(bitmap3);
            }else if(msg.what == 2){
                next();
                playCnEn();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        init();
        Bundle bundle = this.getIntent().getExtras();
        language = this.getIntent().getIntExtra("language", 0);
        user = (User) bundle.getSerializable("user");
        Pictures = (List<Picture>) bundle.getSerializable("Pictures");
        for (int i = 0; i < Pictures.size(); i++) {
            getImage(Pictures, i);
        }

        tvpinyin.setText(Pictures.get(0).getText_cn());
        tvname.setText(Pictures.get(0).getName());
        tvename.setText(Pictures.get(0).getText_en());
        getScreenWidthAndHeight();
        gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() < 0 && Math.abs((int) (e1.getX() - e2.getX())) > 30) {
                    moveRight();
                    return true;
                } else if (e1.getX() - e2.getX() > 0 && Math.abs((int) (e1.getX() - e2.getX())) > 30) {
                    moveLeft();
                    return true;
                }
                return false;
            }
        });
    }

    private void moveLeft() {
        next();
    }

    private void moveRight() {
        pre();
    }


    private void init() {
        ivtop = findViewById(R.id.iv_top);
        ivbuttom1 = findViewById(R.id.iv_bottom1);
        ivbuttom1.setOnClickListener(this);
        ivbuttom2 = findViewById(R.id.iv_bottom2);
        ivbuttom2.setOnClickListener(this);
        ivbutotm3 = findViewById(R.id.iv_bottom3);
        ivbutotm3.setOnClickListener(this);
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();
        tvname = (TextView) findViewById(R.id.tv_name);
        tvname.setOnClickListener(this);
        tvename = findViewById(R.id.tv_ename);
        tvename.setOnClickListener(this);
        tvpinyin = findViewById(R.id.tv_pinyin);
        tvpinyin.setOnClickListener(this);
        ImageView btnnext = findViewById(R.id.btn_next);
        ImageView btnplay = findViewById(R.id.btn_play);
        ImageView btnpre = findViewById(R.id.btn_pre);
        btnnext.setOnClickListener(this);
        btnpre.setOnClickListener(this);
        btnplay.setOnClickListener(this);
        ImageView btnautoplay = findViewById(R.id.auto_play);
        btnautoplay.setOnClickListener(this);
    }

    private void getScreenWidthAndHeight() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        ScreenWidth = display.getWidth();
        ScreenHeight = display.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸监听事件被拦截，然后调用自定义的手势事件
        return gestureDetector.onTouchEvent(event);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulearn, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opt_cn:
                language = 0;
                Intent intent = new Intent();
                intent.putExtra("language", language);
                setResult(2, intent);
                Toast.makeText(LearnActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_en:
                language = 1;
                Intent intent1 = new Intent();
                intent1.putExtra("language", language);
                setResult(2, intent1);
                Toast.makeText(LearnActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_test:
                autoplayDestroy();
                Intent intent2 = new Intent(this, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Pictures", (Serializable) Pictures);
                bundle.putSerializable("user", user);
                intent2.putExtras(bundle);
                startActivity(intent2);
                return true;
            case R.id.opt_wrongbook:
                autoplayDestroy();
                if(user == null){
                    AlertDialog dialog;
                    dialog = new AlertDialog.Builder(this).setTitle("暂未登录")
                            .setMessage("是否前往登录？")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LearnActivity.this, LoginActivity.class);
                                    startActivityForResult(intent, 2);
                                }
                            }).setNegativeButton("取消", null)
                            .create();
                    dialog.show();
                }else{
                    int wid = user.getWid();
                    Cursor cursor = db.rawQuery("select * from wrongbook where wid=?", new String[]{String.valueOf(wid)});
                    if(cursor.getCount() == 0){
                        Toast.makeText(this, "您当前没有错题，恭喜！", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent4 = new Intent(this, WrongTestActivity.class);
                        intent4.putExtra("language", language);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("user", user);
                        intent4.putExtras(bundle1);
                        startActivityForResult(intent4, 2);
                    }
                }
                return true;
            case R.id.opt_logout:
                autoplayDestroy();
                user = null;
                sendReIntent();
                Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendReIntent(){
        Intent intent = new Intent();
        intent.putExtra("language", language);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        setResult(2, intent);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == 1){
            Bundle bundle = data.getExtras();
            user = (User) bundle.getSerializable("user");
            Toast.makeText(this, "欢迎您：" + user.getName(), Toast.LENGTH_SHORT).show();
            sendReIntent();
        }if(requestCode == 2 && resultCode == 3){
            language = data.getIntExtra("language", 0);
            sendReIntent();
        }
    }

    public void getImage(List<Picture> pictures, int i) {
        final String path = pictures.get(i).getPath();
        if (TextUtils.isEmpty(path)) {
            Log.d("getImage", "图片路径为空");
        } else {
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
                            bitmaps.put(i, bitmap);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bitmaps.get(2);
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    conn.disconnect();
                }
            }.start();
        }
    }

    public void next() {
        Bitmap bitmap1;
        Bitmap bitmap2;
        Bitmap bitmap3;
        position += 1;
        if(position == Pictures.size()) position = 0;
        if(position - 1 < 0){
            bitmap1 = bitmaps.get(Pictures.size() - 1);
        }else{
            bitmap1 = bitmaps.get(position - 1);
        }
        if(position + 1 == Pictures.size()){
            bitmap3 = bitmaps.get(0);
        }else{
            bitmap3 = bitmaps.get(position + 1);
        }
        bitmap2 = bitmaps.get(position);
        ivtop.setImageBitmap(bitmap2);
        ivbuttom1.setImageBitmap(bitmap1);
        ivbuttom2.setImageBitmap(bitmap2);
        ivbutotm3.setImageBitmap(bitmap3);
        tvpinyin.setText(Pictures.get(position).getText_cn());
        tvname.setText(Pictures.get(position).getName());
        tvename.setText(Pictures.get(position).getText_en());
    }

    public void pre() {
        Bitmap bitmap1;
        Bitmap bitmap2;
        Bitmap bitmap3;
        position -= 1;
        if(position == -1) position = Pictures.size() - 1;
        if(position - 1 < 0){
            bitmap1 = bitmaps.get(Pictures.size() - 1);
        }else{
            bitmap1 = bitmaps.get(position - 1);
        }
        if(position + 1 == Pictures.size()){
            bitmap3 = bitmaps.get(0);
        }else{
            bitmap3 = bitmaps.get(position + 1);
        }
        bitmap2 = bitmaps.get(position);
        ivtop.setImageBitmap(bitmap2);
        ivbuttom1.setImageBitmap(bitmap1);
        ivbuttom2.setImageBitmap(bitmap2);
        ivbutotm3.setImageBitmap(bitmap3);
        tvpinyin.setText(Pictures.get(position).getText_cn());
        tvname.setText(Pictures.get(position).getName());
        tvename.setText(Pictures.get(position).getText_en());
    }

    public void playCn(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(Pictures.get(position).getAudio_cn());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    }
    public void playCnEn(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(Pictures.get(position).getAudio_cn());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        playEn();
                    }
                });
            }
        });
    }
    public void playEn(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(Pictures.get(position).getAudio_en());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    }

    Timer timer = new Timer();
    TimerTask timerTask = null;
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_next:
                next();
                break;
            case R.id.btn_pre:
                pre();
                break;
            case R.id.btn_play:
                if(language == 0) playCn();
                else playEn();
                break;
            case R.id.auto_play:
                autoplay = Math.abs(autoplay - 1);
                if(autoplay == 1){
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    };
                    timer.schedule(timerTask, 0, 3000);
                }else if(autoplay == 0){
                    if(timerTask != null){
                        timerTask.cancel();
                        timerTask = null;
                    }
                }
                break;
            case R.id.tv_pinyin:
                playCn();
                break;
            case R.id.tv_name:
                playCn();
                break;
            case R.id.tv_ename:
                playEn();
                break;
            case R.id.iv_bottom1:
                pre();
                break;
            case R.id.iv_bottom3:
                next();
                break;
            default:
                break;
        }
    }

    public void autoplayDestroy(){
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoplayDestroy();
    }
}