package com.internship.bank;
import java.io.*;
import java.util.*;

public class ManagingFiles {

    private static final String fileName = "data.txt";

    public static void loadAccounts(List<Account> accounts) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("No existing file found.");
            return;
        }
        try {
            List<Account> loaded = Files.lines(path)
                    .parallel()
                    .filter(line -> !line.isBlank())
                    .map(ManagingFiles::parseLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            accounts.addAll(loaded);
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    private static Account parseLine(String line) {
        try {
            String[] parts = line.split(",");
            String type = parts[0];
            String name = parts[1];
            double balance = Double.parseDouble(parts[2]);
            String hashedPassword = parts[3];

            return switch (type) {
                case "checking" -> new Checking(name, balance, hashedPassword);
                case "savings"  -> new Savings(name, balance, hashedPassword);
                default -> null;
            };
        } catch (Exception e) {
            System.out.println("Skipping malformed line: " + line);
            return null;
        }
    }

}

    public static void saveAllAccounts(List<Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Account acc : accounts) {
                writer.write(acc.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
