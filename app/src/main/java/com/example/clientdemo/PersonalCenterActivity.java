package com.example.clientdemo;
// ⚠️ 改成你自己项目里的包名，保持和 MainActivity.java 顶部一致

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;   // 用于弹出对话框

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;              // 对话框里用的输入框
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 个人中心页面
 * 功能：
 *  1. 显示圆形头像
 *  2. 从 SharedPreferences 中读取用户名和签名
 *  3. 点击签名可以修改签名，并写回 SharedPreferences
 *  4. 每一条信息条目整行可点，点击时弹 Toast 提示
 */
public class PersonalCenterActivity extends AppCompatActivity {

    // 顶部：头像、用户名、签名
    private ImageView ivAvatar;
    private TextView tvUsername;
    private TextView tvSignature;

    // 下方各条目
    private LinearLayout itemProfile;
    private LinearLayout itemFavorite;
    private LinearLayout itemHistory;
    private LinearLayout itemSetting;
    private LinearLayout itemAbout;

    // SharedPreferences 名字，要和 MainActivity 里保持一致
    private static final String PREF_NAME = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_center);

        // 1. 找控件
        initViews();

        // 2. 从 SharedPreferences 读取用户名和签名并显示
        loadUserInfoFromPrefs();

        // 3. 给签名 & 各个条目加点击事件
        initListeners();
    }

    /**
     * 通过 findViewById 找到布局中的控件
     */
    private void initViews() {
        ivAvatar = findViewById(R.id.iv_avatar);
        tvUsername = findViewById(R.id.tv_username);
        tvSignature = findViewById(R.id.tv_signature);

        itemProfile = findViewById(R.id.item_profile);
        itemFavorite = findViewById(R.id.item_favorite);
        itemHistory = findViewById(R.id.item_history);
        itemSetting = findViewById(R.id.item_setting);
        itemAbout = findViewById(R.id.item_about);
    }

    /**
     * 从 SharedPreferences 中读取用户名和签名
     * 如果没有读到，就给一个默认值
     */
    private void loadUserInfoFromPrefs() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String username = sp.getString("username", "默认用户名");
        String signature = sp.getString("signature", "默认签名：欢迎使用本App");

        tvUsername.setText(username);
        tvSignature.setText(signature);
    }

    /**
     * 设置各种点击事件：
     *  1. 点击签名 -> 弹出修改对话框
     *  2. 点击每个条目 -> Toast 提示
     */
    private void initListeners() {

        // 1）点击签名：修改签名
        tvSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditSignatureDialog();
            }
        });

        // 2）各个条目点击：Toast 提示

        itemProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalCenterActivity.this,
                        "点击了：个人信息",
                        Toast.LENGTH_SHORT).show();
            }
        });

        itemFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalCenterActivity.this,
                        "点击了：我的收藏",
                        Toast.LENGTH_SHORT).show();
            }
        });

        itemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalCenterActivity.this,
                        "点击了：浏览历史",
                        Toast.LENGTH_SHORT).show();
            }
        });

        itemSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalCenterActivity.this,
                        "点击了：设置",
                        Toast.LENGTH_SHORT).show();
            }
        });

        itemAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalCenterActivity.this,
                        "点击了：关于我们",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 弹出一个对话框，让用户修改签名
     * 步骤：
     *  1. 创建一个 EditText，并设置为当前签名
     *  2. 用 AlertDialog.Builder 构建一个对话框，标题“修改签名”
     *  3. 用户点击“保存”时：
     *      - 读取 EditText 里的内容
     *      - 写入 SharedPreferences
     *      - 更新 tvSignature 显示
     */
    private void showEditSignatureDialog() {

        // 1. 创建一个输入框，并把当前签名设置进去
        final EditText editText = new EditText(this);
        String currentSignature = tvSignature.getText().toString();
        editText.setText(currentSignature);
        editText.setSelection(currentSignature.length()); // 光标移动到最后，方便继续编辑

        // 2. 构建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改签名");
        builder.setView(editText);

        // “取消”按钮：什么都不做，直接关闭
        builder.setNegativeButton("取消", null);

        // “保存”按钮：把新签名写回 SharedPreferences，并更新 TextView
        builder.setPositiveButton("保存", (dialog, which) -> {
            String newSignature = editText.getText().toString().trim();

            if (newSignature.isEmpty()) {
                Toast.makeText(PersonalCenterActivity.this,
                        "签名不能为空",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 写入 SharedPreferences
            SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("signature", newSignature);
            editor.apply();

            // 立即更新页面上的签名显示
            tvSignature.setText(newSignature);

            Toast.makeText(PersonalCenterActivity.this,
                    "签名已更新",
                    Toast.LENGTH_SHORT).show();
        });

        // 3. 显示对话框
        builder.show();
    }
}
