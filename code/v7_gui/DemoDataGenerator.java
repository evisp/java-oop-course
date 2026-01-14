package v7_gui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public final class DemoDataGenerator {

    private DemoDataGenerator() {}

    // 6-arg version to match BankBootstrap
    public static Bank generateAndSave(String filename,
                                       int customerCount,
                                       int accountsPerCustomer,
                                       int minTxPerAccount,
                                       int maxTxPerAccount,
                                       int historyDays) throws Exception {

        Bank bank = generate(customerCount, accountsPerCustomer, minTxPerAccount, maxTxPerAccount, historyDays);
        BankDataManager.saveBank(bank, filename);
        return bank;
    }

    public static Bank generate(int customerCount,
                                int accountsPerCustomer,
                                int minTxPerAccount,
                                int maxTxPerAccount,
                                int historyDays) {

        Random rnd = new Random(42);
        Bank bank = new Bank("Turing National Bank (Synthetic)");

        List<Account> allAccounts = new ArrayList<>();

        // Customers + accounts
        for (int i = 0; i < customerCount; i++) {
            int id = 3000 + i;
            int age = 18 + rnd.nextInt(60);
            Customer c = new Customer(id, "Customer " + (i + 1), age, "City Block " + (1 + rnd.nextInt(50)));
            bank.addCustomer(c);

            for (int a = 0; a < accountsPerCustomer; a++) {
                Account acc;
                if (a % 2 == 0) {
                    acc = bank.openSavingsAccount(c.getId(), 0.0, 0.02 + rnd.nextDouble() * 0.02);
                } else {
                    acc = bank.openCheckingAccount(c.getId(), 0.0, 200 + rnd.nextInt(1000));
                }
                allAccounts.add(acc);
            }

            // freeze a few accounts to make the dashboard “attention” table interesting
            if (!c.getAccounts().isEmpty() && rnd.nextDouble() < 0.05) {
                c.getAccounts().get(0).freezeAccount();
            }
        }

        // Dated transactions
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(historyDays);

        for (Account acc : allAccounts) {
            int txCount = minTxPerAccount + rnd.nextInt(Math.max(1, maxTxPerAccount - minTxPerAccount + 1));

            List<LocalDateTime> times = new ArrayList<>(txCount);
            for (int t = 0; t < txCount; t++) {
                times.add(randomDateTimeBetween(rnd, start, end));
            }
            times.sort(Comparator.naturalOrder());

            // initial funding
            applyDeposit(acc, 500 + rnd.nextDouble() * 3000, times.get(0), "Initial funding");

            for (int t = 1; t < times.size(); t++) {
                LocalDateTime ts = times.get(t);
                if (rnd.nextDouble() < 0.55) {
                    applyDeposit(acc, 20 + rnd.nextDouble() * 800, ts, "Deposit");
                } else {
                    applyWithdraw(acc, 10 + rnd.nextDouble() * 650, ts, "Withdrawal");
                }
            }
        }

        // Some transfers
        int transferEvents = Math.max(20, customerCount * 3);
        for (int i = 0; i < transferEvents; i++) {
            Account from = allAccounts.get(rnd.nextInt(allAccounts.size()));
            Account to = allAccounts.get(rnd.nextInt(allAccounts.size()));
            if (from == to) continue;

            LocalDateTime ts = randomDateTimeBetween(rnd, start, end);
            double amount = 25 + rnd.nextDouble() * 600;

            applyTransfer(from, to, amount, ts);
        }

        return bank;
    }

    // Helpers (directly writing into protected fields; all in same package v7_gui)
    private static void applyDeposit(Account acc, double amount, LocalDateTime ts, String desc) {
        if (acc == null || acc.isFrozen() || amount <= 0) return;

        acc.balance += amount;

        Transaction tx = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                amount,
                acc.getAccountId(),
                acc.balance,
                desc,
                ts
        );
        acc.transactionHistory.add(tx);
    }

    private static void applyWithdraw(Account acc, double amount, LocalDateTime ts, String desc) {
        if (acc == null || acc.isFrozen() || amount <= 0) return;

        // Simple, always-compiling rule (no dependency on extra getters):
        double min = (acc instanceof SavingsAccount) ? 100.0 : -500.0;
        if (acc.balance - amount < min) return;

        acc.balance -= amount;

        Transaction tx = new Transaction(
                Transaction.TransactionType.WITHDRAW,
                amount,
                acc.getAccountId(),
                acc.balance,
                desc,
                ts
        );
        acc.transactionHistory.add(tx);
    }

    private static void applyTransfer(Account from, Account to, double amount, LocalDateTime ts) {
        if (from == null || to == null || from == to) return;
        if (from.isFrozen() || to.isFrozen() || amount <= 0) return;

        double min = (from instanceof SavingsAccount) ? 100.0 : -500.0;
        if (from.balance - amount < min) return;

        from.balance -= amount;
        to.balance += amount;

        Transaction out = new Transaction(
                Transaction.TransactionType.TRANSFER_OUT,
                amount,
                from.getAccountId(),
                to.getAccountId(),
                from.balance,
                "Transfer to #" + to.getAccountId(),
                ts
        );

        Transaction in = new Transaction(
                Transaction.TransactionType.TRANSFER_IN,
                amount,
                to.getAccountId(),
                from.getAccountId(),
                to.balance,
                "Transfer from #" + from.getAccountId(),
                ts
        );

        from.transactionHistory.add(out);
        to.transactionHistory.add(in);
    }

    private static LocalDateTime randomDateTimeBetween(Random rnd, LocalDateTime start, LocalDateTime end) {
        ZoneId zone = ZoneId.systemDefault();

        long startMs = start.atZone(zone).toInstant().toEpochMilli();
        long endMs = end.atZone(zone).toInstant().toEpochMilli();

        long randomMs = startMs + (long) (rnd.nextDouble() * (endMs - startMs + 1));

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(randomMs), zone);
    }
}
