# Objects that own other objects

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 4, session 2</span><span class="badge badge--accent">Needs: Many objects</span></p>

By the end of this page you can build a class that owns a collection of other objects, keep that collection safe from the outside world, and make the owner coordinate work without doing the work itself. This is the last lecture of Module 1, and the code you finish with is where Sprint 1 begins.

A customer has accounts. That sentence is easy to say and the design it implies is where most first systems go wrong, because the tempting shortcut is to let everyone reach into the customer's list.

## Moving a fact to where it belongs

First, a correction to `Account`.

It currently stores `ownerName`. Once `Customer` exists, the owner's name lives in two places, and two places holding the same fact will eventually disagree. Change the name on the customer and every account still shows the old one.

So the name leaves `Account`:

```java
public class Account {

    private static int lastNumberIssued = 1000;

    private final String accountNumber;
    private double balance;

    public Account(double openingBalance) {
        lastNumberIssued++;
        this.accountNumber = "AC-" + lastNumberIssued;

        if (openingBalance < 0) {
            throw new IllegalArgumentException(
                    "Opening balance cannot be negative: " + openingBalance);
        }
        this.balance = openingBalance;
    }

    @Override
    public String toString() {
        return String.format("Account %s: %.2f EUR", accountNumber, balance);
    }
    // deposit, withdraw, getters unchanged
}
```

This is the week 1 argument again. One rule, one place. One fact, one place.

## The customer

```java
import java.util.ArrayList;
import java.util.List;

public class Customer {

    private static int lastIdIssued = 1000;

    private final String customerId;
    private String name;
    private final List<Account> accounts = new ArrayList<>();

    public Customer(String name) {
        lastIdIssued++;
        this.customerId = "C-" + lastIdIssued;
        this.name = requireText(name, "Customer name");
    }
```

The list is `private`, so nobody outside can touch it. It is also `final`, which means the customer will always have exactly this list. New accounts go into it, closed accounts come out, but the customer is never handed a different list. That is one less thing that can go wrong, for one word.

It is created where it is declared rather than in the constructor, so there is no path through the code where a customer exists with no list at all.

## Opening and closing

```java
    public Account openAccount(double openingBalance) {
        Account account = new Account(openingBalance);
        accounts.add(account);
        return account;
    }

    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public boolean closeAccount(String accountNumber) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return false;
        }
        if (account.getBalance() > 0) {
            return false;
        }
        accounts.remove(account);
        return true;
    }
```

`openAccount` makes the account rather than accepting one. The customer controls what enters its own list, which means there is no way to end up holding an account that belongs to somebody else.

It returns the new account, because the caller usually wants it immediately. Returning it is fine: the account is not secret. The list is.

`closeAccount` refuses to close an account with money still in it. That is a rule about the relationship between a customer and an account, so it lives in the class that owns the relationship. Notice it returns `false` rather than throwing: a customer trying to close a funded account is a normal thing to attempt, not a bug.

## Never hand out the list

This is the line to remember from the whole session.

```java
    // Do not write this
    public List<Account> getAccounts() {
        return accounts;
    }
```

That gives the caller your actual list. They can call `clear()` on it. They can add an account belonging to a different customer. Every guarantee `openAccount` and `closeAccount` were making is gone, and the class did it to itself in three lines.

```java
    public List<Account> getAccounts() {
        return List.copyOf(accounts);
    }
```

`List.copyOf` hands back a separate, unchangeable list. The caller can read it and loop over it and cannot alter yours.

Be precise about what got copied. The **list** is new. The accounts are the same accounts, because a list holds arrows. Somebody with the copy can still call `deposit` on an account inside it. They cannot change which accounts you have, which is what you were protecting.

??? note "The other way you will see this written"

    ```java
    return Collections.unmodifiableList(accounts);
    ```

    This returns a read-only view rather than a copy. It is cheaper for a large list, but it stays connected to yours, so if your list changes afterwards the view changes too. `List.copyOf` is the safer default and the one to reach for unless you have a reason.

## Coordinate, do not reach in

Now the idea the whole module has been building toward.

The customer needs to support a withdrawal from one of its accounts. Here is the version that feels natural and is wrong:

```java
    // Wrong
    public boolean withdrawFrom(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            return true;
        }
        return false;
    }
```

`Customer` has taken over the account's job. It checks the balance rule, which belongs to `Account`, and it needs a `setBalance` to exist, which is the door week 3 deliberately did not build. Two classes now know the withdrawal rule, and that is one too many.

```java
    public boolean withdrawFrom(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return false;
        }
        return account.withdraw(amount);
    }
```

The customer finds the right account and asks it. It does no arithmetic and knows nothing about balance rules. If the overdraft policy changes next year, this method does not change.

That is **ask, do not take**, from the very first session of the course, now operating between two of your own classes.

```java
    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    public int getAccountCount() {
        return accounts.size();
    }
```

`getTotalBalance` calculates rather than stores. Storing a running total would mean two facts that have to agree, and one of them would be wrong within a month.

<svg viewBox="0 0 480 300" role="img" xmlns="http://www.w3.org/2000/svg"><title>A customer owning a private list of accounts</title><desc>A Customer object holds an identifier, a name and a private accounts field. That field holds an arrow to a list object, whose three slots each hold an arrow out to a separate Account object. The accounts sit outside the customer, and the list itself is never handed out.</desc><rect x="20" y="90" width="166" height="118" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="38" y="114" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Customer</text><line x1="38" y1="122" x2="168" y2="122" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)"><text x="38" y="144">C-1001</text><text x="38" y="164">Ada Lovelace</text></g><rect x="38" y="174" width="130" height="22" rx="3" fill="none" stroke="var(--c-border)" stroke-width="1.5"/><text x="46" y="189" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-accent)">accounts</text><circle cx="160" cy="185" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="160" y1="185" x2="206" y2="160"/><polyline points="199,158 207,159 203,167"/></g><rect x="212" y="98" width="86" height="118" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="226" y="120" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)">List</text><line x1="226" y1="128" x2="284" y2="128" stroke="var(--c-border)" stroke-width="1.5"/><g fill="none" stroke="var(--c-border)" stroke-width="1.5"><rect x="226" y="138" width="50" height="16" rx="3"/><rect x="226" y="162" width="50" height="16" rx="3"/><rect x="226" y="186" width="50" height="16" rx="3"/></g><circle cx="268" cy="146" r="4" fill="var(--c-accent)"/><circle cx="268" cy="170" r="4" fill="var(--c-accent)"/><circle cx="268" cy="194" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="268" y1="146" x2="322" y2="82"/><polyline points="315,84 323,81 320,89"/><line x1="268" y1="170" x2="322" y2="156"/><polyline points="315,152 323,156 316,161"/><line x1="268" y1="194" x2="322" y2="228"/><polyline points="315,223 323,228 314,231"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="328" y="58" width="132" height="44" rx="6"/><rect x="328" y="134" width="132" height="44" rx="6"/><rect x="328" y="210" width="132" height="44" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)"><text x="342" y="78">AC-1001</text><text x="342" y="154">AC-1002</text><text x="342" y="230">AC-1003</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-accent)"><text x="342" y="94">500.00</text><text x="342" y="170">2500.00</text><text x="342" y="246">1500.00</text></g><text x="20" y="284" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The customer owns the list. The list holds arrows. Neither is ever handed out.</text></svg>

```java
    @Override
    public String toString() {
        return String.format("Customer %s %s (%d accounts, %.2f EUR)",
                customerId, name, accounts.size(), getTotalBalance());
    }
```

## Putting it together

```java
public class BankApp {

    public static void main(String[] args) {
        Customer ada = new Customer("Ada Lovelace");

        Account savings  = ada.openAccount(500.00);
        Account checking = ada.openAccount(2500.00);

        System.out.println(ada);

        if (ada.withdrawFrom(savings.getAccountNumber(), 200.00)) {
            System.out.println("Withdrawal accepted.");
        } else {
            System.out.println("Not enough money.");
        }

        for (Account account : ada.getAccounts()) {
            System.out.println("  " + account);
        }

        System.out.println("Total: " + ada.getTotalBalance() + " EUR");
        System.out.println(ada.closeAccount(checking.getAccountNumber()));
    }
}
```

```text
Customer C-1001 Ada Lovelace (2 accounts, 3000.00 EUR)
Withdrawal accepted.
  Account AC-1001: 300.00 EUR
  Account AC-1002: 2500.00 EUR
Total: 2800.0 EUR
false
```

The last line is `false` because the checking account still holds 2500 euro. The rule held without anybody remembering it.

Every sentence the user reads was written in `main`. Neither `Customer` nor `Account` says anything. That has been true since week 2 and it is about to pay off: in week 13 this same model gets a window on top of it, and nothing in these two classes will change.

> **If you remember one thing.** Owning a collection means owning what happens to it. Keep it private, decide what may enter it, and ask the things inside it to do their own work.

## Check yourself

!!! verify "Try to break the customer from outside"

    Open two accounts for a customer, then try each of these in `main`.

    ```java
    ada.getAccounts().clear();
    System.out.println(ada.getAccountCount());
    ```

    The count should still be 2, and the `clear()` call should fail rather than quietly succeed. Read the exception and note which class threw it.

    ```java
    Account stolen = ada.getAccounts().get(0);
    stolen.deposit(1000.00);
    System.out.println(ada.getTotalBalance());
    ```

    This one **does** change the customer's total, and it should. Explain in one sentence why `clear()` was blocked and `deposit()` was not. If you can answer that, you have understood both this session and week 2.

## Think about it

1.  `getAccounts` returns a copy of the list, but the accounts inside it are the real ones, so a caller can still change balances. Is that a hole in the design or the correct behaviour? Argue it, then say what a genuine fix would cost.

2.  `closeAccount` refuses when the balance is above zero. Where else could that rule have lived, and what would have gone wrong there? Now suppose the bank wants to allow closing an account by transferring the remainder somewhere else first.

3.  The owner's name moved out of `Account` because two copies of one fact will eventually disagree. Find something in a bank where you would genuinely want the same fact stored twice, and say what you would do to keep the copies honest.

4.  `withdrawFrom` returns `false` both when the account does not exist and when there was not enough money. Those are very different situations for whoever called it. Decide whether that matters, and if it does, describe what you would return instead. You now have three sessions' worth of tools and none of them is quite right, which is the honest state of things until week 9.

## Module 1 is done

You can now take a description of a system, decide what the objects are, write them so they cannot be put into an invalid state, and hold many of them in a way that keeps the rules intact.

**Sprint 1 opens this week.** You will build your own application from your own classes, in the repository you set up in week 1. Everything you need is on these eight pages.

Module 2 starts with a problem this design cannot solve. Ada wants to move money to Alan's account, and neither `Customer` nor `Account` has anywhere sensible to put that.

<div class="page-nav" markdown>
[Back to the roadmap](../roadmap.md){ .page-nav__next }
</div>
