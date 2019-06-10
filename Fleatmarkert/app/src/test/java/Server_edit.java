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

public class Server_edit {
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
            ss = new ServerSocket(8087);
            ss.setSoTimeout(5000);
            Socket s1 = ss.accept();
            OutputStream os = s1.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = s1.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            System.out.println("进入监听");
            String id = dis.readUTF();
            String title = dis.readUTF();
            String content = dis.readUTF();
            System.out.println(id);
            System.out.println(title);
            System.out.println(content);

            Statement statement = conn.createStatement();
            String sql = "use Users;";
            statement.execute(sql);

            sql = "update posts set title='" + title + "' where id=" + id + ";";
            int result = statement.executeUpdate(sql);
            sql = "update posts set content='" + content + "' where id=" + id + ";";
            int result2 = statement.executeUpdate(sql);
            if (result != 0 && result2 != 0) {
                dos.writeUTF("True");
                System.out.println("修改成功");
            } else {
                dos.writeUTF("False");
                System.out.println("修改失败");
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
                    System.out.println("监听一次完毕");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
