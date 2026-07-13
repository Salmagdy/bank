package com.internship.bank;
import java.util.*;

public abstract class Account {

    private final String name;
    private double balance;
    private String password;

    Account(String name, double balance, String hashedPassword) {
        this.name = name;
        this.balance = balance;
        this.password = hashedPassword;
    }

    String getName()
    {
        return name;
    }

    double getBalance()
    {
        return balance;
    }

    String getPassword()
    {
        return password;
    }

    void setBalance(double balance)
    {
        this.balance = balance;
    }

    void changePassword(String oldPassword, String newPassword, Scanner scanner) {

        if (PasswordUtil.verifyPassword(oldPassword.toCharArray(), this.password))
        {
            while(!newPassword.matches("\\d{4}"))
            {
                System.out.println("Invalid password. Please try again");
                newPassword = scanner.nextLine();
            }
            this.password = PasswordUtil.hashPassword(newPassword.toCharArray());
        }
    }

    abstract void withdraw(double withdraw);

    void deposit(double deposit)
    {
        balance += deposit;
        System.out.printf("[SUCCESS] %s deposited %.2f , Current Balance: %.2f \n", name , deposit, balance);
    }

    abstract public String getAccountType();

    public String toCSV() {
        return getAccountType() + "," + name + "," + balance + "," + password;
    }

    public static Account findAccountByName(String name, List<Account> accounts) {
        for (Account acc : accounts) {
            if (acc.name.equals(name)) {
                return acc;
            }
        }
        return null;
    }
}