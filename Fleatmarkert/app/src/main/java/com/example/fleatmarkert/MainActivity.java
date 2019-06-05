package com.example.fleatmarkert;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.Nullable;
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
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Intent intent1 = new Intent();
        intent1.setClass(MainActivity.this, PostListActivity.class);
        Button button1 = (Button) findViewById(R.id.signInButton);
        /*
        设置登录按钮的行为
         */
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerSocket ss = null;
                boolean isCheck = verifyLogin(((EditText) findViewById(R.id.usernameText)).getText().toString(), ((EditText) findViewById(R.id.passwordText)).getText().toString());
                if (isCheck) {
                    MainActivity.this.startActivity(intent1);
                    MainActivity.this.finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("登录失败");
                    builder.setPositiveButton("确认", null);
                    builder.show();
                }
            }
        });

        final Intent intent2 = new Intent();
        intent2.setClass(MainActivity.this, RegistActivity.class);
        Button button2 = (Button) findViewById(R.id.signUpButton);
        /*
        设置注册按钮的行为
         */
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivityForResult(intent2, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case (1): {
                EditText editText = (EditText) findViewById(R.id.usernameText);
                String username=data.getExtras().getString("username");
                if (username!=null)
                    editText.setText(username);
                EditText editText1 = (EditText) findViewById(R.id.passwordText);
                String password=data.getExtras().getString("password");
                if (username!=null)
                    editText.setText(username);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Check the username and password when login
     * @param username username the user input
     * @param password password the user input
     * @return true if username matches the password, else false
     */
    protected boolean verifyLogin(String username, String password){
        ServerSocket ss = null;
        Socket s = null;
        try {
            s = new Socket("123.56.4.12", 8080);

            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeUTF(username);
            dos.writeUTF(password);

            String info = dis.readUTF();
            if (info.equals("True")){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (s != null) s.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
