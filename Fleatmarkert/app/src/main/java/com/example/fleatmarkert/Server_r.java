package com.example.fleatmarkert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Server_r {
    public static void main(String[] args) {
        Connection conn = null;
        ServerSocket ss = null;
        ServerSocket ssr = null;
        String url = "jdbc:mysql://localhost:3306/Users?user=root&password=Wzq213thd";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载数据库驱动程序");
            conn = DriverManager.getConnection(url);
            ss = new ServerSocket(8081);
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

                String sql = "select * from users where username="+username;
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);
                if (result!=null){
                    dos.writeUTF("False");
                } else {
                    String registSql = "insert into users values("+username+","+password+")";
                    dos.writeUTF("True");
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
