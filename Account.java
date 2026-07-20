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

    boolean changePassword(String newPassword, Scanner scanner) {

        while (!newPassword.matches("\\d{4}")) {
            System.out.println("Invalid password. Please try again");
            newPassword = scanner.nextLine();
        }
        this.password = PasswordUtil.hashPassword(newPassword.toCharArray());
        return true;
    }

    abstract void withdraw(double withdraw);

    void deposit(double deposit)
    {
        if (deposit > 0)
        {
            balance += deposit;
            System.out.printf("[SUCCESS] %s deposited %.2f , Current Balance: %.2f \n", name , deposit, balance);
            Logger.getInstance().produce(
                    String.format("%s deposited %.2f, new balance %.2f", name, deposit, balance),
                    Logger.log.INFO
            );
        }
        else
        {
            System.out.printf("[FAILED] %s tried to deposit invalid amount %.2f \n", name , deposit);
            Logger.getInstance().produce(
                    String.format("%s tried to deposit invalid amount %.2f", name, deposit),
                    Logger.log.ERROR
            );
        }

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
