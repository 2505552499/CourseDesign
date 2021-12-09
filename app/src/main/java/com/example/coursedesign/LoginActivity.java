package com.example.coursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursedesign.Bean.Picture;
import com.example.coursedesign.Bean.User;
import com.example.coursedesign.sqlite.MyHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etAccount;
    EditText etPassword;
    Button btnLogin;
    TextView tvRegist;
    String account;
    String password;
    MyHelper  helper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        tvRegist = findViewById(R.id.tv_regist);
        tvRegist.setOnClickListener(this);
        helper = new MyHelper(this);
        db = helper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                account = etAccount.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                Cursor cursor = db.rawQuery("select * from user where account=?", new String[]{account});
                if(cursor.getCount() == 0){
                    Toast.makeText(this, "您输入的账号不存在，请重新输入！", Toast.LENGTH_SHORT).show();
                }else{
                    cursor.moveToFirst();
                    User user = new User(cursor.getInt(0), cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getInt(4));
                    if(user.getPassword().equals(password)){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);
                        intent.putExtras(bundle);
                        setResult(1, intent);
                        finish();
                    }else{
                        Toast.makeText(this, "密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_regist:
                Intent intent = new Intent(this, RegistActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }
}