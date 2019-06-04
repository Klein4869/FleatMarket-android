package com.example.fleatmarkert;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)findViewById(R.id.signInButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final Intent intent = new Intent();
        intent.setClass(MainActivity.this, RegistActivity.class);
        Button button2 = (Button)findViewById(R.id.signUpButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case (1):{
                EditText editText = (EditText)findViewById(R.id.usernameText);
                editText.setText(data.getStringExtra("username"));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
