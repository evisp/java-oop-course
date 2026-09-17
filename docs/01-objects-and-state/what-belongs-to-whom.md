# What belongs to whom

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 3, session 2</span><span class="badge badge--accent">Needs: Objects that refuse</span></p>

By the end of this page you can decide whether a value belongs to one object, to the class as a whole, or should never change at all, and you can write each of those in Java. Your accounts will also start giving themselves account numbers.

Two questions run through this session, and they are the same question asked twice. **Where does this value live?** And **when is it allowed to change?** Getting those right is most of what separates a class that survives contact with a real system from one that quietly rots.

## Some things are decided once

An account number is chosen when the account is opened and then never again. Change it and every record referring to that account is now pointing at nothing.

Right now nothing stops it. There is no `setAccountNumber`, which helps, but a year from now somebody adds one because they need it for an import script, and the damage happens in a place nobody is looking.

```java
    private final String accountNumber;
```

`final` on a field means it is assigned exactly once, in the constructor, and after that the compiler refuses. Not a convention, not a comment. Try to reassign it anywhere and the code does not build.

Use it for identity. An account number, a customer ID, a date of birth. Anything whose whole purpose is to stay the same is a candidate, and making it `final` costs one word and removes a category of bug permanently.

`ownerName` stays non-final, because people really do change their names and the bank really does have to record it.

## Objects that never change at all

Take that idea further. If every field of a class is `final` and no method changes anything, then the object cannot change after it is created. That is an **immutable** object.

You already use one constantly.

```java
String name = "ada lovelace";
name.toUpperCase();
System.out.println(name);      // ada lovelace, unchanged
```

Everybody gets caught by this once. `toUpperCase` did not change the string, because a `String` cannot be changed. It made a new one and handed it back, and you threw it away.

```java
name = name.toUpperCase();     // keep the new one
```

That is not a quirk. It is why you can hand a `String` to any method in the world without worrying about what it does to your copy. Nothing can do anything to it.

The same reasoning applies to anything that records a fact rather than tracking a state. A transaction record should be impossible to edit after it is written, which is exactly what an audit trail means. You will build one in Module 3.

??? note "What a fully immutable class looks like"

    ```java
    public final class Branch {

        private final String code;
        private final String city;

        public Branch(String code, String city) {
            this.code = code;
            this.city = city;
        }

        public String getCode() {
            return code;
        }

        public String getCity() {
            return city;
        }
    }
    ```

    Every field `final`, no setters, nothing that changes anything. Hand this to any code you like. Nobody can alter it, so nobody has to check.

    The class itself is `final` too, which stops anyone extending it and adding mutable state through the back door. That word means something you meet in week 7; for now it is a detail worth seeing rather than understanding.

## Some things belong to the class

Now the other question. Every account has its own balance, its own number, its own owner. Three accounts, three balances.

But the bank needs unique account numbers, and the counter that produces them belongs to no single account. It belongs to all of them, which is to say it belongs to the class.

```java
public class Account {

    private static int lastNumberIssued = 1000;

    private final String accountNumber;
    private String ownerName;
    private double balance;

    public Account(String ownerName, double openingBalance) {
        lastNumberIssued++;
        this.accountNumber = "AC-" + lastNumberIssued;

        this.ownerName = requireText(ownerName, "Owner name");

        if (openingBalance < 0) {
            throw new IllegalArgumentException(
                    "Opening balance cannot be negative: " + openingBalance);
        }
        this.balance = openingBalance;
    }
```

`static` means there is exactly one `lastNumberIssued`, no matter how many accounts exist. Not one per account. One, full stop.

The constructor no longer takes an account number, because the account now issues its own. That is a real improvement: nobody outside can pick a number, so nobody outside can pick one that is already taken.

<svg viewBox="0 0 480 290" role="img" xmlns="http://www.w3.org/2000/svg"><title>Instance fields compared with a static field</title><desc>Three Account objects each hold their own balance and account number. A separate box outside all of them holds a single static counter named lastNumberIssued, and each object has a line to that one shared box rather than holding its own copy.</desc><text x="20" y="24" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">One per object</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="36" width="188" height="62" rx="8"/><rect x="20" y="110" width="188" height="62" rx="8"/><rect x="20" y="184" width="188" height="62" rx="8"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)"><text x="38" y="58">Account</text><text x="38" y="132">Account</text><text x="38" y="206">Account</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)"><text x="38" y="78">AC-1001</text><text x="38" y="152">AC-1002</text><text x="38" y="226">AC-1003</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)"><text x="120" y="78">500.00</text><text x="120" y="152">2500.00</text><text x="120" y="226">1500.00</text></g><text x="256" y="24" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">One for the whole class</text><g stroke="var(--c-border)" stroke-width="1.5" fill="none"><path d="M208 67 C 240 67 250 128 288 128"/><path d="M208 141 C 240 141 250 134 288 134"/><path d="M208 215 C 240 215 250 140 288 140"/></g><rect x="294" y="98" width="166" height="72" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2" stroke-dasharray="6 5"/><text x="312" y="122" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)">static</text><text x="312" y="142" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)">lastNumberIssued</text><text x="312" y="160" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-accent)">1003</text><text x="20" y="278" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Three balances. One counter. Every account reads and updates the same one.</text></svg>

A static method belongs to the class too, and can be called without any object.

```java
    public static int getAccountsOpened() {
        return lastNumberIssued - 1000;
    }
```

```java
Account ada  = new Account("Ada Lovelace", 500.00);
Account alan = new Account("Alan Turing", 2500.00);

System.out.println(Account.getAccountsOpened());   // 2
```

Called on the class, not on an object, because the answer is not about any particular account.

A static method cannot touch instance fields or use `this`, and once you see why, it stops feeling like a rule. There is no object involved, so there is no balance to read. Which account's balance would it be?

Remember `requireText` from last session. It reads no fields and writes no fields; it just checks a string. It should be `static`.

```java
    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " is required");
        }
        return value;
    }
```

This also explains something that has been annoying you since first year. `main` is `static`, so it runs before any object exists, which is why you cannot call an ordinary method from it without making an object first.

!!! mistake "The static counter will bite you in week 11"

    `lastNumberIssued` lives in memory. Close the program and it is gone. Start it again and it goes back to 1000.

    So in week 11, when you save your accounts to a file and load them back, the next account you open will be given a number that already belongs to somebody. Two accounts, one number, and every lookup after that returns whichever one it happened to find first.

    This is not a mistake in the code above. It is the right way to learn `static` and it is genuinely how many small programs work. But real systems get their identifiers from something that survives a restart, usually a database. Remember this paragraph when week 11 arrives.

!!! mistake "Static is not a way to avoid creating objects"

    The first thing most people do with `static` is discover it makes the compiler stop complaining in `main`, and then mark everything static until the errors go away.

    What you get is a program with one copy of everything and no objects at all. That is the pile of shared data from week 1, rebuilt on purpose, with a class name in front of it.

    Before writing `static`, finish this sentence: this value belongs to the class because ____. If you cannot, it belongs to an object.

> **If you remember one thing.** For every value, ask where it lives and when it may change. One per object, one for the whole class, or never again after the moment it was set.

## Check yourself

!!! verify "Prove what is shared and what is not"

    Create three accounts without giving them numbers.

    ```java
    Account ada   = new Account("Ada Lovelace", 500.00);
    Account alan  = new Account("Alan Turing", 2500.00);
    Account grace = new Account("Grace Hopper", 1500.00);

    System.out.println(ada);
    System.out.println(alan);
    System.out.println(grace);
    System.out.println(Account.getAccountsOpened());
    ```

    You should get three different account numbers and a count of 3. If all three accounts have the same number, `lastNumberIssued` is not being incremented before it is used.

    Now deposit into `alan` only and print all three. One balance moves. The counter is shared, the balances are not, and seeing both facts in the same run is the point.

    Finally, add this line and read the compiler error:

    ```java
    ada.accountNumber = "AC-9999";
    ```

    You will get two complaints, not one, and they are for different reasons. Work out what both of them are. That pair of errors is this whole session in one screen.

## Think about it

1.  The account number is `final`, so it can never change. But a bank might genuinely need to renumber accounts after a merger with another bank. Does `final` make that impossible, or just make it obvious? Describe what you would actually have to do.

2.  `lastNumberIssued` is shared by every account ever created, which is convenient and also means every account is coupled to every other one. Name something that could go wrong because of that sharing, other than the restart problem already described.

3.  A `String` cannot be changed, so every operation on it makes a new one. That sounds wasteful. Why would the designers of Java accept that cost? Think about who receives strings and what they would otherwise have to check.

4.  You now have three places a value can live: in an object, in the class, or nowhere because it is computed when asked. A customer's total holdings could be any of those three. Argue for one, then find the situation that breaks your choice.

<div class="page-nav" markdown>
[Week 4, session 1. Many objects](many-objects.md){ .page-nav__next }
</div>
