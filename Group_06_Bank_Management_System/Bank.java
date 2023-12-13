import bankpack.*;
import java.util.*;
import java.sql.*;


class Bank {
    private static Conn conn;
    static Customer[] customers;
    static int totalCustomers;


    Bank() {

        customers = new Customer[1000]; 
        totalCustomers = 0;
    }

    public static int findCustomerByPhoneNumber(String phoneNumber) {
        for (int i = 0; i < totalCustomers; i++) {
            if (customers[i].phone.equals(phoneNumber)) {
                return i;
            }
        }
        return -1;
    }

    public static int findCustomerByUsername(String username) {
        for (int i = 0; i < totalCustomers; i++) {
            if (customers[i].username.equals(username)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection databaseConnection = new Conn().c;
        double amount;
        Bank bank = new Bank();
        while (true) {
            System.out.println("\n-------------------");
            System.out.println("BANK    OF     BMU");
            System.out.println("-------------------\n");
            System.out.println("1. Register account.");
            System.out.println("2. Login.");
            System.out.println("3. Forgot Username.");
            System.out.println("4. Exit.");
            System.out.print("\nEnter your choice : ");
            String input = sc.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                continue;
            }

            if (choice < 1 || choice > 4) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                continue;
            }
            Customer customer;
            String username;
            String password;
            switch (choice) {
                case 1:
                    if (totalCustomers >= 1000) {
                        System.out.println("Bank cannot accommodate more customers.");
                        break;
                    }
                    
                    System.out.print("Enter name : ");
                    String name = sc.nextLine();
                    while (!name.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid name. Please enter only alphabetic characters:");
                        name = sc.nextLine();
                    }

                    System.out.print("Enter address : ");
                    String address = sc.nextLine();

                    String phone = null;
                    boolean validPhoneNumber = false;
                    while (!validPhoneNumber) {
                        System.out.print("Enter contact number (10 digits) : ");
                        phone = sc.nextLine();
                        if (phone.matches("\\d{10}")) {
                            validPhoneNumber = true;
                        } else {
                            System.out.println("Invalid phone number. Please enter a 10-digit number.");
                        }
                    }

                    System.out.println("Set username : ");
                    username = sc.next();
                    while (bank.findCustomerByUsername(username) != -1) {
                        System.out.println("Username already exists. Set again : ");
                        username = sc.next();
                    }
                    System.out.println("Set a password (minimum 8 chars; minimum 1 digit, 1 lowercase, 1 uppercase, 1 special character[!@#$%^&*_]) :");
                    password = sc.next();
                    sc.nextLine();
                    while (!password.matches((("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_]).{8,}")))) {
                        System.out.println("Invalid password condition. Set again :");
                        password = sc.next();
                    }

                    System.out.print("Enter initial deposit (minimum 200 rupees) : ");
                    while (true) {
                        try {
                            amount = Double.parseDouble(sc.nextLine());
                            if (amount < 200) {
                                throw new Exception("Minimum deposit should be 200 rupees.");
                            }
                            break; 
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid amount. Please enter a valid number.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    try {
                        String insertQuery = "INSERT INTO customers (username, password, name, address, phone, initial_deposit) VALUES (?, ?, ?, ?, ?, ?)";
                        Conn conn = new Conn();
                        PreparedStatement preparedStatement = conn.c.prepareStatement(insertQuery);

                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);
                        preparedStatement.setString(3, name);
                        preparedStatement.setString(4, address);
                        preparedStatement.setString(5, phone);
                        preparedStatement.setDouble(6, amount);


                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Account Created Successfully");
                            customer = new Customer(username, password, name, address, phone, amount, new java.util.Date());
                            customers[totalCustomers] = customer;
                            totalCustomers++;
                        } else {
                            System.out.println("Failed to create account. Please try again.");
                        }
                        } 
                    catch (SQLException e) {
                        System.out.println("Error inserting customer information: " + e.getMessage());
                    }

                    break;
                case 2:
                    System.out.println("Enter username : ");
                    username = sc.next();
                    sc.nextLine();
                    System.out.println("Enter password : ");
                    password = sc.next();
                    sc.nextLine();
                    int index = bank.findCustomerByUsername(username);
                    if (index != -1) {
                        customer = customers[index];
                        if (customer.password.equals(password)) {
                            System.out.print("Login successfully");
                            while (true) {
                                System.out.println("\n-------------------");
                                System.out.println("WELCOME  " + customer.name.toUpperCase() );
                                System.out.println("-------------------\n");
                                System.out.println("1. Deposit.");
                                System.out.println("2. Withdraw.");
                                System.out.println("3. Transaction History.");
                                System.out.println("4. Account information.");
                                System.out.println("5. Change Password.");
                                System.out.println("6. Logout.");
                                System.out.print("\nEnter your choice : ");
                                int userChoice;
                                input = sc.nextLine();
                                try {
                                    userChoice = Integer.parseInt(input);
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a number between 1 and 6.");
                                    continue;
                                }

                                if (userChoice < 1 || userChoice > 6) {
                                    System.out.println("Invalid input. Please enter a number between 1 and 6.");
                                    continue;
                                }


                                switch (userChoice) {
                                    case 1:
                                        System.out.print("Enter amount to deposit : ");
                                        while (!sc.hasNextDouble()) {
                                            System.out.println("Invalid amount. Enter again :");
                                            sc.nextLine();
                                        }
                                        amount = sc.nextDouble();
                                        sc.nextLine();
                                        customer.deposit(amount, new java.util.Date());
                                        break;
                                    case 2:
                                        System.out.print("Enter amount to withdraw : ");
                                        while (!sc.hasNextDouble()) {
                                            System.out.println("Invalid amount. Enter again :");
                                            sc.nextLine();
                                        }
                                        amount = sc.nextDouble();
                                        sc.nextLine();
                                        customer.withdraw(amount, new java.util.Date());
                                        break;
                                    case 3:
                                        for (int i = 0; i < customer.transactions.length; i++) {
                                        if (customer.transactions[i] != null) {
                                        System.out.println(customer.transactions[i]); }
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Account Holder Name: " + customer.name);
                                        System.out.println("Account Holder Address: " + customer.address);
                                        System.out.println("Account Holder Contact: " + customer.phone);
                                        System.out.println("Current Balance: $" + String.format("%.2f", customer.balance));
                                        break;
                                    case 5:
                                        boolean passwordChanged = customer.changePassword(sc, databaseConnection); 
                                        if (passwordChanged) {
                                            System.out.println(" ");
                                        } else {
                                            System.out.println("Failed to change password. Please try again.");
                                        }
                                        break;

                                    case 6:
                                        System.out.println("Logout Successfully");
                                        break;
                                
                                }
                                if (userChoice == 6) {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Wrong password.");
                        }
                    } else {
                        System.out.println("Wrong username");
                    }
                    break;

                case 3:
                    while (true) {
                        System.out.println("Enter your mobile number:");
                        String phoneNumber = sc.next();
                        int customerIndex = findCustomerByPhoneNumber(phoneNumber);

                        if (customerIndex != -1) {
                            Customer foundCustomer = customers[customerIndex];
                            System.out.println("Enter your password:");
                            String inputPassword = sc.next();
                            sc.nextLine();

                            if (foundCustomer.password.equals(inputPassword)) {
                                System.out.println("Authentication successful.");
                                System.out.println("Your current username is: " + foundCustomer.username);
                                break;

                            } else {
                                System.out.println("Incorrect password. Authentication failed.");
                            }
                        } else {
                            System.out.println("Mobile number not found.");
                        }
                    }
                    break;



                case 4:
                    System.out.println("\nThank you for choosing Bank Of BMU.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong choice !");
            }
        }
    }


}