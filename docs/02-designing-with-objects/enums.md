# A fixed set of values

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 6, session 2</span><span class="badge badge--accent">Needs: One job per class</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-06-one-job-per-class">Lecture code</a></p>

By the end of this page you can replace a set of strings with a type the compiler checks, attach data to each of its values, and tell the difference between a problem an enum fixes and one it cannot.

Week 4 left this in your code, and at the time it was the best you could do:

```java
List<String> accountTypes = List.of("CHECKING", "SAVINGS", "DEPOSIT");
```

## Three ways a string lets you down

All three of these compile. None of them works.

```java
if (type.equals("checking")) { ... }   // never matches. Wrong case.
if (type.equals("CHEKING"))  { ... }   // never matches. Typo.
account.setType("CRYPTO");             // not an account type at all
```

The first two are silent. The code runs, the branch never fires, and the only sign is a feature that quietly does nothing. The third one is worse: it succeeds, and now there is an account in your bank of a kind that does not exist.

The compiler could have caught all three. It did not, because to a compiler `"CHEKING"` is a perfectly good `String`. Any string is a good `String`. That is the problem, and it is a problem with the type, not with your care.

## A type whose values are listed once

```java
public enum AccountType {

    CHECKING,
    SAVINGS,
    TERM_DEPOSIT;
}
```

Three values. There are no others, and there is no way to make one.

The important half of that is invisible. There is no longer any line anywhere in this program that compiles with a misspelt account type, because `AccountType.CHEKING` is not something you can write. The compiler stops you in the editor, before the program has ever run.

```java
private final AccountType type;
```

A field of this type can hold one of exactly three things, or `null`. Compare that with a `String` field, which can hold anything anybody ever types.

## A constant is an object

This is the part that catches people out, so read it slowly.

An enum is **not** a list of names. It is a small fixed set of objects, and you gave each one a name. Objects can hold fields, so these can too.

```java
public enum AccountType {

    CHECKING("Current account", 0.0000),
    SAVINGS("Savings account", 0.0025),
    TERM_DEPOSIT("Term deposit", 0.0040);

    private final String label;
    private final double monthlyInterestRate;

    AccountType(String label, double monthlyInterestRate) {
        this.label = label;
        this.monthlyInterestRate = monthlyInterestRate;
    }

    public String getLabel() {
        return label;
    }

    public double getMonthlyInterestRate() {
        return monthlyInterestRate;
    }
}
```

The three lines at the top are calls to the constructor below them. `CHECKING` is an object that was built once, when the program started, and it will be the same object every time you mention it for as long as the program runs.

The constructor has no `public` or `private` in front of it, and neither is allowed. The only code that can ever call it is those three lines.

<svg viewBox="0 0 520 250" role="img" xmlns="http://www.w3.org/2000/svg"><title>An account type field pointing at one of three constant objects</title><desc>On the left, an Account object holds a number and a field called type. That field holds an arrow pointing at one of three objects on the right, which are the three constants of the AccountType enum. Each constant is an object holding its own label and its own monthly interest rate.</desc><text x="240" y="16" font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)">AccountType</text><rect x="20" y="80" width="150" height="84" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="36" y="104" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><line x1="36" y1="112" x2="154" y2="112" stroke="var(--c-border)" stroke-width="1.5"/><text x="36" y="130" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">AC-1001</text><rect x="36" y="136" width="118" height="20" rx="3" fill="none" stroke="var(--c-border)" stroke-width="1.5"/><text x="44" y="150" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">type</text><circle cx="146" cy="146" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="146" y1="146" x2="234" y2="122"/><polyline points="223,120 234,122 226,129"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="240" y="26" width="190" height="54" rx="6"/><rect x="240" y="158" width="190" height="54" rx="6"/></g><rect x="240" y="92" width="190" height="54" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)"><text x="256" y="48">CHECKING</text><text x="256" y="114">SAVINGS</text><text x="256" y="180">TERM_DEPOSIT</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="256" y="68">Current account   0.0000</text><text x="256" y="200">Term deposit      0.0040</text></g><text x="256" y="134" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">Savings account   0.0025</text><text x="20" y="238" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Three objects, built once. The field holds an arrow to one of them.</text></svg>

## values, name and valueOf

Three things every enum can do for free.

```java
for (AccountType type : AccountType.values()) {
    System.out.println(type.name() + "  " + type.getLabel());
}
```

```text
CHECKING  Current account
SAVINGS  Savings account
TERM_DEPOSIT  Term deposit
```

`values()` gives you every constant, in the order you declared them. That is what builds the menu in the console:

```java
    private AccountType askForType() {
        AccountType[] types = AccountType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println("  " + (i + 1) + "  " + types[i].getLabel());
        }
```

The menu cannot drift out of step with the enum, because it is made from it. Add a fourth account type and the menu has four options with no further work. A list of strings typed out by hand in the console could be wrong the day somebody edits one file and not the other.

`valueOf` goes the other way, from text back to a constant, and it refuses anything it does not recognise:

```java
AccountType.valueOf("SAVINGS");   // the SAVINGS constant
AccountType.valueOf("CRYPTO");    // throws
```

```text
java.lang.IllegalArgumentException: No enum constant AccountType.CRYPTO
```

This is the only place a bad value can still get in, which is exactly where you want it: at the edge, where text from outside becomes something inside.

!!! mistake "Showing `name()` to a person"

    ```java
    System.out.println("Account type: " + type.name());
    ```

    ```text
    Account type: TERM_DEPOSIT
    ```

    `name()` returns the constant exactly as it is written in the source, shouting and with an underscore in it. It is for your code, not for a reader.

    That is why there is a `label` field. `name()` is what the programmer called it. `getLabel()` is what a person should see. Those are two jobs, and keeping them apart is yesterday's lesson at a smaller scale.

    The giveaway is that nobody can rename a constant without breaking whatever displays it. Once the label is a field, you can rename `TERM_DEPOSIT` to anything you like and the screen does not change.

## What the enum removed

```java
    public double applyMonthlyInterest() {
        double interest = balance * type.getMonthlyInterestRate();
        balance += interest;
        return interest;
    }
```

One line of arithmetic, and no mention of what kind of account this is.

Before the enum carried its own rate, this method had to decide: checking pays nothing, savings pays this, term deposits pay that. Three branches, in a method about interest, and the same three branches again anywhere else that needed a rate.

An interest rate is **data**. Data fits in a field. Put it on the type and the branches disappear. Add a fourth account type with its own rate and this method does not change at all.

The same thing happened to `toString`, which used to need a branch to know what to call itself and now just asks:

```java
    @Override
    public String toString() {
        return String.format("%s %s: %.2f EUR", type.getLabel(), accountNumber, balance);
    }
```

## What it could not remove

Now open `withdraw`.

```java
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A withdrawal must be positive: " + amount);
        }

        double lowestAllowed;
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

        if (balance - amount < lowestAllowed) {
            return false;
        }
        balance -= amount;
        return true;
    }
```

The switch is still here, and no field is going to remove it.

How much may be taken out is not a number. A current account stops at zero. A savings account has to keep a hundred in. A term deposit refuses outright and does not have a limit at all. Those are three different **rules**, not three different values, and a rule does not fit in a `double`.

Two things to notice in that method, because both of them are the point.

The `case` branches use an arrow rather than a colon. No `break`, and no falling through to the next case by accident, which is a bug you can write in C and cannot write here.

And then the `default`. Every constant of `AccountType` is listed above it. There is no fourth value and no way to make one. The branch can never run, and the compiler makes you write it anyway, because to the compiler `lowestAllowed` might otherwise never be given a value.

Sit with that for a second. The language is forcing you to write code that cannot execute, as the price of asking an object what kind of thing it is. That is not a flaw in enums. It is the switch telling you it is the wrong tool for this job.

Worse, look at what happens when a fourth account type is added. `applyMonthlyInterest` keeps working. `toString` keeps working. `withdraw` compiles without a murmur, takes the `default` branch, and refuses every withdrawal from the new account type. A silent bug, in the one method where money moves.

Leave it. That is next week.

??? note "Can a constant have behaviour of its own?"

    Yes, and Java will let each constant override a method with its own version. You will meet code that does this.

    It also tells you something. The moment the constants need different behaviour rather than different data, you are describing things that differ in what they *do*, and Java already has a tool for that. It is the one week 7 is about.

    An enum that has grown a method body per constant is usually a set of classes wearing a disguise.

> **If you remember one thing.** An enum is right when the only thing that differs is data. The moment behaviour differs, you are looking at classes.

## Check yourself

!!! verify "Add a fourth type and find out what breaks"

    Add this constant to `AccountType`, above `TERM_DEPOSIT` so the
    semicolon stays on the last one:

    ```java
    YOUTH_SAVINGS("Youth savings", 0.0030),
    ```

    Run `TypesApp`. The new type appears in the list, the interest is paid at its own rate, the console menu has four options. You changed one file and three things started working.

    Now open an account of that type and try to take money out of it.

    ```java
    Account youth = ada.openAccount(AccountType.YOUTH_SAVINGS, 1000.00);
    System.out.println(youth.withdraw(100.00));
    ```

    Write down what it printed, which file you forgot to open, and how you would have found out if `TypesApp` had not asked.

    Then say how many methods would need the same treatment if the bank had six account types and four rules each.

## Think about it

1.  `valueOf("CRYPTO")` throws rather than returning `null`. Decide whether that is the right choice, given that the text usually came from a person typing. Then suppose it came from a file written by last year's version of your own program.

2.  The label lives on the enum, which means the enum knows what language the program is in. That is the same objection you raised against `describe()` yesterday. Say whether it is a real problem here, then say what it would cost to fix it properly.

3.  `AccountType` is a fixed set because a bank decides what account types exist. Find something in your university system that looks like a fixed set and is not, and say how you would know the difference before you wrote the enum rather than after.

4.  The `default` branch in `withdraw` can never run. Suppose you delete it and give `lowestAllowed` a starting value of zero instead. The code compiles and the branch problem is gone. Say exactly what you have traded away, and which of the two versions you would rather inherit from somebody else.

## Next

Your model does not print, and your account types are checked by the compiler. One switch is left, it cannot be removed with anything you know, and it will quietly break the next time somebody adds an account type.

Next week that switch disappears, and the three kinds of account stop being a field and become three classes.

<div class="page-nav" markdown>
[Building on another class](inheritance.md){ .page-nav__next }
</div>
