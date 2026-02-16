# Database, JDBC, JPA & Spring Data JPA - Comprehensive Guide

## Table of Contents
1. [Relational Databases and SQL Fundamentals](#1-relational-databases-and-sql-fundamentals)
2. [JDBC Programming and Best Practices](#2-jdbc-programming-and-best-practices)
3. [Object-Relational Mapping with JPA and Hibernate](#3-object-relational-mapping-with-jpa-and-hibernate)
4. [Repository Patterns using Spring Data JPA](#4-repository-patterns-using-spring-data-jpa)
5. [Pagination, Sorting, and Custom Queries with JPQL](#5-pagination-sorting-and-custom-queries-with-jpql)

---

## Introduction

This comprehensive guide covers database access in Spring Boot 3 applications, from SQL fundamentals to advanced JPA techniques. All examples follow Jakarta EE 9+ and Spring Boot 3.x standards.

**What you'll learn:**
- Relational database design and SQL operations
- JDBC programming with connection pooling
- JPA entity mapping and relationships
- Spring Data JPA repository patterns
- Advanced querying, pagination, and performance optimization

---

## 1. Relational Databases and SQL Fundamentals

### 1.1 Database Concepts

A **relational database** organizes data into tables (relations) with rows (records) and columns (attributes). Relationships between tables are established through foreign keys.

**Core Concepts:**
- **Table (Relation)**: Collection of related data
- **Row (Tuple)**: Single record
- **Column (Attribute)**: Data field
- **Primary Key (PK)**: Unique identifier
- **Foreign Key (FK)**: Reference to another table's PK
- **Index**: Performance optimization structure
- **Constraint**: Data integrity rule

**Normalization** reduces redundancy:
- **1NF**: Atomic values, no repeating groups
- **2NF**: No partial dependencies
- **3NF**: No transitive dependencies

### 1.2 SQL Categories

```
DDL (Data Definition Language)  → CREATE, ALTER, DROP, TRUNCATE
DML (Data Manipulation Language) → INSERT, UPDATE, DELETE
DQL (Data Query Language)        → SELECT
DCL (Data Control Language)      → GRANT, REVOKE
TCL (Transaction Control)        → COMMIT, ROLLBACK, SAVEPOINT
```

### 1.3 DDL - Creating Database Structure

```sql
-- Create database
CREATE DATABASE ecommerce
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE ecommerce;

-- Create customers table
CREATE TABLE customers (
    customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_name (first_name, last_name)
);

-- Create categories table
CREATE TABLE categories (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create products table with foreign key
CREATE TABLE products (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    category_id BIGINT,
    sku VARCHAR(50) UNIQUE NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    INDEX idx_category (category_id),
    INDEX idx_sku (sku),
    INDEX idx_active (active)
);

-- Create orders table
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address TEXT,
    
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    INDEX idx_customer (customer_id),
    INDEX idx_status (status),
    INDEX idx_date (order_date)
);

-- Create order_items table (composite PK)
CREATE TABLE order_items (
    order_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL,
    
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    
    INDEX idx_order (order_id),
    INDEX idx_product (product_id),
    UNIQUE KEY unique_order_product (order_id, product_id)
);

-- ALTER table examples
ALTER TABLE customers 
    ADD COLUMN loyalty_points INT DEFAULT 0,
    ADD COLUMN date_of_birth DATE;

ALTER TABLE products 
    MODIFY COLUMN description TEXT NOT NULL;

-- DROP examples
DROP TABLE IF EXISTS temp_data;
DROP INDEX idx_email ON customers;

-- TRUNCATE (removes all rows, keeps structure)
TRUNCATE TABLE order_items;
```

### 1.4 DML - Data Manipulation

```sql
-- INSERT single record
INSERT INTO customers (first_name, last_name, email, phone)
VALUES ('John', 'Doe', 'john.doe@example.com', '555-1234');

-- INSERT multiple records
INSERT INTO products (product_name, description, price, stock_quantity, category_id, sku)
VALUES 
    ('Laptop Pro 15', 'High-performance laptop', 1299.99, 50, 1, 'LAPTOP-PRO-15'),
    ('Wireless Mouse', 'Ergonomic design', 29.99, 200, 2, 'MOUSE-WL-001'),
    ('USB-C Hub', '7-in-1 connectivity', 49.99, 150, 2, 'HUB-USBC-7IN1');

-- INSERT from SELECT
INSERT INTO archived_orders (order_id, customer_id, order_date, total_amount)
SELECT order_id, customer_id, order_date, total_amount
FROM orders
WHERE order_date < DATE_SUB(CURDATE(), INTERVAL 1 YEAR);

-- UPDATE single record
UPDATE customers 
SET email = 'john.new@example.com',
    updated_at = CURRENT_TIMESTAMP
WHERE customer_id = 1;

-- UPDATE multiple records with condition
UPDATE products 
SET stock_quantity = stock_quantity - 5,
    updated_at = CURRENT_TIMESTAMP
WHERE product_id IN (10, 20, 30);

-- UPDATE with JOIN
UPDATE products p
INNER JOIN categories c ON p.category_id = c.category_id
SET p.price = p.price * 0.9
WHERE c.category_name = 'Electronics';

-- DELETE with condition
DELETE FROM customers 
WHERE customer_id = 100;

-- DELETE with subquery
DELETE FROM products 
WHERE category_id IN (
    SELECT category_id FROM categories WHERE active = FALSE
);

-- DELETE all records (keeps structure)
DELETE FROM temp_table;
```

### 1.5 DQL - Querying Data

```sql
-- Basic SELECT
SELECT * FROM customers;

SELECT customer_id, first_name, last_name, email 
FROM customers;

-- WHERE clause
SELECT * FROM products 
WHERE price BETWEEN 50 AND 500 
  AND stock_quantity > 0
  AND active = TRUE;

-- Pattern matching with LIKE
SELECT * FROM customers 
WHERE email LIKE '%@gmail.com';

SELECT * FROM products 
WHERE product_name LIKE 'Laptop%';

-- IN operator
SELECT * FROM orders 
WHERE status IN ('PENDING', 'CONFIRMED', 'SHIPPED');

-- NULL handling
SELECT * FROM customers WHERE phone IS NULL;
SELECT * FROM customers WHERE phone IS NOT NULL;

-- Sorting
SELECT * FROM products 
ORDER BY price DESC, product_name ASC;

-- Limiting results
SELECT * FROM products 
ORDER BY created_at DESC 
LIMIT 10;

-- OFFSET for pagination
SELECT * FROM products 
ORDER BY product_id 
LIMIT 20 OFFSET 40;  -- Skip first 40, get next 20

-- DISTINCT
SELECT DISTINCT category_id FROM products;

-- Aggregate functions
SELECT COUNT(*) AS total_customers FROM customers;
SELECT AVG(price) AS average_price FROM products;
SELECT SUM(total_amount) AS total_revenue FROM orders;
SELECT MAX(price) AS max_price, MIN(price) AS min_price FROM products;

-- GROUP BY
SELECT category_id, 
       COUNT(*) AS product_count,
       AVG(price) AS avg_price,
       MIN(price) AS min_price,
       MAX(price) AS max_price
FROM products
GROUP BY category_id;

-- HAVING (filter after GROUP BY)
SELECT category_id, COUNT(*) AS product_count
FROM products
GROUP BY category_id
HAVING COUNT(*) > 5;

-- Multiple GROUP BY columns
SELECT customer_id, 
       YEAR(order_date) AS year,
       COUNT(*) AS order_count,
       SUM(total_amount) AS total_spent
FROM orders
GROUP BY customer_id, YEAR(order_date)
ORDER BY customer_id, year;
```

### 1.6 SQL Joins

```sql
-- INNER JOIN (only matching records)
SELECT 
    o.order_id,
    c.first_name,
    c.last_name,
    o.order_date,
    o.total_amount
FROM orders o
INNER JOIN customers c ON o.customer_id = c.customer_id
WHERE o.order_date >= '2024-01-01';

-- LEFT JOIN (all from left table)
SELECT 
    c.customer_id,
    c.first_name,
    c.last_name,
    COUNT(o.order_id) AS order_count,
    COALESCE(SUM(o.total_amount), 0) AS total_spent
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.first_name, c.last_name
ORDER BY total_spent DESC;

-- RIGHT JOIN (all from right table)
SELECT 
    p.product_name,
    oi.quantity,
    oi.unit_price
FROM order_items oi
RIGHT JOIN products p ON oi.product_id = p.product_id;

-- CROSS JOIN (Cartesian product)
SELECT p1.product_name AS product1, p2.product_name AS product2
FROM products p1
CROSS JOIN products p2
WHERE p1.product_id < p2.product_id;

-- SELF JOIN
SELECT 
    e1.employee_name AS employee,
    e2.employee_name AS manager
FROM employees e1
LEFT JOIN employees e2 ON e1.manager_id = e2.employee_id;

-- Multiple table joins
SELECT 
    o.order_id,
    CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    (oi.quantity * oi.unit_price) AS subtotal
FROM orders o
INNER JOIN customers c ON o.customer_id = c.customer_id
INNER JOIN order_items oi ON o.order_id = oi.order_id
INNER JOIN products p ON oi.product_id = p.product_id
WHERE o.status = 'DELIVERED'
ORDER BY o.order_date DESC;
```

### 1.7 Subqueries

```sql
-- Scalar subquery (single value)
SELECT product_name, price
FROM products
WHERE price > (SELECT AVG(price) FROM products);

-- IN subquery
SELECT product_name FROM products
WHERE product_id IN (
    SELECT DISTINCT product_id 
    FROM order_items 
    WHERE order_id IN (
        SELECT order_id 
        FROM orders 
        WHERE order_date >= '2024-01-01'
    )
);

-- EXISTS subquery
SELECT * FROM customers c
WHERE EXISTS (
    SELECT 1 FROM orders o 
    WHERE o.customer_id = c.customer_id 
      AND o.total_amount > 1000
);

-- NOT EXISTS
SELECT * FROM products p
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi 
    WHERE oi.product_id = p.product_id
);

-- Correlated subquery
SELECT 
    c.customer_id,
    c.first_name,
    c.last_name,
    (SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.customer_id) AS order_count,
    (SELECT COALESCE(SUM(total_amount), 0) FROM orders o WHERE o.customer_id = c.customer_id) AS total_spent
FROM customers c;

-- Subquery in FROM clause
SELECT 
    category,
    avg_price,
    product_count
FROM (
    SELECT 
        category_id AS category,
        AVG(price) AS avg_price,
        COUNT(*) AS product_count
    FROM products
    GROUP BY category_id
) AS category_stats
WHERE product_count > 10;
```

### 1.8 Window Functions

```sql
-- ROW_NUMBER
SELECT 
    product_name,
    category_id,
    price,
    ROW_NUMBER() OVER (PARTITION BY category_id ORDER BY price DESC) AS price_rank
FROM products;

-- RANK and DENSE_RANK
SELECT 
    product_name,
    price,
    RANK() OVER (ORDER BY price DESC) AS rank,
    DENSE_RANK() OVER (ORDER BY price DESC) AS dense_rank
FROM products;

-- Running total
SELECT 
    order_date,
    total_amount,
    SUM(total_amount) OVER (ORDER BY order_date) AS running_total
FROM orders
ORDER BY order_date;

-- Moving average (7-day window)
SELECT 
    order_date,
    total_amount,
    AVG(total_amount) OVER (
        ORDER BY order_date 
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS moving_avg_7days
FROM orders;

-- LAG and LEAD (previous/next row values)
SELECT 
    order_date,
    total_amount,
    LAG(total_amount) OVER (ORDER BY order_date) AS prev_amount,
    LEAD(total_amount) OVER (ORDER BY order_date) AS next_amount
FROM orders;

-- NTILE (divide into quartiles)
SELECT 
    product_name,
    price,
    NTILE(4) OVER (ORDER BY price) AS price_quartile
FROM products;
```

### 1.9 CASE Statements

```sql
-- Simple CASE
SELECT 
    product_name,
    price,
    CASE 
        WHEN price < 50 THEN 'Budget'
        WHEN price BETWEEN 50 AND 500 THEN 'Mid-range'
        WHEN price BETWEEN 500 AND 1000 THEN 'Premium'
        ELSE 'Luxury'
    END AS price_category,
    CASE 
        WHEN stock_quantity = 0 THEN 'Out of Stock'
        WHEN stock_quantity < 10 THEN 'Low Stock'
        ELSE 'In Stock'
    END AS stock_status
FROM products;

-- CASE in aggregate
SELECT 
    customer_id,
    SUM(CASE WHEN YEAR(order_date) = 2022 THEN total_amount ELSE 0 END) AS revenue_2022,
    SUM(CASE WHEN YEAR(order_date) = 2023 THEN total_amount ELSE 0 END) AS revenue_2023,
    SUM(CASE WHEN YEAR(order_date) = 2024 THEN total_amount ELSE 0 END) AS revenue_2024
FROM orders
GROUP BY customer_id;
```

### 1.10 Transactions

```sql
-- Basic transaction
START TRANSACTION;

UPDATE accounts SET balance = balance - 100 WHERE account_id = 1;
UPDATE accounts SET balance = balance + 100 WHERE account_id = 2;
INSERT INTO transaction_log (from_account, to_account, amount) VALUES (1, 2, 100);

COMMIT;
-- or ROLLBACK; if error occurs

-- Transaction with savepoints
START TRANSACTION;

UPDATE inventory SET quantity = quantity - 5 WHERE product_id = 10;
SAVEPOINT after_inventory_update;

UPDATE orders SET status = 'CONFIRMED' WHERE order_id = 100;
SAVEPOINT after_order_update;

-- If needed, rollback to specific savepoint
-- ROLLBACK TO after_inventory_update;

COMMIT;

-- Transaction isolation levels
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```

---

## 2. JDBC Programming and Best Practices

### 2.1 JDBC Architecture

```
Application Layer
      ↓
   JDBC API
      ↓
 JDBC Driver Manager
      ↓
  JDBC Driver (Type 4 - Thin Driver)
      ↓
   Database
```

**JDBC Components:**
- **DriverManager**: Manages driver connections
- **Connection**: Database connection
- **Statement**: Execute SQL
- **PreparedStatement**: Precompiled SQL with parameters (recommended)
- **CallableStatement**: Execute stored procedures
- **ResultSet**: Query results
- **SQLException**: Database errors

### 2.2 Basic JDBC Operations

```java
import java.sql.*;
import java.util.*;

public class JDBCBasicExample {
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        // Driver auto-loaded in JDBC 4.0+
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Get connection with properties
     */
    public static Connection getConnectionWithProps() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);
        props.setProperty("useSSL", "false");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("allowPublicKeyRetrieval", "true");
        
        return DriverManager.getConnection(DB_URL, props);
    }
}
```

### 2.3 PreparedStatement Examples

```java
public class CustomerDAO {
    
    /**
     * CREATE - Insert customer
     */
    public long createCustomer(String firstName, String lastName, String email, String phone) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = JDBCBasicExample.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set parameters (1-indexed)
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Retrieve auto-generated key
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error inserting customer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * READ - Find customer by ID
     */
    public Customer findById(long customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection conn = JDBCBasicExample.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractCustomer(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding customer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * READ - Find customers by email domain
     */
    public List<Customer> findByEmailDomain(String domain) {
        String sql = "SELECT * FROM customers WHERE email LIKE ?";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = JDBCBasicExample.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + domain);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(extractCustomer(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding customers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return customers;
    }
    
    /**
     * UPDATE - Update customer
     */
    public boolean updateCustomer(long customerId, String firstName, String lastName, String email) {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ? WHERE customer_id = ?";
        
        try (Connection conn = JDBCBasicExample.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setLong(4, customerId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * DELETE - Delete customer
     */
    public boolean deleteCustomer(long customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        
        try (Connection conn = JDBCBasicExample.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, customerId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Batch insert
     */
    public int[] batchInsertCustomers(List<Customer> customers) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = JDBCBasicExample.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Customer customer : customers) {
                pstmt.setString(1, customer.getFirstName());
                pstmt.setString(2, customer.getLastName());
                pstmt.setString(3, customer.getEmail());
                pstmt.setString(4, customer.getPhone());
                pstmt.addBatch();
            }
            
            return pstmt.executeBatch();
            
        } catch (SQLException e) {
            System.err.println("Error in batch insert: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new int[0];
    }
    
    /**
     * Extract Customer from ResultSet
     */
    private Customer extractCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        customer.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return customer;
    }
}
```

### 2.4 Transaction Management in JDBC

```java
public class TransactionExample {
    
    /**
     * Transfer funds between accounts with transaction
     */
    public boolean transferFunds(long fromAccountId, long toAccountId, double amount) {
        Connection conn = null;
        
        try {
            conn = JDBCBasicExample.getConnection();
            
            // Disable auto-commit to start transaction
            conn.setAutoCommit(false);
            
            // Deduct from source account
            String deductSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deductSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setLong(2, fromAccountId);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Source account not found");
                }
            }
            
            // Add to destination account
            String addSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(addSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setLong(2, toAccountId);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Destination account not found");
                }
            }
            
            // Log transaction
            String logSql = "INSERT INTO transaction_log (from_account, to_account, amount) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(logSql)) {
                pstmt.setLong(1, fromAccountId);
                pstmt.setLong(2, toAccountId);
                pstmt.setDouble(3, amount);
                pstmt.executeUpdate();
            }
            
            // Commit transaction
            conn.commit();
            System.out.println("Transaction completed successfully");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
            
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            return false;
            
        } finally {
            // Restore auto-commit and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Transaction with savepoints
     */
    public void transactionWithSavepoints() {
        Connection conn = null;
        Savepoint savepoint1 = null;
        Savepoint savepoint2 = null;
        
        try {
            conn = JDBCBasicExample.getConnection();
            conn.setAutoCommit(false);
            
            // Operation 1: Update inventory
            String updateInventorySql = "UPDATE products SET stock_quantity = stock_quantity - 5 WHERE product_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateInventorySql)) {
                pstmt.setLong(1, 1);
                pstmt.executeUpdate();
            }
            
            savepoint1 = conn.setSavepoint("AfterInventoryUpdate");
            
            // Operation 2: Create order
            String createOrderSql = "INSERT INTO orders (customer_id, total_amount, status) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(createOrderSql)) {
                pstmt.setLong(1, 100);
                pstmt.setDouble(2, 99.99);
                pstmt.setString(3, "PENDING");
                pstmt.executeUpdate();
            }
            
            savepoint2 = conn.setSavepoint("AfterOrderCreation");
            
            // Operation 3: Update loyalty points
            String updatePointsSql = "UPDATE customers SET loyalty_points = loyalty_points + 10 WHERE customer_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updatePointsSql)) {
                pstmt.setLong(1, 100);
                pstmt.executeUpdate();
            }
            
            // Commit all changes
            conn.commit();
            System.out.println("All operations completed successfully");
            
        } catch (SQLException e) {
            System.err.println("Error during transaction: " + e.getMessage());
            
            if (conn != null) {
                try {
                    if (savepoint2 != null) {
                        conn.rollback(savepoint2);
                        System.out.println("Rolled back to savepoint 2");
                    } else if (savepoint1 != null) {
                        conn.rollback(savepoint1);
                        System.out.println("Rolled back to savepoint 1");
                    } else {
                        conn.rollback();
                        System.out.println("Rolled back entire transaction");
                    }
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
```

### 2.5 Connection Pooling with HikariCP

```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Connection pooling with HikariCP (industry standard)
 */
public class ConnectionPoolExample {
    
    private static HikariDataSource dataSource;
    
    static {
        HikariConfig config = new HikariConfig();
        
        // Database connection settings
        config.setJdbcUrl("jdbc:mysql://localhost:3306/ecommerce");
        config.setUsername("root");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Pool configuration
        config.setMaximumPoolSize(10);              // Max connections in pool
        config.setMinimumIdle(5);                   // Min idle connections
        config.setConnectionTimeout(30000);          // 30 seconds
        config.setIdleTimeout(600000);              // 10 minutes
        config.setMaxLifetime(1800000);             // 30 minutes
        
        // Performance settings
        config.setAutoCommit(true);
        config.setCachePrepStmts(true);
        config.setPrepStmtCacheSize(250);
        config.setPrepStmtCacheSqlLimit(2048);
        config.setUseServerPrepStmts(true);
        
        // Connection test query
        config.setConnectionTestQuery("SELECT 1");
        
        // Pool name for monitoring
        config.setPoolName("EcommerceHikariPool");
        
        // Leak detection threshold (helps find connection leaks)
        config.setLeakDetectionThreshold(60000);    // 60 seconds
        
        // Create datasource
        dataSource = new HikariDataSource(config);
    }
    
    /**
     * Get connection from pool
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    /**
     * Shutdown pool when application terminates
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    /**
     * Get pool statistics
     */
    public static void printPoolStats() {
        if (dataSource != null) {
            System.out.println("Pool Name: " + dataSource.getPoolName());
            System.out.println("Active Connections: " + 
                dataSource.getHikariPoolMXBean().getActiveConnections());
            System.out.println("Idle Connections: " + 
                dataSource.getHikariPoolMXBean().getIdleConnections());
            System.out.println("Total Connections: " + 
                dataSource.getHikariPoolMXBean().getTotalConnections());
            System.out.println("Threads Awaiting Connection: " + 
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }
}
```

### 2.6 JDBC Best Practices

#### 1. Always use try-with-resources

```java
// GOOD: Resources auto-closed
try (Connection conn = getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {
    // Use resources
} // Automatically closed in reverse order

// BAD: Manual resource management (error-prone)
Connection conn = null;
PreparedStatement pstmt = null;
ResultSet rs = null;
try {
    conn = getConnection();
    pstmt = conn.prepareStatement(sql);
    rs = pstmt.executeQuery();
    // Use resources
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException e) {}
    if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
    if (conn != null) try { conn.close(); } catch (SQLException e) {}
}
```

#### 2. Use PreparedStatement (prevents SQL injection)

```java
// GOOD: Prevents SQL injection
String sql = "SELECT * FROM users WHERE email = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, email);

// BAD: Vulnerable to SQL injection
String sql = "SELECT * FROM users WHERE email = '" + email + "'";
// Attacker can input: "' OR '1'='1"
```

#### 3. Use batch operations for multiple inserts

```java
String sql = "INSERT INTO products (name, price, category) VALUES (?, ?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);

for (Product product : products) {
    pstmt.setString(1, product.getName());
    pstmt.setBigDecimal(2, product.getPrice());
    pstmt.setString(3, product.getCategory());
    pstmt.addBatch();
    
    // Execute every 1000 records to avoid memory issues
    if (products.indexOf(product) % 1000 == 0) {
        pstmt.executeBatch();
        pstmt.clearBatch();
    }
}

pstmt.executeBatch();  // Execute remaining
```

#### 4. Handle exceptions properly

```java
try {
    // Database operations
} catch (SQLIntegrityConstraintViolationException e) {
    // Handle constraint violations (duplicate key, foreign key)
    System.err.println("Constraint violation: " + e.getMessage());
} catch (SQLTimeoutException e) {
    // Handle timeout
    System.err.println("Query timeout: " + e.getMessage());
} catch (SQLException e) {
    // Handle other SQL exceptions
    System.err.println("SQL Error Code: " + e.getErrorCode());
    System.err.println("SQL State: " + e.getSQLState());
    e.printStackTrace();
}
```

#### 5. Use column labels, not indices

```java
// GOOD: Readable and maintainable
customer.setId(rs.getLong("customer_id"));
customer.setFirstName(rs.getString("first_name"));
customer.setEmail(rs.getString("email"));

// BAD: Fragile if query changes
customer.setId(rs.getLong(1));
customer.setFirstName(rs.getString(2));
customer.setEmail(rs.getString(3));
```

#### 6. Set appropriate fetch size for large result sets

```java
PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM large_table");
pstmt.setFetchSize(1000);  // Retrieve 1000 rows at a time

ResultSet rs = pstmt.executeQuery();
while (rs.next()) {
    // Process each row
}
```

#### 7. Use appropriate data types

```java
// GOOD: Appropriate data types
pstmt.setString(1, customer.getName());
pstmt.setBigDecimal(2, product.getPrice());  // BigDecimal for currency
pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
pstmt.setBoolean(4, product.isActive());

// BAD: String for everything
pstmt.setString(2, String.valueOf(product.getPrice()));  // Loss of precision
```

#### 8. Always use connection pooling in production

```java
// GOOD: Connection pool (reuses connections)
Connection conn = ConnectionPoolExample.getConnection();

// BAD: Create new connection every time (expensive!)
Connection conn = DriverManager.getConnection(url, user, password);
```

---

## 3. Object-Relational Mapping with JPA and Hibernate

### 3.1 JPA Overview

**JPA (Java Persistence API)** is a specification for ORM in Java. **Hibernate** is the most popular implementation.

**Benefits:**
- Eliminates boilerplate JDBC code
- Database-independent queries (JPQL)
- Automatic SQL generation
- Caching and lazy loading
- Transaction management
- Object-oriented database access

**JPA Architecture:**
```
Application
    ↓
Entity Manager
    ↓
Persistence Context (1st level cache)
    ↓
JPA Provider (Hibernate)
    ↓
JDBC Driver
    ↓
Database
```

### 3.2 Basic Entity Mapping

```java
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Basic JPA entity mapped to customers table
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_name", columnList = "first_name, last_name")
})
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Lifecycle callbacks
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Customer() {
    }
    
    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    // Getters and setters (omitted for brevity)
}
```

### 3.3 Primary Key Strategies

```java
// AUTO: JPA chooses strategy
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

// IDENTITY: Auto-increment (MySQL, PostgreSQL SERIAL)
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

// SEQUENCE: Database sequence (Oracle, PostgreSQL)
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
@SequenceGenerator(name = "customer_seq", sequenceName = "customer_sequence", allocationSize = 1)
private Long id;

// TABLE: Dedicated key generation table
@Id
@GeneratedValue(strategy = GenerationType.TABLE, generator = "customer_gen")
@TableGenerator(name = "customer_gen", table = "id_generator", 
                pkColumnName = "gen_name", valueColumnName = "gen_value", 
                pkColumnValue = "customer_id", allocationSize = 1)
private Long id;

// UUID
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;
```

### 3.4 Relationship Mappings

#### Many-to-One / One-to-Many

```java
/**
 * Product entity (Many side)
 */
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    
    /**
     * Many products belong to one category
     * FetchType.LAZY: Category loaded only when accessed
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    // Getters and setters
}

/**
 * Category entity (One side)
 */
@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    /**
     * One category has many products
     * mappedBy: Indicates non-owning side
     * cascade: Operations cascade to children
     * orphanRemoval: Delete products when removed from list
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
    
    /**
     * Helper method to maintain bidirectional relationship
     */
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }
    
    // Getters and setters
}
```

#### One-to-One

```java
/**
 * Order entity
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    /**
     * One-to-One relationship (non-owning side)
     */
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private ShippingAddress shippingAddress;
    
    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
        shippingAddress.setOrder(this);
    }
    
    // Getters and setters
}

/**
 * ShippingAddress entity (owning side)
 */
@Entity
@Table(name = "shipping_addresses")
public class ShippingAddress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * One-to-One relationship (owning side)
     * Owning side has @JoinColumn
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;
    
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    
    // Getters and setters
}
```

#### Many-to-Many

```java
/**
 * Student entity (owning side)
 */
@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    
    /**
     * Many-to-Many relationship
     * @JoinTable defines the join table
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "student_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
    
    /**
     * Helper methods for bidirectional relationship
     */
    public void enrollInCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }
    
    public void dropCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }
    
    // Getters and setters
}

/**
 * Course entity (inverse side)
 */
@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String code;
    
    /**
     * Many-to-Many inverse side
     * mappedBy indicates this is the non-owning side
     */
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
    
    // Getters and setters
}
```

#### Composite Primary Key

```java
/**
 * Composite key class
 * Must implement Serializable
 * Must override equals() and hashCode()
 */
@Embeddable
public class OrderItemId implements Serializable {
    
    private Long orderId;
    private Long productId;
    
    public OrderItemId() {
    }
    
    public OrderItemId(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemId)) return false;
        OrderItemId that = (OrderItemId) o;
        return Objects.equals(orderId, that.orderId) &&
               Objects.equals(productId, that.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
    
    // Getters and setters
}

/**
 * Entity with composite key
 */
@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @EmbeddedId
    private OrderItemId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")  // Maps to orderId in OrderItemId
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")  // Maps to productId in OrderItemId
    @JoinColumn(name = "product_id")
    private Product product;
    
    private Integer quantity;
    private BigDecimal unitPrice;
    
    /**
     * Calculated field (not persisted)
     */
    @Transient
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    // Getters and setters
}
```

### 3.5 EntityManager Operations

```java
import jakarta.persistence.*;

public class EntityManagerExample {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * CREATE - Persist new entity
     */
    public void createCustomer() {
        Customer customer = new Customer("John", "Doe", "john@example.com");
        customer.setPhone("555-1234");
        
        em.persist(customer);  // INSERT into database
        
        // After persist, entity is managed and has generated ID
        System.out.println("Created customer with ID: " + customer.getId());
    }
    
    /**
     * READ - Find by ID
     */
    public void findCustomer(Long id) {
        // Returns null if not found
        Customer customer = em.find(Customer.class, id);
        
        if (customer != null) {
            System.out.println("Found: " + customer.getFirstName());
        }
    }
    
    /**
     * READ - Get reference (lazy loading)
     */
    public void getReferenceExample(Long id) {
        // Returns proxy, throws EntityNotFoundException if not found when accessed
        Customer customer = em.getReference(Customer.class, id);
        
        // Database query happens here
        System.out.println("Name: " + customer.getFirstName());
    }
    
    /**
     * UPDATE - Merge detached entity
     */
    public void updateCustomer(Customer detachedCustomer) {
        // merge() returns managed copy
        Customer managedCustomer = em.merge(detachedCustomer);
        
        // Changes to managed entity are auto-persisted
        System.out.println("Updated customer: " + managedCustomer.getId());
    }
    
    /**
     * UPDATE - Modify managed entity (automatic dirty checking)
     */
    public void updateManagedEntity(Long id) {
        Customer customer = em.find(Customer.class, id);
        
        // No explicit save needed - changes auto-detected and persisted
        customer.setEmail("new.email@example.com");
        customer.setPhone("555-9999");
        
        // UPDATE occurs on transaction commit
    }
    
    /**
     * DELETE - Remove entity
     */
    public void deleteCustomer(Long id) {
        Customer customer = em.find(Customer.class, id);
        
        if (customer != null) {
            em.remove(customer);  // DELETE from database
        }
    }
    
    /**
     * Refresh entity from database
     */
    public void refreshEntity(Customer customer) {
        // Discards local changes and reloads from database
        em.refresh(customer);
    }
    
    /**
     * Detach entity (stop tracking)
     */
    public void detachEntity(Customer customer) {
        em.detach(customer);  // Entity becomes detached
        
        // Changes to detached entity are not persisted
        customer.setEmail("not.saved@example.com");
    }
    
    /**
     * Flush changes to database
     */
    public void flushExample() {
        Customer customer = new Customer("Jane", "Smith", "jane@example.com");
        em.persist(customer);
        
        // Force immediate database sync
        em.flush();
        
        // Customer now has generated ID
        System.out.println("Flushed customer ID: " + customer.getId());
    }
    
    /**
     * Clear persistence context
     */
    public void clearExample() {
        // Detaches all managed entities
        em.clear();
    }
}
```

### 3.6 Entity Lifecycle States

```
   New (Transient)
         │
         │ persist()
         ▼
   Managed (Persistent) ←──── merge()
         │                      │
         │ remove()              │
         ▼                      │
   Removed ──────────────────────
         │
         │ flush/commit
         ▼
   Detached ←──── detach()/clear()
```

**State Transitions:**
1. **New**: Created with `new`, not associated with EntityManager
2. **Managed**: Tracked by EntityManager, changes auto-persisted
3. **Removed**: Marked for deletion, will be deleted on commit
4. **Detached**: Was managed, now disconnected from EntityManager

---

## 4. Repository Patterns using Spring Data JPA

### 4.1 Spring Data JPA Overview

Spring Data JPA simplifies data access by:
- Auto-generating implementation from method names
- Providing CRUD operations out-of-the-box
- Supporting pagination and sorting
- Enabling custom queries with @Query
- Offering specifications for dynamic queries

**Repository Hierarchy:**
```
Repository (marker interface)
    ↓
CrudRepository (basic CRUD)
    ↓
PagingAndSortingRepository (pagination & sorting)
    ↓
JpaRepository (JPA-specific, batch operations)
```

### 4.2 Creating Repositories

```java
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

/**
 * Basic repository extending JpaRepository
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Method name query derivation - Spring generates implementation
    
    // Find by single field
    List<Customer> findByLastName(String lastName);
    Optional<Customer> findByEmail(String email);
    
    // Multiple conditions with And
    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);
    
    // Or condition
    List<Customer> findByFirstNameOrLastName(String firstName, String lastName);
    
    // Like / Containing
    List<Customer> findByEmailContaining(String emailPart);
    List<Customer> findByFirstNameStartingWith(String prefix);
    List<Customer> findByLastNameEndingWith(String suffix);
    
    // Comparison operators
    List<Customer> findByLoyaltyPointsGreaterThan(Integer points);
    List<Customer> findByLoyaltyPointsLessThanEqual(Integer points);
    List<Customer> findByLoyaltyPointsBetween(Integer min, Integer max);
    
    // Date/Time queries
    List<Customer> findByCreatedAtAfter(LocalDateTime date);
    List<Customer> findByCreatedAtBefore(LocalDateTime date);
    List<Customer> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Boolean queries
    boolean existsByEmail(String email);
    boolean existsByLoyaltyPointsGreaterThan(Integer points);
    
    // Count queries
    long countByLastName(String lastName);
    
    // Delete queries
    void deleteByEmail(String email);
    List<Customer> removeByLastName(String lastName);  // Returns deleted entities
    
    // Top/First results
    List<Customer> findTop10ByOrderByCreatedAtDesc();
    Customer findFirstByOrderByLoyaltyPointsDesc();
    
    // Distinct
    List<Customer> findDistinctByLastName(String lastName);
    
    // Ignore case
    List<Customer> findByEmailIgnoreCase(String email);
    List<Customer> findByFirstNameContainingIgnoreCase(String part);
    
    // OrderBy
    List<Customer> findByLastNameOrderByFirstNameAsc(String lastName);
    List<Customer> findByLoyaltyPointsGreaterThanOrderByLoyaltyPointsDescFirstNameAsc(Integer points);
}
```

### 4.3 Query Method Keywords

| Keyword | Example | JPQL |
|---------|---------|------|
| And | findByFirstNameAndLastName | WHERE x.firstName = ?1 AND x.lastName = ?2 |
| Or | findByFirstNameOrLastName | WHERE x.firstName = ?1 OR x.lastName = ?2 |
| Is, Equals | findByFirstName, findByFirstNameIs | WHERE x.firstName = ?1 |
| Between | findByAgeBetween | WHERE x.age BETWEEN ?1 AND ?2 |
| LessThan | findByAgeLessThan | WHERE x.age < ?1 |
| LessThanEqual | findByAgeLessThanEqual | WHERE x.age <= ?1 |
| GreaterThan | findByAgeGreaterThan | WHERE x.age > ?1 |
| GreaterThanEqual | findByAgeGreaterThanEqual | WHERE x.age >= ?1 |
| After | findByStartDateAfter | WHERE x.startDate > ?1 |
| Before | findByStartDateBefore | WHERE x.startDate < ?1 |
| IsNull | findByAgeIsNull | WHERE x.age IS NULL |
| IsNotNull, NotNull | findByAge(Is)NotNull | WHERE x.age IS NOT NULL |
| Like | findByFirstNameLike | WHERE x.firstName LIKE ?1 |
| NotLike | findByFirstNameNotLike | WHERE x.firstName NOT LIKE ?1 |
| StartingWith | findByFirstNameStartingWith | WHERE x.firstName LIKE ?1% |
| EndingWith | findByFirstNameEndingWith | WHERE x.firstName LIKE %?1 |
| Containing | findByFirstNameContaining | WHERE x.firstName LIKE %?1% |
| OrderBy | findByAgeOrderByLastNameDesc | WHERE x.age = ?1 ORDER BY x.lastName DESC |
| Not | findByLastNameNot | WHERE x.lastName <> ?1 |
| In | findByAgeIn(Collection ages) | WHERE x.age IN ?1 |
| NotIn | findByAgeNotIn(Collection ages) | WHERE x.age NOT IN ?1 |
| True | findByActiveTrue() | WHERE x.active = TRUE |
| False | findByActiveFalse() | WHERE x.active = FALSE |
| IgnoreCase | findByFirstNameIgnoreCase | WHERE UPPER(x.firstName) = UPPER(?1) |

### 4.4 Custom Queries with @Query

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * JPQL queries
     */
    
    // Basic JPQL
    @Query("SELECT p FROM Product p WHERE p.price > :price")
    List<Product> findExpensiveProducts(@Param("price") BigDecimal price);
    
    // Positional parameters
    @Query("SELECT p FROM Product p WHERE p.name = ?1 AND p.category = ?2")
    List<Product> findByNameAndCategory(String name, String category);
    
    // Join query
    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);
    
    // JOIN FETCH (avoid N+1 problem)
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);
    
    // Aggregate functions
    @Query("SELECT AVG(p.price) FROM Product p WHERE p.category.id = :categoryId")
    Double getAveragePrice(@Param("categoryId") Long categoryId);
    
    @Query("SELECT SUM(p.stockQuantity) FROM Product p WHERE p.category.id = :categoryId")
    Long getTotalStock(@Param("categoryId") Long categoryId);
    
    // GROUP BY
    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    List<Object[]> countByCategory();
    
    // Subquery
    @Query("SELECT p FROM Product p WHERE p.price > (SELECT AVG(p2.price) FROM Product p2)")
    List<Product> findProductsAboveAveragePrice();
    
    // IN clause
    @Query("SELECT p FROM Product p WHERE p.category.name IN :categories")
    List<Product> findByCategoryNames(@Param("categories") List<String> categories);
    
    /**
     * Native SQL queries
     */
    
    // Basic native query
    @Query(value = "SELECT * FROM products WHERE price > ?1", nativeQuery = true)
    List<Product> findExpensiveProductsNative(BigDecimal price);
    
    // Named parameters in native query
    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId ORDER BY price DESC LIMIT :limit",
           nativeQuery = true)
    List<Product> findTopProductsInCategory(@Param("categoryId") Long categoryId, @Param("limit") int limit);
    
    // Complex native query
    @Query(value = """
        SELECT p.*, COUNT(oi.product_id) as sales_count
        FROM products p
        LEFT JOIN order_items oi ON p.product_id = oi.product_id
        GROUP BY p.product_id
        ORDER BY sales_count DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Product> findTopSellingProducts(@Param("limit") int limit);
    
    /**
     * Modifying queries
     */
    
    @Modifying
    @Query("UPDATE Product p SET p.price = p.price * :multiplier WHERE p.category.id = :categoryId")
    int updatePricesByCategory(@Param("categoryId") Long categoryId, @Param("multiplier") BigDecimal multiplier);
    
    @Modifying
    @Query("DELETE FROM Product p WHERE p.active = FALSE AND p.stockQuantity = 0")
    int deleteInactiveProducts();
}
```

### 4.5 DTO Projections

```java
/**
 * DTO class for projection
 */
public class ProductSummaryDTO {
    private String name;
    private BigDecimal price;
    private String categoryName;
    
    public ProductSummaryDTO(String name, BigDecimal price, String categoryName) {
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
    }
    
    // Getters
}

/**
 * Interface-based projection
 */
public interface ProductProjection {
    String getName();
    BigDecimal getPrice();
    String getCategoryName();  // Nested property: category.name
}

/**
 * Repository with projections
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Constructor expression (DTO projection)
    @Query("SELECT new com.example.dto.ProductSummaryDTO(p.name, p.price, p.category.name) " +
           "FROM Product p WHERE p.active = true")
    List<ProductSummaryDTO> findAllActiveSummaries();
    
    // Interface-based projection
    @Query("SELECT p.name as name, p.price as price, p.category.name as categoryName " +
           "FROM Product p WHERE p.price > :minPrice")
    List<ProductProjection> findProjectionsAbovePrice(@Param("minPrice") BigDecimal minPrice);
    
    // Dynamic projection
    <T> List<T> findByActiveTrue(Class<T> type);
}
```

### 4.6 Specifications (Dynamic Queries)

```java
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

/**
 * Specification factory for dynamic queries
 */
public class ProductSpecifications {
    
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> 
            name == null ? null : cb.equal(root.get("name"), name);
    }
    
    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) -> 
            category == null ? null : cb.equal(root.join("category").get("name"), category);
    }
    
    public static Specification<Product> hasPriceGreaterThan(BigDecimal price) {
        return (root, query, cb) -> 
            price == null ? null : cb.greaterThan(root.get("price"), price);
    }
    
    public static Specification<Product> hasPriceLessThan(BigDecimal price) {
        return (root, query, cb) -> 
            price == null ? null : cb.lessThan(root.get("price"), price);
    }
    
    public static Specification<Product> hasStockGreaterThan(Integer stock) {
        return (root, query, cb) -> 
            stock == null ? null : cb.greaterThan(root.get("stockQuantity"), stock);
    }
    
    public static Specification<Product> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }
    
    public static Specification<Product> nameLike(String keyword) {
        return (root, query, cb) -> 
            keyword == null ? null : cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }
}

/**
 * Repository with Specification support
 */
public interface ProductRepository extends JpaRepository<Product, Long>, 
                                           JpaSpecificationExecutor<Product> {
}

/**
 * Service using Specifications
 */
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;
    
    public List<Product> searchProducts(String name, String category, 
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        Specification<Product> spec = Specification.where(
            ProductSpecifications.hasName(name))
            .and(ProductSpecifications.hasCategory(category))
            .and(ProductSpecifications.hasPriceGreaterThan(minPrice))
            .and(ProductSpecifications.hasPriceLessThan(maxPrice))
            .and(ProductSpecifications.isActive());
        
        return repository.findAll(spec);
    }
    
    public Page<Product> searchProductsPaginated(String keyword, BigDecimal minPrice, 
                                                 Pageable pageable) {
        Specification<Product> spec = Specification.where(
            ProductSpecifications.nameLike(keyword))
            .and(ProductSpecifications.hasPriceGreaterThan(minPrice))
            .and(ProductSpecifications.isActive());
        
        return repository.findAll(spec, pageable);
    }
}
```

---

## 5. Pagination, Sorting, and Custom Queries with JPQL

### 5.1 Pagination Basics

```java
import org.springframework.data.domain.*;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;
    
    /**
     * Basic pagination
     */
    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
    
    /**
     * Pagination with sorting
     */
    public Page<Product> getProductsSorted(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }
    
    /**
     * Multiple sort fields
     */
    public Page<Product> getProductsMultiSort(int page, int size) {
        Sort sort = Sort.by("category").ascending()
                       .and(Sort.by("price").descending())
                       .and(Sort.by("name").ascending());
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }
}
```

### 5.2 Repository Methods with Pagination

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Method with Pageable parameter
    Page<Product> findByCategory(String category, Pageable pageable);
    
    // Slice (doesn't count total, more efficient)
    Slice<Product> findByActiveTrue(Pageable pageable);
    
    // List (no pagination metadata)
    List<Product> findByPriceGreaterThan(BigDecimal price, Pageable pageable);
    
    // Custom query with pagination
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Native query with pagination
    @Query(value = "SELECT * FROM products WHERE active = true",
           countQuery = "SELECT count(*) FROM products WHERE active = true",
           nativeQuery = true)
    Page<Product> findActiveProductsNative(Pageable pageable);
}
```

### 5.3 Controller with Pagination

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir,
            @RequestParam(required = false) String category) {
        
        Page<Product> products = productService.searchProducts(page, size, sortBy, sortDir, category);
        return ResponseEntity.ok(products);
    }
}

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;
    
    public Page<Product> searchProducts(int page, int size, String sortBy, 
                                        String sortDir, String category) {
        Sort sort = sortDir.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (category != null) {
            return repository.findByCategory(category, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }
}
```

### 5.4 Advanced JPQL Queries

```java
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Complex joins
     */
    @Query("""
        SELECT o FROM Order o 
        JOIN FETCH o.customer c 
        JOIN FETCH o.orderItems oi 
        JOIN FETCH oi.product p 
        WHERE o.id = :id
        """)
    Optional<Order> findByIdWithDetails(@Param("id") Long id);
    
    /**
     * Aggregate queries
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.id = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.customer.id = :customerId")
    BigDecimal getTotalSpentByCustomer(@Param("customerId") Long customerId);
    
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.status = :status")
    Double getAverageOrderAmount(@Param("status") OrderStatus status);
    
    /**
     * GROUP BY with HAVING
     */
    @Query("""
        SELECT o.customer, COUNT(o), SUM(o.totalAmount) 
        FROM Order o 
        GROUP BY o.customer 
        HAVING COUNT(o) > :minOrders
        """)
    List<Object[]> findCustomersWithMinimumOrders(@Param("minOrders") long minOrders);
    
    /**
     * Subqueries
     */
    @Query("SELECT o FROM Order o WHERE o.totalAmount > (SELECT AVG(o2.totalAmount) FROM Order o2)")
    List<Order> findOrdersAboveAverage();
    
    /**
     * Date range queries
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :start AND :end ORDER BY o.orderDate DESC")
    List<Order> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    /**
     * CASE statements
     */
    @Query("""
        SELECT o, 
        CASE 
            WHEN o.totalAmount < 100 THEN 'Small'
            WHEN o.totalAmount < 500 THEN 'Medium'
            ELSE 'Large'
        END 
        FROM Order o
        """)
    List<Object[]> findOrdersWithSizeCategory();
}
```

### 5.5 Criteria API (Type-safe queries)

```java
@Repository
public class ProductRepositoryCustom {
    
    @PersistenceContext
    private EntityManager em;
    
    public List<Product> searchProducts(ProductSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Name filter
        if (criteria.getName() != null) {
            predicates.add(cb.like(cb.lower(product.get("name")), 
                "%" + criteria.getName().toLowerCase() + "%"));
        }
        
        // Price range
        if (criteria.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("price"), criteria.getMinPrice()));
        }
        if (criteria.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(product.get("price"), criteria.getMaxPrice()));
        }
        
        // Category filter
        if (criteria.getCategory() != null) {
            Join<Product, Category> categoryJoin = product.join("category");
            predicates.add(cb.equal(categoryJoin.get("name"), criteria.getCategory()));
        }
        
        // Active filter
        if (criteria.getActiveOnly()) {
            predicates.add(cb.isTrue(product.get("active")));
        }
        
        // Combine predicates
        query.where(predicates.toArray(new Predicate[0]));
        
        // Sorting
        if (criteria.getSortBy() != null) {
            if (criteria.getSortDirection() == SortDirection.DESC) {
                query.orderBy(cb.desc(product.get(criteria.getSortBy())));
            } else {
                query.orderBy(cb.asc(product.get(criteria.getSortBy())));
            }
        }
        
        // Execute query
        TypedQuery<Product> typedQuery = em.createQuery(query);
        
        // Pagination
        if (criteria.getPage() != null && criteria.getSize() != null) {
            typedQuery.setFirstResult(criteria.getPage() * criteria.getSize());
            typedQuery.setMaxResults(criteria.getSize());
        }
        
        return typedQuery.getResultList();
    }
}
```

### 5.6 Query Performance Tips

**1. Use FETCH JOIN to avoid N+1 queries**
```java
@Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
Optional<Order> findByIdWithItems(@Param("id") Long id);
```

**2. Use projections for read-only queries**
```java
@Query("SELECT new com.example.dto.ProductDTO(p.id, p.name, p.price) FROM Product p")
List<ProductDTO> findAllProjections();
```

**3. Add indexes on frequently queried columns**
```java
@Table(name = "products", indexes = {
    @Index(name = "idx_category", columnList = "category_id"),
    @Index(name = "idx_price", columnList = "price"),
    @Index(name = "idx_name", columnList = "name")
})
```

**4. Use @BatchSize for collections**
```java
@OneToMany(mappedBy = "order")
@BatchSize(size = 10)
private List<OrderItem> orderItems;
```

**5. Enable query caching**
```java
@Query("SELECT p FROM Product p WHERE p.active = true")
@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
List<Product> findActiveProducts();
```

**6. Use Slice instead of Page when total count not needed**
```java
Slice<Product> findByActiveTrue(Pageable pageable);
```

**7. Optimize lazy loading**
```java
// Use EntityGraph to specify fetch plan
@EntityGraph(attributePaths = {"category", "orderItems"})
Optional<Product> findById(Long id);
```

---

## Summary

This comprehensive guide covered:

### 1. SQL Fundamentals
- Database design and normalization
- DDL, DML, and DQL operations
- Joins, subqueries, and window functions
- Transactions and ACID properties

### 2. JDBC Programming
- Connection management
- PreparedStatement for SQL injection prevention
- Transaction handling with savepoints
- Connection pooling with HikariCP
- Best practices and DAO pattern

### 3. JPA/Hibernate
- Entity mapping and annotations
- Relationship mappings (One-to-One, One-to-Many, Many-to-Many)
- Composite keys and inheritance strategies
- EntityManager operations and lifecycle
- Entity states and transitions

### 4. Spring Data JPA
- Repository pattern and hierarchy
- Query method generation from method names
- Custom queries with @Query
- DTO projections
- Dynamic queries with Specifications

### 5. Advanced Querying
- JPQL and native SQL queries
- Pagination and sorting
- Criteria API for type-safe queries
- Performance optimization techniques
- Query caching and batch fetching

---

**Version**: 1.0  
**Last Updated**: February 2026  
**Compatible with**: Spring Boot 3.x, Jakarta EE 9+, Hibernate 6.x
