
                WITH HIBERNATE
```java

@Entity
public class Account {
    
    @Id
    private Integer id;
    private String name;
    private double balance;

    // + GET & SET METHODS

}


public class AccountDAO {

    EntityManager em;

    public Account findById(int id) throws Exception {
        Criteria criteria = ...Your Query Here....;
        Account account = em.find(criteria);
        return account;
    }

    public void save(Account acc) throws Exception {
            em.save(acc);
    }

}

```