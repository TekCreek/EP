
                        WITH JDBC 
                        
```java

public class Account {
    private Integer id;
    private String name;
    private double balance;

}


public class AccountDAO {

    public Account findById(int id) throws Exception {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps =
                    con.prepareStatement("select * from account where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setName(rs.getString("name"));
                account.setBalance(rs.getDouble("balance"));
                return account;
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public void save(Account acc) throws Exception {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = null;
            if (acc.getId() == null) {
                ps = con.prepareStatement(
                        "insert into account(name, balance) values(?, ?)");
                ps.setString(1, acc.getName());
                ps.setDouble(2, acc.getBalance());
                ps.executeUpdate();
            } else {
                ps = con.prepareStatement(
                        "update account set name=?, balance=? where id=?");
                ps.setString(1, acc.getName());
                ps.setDouble(2, acc.getBalance());
                ps.setInt(3, acc.getId());
                ps.executeUpdate();
            }

        } finally {
            if (con != null) {
                con.close();
            }
        }
    }


    private static BasicDataSource dataSource;

    // Static Initializer runs once when class is loaded.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/DemoDB?useSSL=false");
        dataSource.setUsername("demoUser");
        dataSource.setPassword("demoPwd");
        dataSource.setMaxTotal(10);
        dataSource.setMaxIdle(2);
        dataSource.setValidationQuery("select 1");
    }

    private Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
}

```