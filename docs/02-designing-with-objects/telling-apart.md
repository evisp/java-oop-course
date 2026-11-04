# Telling objects apart

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 5, session 2</span><span class="badge badge--accent">Needs: Objects working together</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-05-objects-together">Lecture code</a></p>

By the end of this page you can decide what makes two of your own objects the same object, write `equals` and `hashCode` so that the collections in the library agree with your decision, and spot the one mistake that makes `equals` compile and never run.

You have been answering this question by hand since week 4 without noticing.

## What you have been doing by hand

Here is `findAccount`, from last week and from the week before:

```java
    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
```

Look at the comparison. You decided that an account number identifies an account, and you wrote that decision into a loop. Then `Bank.findAccount` needed the same decision, and `closeAccount` needed it, and every search you write from now on will need it again.

A decision that gets repeated in every loop is a decision that belongs in the class.

## Two objects, one account

Before you can see why this matters, you need two objects that are the same account. Account numbers are issued by a counter, so you cannot normally produce two.

So `Account` gets a second constructor:

```java
    /*
     * The other way. Use this when the account already has a number, which
     * happens when it comes back from somewhere outside the program. It does
     * not touch the counter, because that number was issued once already.
     */
    public Account(String accountNumber, double balance) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("An account number cannot be blank.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException(
                    "An account cannot hold a negative balance: " + balance);
        }
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
```

This is not a trick for a demonstration. In week 11 your accounts get saved to a file and read back, and the ones that come back already have numbers. The constructor exists because of that. It is useful today because it is the only way to get into this situation:

```java
Account account  = ada.openAccount(500.00);
Account restored = new Account(account.getAccountNumber(), 500.00);

System.out.println(account == restored);
```

```text
false
```

Week 2 told you why. `==` compares the arrows, and there are two of them pointing at two different objects. That answer is correct and it is not the answer you wanted.

<svg viewBox="0 0 480 280" role="img" xmlns="http://www.w3.org/2000/svg"><title>Two variables pointing at two separate objects that are the same account</title><desc>On the left, two variables named account and restored each hold an arrow. The arrows go to two separate Account objects, drawn one above the other, which both hold the number AC-1001 and the balance five hundred. Between the two objects, one label says that the double equals operator reports false and another says that equals reports true. Two objects, one account.</desc><text x="20" y="34" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">Your variables</text><text x="240" y="20" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">Two objects</text><rect x="20" y="54" width="118" height="40" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="20" y="174" width="118" height="40" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="34" y="79">account</text><text x="34" y="199">restored</text></g><circle cx="130" cy="74" r="4" fill="var(--c-accent)"/><circle cx="130" cy="194" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="130" y1="74" x2="234" y2="62"/><polyline points="227,58 235,62 227,67"/><line x1="130" y1="194" x2="234" y2="206"/><polyline points="227,202 235,206 227,211"/></g><g fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"><rect x="240" y="30" width="200" height="64" rx="8"/><rect x="240" y="174" width="200" height="64" rx="8"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)"><text x="256" y="54">Account</text><text x="256" y="198">Account</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="256" y="78">AC-1001</text><text x="256" y="222">AC-1001</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)"><text x="368" y="78">500.00</text><text x="368" y="222">500.00</text></g><line x1="340" y1="94" x2="340" y2="174" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="4 4"/><rect x="200" y="118" width="116" height="28" rx="4" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="1.5"/><text x="212" y="136" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">== says false</text><rect x="356" y="118" width="120" height="28" rx="4" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="1.5"/><text x="366" y="136" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">equals says true</text><text x="20" y="266" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Same number, so the same account. Two objects, so not the same arrow.</text></svg>

## Equality is a decision, not a fact

Java cannot work out what "the same account" means. Only you can, because it depends on the thing you are modelling.

For an account, the number says who it is. Two accounts are the same account when the numbers match, and nothing else comes into it. In particular the balance does not: an account with 300 euros in it this morning and 500 in it this afternoon is the same account, not a different one.

Write that down and you have written `equals`.

```java
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Account)) {
            return false;
        }
        Account that = (Account) other;
        return accountNumber.equals(that.accountNumber);
    }
```

Four decisions, in order.

`this == other` is a shortcut. If it is literally the same object there is nothing to compare, so say yes and stop.

`other instanceof Account` asks whether the thing you were handed is even an account. The parameter is `Object`, so somebody can pass you a `String`, and `null` fails this test too, which saves you writing a separate check for it.

The cast is then safe, because `instanceof` just proved it.

The last line is the decision. One field, because one field is what identifies an account.

!!! mistake "Writing `equals(Account other)`"

    This compiles:

    ```java
    public boolean equals(Account other) {
        return accountNumber.equals(other.accountNumber);
    }
    ```

    It is not `equals`. It is a different method that happens to share the name, because `Object.equals` takes an `Object` and this one takes an `Account`.

    Your own code calls it, so it looks like it works. The library never does, because the library only knows about the `Object` version. So `contains`, `indexOf` and `remove` all quietly go back to comparing arrows, and you get a bug with no error message anywhere.

    `@Override` is what stops this. It tells the compiler to check that you really are replacing a method from above. Get the signature wrong and you get an error on the spot instead of a mystery three weeks later.

## hashCode has to agree

```java
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
```

The contract is one sentence: **two objects that are equal must return the same hash code.**

Here is why it is not optional. Some collections find things fast by using the hash code to decide where to put an object, and then using it again to decide where to go looking. If `equals` says two objects are the same but their hash codes differ, the object gets filed in one drawer and searched for in another. It is in the collection and the collection cannot find it.

You have not met those collections yet. They are `HashMap` and `HashSet`, you cover them properly in Data Structures, and they are everywhere in real Java. Which makes this the honest warning: this is one of the hardest bugs to find in the language, because the method that is wrong is not the method that fails. You change `equals`, everything works, and six weeks later a lookup returns nothing.

`Objects.hash` takes any number of fields and combines them properly. The rule that keeps you safe forever:

> Build the hash from exactly the fields `equals` compares. No more, no fewer.

## What it buys you

```java
List<Account> accounts = ada.getAccounts();

System.out.println(accounts.contains(restored));
System.out.println(accounts.indexOf(restored));
```

```text
true
0
```

Before `equals`, those printed `false` and `-1`. The list could only ask "is this the same object", and the answer was no.

And the line from the last session now makes sense:

```java
        if (from.equals(to)) {
            return false;
        }
```

`Bank.transfer` is asking whether the two account numbers it was given turned out to name one account. Without `equals` it would have to pull the numbers out and compare the strings itself, which is the hand-written decision all over again, in a third class.

## Customer makes the same decision

```java
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Customer)) {
            return false;
        }
        Customer that = (Customer) other;
        return customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
```

The id, not the name. Two different people can be called Ada Lovelace, and one person can change their name:

```java
Customer ada = bank.addCustomer("Ada Lovelace");
ada.setName("Ada Byron");

System.out.println(ada.equals(bank.findCustomer(ada.getCustomerId())));
```

```text
true
```

She is still the same customer. The field you compare is the field that says who something is, which is almost never the field a person would read.

??? note "`instanceof` or `getClass`, and why people argue about it"

    You will see this written the other way:

    ```java
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    ```

    The difference only shows up with inheritance, which arrives in week 7. Once `SavingsAccount` extends `Account`, `instanceof` says a savings account and a plain account with the same number are equal, and `getClass` says they are not.

    Which is right depends on what you meant. If the number identifies the account no matter what kind it is, `instanceof` is correct. If two different kinds of account can never be the same thing, `getClass` is.

    This course uses `instanceof`, because an account number here identifies one account at one bank and nothing else. It is a real argument with no single answer, and anyone who tells you otherwise has only met one half of it.

> **If you remember one thing.** `==` asks whether two names point at one object. `equals` asks whether two objects count as the same thing, and only you can decide what counts.

## Check yourself

!!! verify "Make equals wrong in two different ways"

    First, add the balance to the comparison:

    ```java
    return accountNumber.equals(that.accountNumber)
            && balance == that.balance;
    ```

    Then run this:

    ```java
    Account account  = ada.openAccount(500.00);
    Account restored = new Account(account.getAccountNumber(), 500.00);

    System.out.println(ada.getAccounts().contains(restored));
    restored.deposit(100.00);
    System.out.println(ada.getAccounts().contains(restored));
    ```

    The same two objects, naming the same account, and the answer changed because somebody paid money in. Write one sentence saying what is actually wrong with that, beyond it being inconvenient.

    Now put `equals` back and delete `hashCode` entirely. Everything still works. Explain why nothing broke, and why that makes it the dangerous case rather than the safe one.

## Think about it

1.  You decided the account number identifies an account because the bank issues it and it never changes. Find something in your university system that has no such field. Say what you would compare instead, then say what happens when two of those things genuinely have identical values.

2.  `equals` ignores the balance, so two objects that disagree about how much money exists count as the same account. Defend that choice. Then suppose one of them was read from last night's backup and the other is live, and the balances differ. You now have two objects that are equal and one of them is wrong. Whose problem is that?

3.  `Customer.equals` goes by id, so a customer who changes their name stays the same customer. Now suppose the bank discovers that `C-1001` and `C-1340` are the same person who registered twice. What does `equals` say, what should it say, and what would it cost to make it say that?

4.  `hashCode` is built from the account number, which can never change. Say what goes wrong if you build it from the balance instead, and be specific about when the damage happens. Then consider a field that only changes once a year, and decide whether "rarely" is good enough.

## Next

Week 5 gave the bank somewhere to put a rule, and gave your classes a way to say what counts as the same thing. Both of those made the program bigger.

Next week you look at what that growth has done to `main`, which by now reads input, does arithmetic and prints results all in the same place. Open your own Sprint 1 code before the lecture. You will recognise the problem.

<div class="page-nav" markdown>
[Back to the module](index.md){ .page-nav__next }
</div>
