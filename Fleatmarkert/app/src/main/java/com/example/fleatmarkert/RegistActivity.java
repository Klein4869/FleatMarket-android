package com.example.fleatmarkert;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        Button button = (Button) findViewById(R.id.registButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.rusernameText)).getText().toString();
                String password = ((EditText) findViewById(R.id.rpasswordText)).getText().toString();
                boolean isCheck = signUpCheck(username, password, ((EditText) findViewById(R.id.repasswordText)).getText().toString());

                if (isCheck) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("恭喜您注册成功，确认回到登录页面登录");
                    builder.setPositiveButton("确认", null);
                    builder.show();
                    Intent intent = new Intent();
                    intent.setClass(RegistActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    RegistActivity.this.finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("注册失败，用户名重复，请重新输入");
                    builder.setPositiveButton("确认", null);
                    builder.show();
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
