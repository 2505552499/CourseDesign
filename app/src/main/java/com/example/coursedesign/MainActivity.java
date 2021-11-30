package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coursedesign.Bean.Picture;
import com.example.coursedesign.sqlite.MyHelper;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int language = 0;
    int type = 4;
    MyHelper myHelper;
    SQLiteDatabase db;
    Button btn_learn;
    List<Picture> Pictures = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        createPicture(type);
        btn_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Pictures", (Serializable) Pictures);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void init() {
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();
        btn_learn = findViewById(R.id.btn_learn);
    }

    private void createPicture(int type) {
        Cursor pictures = db.query("picture", null, null, null, null, null, null);
        if(pictures.getCount() == 0){
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
        }else{
            pictures.moveToFirst();
            Picture picture = Picture.getInstance(pictures.getInt(0), pictures.getString(1), pictures.getString(2), pictures.getString(3), pictures.getString(4), pictures.getString(5), pictures.getString(6), pictures.getString(7));
            Pictures.add(picture);

        }
        while(pictures.moveToNext()){
//            Pictures.add(new Picture(pictures.getInt(0), pictures.getString(1), bitmaps.get(Pictures.size()), pictures.getString(3), pictures.getString(4), pictures.getString(5), pictures.getString(6), pictures.getString(7)));
            Picture picture = new Picture(pictures.getInt(0), pictures.getString(1), pictures.getString(2), pictures.getString(3), pictures.getString(4), pictures.getString(5), pictures.getString(6), pictures.getString(7));
            Pictures.add(picture);
        }
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
}