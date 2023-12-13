package bankpack;
import java.util.*;
import java.sql.*;

public abstract class AbstractCustomer {
    public String username, password, name, address, phone;
    public double balance;
    public String[] transactions;
    public int transactionCount;

    abstract public void deposit(double amount,  java.util.Date date);
    abstract public void withdraw(double amount,  java.util.Date date);
    abstract public boolean changePassword(Scanner sc, Connection conn);
    abstract public void addTransaction(String message);

    public AbstractCustomer(String username, String password, String name, String address, String phone, double balance,  java.util.Date date) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.balance = balance;
        transactions = new String[1000];
        addTransaction(String.format("Initial deposit - $%.2f as on %tD at %tT.", balance, date, date));
    }

    
}
