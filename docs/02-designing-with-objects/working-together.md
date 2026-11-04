# Objects working together

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 5, session 1</span><span class="badge badge--accent">Needs: Objects that own other objects</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-05-objects-together">Lecture code</a></p>

By the end of this page you can look at a rule that no existing class can enforce and work out which class should own it, write a class whose reason to exist is a job rather than a noun, and put the steps of an operation in an order that cannot leave the system wrong halfway through.

Module 1 ended on a question. Ada wants to send 200 euros to Alan. Everything you built last week was correct and none of it can do this.

## The two lines that look obvious

Open the editor and try it the way it looks like it should work.

```java
Customer ada  = new Customer("Ada Lovelace");
Customer alan = new Customer("Alan Turing");

Account from = ada.openAccount(500.00);
Account to   = alan.openAccount(2500.00);

// Ada has 500. She tries to send 800.
from.withdraw(800.00);
to.deposit(800.00);
```

```text
Before: 3000.00 in the two accounts
Account AC-1001: 500.00 EUR
Account AC-1002: 3300.00 EUR
After:  3800.00 in the two accounts
```

Eight hundred euros appeared out of nothing.

Now look for the class that did something wrong. `withdraw` refused, returned `false` and changed nothing, exactly as week 3 said it would. `deposit` accepted a positive amount and added it, exactly as it should. Both objects behaved perfectly.

!!! mistake "Throwing away what the method told you"

    `withdraw` returns `boolean` for a reason. Written as a statement on its own line, that answer is discarded and the compiler says nothing, because ignoring a return value is legal.

    ```java
    from.withdraw(800.00);   // compiles, and tells you nothing
    ```

    This is the week 3 lesson arriving with consequences. A method that returns an outcome is handing you a decision. If you do not read it, you have decided to carry on regardless.

The rule that broke is this one: **money that leaves one place has to arrive somewhere.** It is not written down anywhere in your program. Nobody owns it, so nobody enforces it.

## Where could that rule live?

Three candidates, and it is worth rejecting each one properly rather than jumping to the answer.

**In `Account`.** A method like `from.transferTo(to, 200.00)` would need the account to hold a reference to another account. An account would then know about accounts that are not its own, which is a much bigger change than it looks: every account becomes a potential route to every other one.

**In `Customer`.** Ada's `Customer` object would have to reach Alan's account. It has no way to get there and no business having one. Week 4 was spent making sure a customer's accounts cannot be reached from outside, and this would be the first thing to break that.

**In `main`.** That is where it is now. `main` cannot be called from anywhere else, cannot be tested, and will be thrown away in week 13 when a window replaces it. A rule kept in `main` is a rule kept nowhere.

So the answer is that there is no good home for it. That is the finding, not the failure.

> When a rule has no sensible owner, that is not a problem with your classes. It is a class you have not written yet.

## A class that exists for a job

```java
import java.util.ArrayList;
import java.util.List;

public class Bank {

    private final String name;
    private final List<Customer> customers = new ArrayList<>();

    public Bank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A bank cannot have a blank name.");
        }
        this.name = name.trim();
    }
```

A bank is also a thing you could point at, which makes this an easy first example. Do not take the wrong lesson from it. `Bank` is not here because the word "bank" appears in the problem. It is here because a job needed somewhere to live, and that is the test you will use from now on when the noun does not exist.

Everything else about the class is week 4 again, one level up.

```java
    public Customer addCustomer(String customerName) {
        Customer customer = new Customer(customerName);
        customers.add(customer);
        return customer;
    }

    public List<Customer> getCustomers() {
        return List.copyOf(customers);
    }
```

A private final list, created where it is declared. A method that makes the thing rather than accepting one, so the bank can never hold somebody else's customer. A copy handed out rather than the list itself. You have written all of this before, for a customer and its accounts. Nothing new is happening, and that is the point: the pattern is the same at every level.

<svg viewBox="0 0 520 300" role="img" xmlns="http://www.w3.org/2000/svg"><title>A bank owning customers that own accounts</title><desc>A Bank object holds a name and a private customers field. That field points at a list whose two slots point out to two separate Customer objects. Each customer holds its own accounts field, pointing out to one Account object. The same shape repeats at every level: something owns a private list, and the list holds arrows to objects that live outside it.</desc><rect x="20" y="108" width="140" height="82" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="36" y="132" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Bank</text><line x1="36" y1="140" x2="144" y2="140" stroke="var(--c-border)" stroke-width="1.5"/><text x="36" y="158" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)">Bank of Tirana</text><rect x="36" y="164" width="108" height="20" rx="3" fill="none" stroke="var(--c-border)" stroke-width="1.5"/><text x="44" y="178" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-accent)">customers</text><circle cx="136" cy="174" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="136" y1="174" x2="190" y2="154"/><polyline points="183,151 191,154 185,160"/></g><rect x="196" y="118" width="64" height="64" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="208" y="134" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">List</text><line x1="208" y1="140" x2="248" y2="140" stroke="var(--c-border)" stroke-width="1.5"/><g fill="none" stroke="var(--c-border)" stroke-width="1.5"><rect x="206" y="148" width="36" height="14" rx="3"/><rect x="206" y="166" width="36" height="14" rx="3"/></g><circle cx="236" cy="155" r="4" fill="var(--c-accent)"/><circle cx="236" cy="173" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="236" y1="155" x2="294" y2="80"/><polyline points="284,85 294,80 292,91"/><line x1="236" y1="173" x2="294" y2="218"/><polyline points="287,215 295,219 288,224"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="300" y="34" width="100" height="80" rx="6"/><rect x="300" y="184" width="100" height="80" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="12" font-weight="700" fill="var(--c-ink)"><text x="312" y="54">Customer</text><text x="312" y="204">Customer</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="312" y="72">C-1001 Ada</text><text x="312" y="222">C-1002 Alan</text></g><g fill="none" stroke="var(--c-border)" stroke-width="1.5"><rect x="312" y="80" width="76" height="18" rx="3"/><rect x="312" y="230" width="76" height="18" rx="3"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="318" y="93">accounts</text><text x="318" y="243">accounts</text></g><circle cx="382" cy="89" r="4" fill="var(--c-accent)"/><circle cx="382" cy="239" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="382" y1="89" x2="420" y2="89"/><polyline points="413,84 421,89 413,94"/><line x1="382" y1="239" x2="420" y2="239"/><polyline points="413,234 421,239 413,244"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="424" y="66" width="76" height="46" rx="6"/><rect x="424" y="216" width="76" height="46" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)"><text x="432" y="86">AC-1001</text><text x="432" y="236">AC-1002</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="432" y="102">300.00</text><text x="432" y="252">2700.00</text></g><text x="20" y="288" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The same shape at every level. Something owns a private list, and the list holds arrows.</text></svg>

## Finding an account without reaching in

A transfer is given two account numbers, so the bank has to be able to find an account. It does not own any.

```java
    public Account findAccount(String accountNumber) {
        for (Customer customer : customers) {
            Account account = customer.findAccount(accountNumber);
            if (account != null) {
                return account;
            }
        }
        return null;
    }
```

The bank asks each customer and the customer searches its own list. Compare that with the version you will be tempted to write:

```java
    // Do not write this
    public Account findAccount(String accountNumber) {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }
```

This one works today. It also means `Bank` now knows how a customer stores its accounts, and knows how an account number is compared. Change either of those and two classes need editing instead of one.

The first version is **ask, do not take** again, from the very first session of the course, now operating across three classes.

## The order of the checks is the method

```java
    public boolean transfer(String fromNumber, String toNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A transfer must be positive: " + amount);
        }

        Account from = findAccount(fromNumber);
        Account to = findAccount(toNumber);

        if (from == null || to == null) {
            return false;
        }
        if (from.equals(to)) {
            return false;
        }
        if (!from.withdraw(amount)) {
            return false;
        }
        to.deposit(amount);
        return true;
    }
```

Read it as four stages, and notice that at the end of each one the system is still correct.

1. A negative or zero amount is a bug in the calling code, so it throws. Nothing has moved.
2. An account that does not exist is an ordinary thing to ask for, so it returns `false`. Nothing has moved.
3. The withdrawal is attempted **first**. If there is not enough money it returns `false`, and again nothing has moved.
4. Only now does the deposit happen. It cannot fail, because the amount was already checked in stage 1.

That last sentence is the whole design. The second half is safe only because the first stage made it impossible for it to fail.

!!! mistake "Depositing before withdrawing"

    ```java
    to.deposit(amount);
    return from.withdraw(amount);
    ```

    Two lines, in the wrong order, and the bank hands out money that was never taken from anywhere. It returns `false` so the caller even thinks nothing happened.

    Writing the risky step first is a habit worth building now. Do the thing that can fail while failing still costs nothing.

??? note "What about a transfer that really must be all or nothing?"

    This method is safe because stage 4 cannot fail. That is a property of this particular pair of operations, not a general solution.

    As soon as the second half can fail for its own reasons, ordering is not enough, and you need a way to undo work that has already happened. Java has tools for that. They need exceptions, which arrive in Module 3, and in a real bank they need the database underneath to help. Be aware that the problem is bigger than the fix you have here.

## The total that never moves

```java
    public double getTotalDeposits() {
        double total = 0;
        for (Customer customer : customers) {
            total += customer.getTotalBalance();
        }
        return total;
    }
```

Calculated, never stored, for the same reason as `getTotalBalance` last week. Now run it around the transfer:

```java
Bank bank = new Bank("Bank of Tirana");

Customer ada  = bank.addCustomer("Ada Lovelace");
Customer alan = bank.addCustomer("Alan Turing");

Account from = ada.openAccount(500.00);
Account to   = alan.openAccount(2500.00);

System.out.printf("Total on deposit: %.2f%n", bank.getTotalDeposits());

System.out.println("Sending 800: " + bank.transfer(
        from.getAccountNumber(), to.getAccountNumber(), 800.00));
System.out.printf("Total on deposit: %.2f%n", bank.getTotalDeposits());

System.out.println("Sending 200: " + bank.transfer(
        from.getAccountNumber(), to.getAccountNumber(), 200.00));
System.out.println(from);
System.out.println(to);
System.out.printf("Total on deposit: %.2f%n", bank.getTotalDeposits());
```

```text
Total on deposit: 3000.00
Sending 800: false
Total on deposit: 3000.00
Sending 200: true
Account AC-1001: 300.00 EUR
Account AC-1002: 2700.00 EUR
Total on deposit: 3000.00
```

Money moved between two accounts and the total never changed. That is not a coincidence you got away with. It is the rule being kept, and now you can watch it being kept.

> **If you remember one thing.** A class can exist because a job needs a home. When you cannot find anywhere sensible to put a rule, stop trying to fit it into a class you already have.

## Check yourself

!!! verify "Break it on purpose, then put it back"

    Open `Bank.transfer` and move `to.deposit(amount)` above the withdrawal, so the deposit runs first. Then run the code above exactly as it is.

    Write down what happens to the total after the failed 800 transfer, and what `transfer` returned. One of those two numbers is lying to the caller. Say which.

    Put it back, then add this method to `Bank`:

    ```java
    public boolean transferWithin(Customer customer, String fromNumber,
                                  String toNumber, double amount)
    ```

    It moves money between two accounts belonging to the same customer. Write it, then answer in one sentence: is `Bank` still the right class for this, or does it belong on `Customer`? There is a defensible answer either way, and the reason matters more than the choice.

## Think about it

1.  `transfer` returns `false` when the account does not exist and `false` when there is not enough money. Those are completely different situations for whoever called it. Decide whether that matters here. Then suppose a regulator asks the bank for a monthly report of declined transfers grouped by reason, and say what has to change.

2.  `findAccount` asks every customer in turn. With the twelve customers in your test code that is fine. Now say what it costs with two million customers, and where you would put the thing that makes it fast. Does that new thing belong to the bank, or is it a class of its own?

3.  `Bank` was easy to justify because a bank is a real thing you could visit. Find a rule in your university system that no existing class can own, and name the class you would invent for it. Then do it again without using the words `Manager`, `Handler`, `Service` or `Util`, and notice whether the better name changes what the class does.

4.  `transfer` takes two account numbers rather than two `Account` objects. Argue for both versions. Then suppose the caller is a web page that only ever has the numbers a user typed, and separately suppose the caller is a nightly job that already holds the objects. Can one method serve both, and should it?

## Next

You now have three classes that work together, and one of them had to ask whether two accounts were the same one:

```java
if (from.equals(to)) {
    return false;
}
```

That line works, and you have not been told why. The next session is about what `equals` actually means on a class you wrote yourself.

<div class="page-nav" markdown>
[Telling objects apart](telling-apart.md){ .page-nav__next }
</div>
