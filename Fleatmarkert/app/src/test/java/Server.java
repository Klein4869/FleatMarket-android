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
        while (true)
            listen();

    }

    private static void listen() {
        Connection conn = null;
        ServerSocket ss = null;
        String url = "jdbc:mysql://localhost:3306/Users?userUnicode=true&&characterEncoding=UTF-8&&user=root&password=Wzq213thd";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载数据库驱动程序");
            conn = DriverManager.getConnection(url);
            ss = new ServerSocket(8080);
            ss.setSoTimeout(5000);
            Socket s1 = ss.accept();
            OutputStream os = s1.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = s1.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            System.out.println("进入监听");
            String username = dis.readUTF();
            String password = dis.readUTF();
            System.out.println(username);
            System.out.println(password);

            Statement statement = conn.createStatement();
            String sql = "use Users;";
            statement.execute(sql);

            sql = "select * from users where username='" + username + "' and password='" + password + "';";
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) {
                System.out.println("登录成功");
                dos.writeUTF("True");
            } else {
                System.out.println("登录失败");
                dos.writeUTF("False");
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
                    System.out.println("一次监听完毕");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}