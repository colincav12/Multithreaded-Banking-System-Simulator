import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BankingSystemSimulator {
    public static void main(String[] args) {
        // Create the UI
        JFrame frame = new JFrame("Banking System Simulator");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2));

        // Create labels and input fields
        JLabel accountLabel = new JLabel("Select Account (1, 2, or 3):");
        JTextField accountField = new JTextField();

        JLabel amountLabel = new JLabel("Enter Amount:");
        JTextField amountField = new JTextField();

        // Create buttons
        JButton depositButton = new JButton("Add Deposit to Pending");
        JButton withdrawButton = new JButton("Add Withdraw to Pending");
        JButton confirmButton = new JButton("Confirm All Transactions");
        JButton balanceButton = new JButton("Check Balance");
        JButton deadlockButton = new JButton("Simulate Deadlock");

        // Message display
        JLabel statusLabel = new JLabel("Status: Waiting for action...");

        // Add components to the panel
        panel.add(accountLabel);
        panel.add(accountField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(confirmButton);
        panel.add(balanceButton);
        panel.add(deadlockButton);
        panel.add(statusLabel);

        // Add panel to frame
        frame.add(panel);

        // Create three bank accounts with initial balance $1000
        BankAccount account1 = new BankAccount(1000);
        BankAccount account2 = new BankAccount(1000);
        BankAccount account3 = new BankAccount(1000);

        // A list to store pending transactions
        List<Runnable> pendingTransactions = new ArrayList<>();

        // Add action listener for deposit (add to pending)
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int accountNum = Integer.parseInt(accountField.getText());
                    int amount = Integer.parseInt(amountField.getText());

                    if (accountNum == 1) {
                        pendingTransactions.add(() -> account1.deposit(amount));
                        statusLabel.setText("Pending: Deposit $" + amount + " to Account 1");
                    } else if (accountNum == 2) {
                        pendingTransactions.add(() -> account2.deposit(amount));
                        statusLabel.setText("Pending: Deposit $" + amount + " to Account 2");
                    } else if (accountNum == 3) {
                        pendingTransactions.add(() -> account3.deposit(amount));
                        statusLabel.setText("Pending: Deposit $" + amount + " to Account 3");
                    } else {
                        statusLabel.setText("Invalid account number.");
                    }
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please enter a valid number.");
                }
            }
        });

        // Add action listener for withdraw (add to pending)
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int accountNum = Integer.parseInt(accountField.getText());
                    int amount = Integer.parseInt(amountField.getText());

                    if (accountNum == 1) {
                        pendingTransactions.add(() -> account1.withdraw(amount));
                        statusLabel.setText("Pending: Withdraw $" + amount + " from Account 1");
                    } else if (accountNum == 2) {
                        pendingTransactions.add(() -> account2.withdraw(amount));
                        statusLabel.setText("Pending: Withdraw $" + amount + " from Account 2");
                    } else if (accountNum == 3) {
                        pendingTransactions.add(() -> account3.withdraw(amount));
                        statusLabel.setText("Pending: Withdraw $" + amount + " from Account 3");
                    } else {
                        statusLabel.setText("Invalid account number.");
                    }
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please enter a valid number.");
                }
            }
        });

        // Add action listener for confirm (execute all pending transactions)
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread confirmThread = new Thread(new Runnable() {
                    public void run() {
                        for (Runnable transaction : pendingTransactions) {
                            transaction.run(); // Execute each pending transaction
                        }
                        pendingTransactions.clear(); // Clear all pending transactions after execution
                        statusLabel.setText("All transactions executed.");
                    }
                });
                confirmThread.start();
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

        // Add action listener for deadlock simulation
        deadlockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateDeadlock(account1, account2);
            }
        });

        // Make frame visible
        frame.setVisible(true);
    }

    // Simulate a deadlock scenario by having two threads attempt to lock both
    // accounts
    private static void simulateDeadlock(BankAccount account1, BankAccount account2) {
        Runnable task1 = () -> {
            synchronized (account1) {
                System.out.println("Thread 1 locked Account 1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                synchronized (account2) {
                    System.out.println("Thread 1 locked Account 2");
                }
            }
        };

        Runnable task2 = () -> {
            synchronized (account2) {
                System.out.println("Thread 2 locked Account 2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                synchronized (account1) {
                    System.out.println("Thread 2 locked Account 1");
                }
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();
    }
}

class BankAccount {
    private int balance;

    // Set initial balance
    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    // Synchronized deposit method to prevent race conditions
    public synchronized void deposit(int amount) {
        System.out.println("Depositing $" + amount);
        balance += amount;
        System.out.println("Deposit complete. New balance: $" + balance);
    }

    // Synchronized withdraw method to prevent race conditions
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            System.out.println("Withdrawing $" + amount);
            balance -= amount;
            System.out.println("Withdraw complete. New balance: $" + balance);
        } else {
            System.out.println("Insufficient balance for withdrawal.");
        }
    }

    // To get balance
    public synchronized int getBalance() {
        return balance;
    }
}