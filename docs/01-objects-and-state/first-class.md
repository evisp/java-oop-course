# Your first class

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 2, session 1</span><span class="badge badge--accent">Needs: Why objects</span></p>

By the end of this page you can write a Java class with fields, a constructor and methods, create objects from it, and print them in a readable way. This is the first code of the bank we will build for the rest of the semester.

Last week you learned to see a problem as things that know something and can be asked to do something. This week you write one of those things down. The vocabulary is small: what it knows becomes **fields**, what it does becomes **methods**, and the moment it comes into existence is the **constructor**.

## One description, many things

A class is not an object. This trips people up for about a week, so it is worth being precise.

The class is the description. It says an account has a number, an owner and a balance, and that you can ask it to take a deposit. It holds no actual number, no actual owner, no actual money. You cannot put fifty euro into the class.

An object is one real account, with real values in it. You can make three, and they share the description while each holds its own state.

<svg viewBox="0 0 480 250" role="img" xmlns="http://www.w3.org/2000/svg"><title>One class describing three objects</title><desc>On the left a form named Account lists three empty fields: account number, owner name and balance. On the right three filled copies of that form each hold different values, showing that many objects share one description while each holds its own state.</desc><text x="20" y="28" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">The class, one description</text><rect x="20" y="40" width="170" height="170" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2" stroke-dasharray="6 5"/><text x="38" y="66" font-family="Source Serif 4, Georgia, serif" font-size="16" font-weight="700" fill="var(--c-ink)">Account</text><line x1="38" y1="76" x2="172" y2="76" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)"><text x="38" y="100">accountNumber</text><text x="38" y="138">ownerName</text><text x="38" y="176">balance</text></g><g fill="none" stroke="var(--c-border)" stroke-width="1.5"><rect x="38" y="108" width="134" height="16" rx="3"/><rect x="38" y="146" width="134" height="16" rx="3"/><rect x="38" y="184" width="134" height="16" rx="3"/></g><text x="264" y="28" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">The objects, each with its own state</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="196" y1="125" x2="256" y2="125"/><polyline points="249,120 257,125 249,130"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="264" y="40" width="196" height="52" rx="6"/><rect x="264" y="99" width="196" height="52" rx="6"/></g><rect x="264" y="158" width="196" height="52" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)"><text x="280" y="60">AC-1001  Ada Lovelace</text><text x="280" y="119">AC-1002  Alan Turing</text><text x="280" y="178">AC-1003  Grace Hopper</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)"><text x="280" y="80">500.00</text><text x="280" y="139">2500.00</text><text x="280" y="198">1500.00</text></g><text x="20" y="238" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">One description. Three things that exist. The description holds no money.</text></svg>

## Fields: what it knows

Start the file. In Eclipse, right-click `src`, then **New**, then **Class**, and name it `Account`.

```java
public class Account {

    private String accountNumber;
    private String ownerName;
    private double balance;
}
```

Three fields, and every one is `private`. That is not a habit to pick up later. It is the wall from last week, and it is the reason anything else on this page is worth doing. Outside this file, `account.balance -= 5000` does not compile.

`double` for money is fine for this course and wrong for a real bank. Rounding errors accumulate. The real answer is `BigDecimal`, which is slower and noisier to read, and there is no reason to fight it in week 2. Now you know.

## The constructor: the moment it exists

Right now nothing can create an `Account` with anything in it. The constructor fixes that.

```java
    public Account(String accountNumber, String ownerName, double openingBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }
```

Read the shape carefully, because it is unlike any method you have written. It has the same name as the class. It has no return type, not even `void`. It runs once, when the object is created, and never again.

`this.accountNumber` means the field belonging to this object. `accountNumber` on its own means the parameter, because the parameter is nearer. Without `this`, the line `accountNumber = accountNumber` assigns the parameter to itself and the field stays empty. The compiler will not warn you. You will find it when the account prints a blank owner.

Now `Account` can exist:

```java
Account ada = new Account("AC-1001", "Ada Lovelace", 500.00);
```

`new` is doing something specific here, and next session is entirely about what.

!!! mistake "Do not add an empty constructor as well"

    Most tutorials show two constructors: a full one and an empty `Account()` that leaves everything blank.

    Think about what that second one produces. An account with no number, no owner and no money, which is not an account. It is a half-built thing that some later line is supposed to remember to finish, and sooner or later a line forgets.

    A constructor exists to guarantee the object is usable the moment it exists. Ask for what you need. Extra constructors are worth writing when there is a real second way to be born, not by reflex.

## Methods: what it does

Now the behaviour. An account can accept money and can be asked to hand some back.

```java
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
```

Both can say no. That is the whole point from last week, now written down: the account holds the balance, so the account decides.

Notice neither of them prints anything. They return what happened and let whoever asked decide what to say about it. Keep doing this, for two reasons. A method that prints can only ever be used by a program with a console, and in week 13 yours will have a window instead. And a method that prints cannot be tested, which you will discover in week 10.

!!! mistake "A domain class does not talk to the user"

    You will see plenty of course material where `withdraw` prints "Insufficient funds". It is tempting because it makes the demo look finished.

    `Account` does not know whether it is being used by a console program, a web page or a test. Reporting is the caller's job. `Account` reports to the caller.

The getters:

```java
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }
```

Three getters and no setters. That is deliberate. There is no `setBalance`, because setting the balance to whatever you like is exactly the power `private` just took away, and handing it straight back through a method would be theatre. The balance changes through `deposit` and `withdraw`, or it does not change.

## toString: seeing your object

Print an object without this and Java gives you `Account@6d06d69c`, which is useless.

```java
    @Override
    public String toString() {
        return String.format("Account %s (%s): %.2f EUR",
                accountNumber, ownerName, balance);
    }
```

`@Override` tells the compiler you meant to replace a method that already exists. If you misspell `toString`, the compiler catches it instead of silently adding a method nobody calls.

`%.2f` gives you two decimal places, which is what money looks like. Use `EUR` rather than the euro symbol: the Windows console mangles it, and a mangled currency in week 2 is a distraction you do not need.

`toString` is for you. It is the fastest debugging tool in Java and it costs four lines.

## The whole thing, running

```java
public class BankApp {

    public static void main(String[] args) {
        Account ada = new Account("AC-1001", "Ada Lovelace", 500.00);
        System.out.println(ada);

        if (ada.withdraw(200.00)) {
            System.out.println("Withdrawal accepted.");
        } else {
            System.out.println("Not enough money.");
        }

        if (ada.withdraw(900.00)) {
            System.out.println("Withdrawal accepted.");
        } else {
            System.out.println("Not enough money.");
        }

        System.out.println(ada);
    }
}
```

```text
Account AC-1001 (Ada Lovelace): 500.00 EUR
Withdrawal accepted.
Not enough money.
Account AC-1001 (Ada Lovelace): 300.00 EUR
```

Look at where the sentences are. Every word the user reads was written in `main`. `Account` never says anything. It just answers.

!!! note "What week 3 will change"

    Two things on this page are deliberately unfinished.

    The constructor accepts anything you hand it, including a negative opening balance and a null owner. Next week it learns to refuse.

    And `deposit` returns `false` for a negative amount, which lumps together two different situations: a customer who asked for too much, and a programmer who passed nonsense. Week 3 separates them.

> **If you remember one thing.** A class describes. An object exists. The constructor is the moment one turns into the other, and nothing should exist in a state you would not want to use.

## Check yourself

!!! verify "Make three, and prove they are separate"

    Add two more accounts in `main`, for Alan Turing and Grace Hopper, with different opening balances.

    1.  Print all three.
    2.  Withdraw 100 from Alan's account only.
    3.  Print all three again.

    Ada and Grace must be unchanged. If all three balances moved, you have accidentally made the field `static`, and that word means something you have not met yet.

    Then try to break it. Add this line in `main`:

    ```java
    ada.balance = 1000000.00;
    ```

    It will not compile. Read the error message carefully and write down, in your own words, what the compiler is refusing to do. That refusal is the entire reason the class is written this way, and it is worth seeing it happen once on purpose.

## Think about it

1.  The class holds no money and yet it decides what an account is allowed to do. So what is a class actually for? Now imagine the bank opens a second branch with different withdrawal rules. Does the class help you or get in the way?

2.  There is no `setBalance` on this class. Name a situation where somebody would genuinely need to set a balance directly. Then decide whether you would add the method, or add something else instead, and say why.

3.  `withdraw` gives back `true` or `false`. `toString` gives back a `String`. `deposit` gives back `true` or `false` even when nothing could sensibly go wrong from the caller's point of view. Is that consistency worth it, or is it noise? Argue it either way, then say what you would do if the bank added twenty more operations.

4.  You were told a domain class should not print. But `toString` is clearly about displaying something. Is that a contradiction? Work out what the real difference is between `toString` and a method that calls `System.out.println`.

<div class="page-nav" markdown>
[Session 2. What a variable really holds](references.md){ .page-nav__next }
</div>
