import java.sql.*;
import java.util.Scanner;

public class DemoEx4 {
    public static void main(String[] args) throws Exception {
        // 1. Register the driver.
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. Establish connection
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DemoDB?useSSL=false","demoUser", "demoPwd");

        System.out.println("Enter id: ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();

        PreparedStatement stmt =
                con.prepareStatement("select * from account where id = ?");

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("Id - " + rs.getInt(1));
            System.out.println("Name - " + rs.getString(2));
            System.out.println("Balance - " + rs.getDouble(3));
        } else {
            System.out.println("No record found with id - " + id);
        }


        con.close();
    }
}
