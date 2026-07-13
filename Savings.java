package com.internship.bank;

public class Savings extends Account {
    final static double minimumBalance = 100;

    Savings(String name, double balance, String pass)
    {
        super(name, balance, pass);

    }

    @Override
    void withdraw(double withdraw)
    {
        System.out.printf("%s tries to withdraw %.2f \n" , super.getName() , withdraw);
        if (super.getBalance() - withdraw >= minimumBalance)
        {
            super.setBalance(super.getBalance() - withdraw);
            System.out.printf("[SUCCESS] %s withdrew %.2f, Current Balance: %.2f \n", super.getName(), withdraw, super.getBalance());
        }
        else
        {
            System.out.printf("[REJECTED] Transaction denied for %s. Minimum balance limit (%.0f) breached. Current Balance: %.2f \n", getName(), minimumBalance ,getBalance());
        }
    }

    @Override
    public String getAccountType() { return "savings"; }
}