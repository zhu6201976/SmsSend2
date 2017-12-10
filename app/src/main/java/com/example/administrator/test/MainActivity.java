package com.example.administrator.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * 短信发送器2
 * 获取手机真实联系人信息，模板信息复杂化
 * 2017年12月10日18:34:26
 */

public class MainActivity extends AppCompatActivity {

    private EditText et_phone_number;
    private EditText et_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
        initUI();

    }

    /**
     * 初始化权限
     */
    private void initPermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.SEND_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }
        String[] permissions = new String[permissionList.size()];
        permissions = permissionList.toArray(permissions);
        if (permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (permissions.length == 2) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "you allow 2 permission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else if (permissions.length == 1) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "you allow 1 permission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }

    /**
     * 初始化控件
     */
    private void initUI() {
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    // 点击按钮,跳转ContactsActivity并回传电话号码
    public void add(View view) {
        startActivityForResult(new Intent(this, ContactsActivity.class), 1);
    }

    // 点击按钮,跳转TemplateActivity并回传模板短信
    public void insertTemplate(View view) {
        startActivityForResult(new Intent(this, TemplateActivity.class), 2);
    }

    // 点击按钮,发送短信
    public void send(View view) {
        String phoneNumber = et_phone_number.getText().toString().trim();
        String content = et_content.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "please input the phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.isEmpty()) {
            Toast.makeText(this, "please input the content", Toast.LENGTH_SHORT).show();
            return;
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, content, null, null);
        Toast.makeText(this, "发送成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                // 点击了add按钮,跳转ContactsActivity
                if (resultCode == 10) {
                    String phone = data.getStringExtra("phone");
                    et_phone_number.setText(phone);
                }
                break;
            case 2:
                // 点击了insertTemplate按钮
                if (resultCode == 20) {
                    String template = data.getStringExtra("template");
                    et_content.setText(template);
                }
                break;
        }
    }
}
