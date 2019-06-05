package com.example.fleatmarkert;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PostListActivity extends AppCompatActivity {

    String username;
    private long lastClickTime = 0;
    private static final int FAST_CLICK_TIME=500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        try {
            username = getIntent().getExtras().getString("username");
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        getTitles();

        final Button button = (Button)findViewById(R.id.toManageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis()-lastClickTime<FAST_CLICK_TIME){
                    return;
                }
                lastClickTime=System.currentTimeMillis();
                Intent intent = new Intent();
                intent.setClass(PostListActivity.this, UserActivity.class);
                intent.putExtra("username",username);
                PostListActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTitles();
    }

    /**
     * 获取帖子标题并显示
     */
    private void getTitles(){
        Socket s = null;
        try {
            s = new Socket("123.56.4.12", 8082);

            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeUTF("请求帖子列表");

            String info = dis.readUTF();
            final String[] titles = info.split("\u999f");
            if (titles[0].equals("")) return;
            info = dis.readUTF();
            final String[] ids = info.split("\u999f");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PostListActivity.this,   // Context上下文
                    android.R.layout.simple_list_item_1,  // 子项布局id
                    titles);

            final ListView listView = (ListView)findViewById(R.id.postListView);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listView.setEnabled(false);
                    showPost((String)ids[position]);
                    listView.setEnabled(true);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(PostListActivity.this);
            builder.setTitle("提示");
            builder.setMessage("连接失败");
            builder.setPositiveButton("确认", null);
            builder.show();
        } finally {
            try {
                if (s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPost(String id) {
        Socket s = null;
        try {
            s = new Socket("123.56.4.12", 8086);

            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeUTF(id);

            String title_info = dis.readUTF();
            String content_info = dis.readUTF();

            new AlertDialog.Builder(PostListActivity.this).setTitle(title_info).setMessage(content_info).setPositiveButton("确定", null).show();


        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(PostListActivity.this);
            builder.setTitle("提示");
            builder.setMessage("连接失败");
            builder.setPositiveButton("确认", null);
            builder.show();
        } finally {
            try {
                if (s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
