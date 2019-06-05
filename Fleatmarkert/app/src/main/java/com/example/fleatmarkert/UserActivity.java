package com.example.fleatmarkert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    private long lastClickTime = 0;
    private static final long FAST_CLICK_TIME = 500;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        try {
            username = getIntent().getExtras().getString("username");
        }catch (NullPointerException e){
            e.printStackTrace();
            username = "未获取";
        }

        TextView textView = (TextView)findViewById(R.id.usernameTextView);
        String displayer = "您好，"+username;
        textView.setText(displayer);

        final Button button = (Button)findViewById(R.id.postManageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis()-lastClickTime<FAST_CLICK_TIME){
                    return;
                }
                lastClickTime=System.currentTimeMillis();
                Intent intent = new Intent();
                intent.setClass(UserActivity.this, PostManageActivity.class);
                intent.putExtra("username",username);
                UserActivity.this.startActivity(intent);
            }
        });
    }
}
