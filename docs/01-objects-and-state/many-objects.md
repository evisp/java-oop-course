# Many objects

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 4, session 1</span><span class="badge badge--accent">Needs: What belongs to whom</span></p>

By the end of this page you can hold a growing number of objects in a `List`, add to it, search it, remove from it, and work through it. You will also be able to say exactly what a list of accounts contains, which is not what most people assume.

One account is not a bank. A real system holds a number of objects that nobody knew in advance, and the tool for that in Java is the collection. Getting the mental picture right now saves you from a specific family of bugs in week 11.

## Arrays run out first

You already know arrays, so the problem is quick to state.

```java
Account[] accounts = new Account[3];
accounts[0] = new Account("Ada Lovelace", 500.00);
accounts[1] = new Account("Alan Turing", 2500.00);
```

Three slots, decided forever at the moment of creation. Open a fourth account and there is nowhere to put it. And `accounts[2]` right now is `null`, so anything that loops over this array and calls a method will fall over on the last element.

Removing is worse. There is no way to make an array shorter, so you either leave a hole or shift everything down by hand, which is the parallel-array problem from week 1 coming back for another go.

## A list grows

```java
import java.util.ArrayList;
import java.util.List;

List<Account> accounts = new ArrayList<>();

accounts.add(new Account("Ada Lovelace", 500.00));
accounts.add(new Account("Alan Turing", 2500.00));
accounts.add(new Account("Grace Hopper", 1500.00));

System.out.println(accounts.size());        // 3
System.out.println(accounts.get(0));        // Account AC-1001: 500.00 EUR
```

No size decided up front, no empty slots, no shifting.

`List<Account>` says this list holds accounts and nothing else. Put a `String` in and the compiler stops you, which is worth more than it sounds: the mistake is caught while you type rather than at 2am in week 11.

The declaration is worth a second look, because it is deliberately mismatched.

```java
List<Account> accounts = new ArrayList<>();
```

The **variable** is a `List`, which is the promise: something you can add to, look through, and count. The **object** is an `ArrayList`, which is one particular way of keeping that promise.

Write it this way round from the start. The rest of your program only ever needs the promise, so if you later swap `ArrayList` for a different kind of list, one line changes instead of forty. Week 8 explains why Java lets you do this at all; for now it is a habit worth having early.

## What the list actually holds

Here is the picture, and it is the same picture from week 2 with one more hop.

<svg viewBox="0 0 480 280" role="img" xmlns="http://www.w3.org/2000/svg"><title>A list variable, the list object, and the accounts it refers to</title><desc>A variable named accounts holds an arrow to an ArrayList object. The ArrayList has three numbered slots, and each slot holds an arrow pointing out to a separate Account object. The accounts themselves sit outside the list.</desc><rect x="20" y="110" width="110" height="40" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="34" y="135" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)">accounts</text><circle cx="120" cy="130" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="120" y1="130" x2="164" y2="130"/><polyline points="157,125 165,130 157,135"/></g><rect x="170" y="70" width="112" height="130" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="186" y="94" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">ArrayList</text><line x1="186" y1="102" x2="266" y2="102" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)"><text x="188" y="126">0</text><text x="188" y="154">1</text><text x="188" y="182">2</text></g><g fill="none" stroke="var(--c-border)" stroke-width="1.5"><rect x="202" y="114" width="62" height="16" rx="3"/><rect x="202" y="142" width="62" height="16" rx="3"/><rect x="202" y="170" width="62" height="16" rx="3"/></g><circle cx="256" cy="122" r="4" fill="var(--c-accent)"/><circle cx="256" cy="150" r="4" fill="var(--c-accent)"/><circle cx="256" cy="178" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="256" y1="122" x2="308" y2="68"/><polyline points="301,70 309,67 306,75"/><line x1="256" y1="150" x2="308" y2="136"/><polyline points="301,132 309,136 302,141"/><line x1="256" y1="178" x2="308" y2="204"/><polyline points="301,199 309,204 300,207"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="314" y="44" width="146" height="48" rx="6"/><rect x="314" y="112" width="146" height="48" rx="6"/><rect x="314" y="180" width="146" height="48" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)"><text x="330" y="64">AC-1001</text><text x="330" y="132">AC-1002</text><text x="330" y="200">AC-1003</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)"><text x="330" y="82">500.00</text><text x="330" y="150">2500.00</text><text x="330" y="218">1500.00</text></g><text x="20" y="266" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The accounts are not inside the list. The list holds the way to reach them.</text></svg>

The accounts are not in the list. The list holds arrows, and the accounts sit outside it, exactly where they were when `new` created them.

!!! mistake "A list of objects is a list of arrows"

    This matters immediately.

    ```java
    Account ada = new Account("Ada Lovelace", 500.00);
    accounts.add(ada);

    ada.deposit(1000.00);

    System.out.println(accounts.get(0).getBalance());   // 1500.00
    ```

    `add` did not copy anything. The list and the variable `ada` both point at the same account, so a deposit through either one is visible through the other.

    This is convenient more often than it is a problem. It stops being convenient in week 11, when you write the same object to a file twice and wonder why you have one account instead of two.

## Working through them

The for-each loop is the normal way to visit every element.

```java
for (Account account : accounts) {
    System.out.println(account);
}
```

Read it as "for each account in accounts". No index, no off-by-one, no way to run off the end.

The loop variable is another arrow to the same object, so this really does change the accounts:

```java
for (Account account : accounts) {
    account.deposit(10.00);      // every account gains 10
}
```

But this does not:

```java
for (Account account : accounts) {
    account = new Account("Nobody", 0.00);   // changes nothing
}
```

The loop variable gets pointed somewhere else and the list is untouched. Same rule as the method parameter in week 2: you can change the object, you cannot change which object the list is holding.

Use an ordinary indexed loop when you need the position:

```java
for (int i = 0; i < accounts.size(); i++) {
    System.out.println(i + ": " + accounts.get(i));
}
```

## Searching and removing

```java
Account first = accounts.get(0);

accounts.remove(0);              // by position
accounts.remove(first);          // by object
System.out.println(accounts.contains(first));
System.out.println(accounts.isEmpty());
```

`remove(int)` and `remove(Object)` are different methods that happen to share a name, which is a design wart in Java worth knowing about. With a `List<Account>` it is unambiguous. With a `List<Integer>` it is a genuine trap.

More importantly, `remove(Object)` and `contains` do not compare account numbers. They ask each element whether it equals the one you passed, and without any instruction from you, "equals" still means "is the same object" from week 2. Two accounts with identical data are not the same account as far as these methods are concerned.

That is often what you want. When it is not, you teach Java what sameness means for your class, which is week 5.

So for now, search by writing the loop yourself:

```java
public static Account findByNumber(List<Account> accounts, String number) {
    for (Account account : accounts) {
        if (account.getAccountNumber().equals(number)) {
            return account;
        }
    }
    return null;
}
```

`.equals` on the strings, never `==`. And `null` when nothing matched, which is honest but puts the burden on the caller. Week 9 offers something better.

!!! mistake "Do not remove from a list while looping over it"

    ```java
    for (Account account : accounts) {
        if (account.getBalance() == 0) {
            accounts.remove(account);      // ConcurrentModificationException
        }
    }
    ```

    The loop is walking the list while you rearrange it underneath. Java notices and throws.

    The simplest fix for now is to collect first and remove afterwards:

    ```java
    List<Account> empty = new ArrayList<>();
    for (Account account : accounts) {
        if (account.getBalance() == 0) {
            empty.add(account);
        }
    }
    accounts.removeAll(empty);
    ```

    Week 12 gives you a one-line version of this.

## A list that cannot change

```java
List<String> accountTypes = List.of("CHECKING", "SAVINGS", "DEPOSIT");
```

`List.of` makes a fixed list. Call `add` on it and it throws. That is the immutability idea from last session applied to a collection: a list of things that were decided once should not be a list anyone can extend.

Use it for fixed sets of values. Use `new ArrayList<>()` for anything that grows.

> **If you remember one thing.** A list of objects is a list of arrows. The objects live outside it, and adding one to a list does not copy it.

## Check yourself

!!! verify "Prove the list holds arrows"

    ```java
    List<Account> accounts = new ArrayList<>();

    Account ada = new Account("Ada Lovelace", 500.00);
    accounts.add(ada);
    accounts.add(new Account("Alan Turing", 2500.00));

    ada.deposit(1000.00);

    for (Account account : accounts) {
        System.out.println(account);
    }
    ```

    Before running, write down what the first line of output will be. If you expected 500.00, look at the diagram again before reading the actual answer.

    Then add a second account object with the same opening balance as one already in the list, and check `accounts.contains(...)` with it. Predict the answer first. Then say in one sentence which week 2 idea explains it.

## Think about it

1.  An array's size is fixed and a list's is not, which makes the list sound strictly better. Arrays are still everywhere in Java. Find a reason somebody would choose one, beyond "it was already there".

2.  You wrote `List<Account> accounts = new ArrayList<>()` rather than `ArrayList<Account> accounts`. Nothing in your program behaves differently today. What exactly did you buy, and when would you find out whether it was worth it?

3.  `contains` compares identity, so an account you just rebuilt from a file will not be found even with the same number. Whose problem is that: the list's, the account's, or the caller's? Say what you would change and what it would cost.

4.  `findByNumber` returns `null` when nothing matches. Every caller now has to remember to check. Suggest a different thing it could return, and say what makes your suggestion better or worse than `null`.

<div class="page-nav" markdown>
[Session 2. Objects that own other objects](owning-objects.md){ .page-nav__next }
</div>
