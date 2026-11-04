# Promises a class must keep

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 8, session 1</span><span class="badge badge--accent">Needs: When inheritance hurts</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-08-abstraction">Lecture code</a></p>

By the end of this page you can stop a class from being built directly, declare a method that every subclass is forced to answer, and explain why that turns a bug somebody finds in production into an error you cannot get past.

Last session ended on a line that compiles and should not.

## The line that should never have worked

```java
accounts.add(new Account(AccountType.CHECKING, 1000.00));
```

What kind of account is that?

It has a number and a balance. It has no interest rate of its own, no minimum balance, no fee, and no rules about withdrawing beyond the ones every account shares. No bank opens one. It is not a current account, a savings account or a term deposit, and those are the only three kinds that exist.

`Account` was written so that three real classes could share code. Nothing stopped you building it directly by mistake, and in week 7 that line sat in the lecture code doing exactly that.

## One word

```java
public abstract class Account implements Comparable<Account>, HasBalance {
```

Ignore the two interfaces for now. They are the next session. The word that matters is `abstract`.

```text
Account is abstract; cannot be instantiated
```

Nothing else about the class changed. Same fields, same constructors, same methods. The only difference is that the compiler will no longer let anybody build one.

## An abstract class is still a class

This is the part people get wrong. `abstract` does not mean empty. It means **incomplete**.

```java
Account current = new CheckingAccount(1000.00);

System.out.println(current.getAccountNumber());
current.deposit(500.00);
System.out.println(current.withdraw(200.00));
System.out.println(current);
```

```text
AC-1002
true
Current account AC-1002: 1300.00 EUR
```

Fields, a constructor, and ordinary methods with real bodies, all inherited and all working. Exactly one method in `Account` has no body. Everything else is real code that three subclasses rely on and none of them wrote.

The variable is still declared as `Account`. That was never the problem. You can declare as many `Account` variables as you like, you just cannot make an `Account` to put in one.

## The constructors changed too

```java
    protected Account(AccountType type, double openingBalance) {
```

`protected` rather than `public`, which means only a subclass can call it.

That is not a restriction so much as an admission. Only a subclass was ever going to call it, through `super(...)` on the first line of its own constructor. Making it `public` advertised a door that leads nowhere.

## The method with no body

Here is the second half, and it is the more valuable one.

In week 7 this method had a default:

```java
    public double applyMonthlyInterest() {
        return 0.00;
    }
```

That looks careful. It is a lie waiting to happen. Write a new subclass, forget to override it, and the account pays no interest for as long as the program runs. Nothing complains, because returning zero is a perfectly legal thing for a method to do.

```java
    public abstract double applyMonthlyInterest();
```

No body. No braces. A semicolon.

Now try writing the subclass that forgets:

```java
public class GiftAccount extends Account {
    public GiftAccount(double opening) {
        super(AccountType.CHECKING, opening);
    }
}
```

```text
GiftAccount is not abstract and does not override
abstract method applyMonthlyInterest() in Account
```

The bug is not unlikely any more. It is impossible.

!!! mistake "Giving a parent a default it cannot actually know"

    The pattern is not only about returning zero. It is any time a parent answers a question only its children can answer.

    ```java
    public double applyMonthlyInterest() { return 0.00; }
    public String getCategory()          { return "unknown"; }
    public Customer getOwner()           { return null; }
    ```

    All three compile. All three look defensive. All three are the same mistake, which is a parent guessing on behalf of a class that has not been written yet.

    The giveaway is what happens when a subclass forgets. With a default, you get a program that runs and produces wrong numbers, discovered by a user. With `abstract`, you get an error with the class name in it, discovered by you.

    An abstract method is not the parent admitting it does not know. It is the parent refusing to guess.

<svg viewBox="0 0 520 290" role="img" xmlns="http://www.w3.org/2000/svg"><title>An abstract parent with a hole that each subclass fills</title><desc>At the top, an Account class is drawn with a dashed border and marked abstract. It holds the account number and the balance, and it has a dashed empty slot where applyMonthlyInterest would be. To its left, an attempt to construct an Account directly is struck through and labelled as not compiling. Below, three concrete subclasses each point up at the parent, and each one has the slot filled in with its own answer.</desc><text x="16" y="58" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">new Account(...)</text><line x1="14" y1="54" x2="112" y2="54" stroke="var(--c-accent)" stroke-width="1.5"/><text x="16" y="78" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">will not compile</text><rect x="150" y="20" width="220" height="86" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2" stroke-dasharray="6 4"/><text x="170" y="44" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><text x="296" y="44" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">abstract</text><line x1="170" y1="52" x2="350" y2="52" stroke="var(--c-border)" stroke-width="1.5"/><text x="170" y="70" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">accountNumber, balance</text><rect x="170" y="78" width="180" height="20" rx="3" fill="none" stroke="var(--c-accent)" stroke-width="1.5" stroke-dasharray="4 3"/><text x="176" y="92" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">applyMonthlyInterest()</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="94" y1="166" x2="196" y2="112"/><polyline points="190,109 197,112 191,120"/><line x1="260" y1="166" x2="260" y2="112"/><polyline points="255,119 260,111 265,119"/><line x1="426" y1="166" x2="324" y2="112"/><polyline points="335,112 324,112 330,121"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="170" width="156" height="66" rx="6"/><rect x="182" y="170" width="156" height="66" rx="6"/><rect x="348" y="170" width="156" height="66" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)"><text x="30" y="192">CheckingAccount</text><text x="196" y="192">SavingsAccount</text><text x="362" y="192">TermDepositAccount</text></g><g fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="1.5"><rect x="30" y="200" width="128" height="20" rx="3"/><rect x="196" y="200" width="128" height="20" rx="3"/><rect x="362" y="200" width="128" height="20" rx="3"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="38" y="214">nothing</text><text x="204" y="214">0.25% a month</text><text x="370" y="214">0.40% a month</text></g><text x="16" y="266" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The hole is the promise. Every class you can actually build has to fill it.</text></svg>

## What this cost

Almost nothing, and that is worth noticing.

Six of the nine files that existed last week did not change at all. `CheckingAccount`, `SavingsAccount` and `TermDepositAccount` were already overriding `applyMonthlyInterest`, so making it compulsory cost them nothing. `AccountType`, `BankConsole` and `ConsoleApp` never found out.

A class that was already doing the right thing does not notice when the right thing becomes compulsory. That is usually how you can tell a rule is a good one: adding it breaks the code that was already wrong and leaves everything else alone.

> **If you remember one thing.** Make a class abstract when you cannot say what one of them would be. Make a method abstract when the parent would only be guessing.

## Check yourself

!!! verify "Write the class that forgets, then put the bug back"

    Create `GiftAccount.java` with nothing but the constructor, exactly as it appears above. Read the error. Write down which file the compiler pointed at, and which file the mistake was really in.

    Now fix it by adding the method, and have it return `0.00` with a comment saying you will work out the rate later. It compiles and it runs.

    Then do the dangerous thing. Remove `abstract` from `applyMonthlyInterest` in `Account`, give it a body returning `0.00`, and delete your override from `GiftAccount` again.

    Everything still compiles and runs. Write one sentence saying what is now wrong with the program, and one more saying who is going to discover it and when.

## Think about it

1.  `Account` has one abstract method and five concrete ones. Say what would have to be true for it to have no concrete methods at all. Then say what you would use instead if that happened.

2.  The constructors are `protected`. A subclass can call them and nothing else can. Find the one situation where you might still want a `public` constructor on an abstract class, and say whether you can defend it.

3.  `withdraw` is concrete in `Account` and every subclass overrides it anyway. Argue that it should be abstract too. Then argue that it should be `final`. Both are defensible and they cannot both be right.

4.  Making `applyMonthlyInterest` abstract broke nothing, because every existing subclass already overrode it. Now suppose the bank has twelve account types written by three different teams. Describe how you would make the same change without breaking all of them on a Monday morning.

## Next

The parent can now refuse to guess. There is still one thing you have been writing since week 4 that nobody has explained, and it is in the first line of the class you just changed.

```java
public abstract class Account implements Comparable<Account>, HasBalance {
```

<div class="page-nav" markdown>
[Many classes, one call](interfaces.md){ .page-nav__next }
</div>
