package com.example.fleatmarkert;

import android.os.SystemClock;
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

public class EditPostActivity extends AppCompatActivity {

    private long lastClickTime = 0;
    private static final long FAST_CLICK_TIME = 500;

    String id;
    String username;
    String titlepre;
    String contentpre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        try{
            id = getIntent().getExtras().getString("id");
            username = getIntent().getExtras().getString("username");
            titlepre = getIntent().getExtras().getString("title");
            contentpre = getIntent().getExtras().getString("content");
            ((EditText)findViewById(R.id.titleText)).setText(titlepre);
            ((EditText)findViewById(R.id.contentText)).setText(contentpre);
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        final Button button = (Button)findViewById(R.id.submitAddButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis()-lastClickTime<FAST_CLICK_TIME){
                    return;
                }
                lastClickTime=System.currentTimeMillis();
                String title = ((EditText)findViewById(R.id.titleText)).getText().toString();
                String content = ((EditText)findViewById(R.id.contentText)).getText().toString();
                if (!title.equals("")&&!content.equals("")) {
                    edit(id, title, content);
                } else{
                    new AlertDialog.Builder(EditPostActivity.this).setTitle("提示").setMessage("标题和内容都不能为空!").setPositiveButton("确定",null).show();
                }
            }
        });
    }

    private void edit(String id,String title, String content){
        Socket s = null;
        try{
            s = new Socket("123.56.4.12", 8087);

            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeUTF(id);
            dos.writeUTF(title);
            dos.writeUTF(content);

            String info = dis.readUTF();

            if (info.equals("True")){
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
                builder.setTitle("提示");
                builder.setMessage("修改成功，即将返回个人主页");
                builder.show();
                EditPostActivity.this.finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
                builder.setTitle("提示");
                builder.setMessage("修改失败").setPositiveButton("确定",null);
                builder.show();
                EditPostActivity.this.finish();
            }


        } catch (IOException e){
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
            builder.setTitle("提示");
            builder.setMessage("连接失败");
            builder.setPositiveButton("确认", null);
            builder.show();
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
