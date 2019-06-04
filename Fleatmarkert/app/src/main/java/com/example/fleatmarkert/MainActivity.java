package com.example.fleatmarkert;

import android.content.Intent;
import android.support.annotation.Nullable;
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
                editText.setText(data.getStringExtra("username"));
                EditText editText1 = (EditText) findViewById(R.id.passwordText);
                editText1.setText(data.getStringExtra("password"));
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
