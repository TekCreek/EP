
            WITH SPRING DATA

```java

@Entity
public class Account {
    
    @Id
    private Integer id;
    private String name;
    private double balance;

    // + GET & SET METHODS
}

@Repository
public interface AccountRepository 
         extends  CrudRepository<Account, Integer> {

    // findById 
    // save

    Optional<Account> findByName(String name);

}

// Service 

class AccountService {

    @Autowired
    AccountRepository accRepo;

    public void openAccount(..) {
        Account acc = ....create account object ...
        accRepo.save(acc);
    }

}


```