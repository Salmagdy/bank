package com.internship.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int MAX_PASSWORD_TRIES = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Account> accounts = new ArrayList<>();
        ManagingFiles.loadAccounts(accounts);

        System.out.println("BANK SYSTEM");

        mainLoop:
        while (true) {
            int mainMenuChoice = promptMainMenu(scanner);
            switch (mainMenuChoice) {
                case 0 -> {
                    break mainLoop;
                }
                case 1 -> handleExistingAccount(scanner, accounts);
                case 2 -> handleNewAccount(scanner, accounts);
                default -> System.out.println("Invalid choice, try again");
            }
        }
        ManagingFiles.saveAllAccounts(accounts);
    }


    private static int promptMainMenu(Scanner scanner) {
        System.out.print("1- Existing Account \n2- New Account\n0- Exit\nChoice:");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static int promptAccountMenu(Scanner scanner) {
        System.out.println("Select the operation: ");
        System.out.print("1- Withdraw money \n2- Deposit money \n3- Check balance \n4- Change password\n0- Exit \nChoice:");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static int promptCreateAccountMenu(Scanner scanner) {
        System.out.println("Select the operation: ");
        System.out.println("1- Create a checking account \n2- Create a savings account \n0- Exit \n");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static void handleExistingAccount(Scanner scanner, List<Account> accounts) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        Account a = Account.findAccountByName(name, accounts);
        if (a == null) {
            System.out.println("Account not found");
            return;
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (!checkPassword(password, a, scanner)) {
            System.out.println("Too many failed attempts.");
            return;
        }

        int subMenuChoice = promptAccountMenu(scanner);
        while (subMenuChoice != 0) {
            switch (subMenuChoice) {
                case 1 -> {
                    System.out.print("Enter the withdrawal amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    a.withdraw(amount);
                }
                case 2 -> {
                    System.out.print("Enter the deposit amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    a.deposit(amount);
                }
                case 3 -> System.out.println("Balance is: " + a.getBalance());
                case 4 -> {
                    System.out.print("Enter the previous password: ");
                    String oldPassword = scanner.nextLine();
                    if (!checkPassword(oldPassword, a, scanner)) {
                        System.out.println("Too many failed attempts.");
                        return;
                    }
                    System.out.print("Enter the new password: ");
                    String newPassword = scanner.nextLine();
                    a.changePassword(oldPassword, newPassword, scanner);
                }
                default -> System.out.println("Invalid choice, try again");
            }
            subMenuChoice = promptAccountMenu(scanner);
        }
    }

    private static void handleNewAccount(Scanner scanner, List<Account> accounts) {
        int subMenuChoice = promptCreateAccountMenu(scanner);
        while (subMenuChoice != 0) {
            switch (subMenuChoice) {
                case 1 -> {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter the amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    String hashed = collectNewPassword(scanner);
                    Account a = new Checking(name, amount, hashed);
                    accounts.add(a);
                    System.out.printf("[SUCCESS] %s initialized with %.2f \n", name, amount);
                }
                case 2 -> {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();

                    System.out.printf("Enter the amount (must be more than %.0f): ", Savings.minimumBalance);
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    while (amount < Savings.minimumBalance) {
                        System.out.printf("[REJECTED] Amount needs to be at least %.0f \n", Savings.minimumBalance);
                        System.out.print("Enter the modified amount: ");
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                    }

                    String hashed = collectNewPassword(scanner);
                    Account a = new Savings(name, amount, hashed);
                    accounts.add(a);
                    System.out.printf("[SUCCESS] %s initialized with %.2f \n", name, amount);
                }
                default -> System.out.println("Invalid choice, try again");
            }
            subMenuChoice = promptCreateAccountMenu(scanner);
        }
    }

    public static boolean checkPassword(String password, Account a, Scanner scanner) {
        int passwordTrials = 0;
        while (!PasswordUtil.verifyPassword(password.toCharArray(), a.getPassword())) {
            if (passwordTrials >= MAX_PASSWORD_TRIES) {
                return false;
            }
            passwordTrials++;
            System.out.println("Incorrect password, please try again");
            password = scanner.nextLine();
        }
        return true;
    }

    private static String collectNewPassword(Scanner scanner) {
        String password;
        do {
            System.out.print("Enter your password (4 digits): ");
            password = scanner.nextLine();
            while (!password.matches("\\d{4}")) {
                System.out.println("Invalid password. Please try again");
                password = scanner.nextLine();
            }
            System.out.print("Confirm your password: ");
            String confirm = scanner.nextLine();
            if (!password.equals(confirm)) {
                System.out.println("Passwords do not match, try again");
                password = null;
            }
        } while (password == null);

        return PasswordUtil.hashPassword(password.toCharArray());
    }
}