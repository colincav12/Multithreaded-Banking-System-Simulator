import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankingSystemSimulator {
    public static void main(String[] args) {
        // Create the UI
        JFrame frame = new JFrame("Banking System Simulator");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        // Create labels and input fields
        JLabel accountLabel = new JLabel("Select Account (1, 2, or 3):");
        JTextField accountField = new JTextField();

        JLabel amountLabel = new JLabel("Enter Amount:");
        JTextField amountField = new JTextField();

        // Create buttons
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton balanceButton = new JButton("Check Balance");

        // Message display
        JLabel statusLabel = new JLabel("Status: Waiting for action...");

        // Add components to the panel
        panel.add(accountLabel);
        panel.add(accountField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(balanceButton);
        panel.add(statusLabel);

        // Add panel to frame
        frame.add(panel);

        // Create three bank accounts with initial balance $1000
        BankAccount account1 = new BankAccount(1000);
        BankAccount account2 = new BankAccount(1000);
        BankAccount account3 = new BankAccount(1000);

        // Add action listener for deposit
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int accountNum = Integer.parseInt(accountField.getText());
                    int amount = Integer.parseInt(amountField.getText());

                    Thread depositThread = new Thread(new Runnable() {
                        public void run() {
                            if (accountNum == 1) {
                                account1.deposit(amount);
                                statusLabel.setText("Deposited $" + amount + " into Account 1. Current balance: $"
                                        + account1.getBalance());
                            } else if (accountNum == 2) {
                                account2.deposit(amount);
                                statusLabel.setText("Deposited $" + amount + " into Account 2. Current balance: $"
                                        + account2.getBalance());
                            } else if (accountNum == 3) {
                                account3.deposit(amount);
                                statusLabel.setText("Deposited $" + amount + " into Account 3. Current balance: $"
                                        + account3.getBalance());
                            } else {
                                statusLabel.setText("Invalid account number.");
                            }
                        }
                    });
                    depositThread.start();
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please enter a valid number.");
                }
            }
        });

        // Add action listener for withdraw
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int accountNum = Integer.parseInt(accountField.getText());
                    int amount = Integer.parseInt(amountField.getText());

                    Thread withdrawThread = new Thread(new Runnable() {
                        public void run() {
                            if (accountNum == 1) {
                                account1.withdraw(amount);
                                statusLabel.setText("Withdrew $" + amount + " from Account 1. Current balance: $"
                                        + account1.getBalance());
                            } else if (accountNum == 2) {
                                account2.withdraw(amount);
                                statusLabel.setText("Withdrew $" + amount + " from Account 2. Current balance: $"
                                        + account2.getBalance());
                            } else if (accountNum == 3) {
                                account3.withdraw(amount);
                                statusLabel.setText("Withdrew $" + amount + " from Account 3. Current balance: $"
                                        + account3.getBalance());
                            } else {
                                statusLabel.setText("Invalid account number.");
                            }
                        }
                    });
                    withdrawThread.start();
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please enter a valid number.");
                }
            }
        });

        // Add action listener for check balance
        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int accountNum = Integer.parseInt(accountField.getText());

                    Thread balanceThread = new Thread(new Runnable() {
                        public void run() {
                            if (accountNum == 1) {
                                statusLabel.setText("Current balance of Account 1: $" + account1.getBalance());
                            } else if (accountNum == 2) {
                                statusLabel.setText("Current balance of Account 2: $" + account2.getBalance());
                            } else if (accountNum == 3) {
                                statusLabel.setText("Current balance of Account 3: $" + account3.getBalance());
                            } else {
                                statusLabel.setText("Invalid account number.");
                            }
                        }
                    });
                    balanceThread.start();
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please enter a valid number.");
                }
            }
        });

        // Make frame visible
        frame.setVisible(true);
    }
}

class BankAccount {
    private int balance;

    // Set initial balance
    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        System.out.println("Depositing $" + amount);
        balance += amount;
        System.out.println("Deposit complete. New balance: $" + balance);
    }

    public void withdraw(int amount) {
        if (balance >= amount) {
            System.out.println("Withdrawing $" + amount);
            balance -= amount;
            System.out.println("Withdraw complete. New balance: $" + balance);
        } else {
            System.out.println("Insufficient balance for withdrawal.");
        }
    }

    public  int getBalance() {
        return balance;
    }
}