# Object Collaboration: Responsibility and Delegation

*When a rule needs more than one object*

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 5, session 1</span><span class="badge badge--accent">Needs: Objects that own other objects</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-05-objects-together">Lecture code</a></p>

**What you will learn**

- How to recognise a rule that involves more than one object
- How to decide which class should be responsible for it
- How objects delegate work to each other instead of reaching into each other's data

## The problem

Think about a football transfer. Ardi plays for Riverside FC, and Hillside FC wants to sign him.

Each club keeps its own squad list, and each club is careful with it. A club will not register the same player twice. A club will not release a player it does not have, and it can refuse to release one whose contract has not ended. Every one of those rules is about one club and its own list, and each club can enforce them by itself.

The transfer takes two steps. Riverside releases Ardi, and Hillside registers him.

Now suppose someone does it in the other order. Hillside registers Ardi first. Then Riverside is asked to release him and refuses, because his contract runs until the summer. Riverside was entitled to say no. Hillside did nothing wrong either; it registered a player, which is what registering is.

And yet Ardi is now on two squads at once, and could in principle play for both.

Which club made the mistake? Neither of them. The rule that broke, **a player is registered with exactly one club**, does not belong to Riverside or to Hillside. It is a rule about the two of them together. So who is supposed to enforce it?

## Building the idea

Every rule you have written so far in this course has belonged to one object. An account refuses a negative deposit. A customer refuses a blank name. Each object guards its own data, and because that data is private, nobody else can break its rules.

The transfer rule is a different kind of rule. To check it, you have to be able to see both clubs at the same time, and neither club can. Riverside has no idea what is on Hillside's list, and that is how it should be.

So ask the question directly: in this picture, who can see both clubs?

<svg viewBox="0 0 520 260" role="img" xmlns="http://www.w3.org/2000/svg"><title>A rule between two clubs, first with no owner and then owned by the federation</title><desc>Two panels. On the left, two club boxes named Riverside FC and Hillside FC each hold a short squad list and their own rules. Above them floats a dashed box reading: a player is registered with exactly one club, followed by a question mark, because neither club owns that rule. On the right, the same two clubs sit below a box labelled Federation, which owns the rule. An arrow labelled release goes from the federation down to Riverside, and an arrow labelled register goes down to Hillside.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="22">Nobody owns the rule</text><text x="284" y="22">The federation owns it</text></g><line x1="262" y1="34" x2="262" y2="228" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="140" width="110" height="84" rx="6"/><rect x="138" y="140" width="110" height="84" rx="6"/><rect x="284" y="140" width="110" height="84" rx="6"/><rect x="406" y="140" width="110" height="84" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)"><text x="28" y="161">Riverside FC</text><text x="150" y="161">Hillside FC</text><text x="296" y="161">Riverside FC</text><text x="418" y="161">Hillside FC</text></g><g stroke="var(--c-border)" stroke-width="1.5"><line x1="28" y1="168" x2="114" y2="168"/><line x1="150" y1="168" x2="236" y2="168"/><line x1="296" y1="168" x2="382" y2="168"/><line x1="418" y1="168" x2="504" y2="168"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="28" y="186">Ardi</text><text x="28" y="200">Besa</text><text x="150" y="186">Dren</text><text x="150" y="200">Era</text><text x="296" y="186">Ardi</text><text x="296" y="200">Besa</text><text x="418" y="186">Dren</text><text x="418" y="200">Era</text></g><g font-family="JetBrains Mono, monospace" font-size="9" fill="var(--c-muted)" opacity="0.8"><text x="28" y="216">own rules</text><text x="150" y="216">own rules</text><text x="296" y="216">own rules</text><text x="418" y="216">own rules</text></g><rect x="30" y="50" width="206" height="54" rx="6" fill="none" stroke="var(--c-accent)" stroke-width="1.5" stroke-dasharray="5 4"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="42" y="72">a player is registered</text><text x="42" y="88">with exactly one club</text></g><text x="212" y="86" font-family="Source Serif 4, Georgia, serif" font-size="22" font-weight="700" fill="var(--c-accent)">?</text><rect x="320" y="40" width="160" height="58" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="336" y="64" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">Federation</text><text x="336" y="86" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">owns the rule</text><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="364" y1="100" x2="342" y2="134"/><polyline points="342,125 341,135 350,130"/><line x1="436" y1="100" x2="458" y2="134"/><polyline points="450,130 459,135 458,125"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="290" y="120">release</text><text x="456" y="120">register</text></g><text x="16" y="250" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Each club guards its own list. A rule about both lists needs someone who sees both.</text></svg>

The federation can. It knows every club and every registration, and in real football this is exactly how it works: a new club cannot register a player until the federation confirms that the old registration has ended. The rule lives with the one party that can see the whole picture.

Notice how the federation goes about it. It does not reach into Riverside's squad list and cross Ardi out. It *asks* Riverside to release him, and Riverside applies its own rules, contract and all, before answering yes or no. Only then does the federation ask Hillside to register him. The clubs keep full control of their own lists. The federation adds one thing that neither club could provide: the decision about what happens, and in which order.

### Order matters

That order is worth a closer look, because it is what went wrong at the start.

Of the two steps, only one can fail. Riverside might refuse to release Ardi. Registering a free player at Hillside, on the other hand, always works. So do the step that can fail **first**. If Riverside refuses, nothing has happened yet, and there is nothing to undo.

<svg viewBox="0 0 520 220" role="img" xmlns="http://www.w3.org/2000/svg"><title>The same refused transfer in two orders</title><desc>Two rows of three boxes show how many clubs Ardi is registered with after each step of a transfer that Riverside refuses. In the top row, labelled release first, the release is refused and the transfer stops, and Ardi stays registered with one club throughout. In the bottom row, labelled register first, Hillside registers him, so he is with two clubs, and then the release is refused, leaving him with two clubs. The last box of the bottom row is highlighted as broken.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="62">Release first</text><text x="16" y="152">Register first</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="130" y="30" width="112" height="56" rx="6"/><rect x="264" y="30" width="112" height="56" rx="6"/><rect x="130" y="120" width="112" height="56" rx="6"/><rect x="264" y="120" width="112" height="56" rx="6"/></g><rect x="398" y="30" width="112" height="56" rx="6" fill="var(--c-raised)" stroke="var(--c-border)" stroke-width="2"/><rect x="398" y="120" width="112" height="56" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2" stroke-dasharray="5 4"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="142" y="52">start</text><text x="276" y="52">release: refused</text><text x="410" y="52">stop</text><text x="142" y="142">start</text><text x="276" y="142">register</text><text x="410" y="142">release: refused</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="142" y="72">1 club</text><text x="276" y="72">1 club</text><text x="410" y="72">1 club</text><text x="142" y="162">1 club</text><text x="276" y="162">2 clubs</text></g><text x="410" y="162" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)">2 clubs</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="245" y1="58" x2="259" y2="58"/><polyline points="254,53 260,58 254,63"/><line x1="379" y1="58" x2="393" y2="58"/><polyline points="388,53 394,58 388,63"/><line x1="245" y1="148" x2="259" y2="148"/><polyline points="254,143 260,148 254,153"/><line x1="379" y1="148" x2="393" y2="148"/><polyline points="388,143 394,148 388,153"/></g><text x="16" y="208" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">A transfer that Riverside refuses. Only one order leaves Ardi with one club.</text></svg>

If you step back, the whole story fits in three sentences, and you could probably have come up with them yourself:

1. Some rules are about one object, and that object enforces them.
2. Some rules are about several objects together, and they belong to someone who can see all of them.
3. That someone asks each object to do its own part, and decides the order, doing the risky step first.

The same shape turns up everywhere once you look for it. A seat on a flight holds at most one passenger, so swapping two passengers' seats is handled by the booking system rather than by either seat. An item in an online game belongs to exactly one player, so trades go through the game server, and online games have had famous bugs where a badly ordered trade duplicated an item.

## Naming it

These ideas have standard names, and you will meet them in every book on object-oriented design.

!!! abstract "Definitions"

    **Responsibility.** A job or a rule that a class owns. Deciding which class owns which rule is called **assigning responsibilities**, and it is a large part of what design means.

    **Delegation.** Asking the object that owns some data to do the work, rather than reaching into that data yourself. The federation delegates "release Ardi" to Riverside.

    **Invariant.** A rule that must be true whenever no operation is in the middle of running. "A player is registered with exactly one club" is an invariant of the whole league, even though for a moment, halfway through a transfer, it may not hold.

## In our bank

Our bank has exactly this problem, with money in place of players. Ada and Alan each have an account. Ada has 500 euros and tries to send 800 to Alan, using the classes from Module 1 and the most obvious two lines:

```java
Customer ada = new Customer("Ada Lovelace");
Customer alan = new Customer("Alan Turing");

Account from = ada.openAccount(500.00);
Account to = alan.openAccount(2500.00);

// Ada has 500. She tries to send 800.
from.withdraw(800.00);   // returns false, and nobody looked
to.deposit(800.00);      // so this runs anyway
```

```text
Before: 3000.00 in the two accounts
Account AC-1001: 500.00 EUR
Account AC-1002: 3300.00 EUR
After:  3800.00 in the two accounts
```

800 euros have appeared from nowhere.

You have seen this story already. `withdraw` correctly refused, because 800 is more than 500. `deposit` correctly accepted a positive amount. Each account kept its own rules, and the rule that broke belongs to neither of them: **money that leaves one account must arrive in another, and money that arrives must have left somewhere.** It is the bank's version of "one player, one club".

So the question from the football story comes back. Who can see both accounts?

| Candidate | Why it is the wrong owner |
|---|---|
| `Account` | `from.transferTo(to, amount)` would work for this one rule. But transfers collect rules of their own, such as daily limits, fees and checks between customers, and each would land in a class whose job is to look after one balance. |
| `Customer` | Ada's `Customer` has no way to reach Alan's account, and Module 1 made sure it never would. |
| `main` | That is where the rule lives now. Code in `main` cannot be called from anywhere else, cannot be tested, and is replaced when the program gets a window. |

None of these can see every account. The bank can: it holds every customer, and through them every account. It plays the role the federation played for the clubs, so the transfer rule goes there.

### The Bank class

```java
public class Bank {

    private final String name;
    private final List<Customer> customers = new ArrayList<>();

    public Bank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "A bank cannot have a blank name.");
        }
        this.name = name.trim();
    }

    // Same shape as openAccount on Customer. The bank makes the customer
    // rather than accepting one, so it can never hold somebody else's.
    public Customer addCustomer(String customerName) {
        Customer customer = new Customer(customerName);
        customers.add(customer);
        return customer;
    }

    public List<Customer> getCustomers() {
        return List.copyOf(customers);
    }
```

If this looks familiar, it should. It is the pattern you used for `Customer` in week 4, one level up: a private list created where it is declared, a method that creates the customer rather than accepting one from outside, and a copy handed out instead of the list itself.

<svg viewBox="0 0 520 300" role="img" xmlns="http://www.w3.org/2000/svg"><title>A bank owning customers that own accounts</title><desc>A Bank object holds a name and a private customers field. That field points at a list whose two slots point out to two separate Customer objects. Each customer holds its own accounts field, pointing out to one Account object. The same shape repeats at every level: something owns a private list, and the list holds arrows to objects that live outside it.</desc><rect x="20" y="108" width="140" height="82" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="36" y="132" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Bank</text><line x1="36" y1="140" x2="144" y2="140" stroke="var(--c-border)" stroke-width="1.5"/><text x="36" y="158" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)">Bank of Tirana</text><rect x="36" y="164" width="108" height="20" rx="3" fill="none" stroke="var(--c-border)" stroke-width="1.5"/><text x="44" y="178" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-accent)">customers</text><circle cx="136" cy="174" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="136" y1="174" x2="190" y2="154"/><polyline points="183,151 191,154 185,160"/></g><rect x="196" y="118" width="64" height="64" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="208" y="134" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">List</text><line x1="208" y1="140" x2="248" y2="140" stroke="var(--c-border)" stroke-width="1.5"/><g fill="none" stroke="var(--c-border)" stroke-width="1.5"><rect x="206" y="148" width="36" height="14" rx="3"/><rect x="206" y="166" width="36" height="14" rx="3"/></g><circle cx="236" cy="155" r="4" fill="var(--c-accent)"/><circle cx="236" cy="173" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="236" y1="155" x2="294" y2="80"/><polyline points="284,85 294,80 292,91"/><line x1="236" y1="173" x2="294" y2="218"/><polyline points="287,215 295,219 288,224"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="300" y="34" width="100" height="80" rx="6"/><rect x="300" y="184" width="100" height="80" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="12" font-weight="700" fill="var(--c-ink)"><text x="312" y="54">Customer</text><text x="312" y="204">Customer</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="312" y="72">C-1001 Ada</text><text x="312" y="222">C-1002 Alan</text></g><g fill="none" stroke="var(--c-border)" stroke-width="1.5"><rect x="312" y="80" width="76" height="18" rx="3"/><rect x="312" y="230" width="76" height="18" rx="3"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="318" y="93">accounts</text><text x="318" y="243">accounts</text></g><circle cx="382" cy="89" r="4" fill="var(--c-accent)"/><circle cx="382" cy="239" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="382" y1="89" x2="420" y2="89"/><polyline points="413,84 421,89 413,94"/><line x1="382" y1="239" x2="420" y2="239"/><polyline points="413,234 421,239 413,244"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="424" y="66" width="76" height="46" rx="6"/><rect x="424" y="216" width="76" height="46" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)"><text x="432" y="86">AC-1001</text><text x="432" y="236">AC-1002</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="432" y="102">300.00</text><text x="432" y="252">2700.00</text></g><text x="20" y="288" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The same shape at every level. Something owns a private list, and the list holds arrows.</text></svg>

### Delegation: finding an account by asking

A transfer arrives as two account numbers, so the first job is to find the accounts. The bank does not hold accounts directly, so it does what the federation did: it asks the ones who do.

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

Each customer searches its own accounts with the `findAccount` method it already had. The bank never opens a customer's list.

### The transfer

```java
    public boolean transfer(String fromNumber, String toNumber,
                            double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "A transfer must be positive: " + amount);
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

Look for any arithmetic in this method, or any check on a balance. There is none. `Account` still does both, exactly as before. What `transfer` contributes is the order, and the order follows the same reasoning as the football transfer:

1. A zero or negative amount is a mistake in the code that called the method, so it throws. Nothing has moved.
2. An account number that does not exist is an ordinary request that cannot be met, so it returns `false`. Nothing has moved.
3. A transfer from an account to itself is refused. Nothing has moved.
4. The withdrawal is the step that can fail, so it runs **first**. If there is not enough money, it returns `false`, and nothing has moved.
5. The deposit runs last. It cannot fail, because stage 1 already rejected every amount that `deposit` would refuse.

Here is the same picture as before, with money in place of players:

<svg viewBox="0 0 520 220" role="img" xmlns="http://www.w3.org/2000/svg"><title>The same failed transfer in two orders</title><desc>Two rows of three boxes show the total in the bank after each step of a transfer of 800 from an account holding 500. In the top row, labelled withdraw first, the withdrawal fails and the method stops, and the total stays at 3000 throughout. In the bottom row, labelled deposit first, the deposit raises the total to 3800, the withdrawal then fails, and the method ends with 3800. The last box of the bottom row is highlighted as broken.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="62">Withdraw first</text><text x="16" y="152">Deposit first</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="130" y="30" width="112" height="56" rx="6"/><rect x="264" y="30" width="112" height="56" rx="6"/><rect x="130" y="120" width="112" height="56" rx="6"/><rect x="264" y="120" width="112" height="56" rx="6"/></g><rect x="398" y="30" width="112" height="56" rx="6" fill="var(--c-raised)" stroke="var(--c-border)" stroke-width="2"/><rect x="398" y="120" width="112" height="56" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2" stroke-dasharray="5 4"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="142" y="52">start</text><text x="276" y="52">withdraw: false</text><text x="410" y="52">return false</text><text x="142" y="142">start</text><text x="276" y="142">deposit 800</text><text x="410" y="142">withdraw: false</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="142" y="72">3000.00</text><text x="276" y="72">3000.00</text><text x="410" y="72">3000.00</text><text x="142" y="162">3000.00</text><text x="276" y="162">3800.00</text></g><text x="410" y="162" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)">3800.00</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="245" y1="58" x2="259" y2="58"/><polyline points="254,53 260,58 254,63"/><line x1="379" y1="58" x2="393" y2="58"/><polyline points="388,53 394,58 388,63"/><line x1="245" y1="148" x2="259" y2="148"/><polyline points="254,143 260,148 254,153"/><line x1="379" y1="148" x2="393" y2="148"/><polyline points="388,143 394,148 388,153"/></g><text x="16" y="208" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Sending 800 from an account holding 500. Only one order leaves the total alone.</text></svg>

In the bottom row the method also returns `false`, so whoever called it believes nothing happened, while 800 euros have appeared in Alan's account.

### Checking the invariant

An invariant you can calculate is an invariant you can check. The bank adds up what its customers hold every time it is asked:

```java
    public double getTotalDeposits() {
        double total = 0;
        for (Customer customer : customers) {
            total += customer.getTotalBalance();
        }
        return total;
    }
```

It calculates the total instead of storing it, for the same reason `Customer.getTotalBalance` does: a stored copy could drift away from the real figure. Now run the same two transfers through the bank. `TransferApp` prints the total after each one:

```java
boolean tooMuch = bank.transfer(
        from.getAccountNumber(), to.getAccountNumber(), 800.00);
boolean sent = bank.transfer(
        from.getAccountNumber(), to.getAccountNumber(), 200.00);
```

```text
Total on deposit: 3000.00
Sending 800: false
Total on deposit: 3000.00   (nothing moved)
Sending 200: true
Account AC-1003: 300.00 EUR
Account AC-1004: 2700.00 EUR
Total on deposit: 3000.00   (still the same)
```

One transfer refused, one accepted, and the total never moves.

## Common mistakes

!!! mistake "Ignoring the answer a method gives you"

    ```java
    from.withdraw(800.00);   // compiles, and the false is lost
    ```

    `withdraw` returns `boolean` so that the caller can decide what to do next. Calling it as a statement on its own line throws that answer away, and Java allows it without a warning. If a method reports an outcome, read it.

!!! mistake "Reaching into another object's data instead of asking"

    ```java
    // Inside Bank. It works, and it is the wrong shape.
    for (Customer customer : customers) {
        for (Account account : customer.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
    }
    ```

    This version makes `Bank` depend on how a customer stores its accounts and on how account numbers are compared. Change either of those, and you edit two classes instead of one. Asking `customer.findAccount(...)` keeps that knowledge where it belongs.

## The bigger picture

Real software runs into this all the time. When you buy something online, one order touches the stock in a warehouse, a payment, and a delivery. None of those three can own the rule "an order is either fully placed or not placed at all", so real systems have a class whose whole job is to coordinate them, often with a name like `Checkout` or `OrderService`.

Sometimes the owner already exists in your design, as the bank did here. Sometimes it does not, and you create a class named after the thing that happens: a `Transfer`, a `Booking`, an `Enrolment`. Both follow the same principle.

??? note "What if both steps can fail?"

    The transfer is safe because the deposit cannot fail. That is a property of these two particular operations.

    If the receiving account could also refuse, for example because of a maximum balance, no order would be safe on its own. You would need a way to undo the withdrawal when the deposit fails. Java's tools for that need exceptions, which arrive in Module 3, and real banks rely on their database to do it for them.

## Summary

- Some rules belong to one object. Others span several objects, and no single one of them can enforce those.
- A rule that spans objects belongs to a class that can see all of them. In the bank, that class is `Bank`.
- That class **delegates**: it asks each object to do its own part, and only decides what happens and in which order.
- Do the step that can fail first, so that a failure leaves nothing to undo.

**Key terms:** responsibility, delegation, invariant.

## Practice

**Quick checks.** Try these before you open the code.

1.  `transfer` is rewritten with `to.deposit(amount)` above the withdrawal. Ada has 500. What does sending 800 return, and what is the total afterwards?
2.  `bank.transfer(number, number, 50.00)`, with the same number twice. What does it return, and has any money moved?
3.  `bank.transfer(fromNumber, toNumber, -50.00)`. What happens, and why is it handled differently from question 2?

??? success "Answers"

    1.  It returns `false`, and the total is 3800. The deposit ran before the withdrawal was refused, so the caller is told nothing happened while 800 euros appeared.
    2.  It returns `false`, and nothing has moved. The check for the same account runs before any money is touched.
    3.  It throws `IllegalArgumentException`. A transfer to yourself is an ordinary request that the bank refuses. A negative amount means the code that called the method has a bug, so it is reported loudly instead.

!!! verify "Coding exercise"

    Open `Bank.transfer`, move `to.deposit(amount)` above the withdrawal, and run `TransferApp` to check your answer to question 1. Then put it back.

    Now add this method to `Bank`:

    ```java
    public boolean transferWithin(Customer customer, String fromNumber,
                                  String toNumber, double amount)
    ```

    It moves money between two accounts that belong to the same customer. When it works, decide whether it belongs on `Bank` or on `Customer`, and write one sentence saying why. Either answer can be defended.

**Think about it.**

1.  `transfer` returns `false` both when an account does not exist and when there is not enough money. For whoever called it, those are different situations. What would have to change if the bank had to report declined transfers grouped by reason?

    ??? success "Hint"

        A `boolean` can only say yes or no. Count how many different answers the caller needs.

2.  In this week's seminar, a student enrols on a course. That changes the student's list of courses and the course's list of students. Which rules involve both of them, and which class should own those rules?

    ??? success "Hint"

        Ask the question from this page: who can see both the student and the course? If nobody can, that is a class you have not written yet.

## Next

`transfer` used `from.equals(to)` to check whether two account numbers named the same account. The next session asks what "the same" means for objects you design yourself.

<div class="page-nav" markdown>
[Object Equality: equals and hashCode](telling-apart.md){ .page-nav__next }
</div>
