package bankpack;
import java.util.*;
import java.sql.*;

public class Customer extends AbstractCustomer {
    private Connection conn;
    public Customer(String username, String password, String name, String address, String phone, double balance,  java.util.Date date) {
        super(username, password, name, address, phone, balance, date);
    }

    @Override
    public void deposit(double amount,  java.util.Date date) {
        balance += amount;
        addTransaction(String.format("$%.2f credited to your account. Balance - $%.2f as on %tD at %tT.", amount, balance, date, date));
        System.out.println("Deposit successful. $" + String.format("%.2f", amount) + " credited to your account.");
    }

    @Override
    public void withdraw(double amount,  java.util.Date date) {
        if (amount > (balance - 200)) {
            System.out.println("Insufficient balance.");
            return;
        }
        balance -= amount;
        addTransaction(String.format("$%.2f debited from your account. Balance - $%.2f as on %tD at %tT.", amount, balance, date, date));
        System.out.println("Withdrawal successful. $" + String.format("%.2f", amount) + " debited from your account.");
    }

    @Override
        public boolean changePassword(Scanner sc, Connection conn) {
        this.conn = conn; 
        System.out.print("Enter your old password: ");
        String oldPassword = sc.next();
        sc.nextLine();

        while (!password.equals(oldPassword)) {
            System.out.println("Password does not match with old password. Please enter the correct old password:");
            oldPassword = sc.next();
            sc.nextLine();
        }

        System.out.print("Enter your new password (minimum 8 chars; minimum 1 digit, 1 lowercase, 1 uppercase, 1 special character[!@#$%^&*_]): ");
        String newPassword = sc.next();
        sc.nextLine();

        while (!newPassword.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_]).{8,}$")) {
            System.out.print("Invalid password condition. Set again: ");
            newPassword = sc.next();
            sc.nextLine();
        }

        password = newPassword;
        System.out.println("Password changed successfully.");

    try {
           
            String updateQuery = "UPDATE customers SET password = ? WHERE username = ?";
            PreparedStatement updatePassword = conn.prepareStatement(updateQuery);
            updatePassword.setString(1, newPassword);
            updatePassword.setString(2, this.username);

            int rowsAffected = updatePassword.executeUpdate();

             if (rowsAffected > 0) {
            System.out.println("  ");
            return true; 
        } else {
            System.out.println("Failed to change password in the database. Please try again.");
        }

        } catch (SQLException e) {
            System.out.println("Error changing password in the database: " + e.getMessage());
        }

        return false; 
    }

    public void addTransaction(String message) {
        if (transactionCount < 1000) {
            transactions[transactionCount] = message;
            transactionCount++;
        } 
    }

}
