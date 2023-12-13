package bankpack;
import java.sql.*;

public class Conn {
    public Connection c;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///bmubank", "root", "1234");
            
        } catch (Exception e) {
            System.out.println("Error establishing connection: " + e.getMessage());
             
        }
    }
}