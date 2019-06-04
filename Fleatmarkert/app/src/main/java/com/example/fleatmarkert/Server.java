package com.example.fleatmarkert;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.transform.Result;

public class Server {
    public static void main(String[] args) {
        Connection conn = null;
        ServerSocket ss = null;
        ServerSocket ssr = null;
        String url = "jdbc:mysql://localhost:3306/Users?user=root&password=Wzq213thd";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载数据库驱动程序");
            conn = DriverManager.getConnection(url);
            ss = new ServerSocket(8080);
            Socket s1 = ss.accept();
            OutputStream os = s1.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = s1.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            while (true) {
                String username = dis.readUTF();
                String password = dis.readUTF();
                System.out.println(username);
                System.out.println(password);

                String sql = "select * from users where username="+username+" and password="+password;
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);
                if (result!=null){
                    dos.writeUTF("True");
                } else {
                    dos.writeUTF("False");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                    System.out.println("服务端关闭");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}