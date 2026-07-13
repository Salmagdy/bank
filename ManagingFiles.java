package com.internship.bank;
import java.io.*;
import java.util.*;

public class ManagingFiles {

    private static final String FILE_NAME = "data.txt";

    public static void loadAccounts(List<Account> accounts) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                String name = parts[1];
                double balance = Double.parseDouble(parts[2]);
                String hashedPassword = parts[3];

                Account acc = switch (type) {
                    case "checking" -> new Checking(name, balance, hashedPassword);
                    case "savings"  -> new Savings(name, balance, hashedPassword);
                    default -> throw new IllegalStateException("Unknown account type: " + type);
                };
                accounts.add(acc);
            }
        } catch (IOException e) {
            System.out.println("No existing file found.");
        }
    }

    public static void saveAllAccounts(List<Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (Account acc : accounts) {
                writer.write(acc.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}