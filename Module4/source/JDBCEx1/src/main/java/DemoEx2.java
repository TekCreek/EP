import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DemoEx2 {
    public static void main(String[] args) throws Exception {
        // 1. Register the driver.
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. Establish connection
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DemoDB?useSSL=false&allowPublicKeyRetrieval=true","demoUser", "demoPwd")) {

            // 3. Statement
            Statement st = con.createStatement();

            // 4. Execute the command
            ResultSet rs = st.executeQuery("select * from account");

            // Retrieving field values using column names.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");

                System.out.printf("id :%d , name: %s, balance: %f %n", id, name, balance);
            }
        }

    }
}
