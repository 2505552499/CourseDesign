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
    int language = 0;
    int position = 0;
    int autoplay = 0;
    int type = 4;
    User user;
    MyHelper myHelper;
    SQLiteDatabase db;
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
                Bitmap bitmap1 = bitmaps.get(0);
                Bitmap bitmap2 = bitmaps.get(1);
                Bitmap bitmap3 = bitmaps.get(2);
                ivtop.setImageBitmap(bitmap1);
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
        ivbuttom2 = findViewById(R.id.iv_bottom2);
        ivbutotm3 = findViewById(R.id.iv_bottom3);
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();
        tvname = (TextView) findViewById(R.id.tv_name);
        tvename = findViewById(R.id.tv_ename);
        tvpinyin = findViewById(R.id.tv_pinyin);
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
                Toast.makeText(LearnActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_en:
                language = 1;
                Toast.makeText(LearnActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_test:
                Intent intent = new Intent(this, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Pictures", (Serializable) Pictures);
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.opt_wrongbook:
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
                        Intent intent1 = new Intent(this, WrongTestActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("user", user);
                        intent1.putExtras(bundle1);
                        startActivity(intent1);
                    }
                }
                return true;
            case R.id.opt_logout:
                user = null;
                Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == 1){
            Bundle bundle = data.getExtras();
            user = (User) bundle.getSerializable("user");
            Toast.makeText(this, "欢迎您：" + user.getName(), Toast.LENGTH_SHORT).show();
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
        position += 1;
        if (position == Pictures.size()) position = 0;
        if (position < Pictures.size()) {
            Bitmap bitmap = bitmaps.get(position);
            Bitmap bitmap2 = bitmaps.get(position + 1);
            Bitmap bitmap3 = bitmaps.get(position + 2);
            ivtop.setImageBitmap(bitmap);
            ivbuttom1.setImageBitmap(bitmap);
            ivbuttom2.setImageBitmap(bitmap2);
            ivbutotm3.setImageBitmap(bitmap3);
            ivtop.setImageBitmap(bitmap);
            tvpinyin.setText(Pictures.get(position).getText_cn());
            tvname.setText(Pictures.get(position).getName());
            tvename.setText(Pictures.get(position).getText_en());
        }
    }

    public void pre() {
        position -= 1;
        if (position == -1) position = Pictures.size() - 1;
        if (position >= 0) {
            Bitmap bitmap = bitmaps.get(position);
            Bitmap bitmap2 = bitmaps.get(position + 1);
            Bitmap bitmap3 = bitmaps.get(position + 2);
            ivtop.setImageBitmap(bitmap);
            ivbuttom1.setImageBitmap(bitmap);
            ivbuttom2.setImageBitmap(bitmap2);
            ivbutotm3.setImageBitmap(bitmap3);
            tvpinyin.setText(Pictures.get(position).getText_cn());
            tvname.setText(Pictures.get(position).getName());
            tvename.setText(Pictures.get(position).getText_en());
        }
    }

    public void play() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            if (language == 0) {
                mediaPlayer.setDataSource(Pictures.get(position).getAudio_cn());
            } else if (language == 1) {
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
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
            }
        });
    }
    public void playCnEn(){
        MediaPlayer mediaPlayer = new MediaPlayer();
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
                        playEn();
                    }
                });
            }
        });
    }
    public void playEn(){
        MediaPlayer mediaPlayer = new MediaPlayer();
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
                play();
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
            default:
                break;
        }
    }
}