# Many classes, one call

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 8, session 2</span><span class="badge badge--accent">Needs: Promises a class must keep</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-08-abstraction">Lecture code</a></p>

By the end of this page you can explain the declaration you have been writing since week 4, give one of your classes a natural sort order, and write an interface so that classes with nothing in common can be handled by the same code.

This is the last lecture of Module 2, and it pays two debts from week 4.

## The declaration from week 4

```java
List<Account> accounts = new ArrayList<>();
```

You were told to write it this way round and told the reason would come in week 8.

`List` is an **interface**. There is no code in it at all. It is a list of method names: `add`, `get`, `size`, `remove`, `contains`, and the rest. It says what a list must be able to do and says nothing about how.

`ArrayList` is one class that provides them. `LinkedList` is another, which stores things in a completely different way for completely different reasons.

```java
List<Account> accounts = new ArrayList<>();
accounts.add(new SavingsAccount(500.00));
accounts.add(new CheckingAccount(1500.00));
System.out.println(accounts.size());

List<Account> same = new LinkedList<>();
same.add(new SavingsAccount(500.00));
same.add(new CheckingAccount(1500.00));
System.out.println(same.size());
```

```text
2
2
```

The right-hand side changed completely and not one other line moved. That is what the declaration buys you: the left side says what you need, the right side says who is doing it today, and today can change.

Write `ArrayList` on the left instead and the program still runs. It also now insists on an `ArrayList` for ever, in this method and in every method you pass the list to.

## The sort rule from week 4

Week 4 showed you one line of sorting and said the rule came later.

```java
public abstract class Account implements Comparable<Account> {
```

`Comparable` is another interface, with one method in it. Implement it and your class carries its own idea of order.

```java
    @Override
    public int compareTo(Account other) {
        int byBalance = Double.compare(balance, other.balance);
        if (byBalance != 0) {
            return byBalance;
        }
        return accountNumber.compareTo(other.accountNumber);
    }
```

Return a negative number if this one comes first, zero if they tie, a positive number if the other one does. `Double.compare` does that and gets the awkward cases right, which is why nobody should write `balance - other.balance` by hand.

The second line is the part people leave out. Sorting by balance alone means two different accounts holding the same money tie, and a tie says "these are the same" to anything that sorts. They are not the same: `equals` settled that in week 5 and it goes by the number. So the number breaks the tie, and `compareTo` and `equals` now agree with each other.

```java
Collections.sort(accounts);
```

```text
Current account AC-1006: 200.00 EUR
Term deposit AC-1007: 900.00 EUR
Savings account AC-1005: 1500.00 EUR
```

No rule passed in. The class brought its own.

And the version week 4 actually showed you, where the rule is supplied at the call instead:

```java
accounts.sort(Comparator.comparing(Account::getAccountNumber).reversed());
```

One class, one natural order, built in. Any number of other orders, supplied by whoever needs them. Both are now explained.

## Writing one of your own

Three classes in this program can tell you a name and an amount of money: an `Account`, a `Customer`, and the `Bank` itself.

They have nothing else in common. A `Customer` is not a kind of `Account`. A `Bank` is not a kind of `Customer`. There is no honest parent for the three of them, and a class only gets one parent anyway, which `Account` has already spent.

```java
public interface HasBalance {

    String getLabel();

    double getBalance();
}
```

That is the whole file. Two method names, no bodies, no fields, no constructor. An interface holds no state, because it is not a thing, it is a question you can ask things.

Any class that says `implements HasBalance` has to supply both methods, and the compiler checks.

```java
public abstract class Account implements Comparable<Account>, HasBalance {

    @Override
    public String getLabel() {
        return type.getLabel() + " " + accountNumber;
    }
```

`getBalance` was already there and already the right shape, so `Account` paid one method for the privilege. That happens a lot. An interface usually describes something your class could nearly do already.

```java
public class Customer implements HasBalance {

    @Override
    public String getLabel() {
        return String.format("Customer %s (%s)", customerId, name);
    }

    @Override
    public double getBalance() {
        return getTotalBalance();
    }
```

`getTotalBalance` was already there and is the better name, because it says the number is added up rather than held. The interface fixes the name it wants, so `getBalance` calls the one that does the work. There is still exactly one piece of arithmetic in the class.

That is normal and worth saying out loud. An interface is a shared vocabulary, and sometimes your own word for something is clearer than the shared one. Keep both and have one call the other.

`Bank` does the same thing in two lines.

<svg viewBox="0 0 520 260" role="img" xmlns="http://www.w3.org/2000/svg"><title>Three unrelated classes answering the same two questions</title><desc>On the left, three boxes named Bank, Customer and Account are stacked with no lines joining them, because they share no parent class. Each carries a small tag reading HasBalance. Three arrows lead from them into a single box on the right, which is one loop over a list of HasBalance, asking each item for its label and its balance.</desc><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="24" width="180" height="50" rx="6"/><rect x="16" y="100" width="180" height="50" rx="6"/><rect x="16" y="176" width="180" height="50" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)"><text x="32" y="55">Bank</text><text x="32" y="131">Customer</text><text x="32" y="207">Account</text></g><g font-family="JetBrains Mono, monospace" font-size="9" fill="var(--c-accent)"><text x="110" y="55">HasBalance</text><text x="110" y="131">HasBalance</text><text x="110" y="207">HasBalance</text></g><text x="16" y="248" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">No lines between them, because there is no parent they could share.</text><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="200" y1="50" x2="294" y2="112"/><polyline points="283,111 294,112 288,102"/><line x1="200" y1="125" x2="294" y2="127"/><polyline points="287,122 295,127 287,132"/><line x1="200" y1="200" x2="294" y2="142"/><polyline points="283,143 294,142 288,152"/></g><rect x="300" y="92" width="204" height="70" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="314" y="116" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)">for (HasBalance item : all)</text><line x1="314" y1="124" x2="490" y2="124" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="314" y="142">item.getLabel()</text><text x="314" y="156">item.getBalance()</text></g></svg>

## One list, three classes

```java
List<HasBalance> everything = new ArrayList<>();
everything.add(bank);
for (Customer customer : bank.getCustomers()) {
    everything.add(customer);
    everything.addAll(customer.getAccounts());
}

for (HasBalance item : everything) {
    System.out.printf("%-34s %10.2f%n", item.getLabel(), item.getBalance());
}
```

```text
Bank of Tirana                        7500.00
Customer C-1001 (Ada Lovelace)        2500.00
Current account AC-1008                500.00
Savings account AC-1009               2000.00
Customer C-1002 (Alan Turing)         5000.00
Term deposit AC-1010                  5000.00
```

A bank, two customers and three accounts, in one list, printed by one loop. The loop asks two questions and has no idea what it is holding.

This is the thing an abstract class could not have done. Inheritance would have required all six objects to descend from one parent, and no such parent exists.

!!! mistake "Inventing a parent so two classes can share a method"

    ```java
    public abstract class BankThing {
        public abstract String getLabel();
        public abstract double getBalance();
    }

    public class Bank extends BankThing { ... }
    public class Customer extends BankThing { ... }
    ```

    It compiles. It produces the same output. It is still wrong, for two reasons.

    It is a lie about what those classes are. `BankThing` is not a concept in banking, it is a concept in your frustration, and the name gives it away. Any class you cannot name without inventing a word is usually an interface you have not written yet.

    And it spends the one parent each class gets. `Account` has already used its one, so it cannot extend `BankThing` at all, and the whole idea collapses on the third class.

## Which to reach for

`Account` is an abstract class because the three kinds of account share real code. A number, a balance, `deposit`, `equals`, and the arithmetic inside `withdraw`. Sharing that code is the job.

`HasBalance` is an interface because `Account`, `Customer` and `Bank` share no code at all. Not one line. They share one question that each answers in its own way.

> Shared code, one family. Use an abstract class.
>
> Shared question, no family. Use an interface.

??? note "Interfaces with code in them"

    You will meet interfaces whose methods have bodies, marked `default`. They have been legal since Java 8 and they are not a contradiction of this page, but they are worth knowing why they exist.

    When a new method is added to an interface, every class that implements it stops compiling until it supplies the method. For `List`, that means every `List` implementation in the world. `default` methods were added so the library could grow without breaking everyone.

    They are a tool for evolving an interface you have already published. They are not a reason to start putting logic in interfaces, and in your own code you will rarely need one.

## Where Module 2 started and where it ended

Open `week-06-one-job-per-class/src/Account.java` and look at `withdraw`. Then look at `Bank.applyMonthlyInterest` today.

```java
// week 6, and this shape again in every method that cared
switch (type) {
    case CHECKING -> lowestAllowed = 0.00;
    case SAVINGS -> lowestAllowed = 100.00;
    case TERM_DEPOSIT -> { return false; }
    default -> { return false; }     // can never run, required anyway
}

// week 8
for (Customer customer : customers) {
    paid += customer.applyMonthlyInterest();
}
```

Adding a fourth kind of account used to mean finding every switch and hoping you found them all. It now means writing one file, and the compiler telling you the one other place to touch.

> **If you remember one thing.** A class can only have one parent and as many interfaces as it likes. When you find yourself inventing a parent so two classes can share a method, you wanted an interface.

## Check yourself

!!! verify "Take one away and see where it breaks"

    Delete `implements HasBalance` from `Bank`, and leave the two methods where they are.

    It will not compile. Write down which file the error is in, and why it is not in `Bank`.

    Now put it back and do the opposite. Add a field to `HasBalance`:

    ```java
    double minimumBalance = 100.00;
    ```

    Read what the compiler says about that, then work out what Java actually did with it, because the error you get may not be the one you expected. Say in one sentence why an interface cannot hold state the way a class can.

    Finally, make `Customer` implement `Comparable<Customer>` so a list of customers sorts by total balance. Then say what your tiebreaker should be and why, without looking back at `Account`.

## Think about it

1.  `Account` implements two interfaces and extends nothing. `Customer` implements one. Find the limit: write down what you would do if a class genuinely needed the code from two different parents, and say what it costs.

2.  `HasBalance` has two methods. Argue that it should have one, `getBalance`, and that the label belongs somewhere else. Then argue that it should have three, with a currency. There is a real principle underneath about how small an interface should be.

3.  `Customer.getBalance` just calls `getTotalBalance`. Say whether the second method is worth keeping now that the first exists. Then suppose a colleague deletes one of them, and say which one they would delete and what breaks.

4.  `compareTo` and `equals` were deliberately made to agree. Find a class where you would want them to disagree, and describe exactly what would go wrong when somebody sorted a list of them.

## Module 2 is done

You can now look at a job and decide which class should own it, write a class whose reason to exist is a responsibility rather than a noun, say what makes two of your objects the same object, keep a model that never talks to a screen, build one class on another and say when you should not, and write one loop that works on several kinds of object without asking what it has.

**Sprint 2 opens this week.** You take the application you shipped for Sprint 1, which you now know enough to be unhappy with, and redesign it with everything in these four weeks. That is the point of running it this way round. Nobody believes an argument about design until they have felt the cost of a weak one in their own code.

Module 3 starts with two things in this code that have been quietly wrong since week 5.

```java
Account found = bank.findAccount("AC-9999");   // null
bank.transfer(from, to, 800.00);               // false
```

The first one hands the caller a `null` and hopes they check. The second returns `false` for an account that does not exist and `false` for not enough money, which are completely different problems wearing the same answer. You have had three modules' worth of tools and none of them is right.

<div class="page-nav" markdown>
[Back to the roadmap](../roadmap.md){ .page-nav__next }
</div>
