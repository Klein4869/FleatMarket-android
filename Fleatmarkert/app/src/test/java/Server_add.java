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

public class Server_add {
    public static void main(String[] args) {
        while (true) {
            listen();
        }
    }

    private static void listen() {
        Connection conn = null;
        ServerSocket ss = null;
        String url = "jdbc:mysql://localhost:3306/Users?userUnicode=true&&characterEncoding=UTF-8&&user=root&password=Wzq213thd";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载数据库驱动程序");
            conn = DriverManager.getConnection(url);
            ss = new ServerSocket(8084);
            ss.setSoTimeout(5000);
            Socket s1 = ss.accept();
            OutputStream os = s1.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = s1.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            System.out.println("进入监听");
            String title = dis.readUTF();
            String content = dis.readUTF();
            String username = dis.readUTF();
            System.out.println(title);
            System.out.println(username);

            Statement statement = conn.createStatement();
            String sql = "use Users;";
            statement.execute(sql);

            sql = "select max(id) id from posts;";
            ResultSet result = statement.executeQuery(sql);
            int id = -1;
            while (result.next()) {
                id = result.getInt("id");
                id++;
                System.out.println("已生成id");
            }
            assert id != -1;
            sql = "insert into posts values(" + id + ",'" + title + "','" + content + "','" + username + "')";
            statement.execute(sql);
            dos.writeUTF("True");

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
                    System.out.println("监听一次完毕");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
