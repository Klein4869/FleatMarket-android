package com.example.fleatmarkert;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostManageActivity extends AppCompatActivity {

    String username;

    private HashMap<Integer, String> idToTitle = new HashMap<Integer, String>();

    private List<Map<String, Object>> mData;

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_manage);

        try {
            username = getIntent().getExtras().getString("username");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getMines();

        Button button = (Button) findViewById(R.id.AddButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PostManageActivity.this, AddPostActivity.class);
                intent.putExtra("username", username);
                PostManageActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMines();
    }

    private void getMines() {
        Socket s = null;

        try {
            s = new Socket("123.56.4.12", 8083);

            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeUTF(username);

            String info = dis.readUTF();
            String[] titles = info.split("\u999f");

            info = dis.readUTF();
            String[] ids = info.split("\u999f");

            mData = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < titles.length; ++i) {
                if (titles[i].equals("")) {
                    break;
                }
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("title", titles[i]);
                map.put("id", ids[i]);

                idToTitle.put(Integer.parseInt(ids[i]), titles[i]);
                mData.add(map);
            }

            MyAdapter myAdapter = new MyAdapter(this);

            lv = (ListView) findViewById(R.id.myPostList);

            lv.setAdapter(myAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showPost((String) mData.get(position).get("id"));
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

            new AlertDialog.Builder(PostManageActivity.this).setTitle(title_info).setMessage(content_info).setPositiveButton("确定", null).show();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public final class ViewHolder {
        public TextView title;
        public Button showButton;
        public Button deleteButton;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.vlist, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
                viewHolder.showButton = (Button) convertView.findViewById(R.id.showButton);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.title.setText((String) mData.get(position).get("title"));
            viewHolder.showButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPost((String)mData.get(position).get("id"));
                }
            });
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(PostManageActivity.this).setTitle("提示").setMessage("确定要删除吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Socket s = null;
                            try {
                                s = new Socket("123.56.4.12", 8085);

                                InputStream is = s.getInputStream();
                                DataInputStream dis = new DataInputStream(is);

                                OutputStream os = s.getOutputStream();
                                DataOutputStream dos = new DataOutputStream(os);

                                String id = (String) mData.get(position).get("id");
                                System.out.println(id);

                                dos.writeUTF(id);

                                String info = dis.readUTF();
                                if (info.equals("True")) {
                                    new AlertDialog.Builder(PostManageActivity.this).setTitle("提示").setMessage("删除成功").setPositiveButton("确定", null).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (s != null) {
                                    try {
                                        s.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                getMines();
                            }
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });

            return convertView;
        }
    }
}
