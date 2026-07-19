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
        if (super.getBalance() - withdraw >= minimumBalance && withdraw > 0)
        {
            super.setBalance(super.getBalance() - withdraw);
            System.out.printf("[SUCCESS] %s withdrew %.2f, Current Balance: %.2f \n", super.getName(), withdraw, super.getBalance());

            Logger.getInstance().produce(
                    String.format("%s withdrew %.2f (savings), new balance %.2f", getName(), withdraw, getBalance()),
                    Logger.log.INFO
            );
        }
        else
        {
            System.out.printf("[REJECTED] Transaction denied for %s. Minimum balance limit (%.0f) breached. Current Balance: %.2f \n", getName(), minimumBalance ,getBalance());

            Logger.getInstance().produce(
                    String.format("%s REJECTED withdrawal of %.2f (savings, min balance breach)", getName(), withdraw),
                    Logger.log.WARNING
            );
        }
    }

    @Override
    public String getAccountType() { return "savings"; }
}
