package com.example.fleatmarkert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
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

        Button button = (Button)findViewById(R.id.postManageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserActivity.this, PostManageActivity.class);
                intent.putExtra("username",username);
                UserActivity.this.startActivity(intent);
            }
        });
    }
}
