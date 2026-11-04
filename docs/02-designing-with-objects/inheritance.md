# Building on another class

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 7, session 1</span><span class="badge badge--accent">Needs: A fixed set of values</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-07-inheritance">Lecture code</a></p>

By the end of this page you can build one class on top of another, say which part of an object came from which class, and explain why a subclass should go through its parent's methods instead of reaching into its fields.

Last week ended with a switch nobody could remove and a bug waiting inside it.

## The thing that has to go

```java
        switch (type) {
            case CHECKING -> lowestAllowed = 0.00;
            case SAVINGS -> lowestAllowed = 100.00;
            case TERM_DEPOSIT -> {
                return false;
            }
            default -> {
                return false;
            }
        }
```

You found the bug yourself at the end of the last session. Add a fourth account type and this method compiles, takes the `default` branch, and refuses every withdrawal. No error, no warning, in the one method where money moves.

The fix is not a better switch. It is to stop asking an object what kind of thing it is.

## Three classes instead of one field

```java
public class SavingsAccount extends Account {

    private static final double MINIMUM_BALANCE = 100.00;
    private static final double MONTHLY_RATE = 0.0025;

    public SavingsAccount(double openingBalance) {
        super(AccountType.SAVINGS, openingBalance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A withdrawal must be positive: " + amount);
        }
        if (getBalance() - amount < MINIMUM_BALANCE) {
            return false;
        }
        return super.withdraw(amount);
    }

    @Override
    public double applyMonthlyInterest() {
        double interest = getBalance() * MONTHLY_RATE;
        if (interest <= 0) {
            return 0.00;
        }
        deposit(interest);
        return interest;
    }
}
```

The whole class. Two numbers and two methods.

`extends Account` is the new word, and the most useful way to read this file is by what is missing from it. There is no account number here. No balance. No `deposit`, no `getBalance`, no `equals`, no `toString`. This class has every one of those and did not write any of them.

Both branches that used to be about savings accounts, the `case SAVINGS` in the switch and the rate on the enum constant, are now in the same place, which is the class they were always about.

`CheckingAccount` and `TermDepositAccount` are the same shape and the same length.

## One object, two layers

This is the picture to hold on to, and the mistake to avoid is thinking there are two objects.

```java
SavingsAccount savings = new SavingsAccount(2000.00);
Account asAnAccount = savings;

System.out.println(asAnAccount == savings);
```

```text
true
```

One object. The `Account` layer holds the number and the balance, the `SavingsAccount` layer holds the rules, and both arrows point at the same thing.

<svg viewBox="0 0 520 260" role="img" xmlns="http://www.w3.org/2000/svg"><title>One object made of two layers, reached through two variables</title><desc>Two variables, one declared as SavingsAccount and one declared as Account, both hold arrows pointing at a single object. That object is drawn as an outer SavingsAccount layer containing an inner Account layer. The inner layer holds the account number and the balance, and the outer layer holds the minimum balance and the monthly rate. A note explains that the variable type decides what you may ask for, not what runs.</desc><rect x="20" y="52" width="130" height="38" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="20" y="162" width="130" height="38" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)"><text x="32" y="76">savings</text><text x="32" y="186">asAnAccount</text></g><circle cx="142" cy="71" r="4" fill="var(--c-accent)"/><circle cx="142" cy="181" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="142" y1="71" x2="236" y2="100"/><polyline points="228,97 237,101 228,106"/><line x1="142" y1="181" x2="236" y2="152"/><polyline points="228,146 237,151 228,157"/></g><rect x="242" y="30" width="250" height="196" rx="10" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="262" y="56" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">SavingsAccount</text><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="262" y="76">MINIMUM_BALANCE  100.00</text><text x="262" y="92">MONTHLY_RATE     0.0025</text></g><rect x="262" y="104" width="210" height="100" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="280" y="128" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">Account</text><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="280" y="150">accountNumber  AC-1001</text><text x="280" y="168">balance        2000.00</text><text x="280" y="186">type           SAVINGS</text></g><text x="20" y="244" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">One object. The variable's type decides what you may ask for, not what runs.</text></svg>

The variable's type decides what the compiler will let you ask for. It does not decide what runs. This fails to compile:

```java
asAnAccount.getWithdrawalsThisMonth();   // Account has no such method
```

even though the object on the other end of that arrow has the method and would run it happily.

## super, and the order it forces

A constructor is not inherited. Every subclass writes its own, and the first thing it does is call the one above:

```java
    public SavingsAccount(double openingBalance) {
        super(AccountType.SAVINGS, openingBalance);
    }
```

`super(...)` has to be the first statement, and Java enforces that rather than trusting you to remember. The `Account` layer has to exist before the `SavingsAccount` layer can be built on top of it. Leave it out and the compiler puts in a call to a no-argument constructor that `Account` does not have, and tells you so.

Notice what this constructor decides on the caller's behalf. The type is not a parameter. Nobody can open a savings account and claim it is a term deposit, because the only code that sets the type is the class the type belongs to.

## The field is still private

Three subclasses need to change the balance. None of them can reach it.

```java
    private double balance;
```

!!! mistake "Making the field `protected` so the subclasses can reach it"

    ```java
    protected double balance;        // do not
    ```

    This is the easy answer and it is everywhere, including in textbooks.

    `protected` means every subclass can set the balance to anything. Not just the three you wrote. Every subclass anybody writes, for as long as the class exists, including the one somebody adds next year in a hurry.

    The moment that is true, nothing in `Account` is a rule any more. The check in `withdraw` is a suggestion that a subclass is free to walk around, and the invariant you spent week 3 building is gone.

    A field is private, or it is public to people you have not met yet.

So the subclasses go through the front door like everyone else:

```java
    @Override
    public boolean withdraw(double amount) {
        if (getBalance() - amount < MINIMUM_BALANCE) {
            return false;
        }
        return super.withdraw(amount);
    }
```

That is the shape to copy. **Do your own part, then hand the common part up.** The subclass decides whether the withdrawal is allowed. `Account` does the arithmetic, exactly once, in one place, where nobody can get it wrong in only one of three files.

`CheckingAccount` uses the same shape for something slightly cleverer:

```java
    @Override
    public boolean withdraw(double amount) {
        double fee = withdrawalsThisMonth < FREE_WITHDRAWALS ? 0.00 : FEE;

        if (!super.withdraw(amount + fee)) {
            return false;
        }

        withdrawalsThisMonth++;
        return true;
    }
```

One call, taking the money and the fee together. If there is not enough for both, `super.withdraw` returns `false` and nothing happens, so nobody is ever charged a fee for a withdrawal that did not go through. That falls out of the design rather than being handled.

## One word that removes a whole class of surprise

```java
    public final void deposit(double amount) {
```

Nothing about putting money in differs by kind of account, so no subclass may change it.

`final` on a method costs five characters and buys this: when you have read `deposit` in `Account`, you have read every deposit in the program. There is no second version hiding in a subclass you have not opened.

## A subclass can add state, not just change methods

```java
public class CheckingAccount extends Account {

    private static final int FREE_WITHDRAWALS = 3;
    private static final double FEE = 0.50;

    private int withdrawalsThisMonth = 0;
```

Three free withdrawals a month, then fifty cents each. That counter exists only on current accounts. A savings account has no such thing, and putting the field on `Account` so that every account carries it would have been a lie about every account that is not a current one.

```java
    @Override
    public double applyMonthlyInterest() {
        withdrawalsThisMonth = 0;
        return 0.00;
    }
```

A current account pays no interest. The month ending still matters to it, because the free allowance starts again, so it has a real reason to override the method even though the money part is zero.

## The payoff

```java
for (Account account : accounts) {
    double paid = account.applyMonthlyInterest();
    System.out.printf("%-38s paid %.2f%n", account, paid);
}
```

```text
Current account AC-1002: 1000.00 EUR   paid 0.00
Savings account AC-1003: 1002.50 EUR   paid 2.50
Term deposit AC-1004: 1004.00 EUR      paid 4.00
```

One loop, written against `Account`, and three different methods ran.

No `if`. No `switch`. No `getType`. The loop does not know which kind of account it is holding and has no way to find out without going out of its way. The object decides, because the method belongs to the object and not to the loop.

And the method that contains that loop, `Bank.applyMonthlyInterest`, did not change this week. Nor did `Customer.applyMonthlyInterest`, `BankConsole` or `ConsoleApp`. Three new classes entered the program and four files never found out.

## The one switch that survives

```java
    public Account openAccount(AccountType type, double openingBalance) {
        Account account = switch (type) {
            case CHECKING -> new CheckingAccount(openingBalance);
            case SAVINGS -> new SavingsAccount(openingBalance);
            case TERM_DEPOSIT -> new TermDepositAccount(openingBalance);
        };
        accounts.add(account);
        return account;
    }
```

Somewhere a choice a person made has to turn into an object. That now happens once, here, instead of in every method that cared about account type.

Two things are different about this switch, and both are worth a minute.

It is a switch **expression**, not a statement. It produces a value, and because every constant of `AccountType` is listed, the compiler can prove it is complete. There is no `default` branch and none is allowed. Compare that with last week, where you were forced to write a branch that could never run.

And now add a fourth constant to `AccountType`. **This file stops compiling** until you say what it should build. Last week the same change compiled silently and refused every withdrawal.

That is the trade. You went from a bug that waits for a user to find it, to an error that stops you before lunch.

??? note "Why can a class only have one parent?"

    Because two parents means two answers to the same question. If both of them have a `withdraw`, which one runs? Languages that allow it have to invent rules for that, and the rules are hard to hold in your head.

    Java's answer is one parent, and then a separate mechanism for the cases where a class genuinely needs to be two things at once. That mechanism is interfaces, and it is next week.

> **If you remember one thing.** A subclass adds to what the parent already does. If you find yourself wanting to reach past the parent to do it, you are no longer adding, you are overruling.

## Check yourself

!!! verify "Two mistakes, both of which compile"

    Open `SavingsAccount`, delete the `@Override` above `withdraw`, and rename the method `withDraw` with a capital D.

    It compiles. Run `InheritanceApp` and look at the third section, which tries to take 950 out of each account. Write down what the savings account did and why.

    Then put `@Override` back, on the misspelt method, without fixing the name. Read the error. Say in one sentence what `@Override` actually does, given that it changes nothing about how the program runs.

    Now the second one. Make `balance` `protected` in `Account` and set it directly from `SavingsAccount.withdraw` instead of calling `super.withdraw`:

    ```java
    balance -= amount;
    return true;
    ```

    This also works. Write down which checks are no longer running, and which class is now responsible for them. Then put it back.

## Think about it

1.  `deposit` is `final` and `withdraw` is not. Say what would have to be true about the bank for `withdraw` to be `final` too. Then say what would have to be true for `deposit` to need overriding, and whether you would allow it.

2.  `CheckingAccount` counts withdrawals and resets the counter inside `applyMonthlyInterest`. That is a counter being reset by a method about interest. Defend it, then say what you would do if the bank also wanted a quarterly fee on a different cycle.

3.  Every subclass passes its own `AccountType` up to `super`. That means the type and the class now say the same thing twice. Find the line of code somebody could write that uses the type to undo this whole week, and say what would stop them.

4.  The three subclasses together are shorter than the switch they replaced, but there are three more files. Argue that the program got simpler. Then argue that it got more complicated. Decide which matters more at six account types, and at sixty.

## Next

Three subclasses, one loop, and no switch where it used to be. Everything on this page says inheritance is the answer.

The next session is about the times it is not, and the warning sign is already sitting in `TermDepositAccount`.

<div class="page-nav" markdown>
[When inheritance hurts](inheritance-hurts.md){ .page-nav__next }
</div>
