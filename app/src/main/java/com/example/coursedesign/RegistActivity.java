package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coursedesign.sqlite.MyHelper;

import java.util.Timer;
import java.util.TimerTask;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_account;
    EditText et_name;
    EditText et_password;
    Button btn_regist;
    MyHelper heler;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
    }

    private void init() {
        et_account = findViewById(R.id.et_account_regist);
        et_name = findViewById(R.id.et_name_regist);
        et_password = findViewById(R.id.et_password_regist);
        btn_regist = findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(this);
        heler = new MyHelper(this);
        db = heler.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_regist:
                String account = et_account.getText().toString();
                String name = et_name.getText().toString();
                String password = et_password.getText().toString();
                Cursor cursor = db.rawQuery("select * from user where account=?", new String[]{account});
                Cursor cursor1 = db.rawQuery("select * from user ", null);
                int wid = cursor1.getCount() + 1;
                if(cursor.getCount() == 0){
                    db.execSQL("insert into user (account, name, password, wid) values(?, ?, ?, ?)", new Object[]{account, name, password, wid});
                    Toast.makeText(this, "注册成功！2秒后自动回到登录界面", Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    timer.schedule(timerTask, 2000);
                }else{
                    Toast.makeText(this, "此账号已被注册，请重新输入一个账号", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}