package com.example.clientdemo;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录页面（当前用 MainActivity 充当登录页）
 * 功能：
 *  1. 账号/密码输入 用户名：admin，密码：123456
 *  2. 点击登录：到数据库中验证
 *  3. 验证成功：把用户名和签名存到 SharedPreferences，并跳转到个人中心
 *  4. 微信 / Apple 登录：点击弹 Toast
 */
public class MainActivity extends AppCompatActivity {

    // 控件
    private EditText etUsername;   // 账号输入框
    private EditText etPassword;   // 密码输入框
    private Button btnLogin;       // 登录按钮
    private View btnWechat;      // 微信登录按钮
    private View btnApple;       // Apple 登录按钮
    private TextView tvRegister;   // 立即注册
    private TextView tvForget;     // 忘记密码

    // 数据库帮助类
    private DBHelper dbHelper;

    // SharedPreferences 名字
    private static final String PREF_NAME = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 绑定布局
        setContentView(R.layout.activity_main);

        // 创建数据库帮助类对象
        dbHelper = new DBHelper(this);

        // 找控件
        initViews();

        // 设置各种点击事件
        initListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin   = findViewById(R.id.btn_login);
        btnWechat  = findViewById(R.id.btn_wechat);
        btnApple   = findViewById(R.id.btn_apple);
        tvRegister = findViewById(R.id.tv_register);
        tvForget   = findViewById(R.id.tv_forget);
    }

    private void initListeners() {

        // 登录按钮：真正做数据库验证的地方
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // 1. 判空
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "账号或密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return; // 直接结束，不再往下执行
                }

                // 2. 从数据库中查询是否存在这条账号记录
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                // 使用 ? 占位符可以避免 SQL 注入问题
                Cursor cursor = db.rawQuery(
                        "SELECT * FROM " + DBHelper.TABLE_USER +
                                " WHERE username=? AND password=?",
                        new String[]{username, password}
                );

                if (cursor != null && cursor.moveToFirst()) {
                    // ☆ 查询到了数据：说明账号密码正确，登录成功

                    Toast.makeText(MainActivity.this,
                            "登录成功，正在进入个人中心",
                            Toast.LENGTH_SHORT).show();

                    // 3. 使用 SharedPreferences 保存用户名和签名
                    SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    // 保存用户名为当前登录用户名
                    editor.putString("username", username);

                    // 如果之前没有保存过签名，可以给一个默认的签名
                    // （实际项目中会提供一个修改签名的入口，这里作业只要求存取）
                    if (!sp.contains("signature")) {
                        editor.putString("signature", "这个是默认签名，可以以后再修改");
                    }

                    editor.apply(); // 提交保存

                    // 4. 跳转到个人中心页面
                    Intent intent = new Intent(MainActivity.this, PersonalCenterActivity.class);
                    startActivity(intent);

                    // 可选：关闭登录页面
                    // finish();

                } else {
                    // ☆ 查询不到数据：账号或密码错误
                    Toast.makeText(MainActivity.this,
                            "账号或密码错误，请检查后重试",
                            Toast.LENGTH_SHORT).show();
                }

                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
        });

        // 微信登录：只做 Toast
        btnWechat.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,
                    "点击了微信登录（仅实现UI和提示，不做真实登录）",
                    Toast.LENGTH_SHORT).show();
        });

        // Apple 登录：只做 Toast
        btnApple.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,
                    "点击了 Apple 登录（仅实现UI和提示，不做真实登录）",
                    Toast.LENGTH_SHORT).show();
        });

        // 忘记密码：简单提示
        tvForget.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,
                    "忘记密码功能暂未实现～",
                    Toast.LENGTH_SHORT).show();
        });

        // 注册：简单提示
        tvRegister.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,
                    "注册功能暂未实现，本次作业重点是登录和个人中心",
                    Toast.LENGTH_SHORT).show();
        });
    }
}
