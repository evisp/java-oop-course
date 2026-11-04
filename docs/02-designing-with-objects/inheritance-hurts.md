# When inheritance hurts

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 7, session 2</span><span class="badge badge--accent">Needs: Building on another class</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-07-inheritance">Lecture code</a></p>

By the end of this page you can say what a subclass is allowed to change, recognise the subclass that compiles and still breaks every caller, and choose between extending a class and simply holding one.

Everything in the last session argued for inheritance. This one is about the price, and the whole lesson is sitting in one method of `TermDepositAccount`.

## A method written before your class existed

```java
    private static double emptyAccount(Account account) {
        String before = account.toString();
        double available = account.getBalance();

        if (available <= 0) {
            System.out.printf("%-38s nothing in it%n", before);
            return 0.00;
        }

        if (account.withdraw(available)) {
            System.out.printf("%-38s took %.2f%n", before, available);
            return available;
        }

        System.out.printf("%-38s would not give it up%n", before);
        return 0.00;
    }
```

This is the method that matters, so read what is not in it.

No `try` and no `catch`. No check for what kind of object it was handed. No mention of current accounts, savings accounts or term deposits anywhere.

All three of those absences are deliberate. The method takes an `Account` and was written against what `Account` promises: `getBalance` tells you what is in there, and `withdraw` either takes the money and returns `true`, or takes nothing and returns `false`. Nothing else was agreed, and nothing else is assumed.

Run it over a mixed list and it does what it should:

```text
Current account AC-1001: 500.00 EUR    took 500.00
Savings account AC-1002: 500.00 EUR    would not give it up
Term deposit AC-1003: 500.00 EUR       would not give it up

Collected 500.00 EUR in total.
```

Three kinds of account, one method, no crash. Nobody will have to come back and update it when a fourth kind is added.

## The version you will be tempted to write

Here is `TermDepositAccount.withdraw` as it stands:

```java
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A withdrawal must be positive: " + amount);
        }
        return false;
    }
```

Always `false`. A boring answer.

So you write the better one:

```java
    @Override
    public boolean withdraw(double amount) {
        throw new UnsupportedOperationException(
                "A term deposit cannot be withdrawn from before maturity.");
    }
```

Take the case for this seriously, because it is a good one. A term deposit genuinely cannot be withdrawn from. Returning a quiet `false` looks like "not enough money" and a careless caller will treat it that way. An exception says what is actually true, loudly, and cannot be ignored.

Make that change and run the same program again. Nothing in `SubstitutionApp` is touched.

```text
Current account AC-1001: 500.00 EUR    took 500.00
Savings account AC-1002: 500.00 EUR    would not give it up
Exception in thread "main" java.lang.UnsupportedOperationException:
A term deposit cannot be withdrawn from before maturity.
	at TermDepositAccount.withdraw(TermDepositAccount.java:56)
	at SubstitutionApp.emptyAccount(SubstitutionApp.java:64)
	at SubstitutionApp.main(SubstitutionApp.java:30)
```

Read the stack trace from the bottom. The crash landed in `emptyAccount`, a method with no `try`, no `catch` and no type check, which was correct this morning and is broken this afternoon because a class it has never heard of appeared somewhere else in the program.

<svg viewBox="0 0 520 280" role="img" xmlns="http://www.w3.org/2000/svg"><title>A caller written against Account, and three subclasses answering it</title><desc>On the left a box represents a method that takes an Account and contains no try, no catch and no type check. An arrow labelled withdraw goes to three boxes on the right. The current account answers true and the savings account answers false, and both are marked as handled. The term deposit is shown twice: version one answers false and is handled, version two throws an exception, and an arrow carries that exception back to the caller, marked as the place where the program stops.</desc><rect x="16" y="66" width="168" height="110" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="32" y="92" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)">emptyAccount(Account)</text><line x1="32" y1="100" x2="168" y2="100" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="32" y="120">no try</text><text x="32" y="138">no catch</text><text x="32" y="156">no type check</text></g><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="188" y1="96" x2="276" y2="52"/><polyline points="268,50 277,51 272,59"/><line x1="188" y1="108" x2="276" y2="110"/><polyline points="269,105 277,110 269,115"/><line x1="188" y1="124" x2="276" y2="176"/><polyline points="268,170 277,177 268,181"/></g><text x="196" y="88" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">withdraw</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="282" y="30" width="222" height="44" rx="6"/><rect x="282" y="88" width="222" height="44" rx="6"/><rect x="282" y="146" width="222" height="44" rx="6"/></g><rect x="282" y="204" width="222" height="44" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2" stroke-dasharray="5 4"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)"><text x="296" y="50">CheckingAccount</text><text x="296" y="108">SavingsAccount</text><text x="296" y="166">TermDepositAccount v1</text><text x="296" y="224">TermDepositAccount v2</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="296" y="66">true, handled</text><text x="296" y="124">false, handled</text><text x="296" y="182">false, handled</text></g><text x="296" y="240" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">throws, nobody is catching</text><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-dasharray="5 4"><path d="M282 226 C 220 226 180 216 100 190"/></g><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><polyline points="108,186 99,189 105,196"/></g><text x="140" y="228" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">lands here</text><text x="16" y="270" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The damage is never in the subclass. It is in code that cannot see the subclass.</text></svg>

## The test, in one line

> Anywhere an `Account` works, every subclass of `Account` has to work.

Version 1 passes. It returns `false`, which is one of the two answers `Account` said `withdraw` could give. It is a stricter account, not a differently behaved one, and every caller ever written keeps working.

Version 2 fails. It gives an answer that was never on the list.

Now think about what it would take to defend `emptyAccount` against version 2. You would wrap the call in a `try`, or you would ask the account what kind it is before calling. Both of those put knowledge of the subclasses back into code that had successfully forgotten them, which is the switch you spent the last session removing. One bad subclass undoes the whole week.

!!! mistake "Narrowing what a method promises"

    The rule has a general shape, and it is worth more than this one example. A subclass may do **more** than its parent promised. It may never do **less**.

    These all break it:

    - Throwing where the parent returned a value, as above.
    - Refusing inputs the parent accepted. If `Account.deposit` takes any positive amount and your subclass rejects anything over a thousand, a caller holding an `Account` has no way to know.
    - Returning `null` where the parent never did.
    - Needing something to be called first. If your `withdraw` only works after `activate()` has run, every caller now has to know that, and they do not.

    The giveaway is a sentence that starts "yes, but you have to". If using your subclass through its parent's type requires a warning, it is not a subclass.

## This is not an argument about the word "is"

Every course teaches the "is a" test. A savings account **is an** account, so inheritance. A customer **has** accounts, so no inheritance.

It works often enough to be worth knowing and it is not the real test, because a term deposit is also an account. In English it passes easily. It is the thing a bank would put in the same sentence as a current account, in the same part of the brochure, on the same page of the website.

What it cannot do is keep the promise. The test is about behaviour, not vocabulary:

> Can anything that was written for the parent go on working if it is handed your class, without being told?

Answer that before you type `extends`. English will tell you what something is called. Only the promises tell you what it can replace.

## Holding one instead of extending one

When a class fails that test, you are not stuck. Hold the other object instead of becoming it.

```java
// Not this
public class AccountHistory extends Account { ... }

// This
public class AccountHistory {

    private final Account account;

    public AccountHistory(Account account) {
        this.account = account;
    }
}
```

`AccountHistory` has an account. It is not one. Nothing written for `Account` will ever be handed an `AccountHistory`, so there is no promise to break, and the history can do whatever it likes.

You have been doing this since week 4 without a name for it.

```java
public class Customer {
    private final List<Account> accounts = new ArrayList<>();

public class Bank {
    private final List<Customer> customers = new ArrayList<>();
```

Neither of those extends anything. `Customer` holds accounts and `Bank` holds customers, and the whole module so far has been built out of classes holding other classes. Inheritance is the exception in your program, not the rule, and that is the normal proportion.

## Which to reach for

Use inheritance when both of these are true. The subclass keeps every promise the parent made, and there is real shared code worth not writing twice. `SavingsAccount` qualifies: it returns the same answers `withdraw` always could, and it inherits a number, a balance, `deposit`, `equals` and `toString` it did not have to write.

Use composition the rest of the time, which is most of the time. It costs you one field and one line in a constructor, and in exchange your class is never handed to code that expected something else.

Inheritance ties two classes together for as long as the program lives. Composition can be undone on a Tuesday afternoon.

> **If you remember one thing.** A subclass is a promise that your class can stand in for its parent anywhere, without warning anybody. If you cannot make that promise, hold the object instead of extending it.

## Check yourself

!!! verify "Break it, then decide what you would have done instead"

    Open `TermDepositAccount`. Comment out version 1 of `withdraw`, uncomment version 2, and run `SubstitutionApp` without changing a line of it.

    Then answer four things in writing.

    1.  Which file did you edit, and which file crashed?
    2.  `emptyAccount` is now wrong. Say what it would have to do to become right again, and what that costs the next person who adds an account type.
    3.  The bank genuinely does want a loud failure when somebody tries to empty a term deposit. Describe a way to get one that does not break `emptyAccount`. There is more than one answer.
    4.  Put version 1 back. Now say whether `TermDepositAccount` should have been a subclass of `Account` at all, and what the alternative would look like.

## Think about it

1.  `SavingsAccount.withdraw` refuses when the balance would drop below a hundred. That is also doing less than `Account.withdraw` would have done. Explain why it passes the test and version 2 of the term deposit does not, in one sentence that does not use the word "exception".

2.  `Account.deposit` is `final`, so no subclass can narrow it. Say which other methods you would make `final` if the bank were going to let other teams write their own account types. Then say what you lose by doing that.

3.  The test on this page is about what a caller may assume. None of those assumptions are written down anywhere in the code, only in comments and in this page. Describe where you would put them so that they could be checked. You are describing something from week 10.

4.  Find something in your university system that you modelled with inheritance, or were about to. Apply the test to it honestly. Then do the same for something you modelled by holding an object, and say whether it should have been inheritance after all.

## Next

The three subclasses work, the loop does not ask what it is holding, and one problem is left over from the last session that nobody has mentioned.

```java
accounts.add(new Account(AccountType.CHECKING, 1000.00));
```

That line compiles. What kind of account is it?

<div class="page-nav" markdown>
[Promises a class must keep](abstract-classes.md){ .page-nav__next }
</div>
