package com.example.fleatmarkert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PostManageActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_manage);

        try{
            username = getIntent().getExtras().getString("username");
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        getMines();

        Button button = (Button)findViewById(R.id.AddButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PostManageActivity.this,AddPostActivity.class);
                intent.putExtra("username",username);
                PostManageActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMines();
    }

    private void getMines(){
        Socket s = null;

        try{
            s = new Socket("123.56.4.12", 8083);

            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeUTF(username);

            String info = dis.readUTF();

            String[] titles = info.split("\u999f");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PostManageActivity.this,   // Context上下文
                    android.R.layout.simple_list_item_1,  // 子项布局id
                    titles);

            ListView listView = (ListView)findViewById(R.id.myPostListView);

            listView.setAdapter(adapter);

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (s!=null){
                try {
                    s.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
