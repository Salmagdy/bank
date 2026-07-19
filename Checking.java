package com.internship.bank;

public class Checking extends Account {
    final static double operationFee = 2;

    Checking(String name, double balance, String pass)
    {
        super(name, balance, pass);
    }

    @Override
    void withdraw(double withdraw)
    {
        System.out.printf("%s tries to withdraw %.2f \n" , getName() ,withdraw);
        if (withdraw + operationFee <= getBalance() && withdraw > 0)
        {
            setBalance(getBalance() - withdraw - operationFee);
            System.out.printf("[SUCCESS] %s withdrew %.2f , Current Balance: %.2f \n", getName() , withdraw , getBalance());

            Logger.getInstance().produce(
                    String.format("%s withdrew %.2f (checking), new balance %.2f", getName(), withdraw, getBalance()),
                    Logger.log.INFO
            );
        }
        else
        {
            System.out.printf("[REJECTED] Transaction denied for %s. Insufficient balance \n", getName());
            System.out.printf("Current balance is: %.2f \n" , getBalance());

            Logger.getInstance().produce(
                    String.format("%s REJECTED withdrawal of %.2f (checking, insufficient balance)", getName(), withdraw),
                    Logger.log.WARNING
            );
        }

    }

    @Override
    public String getAccountType() { return "checking"; }
}
