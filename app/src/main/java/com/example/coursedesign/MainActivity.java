package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coursedesign.Bean.Picture;
import com.example.coursedesign.Bean.User;
import com.example.coursedesign.sqlite.MyHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int language = 0;
    int typeid = 4;
    MyHelper myHelper;
    SQLiteDatabase db;
    Button btn_learn;
    List<Picture> Pictures;
    User user;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Pictures = createPicture(typeid);
        btn_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Pictures", (Serializable) Pictures);
                bundle.putSerializable("user", user);
                intent.putExtra("language", language);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void init() {
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();
        btn_learn = findViewById(R.id.btn_learn);
    }

    private List<Picture>  createPicture(int typeid) {
        List<Picture> tempPictures = new ArrayList<>();
        String type = "fruit";
        switch (typeid){
            case 1:
                type = "number";
                break;
            case 2:
                type = "animal";
                break;
            case 3:
                type = "vehicle";
                break;
            case 4:
                type = "fruit";
                break;
            case 5:
                type = "color";
                break;
            case 6:
                type = "shape";
                break;
        }
        String[] columms = {"type"};
        Cursor cursor = db.query("picture", null, "type=?", new String[]{type}, null, null, null);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "没有数据,请重新选择分类", Toast.LENGTH_SHORT).show();
        }else{
            cursor.moveToFirst();
            Picture picture = Picture.getInstance(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            tempPictures.add(picture);

        }
        while(cursor.moveToNext()){
            Picture picture = new Picture(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            tempPictures.add(picture);
        }
        return tempPictures;
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.opt_number:
                typeid = 1;
                Pictures.clear();
                Pictures = createPicture(typeid);
                if(Pictures.size() != 0)
                    Toast.makeText(this, "切换分类为数字成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_animal:
                typeid = 2;
                Pictures.clear();
                Pictures = createPicture(typeid);
                if(Pictures.size() != 0)
                    Toast.makeText(this, "切换分类为动物成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_vehicle:
                typeid = 3;
                Pictures.clear();
                Pictures = createPicture(typeid);
                if(Pictures.size() != 0)
                    Toast.makeText(this, "切换分类为交通工具成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_fruit:
                typeid = 4;
                Pictures.clear();
                Pictures = createPicture(typeid);
                if(Pictures.size() != 0)
                    Toast.makeText(this, "切换分类为水果成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_color:
                typeid = 5;
                Pictures.clear();
                Pictures = createPicture(typeid);
                if(Pictures.size() != 0)
                    Toast.makeText(this, "切换分类为颜色成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_shape:
                typeid = 6;
                Pictures.clear();
                Pictures = createPicture(typeid);
                if(Pictures.size() != 0)
                    Toast.makeText(this, "切换分类为形状成功", Toast.LENGTH_SHORT).show();
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
                if(Pictures.size() != 0){
                    Intent intent = new Intent(this, TestActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Pictures", (Serializable) Pictures);
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "所选分类没有数据，请重新选择", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.opt_login:
                if(user == null){
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                }else{
                    Toast.makeText(this, "您已经登录，换账号请先注销", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.opt_logout:
                user = null;
                Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opt_wrongbook:
                if(user == null){
                    Toast.makeText(this, "请先登录后使用错题集功能", Toast.LENGTH_SHORT).show();
                }else{
                    int wid = user.getWid();
                    Cursor cursor = db.rawQuery("select * from wrongbook where wid=?", new String[]{String.valueOf(wid)});
                    if(cursor.getCount() == 0){
                        Toast.makeText(this, "您当前没有错题，恭喜！", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent1 = new Intent(this, WrongTestActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);
                        intent1.putExtra("language", language);
                        intent1.putExtras(bundle);
                        startActivityForResult(intent1, 1);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            Bundle bundle = data.getExtras();
            user = (User) bundle.getSerializable("user");
            Toast.makeText(this, "欢迎您：" + user.getName(), Toast.LENGTH_SHORT).show();
        }
        if(requestCode == 1 && resultCode == 2){
            Bundle bundle = data.getExtras();
            user = (User) bundle.getSerializable("user");
            language = data.getIntExtra("language", 0);
        }
        if(requestCode == 1 && resultCode == 3){
            language = data.getIntExtra("language", 0);
        }
    }
}