# Objects that refuse

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 3, session 1</span><span class="badge badge--accent">Needs: What a variable really holds</span></p>

By the end of this page you can write a class that cannot be put into an invalid state, tell the difference between a caller asking for something reasonable and a caller passing you nonsense, and decide which methods a class should expose rather than exposing all of them.

Your `Account` is protected in one direction only. Nobody can reach in and change the balance, which is real progress. But the constructor will still accept a negative opening balance and a null owner, and once an object is born wrong it stays wrong for the rest of its life.

## The thing that must always be true

Every useful class has one or two statements that must hold from the moment the object exists until it is thrown away. For an account:

> The balance is never negative.

That is an **invariant**. It is not a nice-to-have and it is not checked occasionally. If it is ever false, the object is broken, and every part of the program that trusted it is now wrong in a way nobody will notice until the totals do not add up.

Two things have to happen for an invariant to survive. It must be true when the object is created, and no method may leave it false. That is the whole job of this session.

## The constructor as a gate

Right now this compiles and runs:

```java
Account broken = new Account("AC-1001", null, -5000.00);
```

An account owned by nobody, holding negative money. Nothing stops it, and from here it will be passed around, added to lists, and totalled.

```java
    public Account(String accountNumber, String ownerName, double openingBalance) {
        this.accountNumber = requireText(accountNumber, "Account number");
        this.ownerName = requireText(ownerName, "Owner name");

        if (openingBalance < 0) {
            throw new IllegalArgumentException(
                    "Opening balance cannot be negative: " + openingBalance);
        }
        this.balance = openingBalance;
    }

    private String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " is required");
        }
        return value;
    }
```

`throw` stops the constructor immediately. The remaining lines never run, `new` never gives back a reference, and **the object never exists**. There is no half-built account to worry about later, because there is no account.

You will meet exceptions properly in week 9, where you learn to catch them. Throwing one is the easy half and you can use it now. `IllegalArgumentException` is Java's standard way of saying: the value you handed me is not acceptable.

Put the bad value in the message. `"Opening balance cannot be negative: -5000.0"` tells you what happened. `"Invalid input"` tells you nothing and you will be the one reading it.

`requireText` exists because the same check is about to appear twice. One rule, one place, which is the argument from week 1 applied to your own code.

!!! mistake "What your first instinct will be, and why it fails"

    Almost everyone writes this first:

    ```java
    if (openingBalance < 0) {
        System.out.println("Opening balance cannot be negative");
    }
    this.balance = openingBalance;
    ```

    Read it again. It complains, and then assigns the bad value anyway. The account is created, the balance is negative, and the only thing that happened is a line of text nobody was watching.

    This is worse than no check at all, because it looks like validation. A guard that does not stop anything is a comment with extra steps.

## Two kinds of no

Now the interesting part, and the thing that separates a class that merely works from one that is pleasant to use.

A caller with 500 euro asks to withdraw 900. That is a completely normal thing to happen. The bank will not allow it, the customer sees a message, life continues. The account should say no and the program should carry on.

A caller asks to withdraw -50. That is not a customer doing anything. No interface offers a negative withdrawal. Somebody wrote a bug, and the sooner it is loud the better.

Those two deserve different answers.

<svg viewBox="0 0 480 310" role="img" xmlns="http://www.w3.org/2000/svg"><title>Three requests to an account and three different outcomes</title><desc>Three rows. In the first, a request to withdraw nine hundred from an account holding five hundred is refused and returns false, which is a normal outcome the caller handles. In the second, a request to withdraw minus fifty throws an exception, because no valid caller would ask that. In the third, an attempt to create an account with a negative balance throws before any object exists.</desc><rect x="248" y="18" width="212" height="70" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="266" y="42" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><text x="266" y="66" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-accent)">balance 500.00</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="20" y1="44" x2="242" y2="44"/><polyline points="235,39 243,44 235,49"/><line x1="242" y1="66" x2="20" y2="66"/><polyline points="27,61 19,66 27,71"/></g><text x="34" y="38" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)">withdraw(900)</text><text x="34" y="82" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-muted)">false</text><text x="20" y="108" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Normal. The caller decides what to tell the customer.</text><line x1="20" y1="124" x2="460" y2="124" stroke="var(--c-border)" stroke-width="1" stroke-dasharray="4 4"/><rect x="248" y="142" width="212" height="52" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="266" y="174" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><g stroke="var(--c-accent)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="20" y1="162" x2="242" y2="162"/><polyline points="235,157 243,162 235,167"/></g><text x="34" y="156" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)">withdraw(-50)</text><g stroke="var(--c-accent)" stroke-width="2" stroke-linecap="round"><line x1="128" y1="176" x2="144" y2="192"/><line x1="144" y1="176" x2="128" y2="192"/></g><text x="156" y="189" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)">IllegalArgumentException</text><text x="20" y="214" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">A bug. Stop now, loudly, with the value in the message.</text><line x1="20" y1="230" x2="460" y2="230" stroke="var(--c-border)" stroke-width="1" stroke-dasharray="4 4"/><rect x="248" y="248" width="212" height="46" rx="8" fill="none" stroke="var(--c-border)" stroke-width="2" stroke-dasharray="6 5"/><text x="266" y="276" font-family="Inter, sans-serif" font-size="13" fill="var(--c-muted)">no object is created</text><g stroke="var(--c-accent)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="20" y1="266" x2="230" y2="266"/></g><text x="34" y="260" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)">new Account(..., -5000)</text><g stroke="var(--c-accent)" stroke-width="2" stroke-linecap="round"><line x1="226" y1="258" x2="242" y2="274"/><line x1="242" y1="258" x2="226" y2="274"/></g><text x="20" y="306" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The gate holds. There is no broken account to find later.</text></svg>

```java
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be positive: " + amount);
        }
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal must be positive: " + amount);
        }
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
```

`deposit` returns nothing now. Last week it returned a `boolean`, and that `boolean` was always `true` unless the caller was broken. A return value that only ever means "you passed me rubbish" is better expressed as an exception, so the method gets simpler and the caller stops writing an `if` around every deposit.

`withdraw` keeps its `boolean`, because refusing a withdrawal is a real outcome that a correct caller must handle.

That is the rule worth carrying: **return a value for outcomes the caller should expect, throw for situations the caller should have prevented.**

## Which doors to build

Encapsulation is not a rule about fields. It is a decision about what your class lets the world do.

```java
    public void setOwnerName(String ownerName) {
        this.ownerName = requireText(ownerName, "Owner name");
    }
```

This setter is fine. People change their name, the bank has to record it, and the method still enforces the rule. It is a real operation with a guard on it.

There is still no `setBalance`, and there never will be. Adding it would hand back exactly the power `private` took away, and the invariant would be gone the same afternoon. The balance moves through `deposit` and `withdraw`, or it does not move.

Ask of every public method: is this something the thing genuinely does? An account accepts deposits. An account does not have its balance assigned by a stranger.

!!! mistake "A getter and a setter for every field is not encapsulation"

    You will see classes where all fields are private and every one has `getX` and `setX`. Count what that protects. Anybody can still read anything and write anything. It is the same open data with four extra lines per field, and now it is longer.

    Every setter you add is a rule you decided not to enforce. Write them when the operation is real, and be suspicious of yourself when you write them by reflex.

??? note "The whole Account class so far"

    ```java
    public class Account {

        private String accountNumber;
        private String ownerName;
        private double balance;

        public Account(String accountNumber, String ownerName, double openingBalance) {
            this.accountNumber = requireText(accountNumber, "Account number");
            this.ownerName = requireText(ownerName, "Owner name");

            if (openingBalance < 0) {
                throw new IllegalArgumentException(
                        "Opening balance cannot be negative: " + openingBalance);
            }
            this.balance = openingBalance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = requireText(ownerName, "Owner name");
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit must be positive: " + amount);
            }
            balance += amount;
        }

        public boolean withdraw(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal must be positive: " + amount);
            }
            if (amount > balance) {
                return false;
            }
            balance -= amount;
            return true;
        }

        private String requireText(String value, String label) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(label + " is required");
            }
            return value;
        }

        @Override
        public String toString() {
            return String.format("Account %s (%s): %.2f EUR",
                    accountNumber, ownerName, balance);
        }
    }
    ```

> **If you remember one thing.** `private` is not about hiding information. It is about making sure you are the only one who can break your own rules.

## Check yourself

!!! verify "Try to create a broken account"

    In `main`, write each of these on its own and run the program each time.

    ```java
    Account a = new Account("AC-1001", "Ada Lovelace", -100.00);
    Account b = new Account("AC-1002", "", 500.00);
    Account c = new Account(null, "Alan Turing", 500.00);
    ```

    Each should stop the program with a message naming the problem. Read each message and check it tells you enough to fix the call without opening `Account.java`. If one does not, improve it.

    Then the important half. Add a valid account and try these two:

    ```java
    Account ada = new Account("AC-1001", "Ada Lovelace", 500.00);

    boolean ok = ada.withdraw(900.00);    // what happens?
    ada.withdraw(-50.00);                 // what happens?
    System.out.println(ada.getBalance());
    ```

    One of those lines returns. One of them ends the program. Before running, write down which is which and why, in one sentence each. That distinction is the session.

## Think about it

1.  The constructor refuses a negative opening balance. But a customer could deposit 500 and then the bank could apply a 600 euro fee. Should the account allow the balance to go below zero in that case? Decide, and then say where in the class your decision would live. Now suppose the bank introduces overdrafts next year.

2.  You were told to throw for a negative amount and return `false` for insufficient funds. Someone argues both are just failures and both should return `false`. Make their case as strongly as you can, then say what it would cost.

3.  `setOwnerName` was allowed and `setBalance` was not, yet both change a private field through a validated method. What is the actual difference? Try to state it as a test you could apply to a method you have not seen before.

4.  The exception message contains the bad value. That is helpful to a developer. Is there a situation where putting the value in the message would be a bad idea? Think about what else gets stored in a bank account, and about who reads log files.

<div class="page-nav" markdown>
[Session 2. What belongs to whom](what-belongs-to-whom.md){ .page-nav__next }
</div>
