package com.example.fleatmarkert;

import android.content.Intent;
import android.os.SystemClock;
import android.os.Trace;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RegistActivity extends AppCompatActivity {
    private long lastClickTime = 0;
    private static final long FAST_CLICK_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        final Button button = (Button) findViewById(R.id.registButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis()-lastClickTime<FAST_CLICK_TIME){
                    return;
                }
                lastClickTime=System.currentTimeMillis();
                String username = ((EditText) findViewById(R.id.rusernameText)).getText().toString();
                String password = ((EditText) findViewById(R.id.rpasswordText)).getText().toString();
                String repassword = ((EditText) findViewById(R.id.repasswordText)).getText().toString();
                if (!username.equals("") && !password.equals("") && !repassword.equals("")) {
                    if (password.equals(repassword)) {

                        boolean isCheck = signUpCheck(username, password, repassword);

                        if (isCheck) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("恭喜您注册成功，即将回到登录页面登录");
                            builder.show();
                            Intent intent = new Intent();
                            intent.putExtra("username", username);
                            intent.putExtra("password", password);
                            setResult(1, intent);
                            RegistActivity.this.finish();
                        } else {
                            new AlertDialog.Builder(RegistActivity.this).setTitle("提示").setMessage("用户名重复!").setPositiveButton("确定", null).show();
                        }
                    } else {
                        new AlertDialog.Builder(RegistActivity.this).setTitle("提示").setMessage("两次密码不一致!").setPositiveButton("确定", null).show();
                    }
                } else {
                    new AlertDialog.Builder(RegistActivity.this).setTitle("提示").setMessage("用户名和密码都不可为空!").setPositiveButton("确定",null).show();
                }

            }
        });
    }

    protected boolean signUpCheck(String username, String password, String repassword) {
        Socket s = null;
        try {
            s = new Socket("123.56.4.12", 8081);

            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            if (!password.equals(repassword)) {
                return false;
            }

            dos.writeUTF(username);
            dos.writeUTF(password);

            String info = dis.readUTF();
            if (info.equals("True")) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
            builder.setTitle("提示");
            builder.setMessage("连接超时");
            builder.setPositiveButton("确认", null);
            builder.show();
            return false;
        } finally {
            try {
                if (s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
