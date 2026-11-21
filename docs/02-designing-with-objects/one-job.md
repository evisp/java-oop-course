# The Single Responsibility Principle

*One class, one reason to change*

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 6, session 1</span><span class="badge badge--accent">Needs: Object Equality</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-06-one-job-per-class">Lecture code</a></p>

**What you will learn**

- How to recognise a class that is doing several jobs at once
- A practical test for "one job": who would ask for this to change?
- Why the code that talks to the user belongs in a class of its own

## The problem

Grace Hopper runs a small café, and she does everything herself. She takes the orders at the counter, makes the coffee, and takes the payment. With ten customers a day, it works well.

Then, in the same week, three people ask for changes.

The owner wants the menu reworded, with the sizes renamed and the prices shown differently. The health inspector introduces a new rule about how long milk can stay out of the fridge. The bank replaces the card terminal, and payments now work in a different way.

All three changes land on Grace. While she is rewriting the menu, she gets the milk rule wrong. While she is learning the new terminal, the coffee goes cold. Every change, whatever it was about, disturbs everything else she does.

Grace is not bad at any of her jobs. So what exactly has gone wrong?

## Building the idea

Look at who asked for the changes. Three different people, with three different concerns: the owner cares about how things are presented, the inspector about how coffee is made, the bank about how money is handled. None of them cares about the other two.

That gives you a useful way to count jobs. Instead of asking "what does this person do?", ask **"who could ask for this to change?"** Grace has three answers, so she has three jobs, and every change from any of those three people goes through her.

Now split the work. A waiter takes the orders, a barista makes the coffee, a cashier takes the payment.

<svg viewBox="0 0 520 260" role="img" xmlns="http://www.w3.org/2000/svg"><title>One person with three reasons to change, compared with three roles with one reason each</title><desc>Two panels. On the left, three people who can ask for changes, the owner, the inspector and the bank, each have an arrow pointing at a single box for Grace, who takes orders, makes coffee and takes payment. On the right, the same three people each have one arrow, pointing at a separate role: the owner at the waiter, the inspector at the barista, and the bank at the cashier.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="22">One person, three jobs</text><text x="276" y="22">Three roles, one job each</text></g><line x1="262" y1="34" x2="262" y2="232" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="44" width="96" height="44" rx="6"/><rect x="16" y="112" width="96" height="44" rx="6"/><rect x="16" y="180" width="96" height="44" rx="6"/><rect x="276" y="44" width="96" height="44" rx="6"/><rect x="276" y="112" width="96" height="44" rx="6"/><rect x="276" y="180" width="96" height="44" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)"><text x="26" y="63">Owner</text><text x="26" y="131">Inspector</text><text x="26" y="199">Bank</text><text x="286" y="63">Owner</text><text x="286" y="131">Inspector</text><text x="286" y="199">Bank</text></g><g font-family="JetBrains Mono, monospace" font-size="9" fill="var(--c-muted)"><text x="26" y="79">menu wording</text><text x="26" y="147">milk rule</text><text x="26" y="215">card terminal</text><text x="286" y="79">menu wording</text><text x="286" y="147">milk rule</text><text x="286" y="215">card terminal</text></g><rect x="150" y="76" width="96" height="116" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="162" y="100" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Grace</text><line x1="162" y1="108" x2="234" y2="108" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="162" y="128">orders</text><text x="162" y="150">coffee</text><text x="162" y="172">payment</text></g><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="114" y1="66" x2="144" y2="104"/><polyline points="136,101 145,105 143,95"/><line x1="114" y1="134" x2="144" y2="134"/><polyline points="137,129 145,134 137,139"/><line x1="114" y1="202" x2="144" y2="164"/><polyline points="143,173 145,163 136,167"/></g><g fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"><rect x="410" y="44" width="96" height="44" rx="6"/><rect x="410" y="112" width="96" height="44" rx="6"/><rect x="410" y="180" width="96" height="44" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)"><text x="420" y="63">Waiter</text><text x="420" y="131">Barista</text><text x="420" y="199">Cashier</text></g><g font-family="JetBrains Mono, monospace" font-size="9" fill="var(--c-muted)"><text x="420" y="79">orders</text><text x="420" y="147">coffee</text><text x="420" y="215">payment</text></g><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="374" y1="66" x2="404" y2="66"/><polyline points="397,61 405,66 397,71"/><line x1="374" y1="134" x2="404" y2="134"/><polyline points="397,129 405,134 397,139"/><line x1="374" y1="202" x2="404" y2="202"/><polyline points="397,197 405,202 397,207"/></g><text x="16" y="252" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Count who can ask for a change: three people for Grace, one for each new role.</text></svg>

Each role now has exactly one person who can ask it to change. When the inspector's milk rule changes, only the barista needs to know. The waiter's menu and the cashier's terminal are not touched, so they cannot be broken by accident.

The roles still work together, and they do it by asking, as the objects did last week. The waiter does not walk into the kitchen and make the coffee. The waiter passes on the order, and the barista hands back a cup.

### The kitchen never talks to the customers

One of these splits deserves a closer look. Some roles do the work: the barista knows the recipes and the cashier knows the prices. One role talks to the customers: the waiter asks what they want and tells them what happened. The barista never needs to know whether the order came from a customer at the counter, a phone call, or an app.

<svg viewBox="0 0 520 220" role="img" xmlns="http://www.w3.org/2000/svg"><title>The counter talks to customers, the kitchen does the work</title><desc>On the left, a customer box exchanges messages with a waiter box: the customer gives an order and receives a sentence back. A dashed vertical line separates this counter side from the kitchen side on the right, where a barista box receives the order from the waiter and returns a cup. Only the order and the cup cross the line.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="22">Talks to people</text><text x="330" y="22">Does the work</text></g><line x1="300" y1="34" x2="300" y2="196" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="86" width="96" height="48" rx="6"/></g><text x="28" y="115" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">Customer</text><rect x="168" y="76" width="104" height="68" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="180" y="102" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">Waiter</text><text x="180" y="124" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">asks, reports</text><rect x="352" y="76" width="140" height="68" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="364" y="102" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">Barista</text><text x="364" y="124" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">knows the recipes</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="114" y1="100" x2="162" y2="100"/><polyline points="155,95 163,100 155,105"/><line x1="162" y1="122" x2="114" y2="122"/><polyline points="121,117 113,122 121,127"/></g><g font-family="JetBrains Mono, monospace" font-size="9" fill="var(--c-muted)"><text x="138" y="92" text-anchor="middle">"a latte"</text><text x="138" y="140" text-anchor="middle">"enjoy"</text></g><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="274" y1="100" x2="346" y2="100"/><polyline points="339,95 347,100 339,105"/><line x1="346" y1="122" x2="274" y2="122"/><polyline points="281,117 273,122 281,127"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="290" y="92">order</text><text x="292" y="140">cup</text></g><text x="16" y="212" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Only the order and the cup cross the line. The barista never speaks to a customer.</text></svg>

The payoff comes when the café changes how it serves people. Add a delivery app, and the app replaces the waiter: it asks for the order and reports what happened. The barista does not change at all, because the barista never knew who was asking.

The same shape appears in almost every program. One part does the work and keeps the rules. Another part deals with the people using it: reading what they type, showing them results. Keeping the two apart means either one can change without breaking the other.

## Naming it

!!! abstract "Definitions"

    **The Single Responsibility Principle.** A class should have one reason to change. Put differently: one person, or one kind of request, should be able to ask for a change to it. The principle was named by Robert C. Martin, and it is the "S" in a set of design principles you will hear called SOLID.

    **Separation of concerns.** Keeping code that deals with different concerns, such as business rules, presentation and storage, in different places, so that a change to one does not disturb the others.

    **Model and user interface.** The *model* is the part of a program that holds the data and enforces the rules. The *user interface* is the part that talks to people. In the café, the barista and the cashier are the model, and the waiter is the user interface.

## In our bank

Open the `main` method you wrote for Sprint 1 and count its jobs. Most Sprint 1 programs do all of these in one method: read what the user typed, work out what they meant, find the right objects, do the arithmetic, decide whether it was allowed, and print a sentence about the result.

That is Grace on a busy day. The wording of the messages, the rules about money, and the way input is read all live in one method, so a change to any of them means reading all of them. The method cannot be called from anywhere else, and it cannot be tested, because a test cannot read what was printed on the screen.

Our bank had the same problem. In week 6 it is split the way the café was.

### Who could ask for a change?

| Class | Its one reason to change |
|---|---|
| `Account`, `Customer`, `Bank` | The bank changes its rules about money |
| `BankConsole` | The wording, the language or the menu changes |
| `ConsoleApp` | The way the program is put together changes |

`Account`, `Customer` and `Bank` are the model, the kitchen. `BankConsole` is the user interface, the waiter. `ConsoleApp` is the owner who opens the café in the morning: it creates everyone and introduces them to each other.

### ConsoleApp: put it together and start

```java
public class ConsoleApp {

    public static void main(String[] args) {
        Bank bank = new Bank("Bank of Tirana");
        seed(bank);

        Scanner input = new Scanner(System.in);
        BankConsole console = new BankConsole(bank, input);
        console.run();
    }
```

That is the whole `main`. It builds the model, builds the class that talks to the user, and hands one to the other. (`seed` adds Ada Lovelace, Alan Turing and Grace Hopper as customers, so there is something to look at.)

### BankConsole: the waiter

```java
public class BankConsole {

    private final Bank bank;
    private final Scanner input;

    public BankConsole(Bank bank, Scanner input) {
        this.bank = bank;
        this.input = input;
    }

    public void run() {
        System.out.println("Welcome to " + bank.getName() + ".");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = input.nextLine().trim();
            switch (choice) {
                case "1" -> listCustomers();
                case "2" -> openAccount();
                case "3" -> transfer();
                case "4" -> payInterest();
                case "0" -> running = false;
                default -> System.out.println(
                        "There is no option " + choice + ".");
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
    }
```

Two things are worth noticing. `BankConsole` is handed the bank and the scanner instead of creating them, so the same console could be pointed at a different bank, such as a test bank full of sample data. And the menu uses the arrow form of `switch`, which runs exactly one branch, with no `break` needed. The next session looks at `switch` more closely.

### The console asks, the bank decides

Here is the handler for a transfer:

```java
    private void transfer() {
        System.out.print("From account number: ");
        String from = input.nextLine().trim();
        System.out.print("To account number: ");
        String to = input.nextLine().trim();
        double amount = askForAmount("Amount: ");

        if (amount <= 0) {
            System.out.println("A transfer has to be a positive amount.");
            return;
        }

        // The console asks, the bank decides, the console reports.
        if (bank.transfer(from, to, amount)) {
            System.out.printf("Sent %.2f EUR.%n", amount);
        } else {
            System.out.println("That transfer did not go through.");
        }
    }
```

Look for any arithmetic, or any check on a balance. There is none. The console collects three pieces of text, asks the bank, and turns `true` or `false` into a sentence. This is the waiter passing an order to the kitchen and reporting back.

```text
> From account number: To account number: Amount: Sent 200.00 EUR.
> From account number: To account number: Amount: A transfer has to be a positive amount.
```

You may have noticed that the amount is checked twice: here, and again inside `Bank.transfer`, which throws on a zero or negative amount. They are two different rules that happen to look alike. A person typing `-5` is an ordinary mistake, so the console catches it and explains. Code calling `transfer` with `-5` is a bug, so the bank refuses it loudly. Each check belongs to the class that is responsible for that kind of mistake.

<svg viewBox="0 0 520 300" role="img" xmlns="http://www.w3.org/2000/svg"><title>The model on one side of a line and the console on the other</title><desc>On the left, three stacked boxes named Bank, Customer and Account make up the model, and none of them prints anything. A dashed vertical line separates them from the right side, where a ConsoleApp box hands control to a BankConsole box holding the Scanner, the printing and the menu. Two arrows cross the line: the console asks, and the model answers. Below the console sits a faint dashed box labelled as a window in week thirteen, showing what replaces it.</desc><g font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)"><text x="20" y="30">The model</text><text x="320" y="30">What the person sees</text></g><line x1="250" y1="38" x2="250" y2="268" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="50" width="170" height="52" rx="6"/><rect x="20" y="114" width="170" height="52" rx="6"/><rect x="20" y="178" width="170" height="52" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)"><text x="38" y="82">Bank</text><text x="38" y="146">Customer</text><text x="38" y="210">Account</text></g><text x="20" y="252" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">no println, no Scanner, anywhere</text><rect x="320" y="40" width="180" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="336" y="62" font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)">ConsoleApp</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="410" y1="76" x2="410" y2="84"/><polyline points="405,79 410,85 415,79"/></g><rect x="320" y="88" width="180" height="94" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="336" y="112" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">BankConsole</text><line x1="336" y1="120" x2="484" y2="120" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="336" y="140">Scanner</text><text x="336" y="158">println</text><text x="336" y="174">the menu</text></g><rect x="320" y="204" width="180" height="48" rx="8" fill="none" stroke="var(--c-muted)" stroke-width="1.5" stroke-dasharray="5 5" opacity="0.6"/><text x="336" y="233" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">a window, week 13</text><line x1="410" y1="184" x2="410" y2="202" stroke="var(--c-muted)" stroke-width="1.5" stroke-dasharray="3 3" opacity="0.6"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="316" y1="130" x2="196" y2="130"/><polyline points="203,125 195,130 203,135"/><line x1="196" y1="158" x2="316" y2="158"/><polyline points="309,153 317,158 309,163"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="224" y="124">asks</text><text x="216" y="174">answers</text></g><text x="20" y="288" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Method calls cross the line. Nothing else does.</text></svg>

### Checking it yourself

This is a claim you can check rather than take on trust. Search `Account.java`, `Customer.java`, `Bank.java` and `AccountType.java` for `System.out`. In Eclipse, that is **Ctrl+H**, File Search.

```text
0 results
```

Four classes, a few hundred lines, and not one of them prints anything. Every sentence the user sees comes from `BankConsole`.

## Common mistakes

!!! mistake "Returning a sentence instead of printing one"

    A common half-fix is to stop printing and return the sentence instead:

    ```java
    // In Account. It prints nothing, and it is still the wrong place.
    public String describe() {
        return "Account " + accountNumber + " holds " + balance + " EUR";
    }
    ```

    Nothing is printed, so it looks solved. But `Account` now owns the wording, the order of the information and the language. Translate the program, or show the balance in a table, and you are editing `Account` again. If a method's result is a sentence meant for a person, it belongs with the other sentences, in the user interface.

    `toString` is the exception. It is there to help you while you write and debug code, not to talk to the user.

!!! mistake "Printing from the model \"just for now\""

    A `System.out.println` inside `Account.withdraw` that explains a refusal is genuinely useful while you are testing. It also means `Account` has decided it is talking to a terminal. Once the program has a window, that message goes nowhere. If you need to see what the model is doing, use the debugger, or ask the model for the information and print it from the console.

## The bigger picture

The split you made this week pays off twice later in the course. In week 10 you write tests, and a test can only check what a method returns, so methods that return results can be tested and methods that print cannot. In week 13 the program gets a window, and `BankConsole` is the one class that gets replaced.

Web and mobile applications are built the same way, usually with more layers: the screens you see, the code that handles requests, the business rules, the storage. Each layer changes for its own reasons, and different teams often own different layers.

??? note "Can you split too far?"

    Yes. One responsibility does not mean one method per class, and it does not mean dozens of tiny classes with names like `AmountChecker` and `NameTrimmer`. A responsibility is a reason to change, not a single action. `Account` both deposits and withdraws, and that is fine, because the same person, the bank's rule-maker, would ask for changes to both.

    A useful sign that you have gone too far: to make one ordinary change, you have to edit five small classes that always change together. Then they are really one responsibility, spread too thin.

## Summary

- A class has one responsibility when only one kind of request could make it change.
- "Who would ask for this to change?" is a more reliable test than "what does this class do?".
- Keep the model, which holds the data and the rules, separate from the user interface, which talks to people.
- In the bank, `Account`, `Customer` and `Bank` never print. `BankConsole` does all the reading and printing, and `ConsoleApp` puts them together.

**Key terms:** Single Responsibility Principle, separation of concerns, model, user interface.

## Practice

**Quick checks.**

1.  Who could ask for a change to each of these: `Account`, `BankConsole`, and a class `MonthlyReport` that calculates the month's totals and also emails them to the manager?
2.  `BankConsole` is handed its `Bank` instead of creating one. Name one thing that makes possible.
3.  The bank decides that transfers over 10,000 EUR need a second approval. Which class changes?

??? success "Answers"

    1.  `Account`: whoever sets the bank's rules about money. `BankConsole`: whoever decides what the user sees and how. `MonthlyReport`: two different people, whoever defines the totals and whoever manages how reports are sent, so it has two responsibilities.
    2.  The same console can be run against a different bank, for example one filled with test data, without changing `BankConsole`.
    3.  `Bank`. It is a rule about money, so it belongs in the model. The console may need a new message too, but the decision itself lives in the bank.

!!! verify "Coding exercise"

    Add this line inside `Account.withdraw`, just before it returns `false` for a refused withdrawal:

    ```java
    System.out.println("Refused: not enough money in " + accountNumber);
    ```

    Run the program and try a withdrawal that is too large. The message is useful. Now answer three questions in writing:

    1.  Who reads that message in week 13, when the program has a window and no terminal?
    2.  A test wants to check that taking 900 from an account holding 500 is refused. What can the test look at?
    3.  The bank needs the message in Albanian. How many classes now contain user-facing text?

    Then remove the line from `Account` and make `BankConsole` print an equivalent message when `withdraw` returns `false`. The behaviour is the same, and all three answers change.

**Think about it.**

1.  The amount is checked in `BankConsole.transfer` and again in `Bank.transfer`. Make the case that this is duplication and one check should go. Then make the case that they are two different rules. Which argument is stronger?

    ??? success "Hint"

        Ask who each check protects against. One protects against a person typing something wrong, the other against a programmer calling the method wrongly.

2.  Before the seminar, open your `UniversityApp.main`. List every job it does, and for each one, say who could ask for it to change. Which of those jobs should move into a `UniversityConsole`?

    ??? success "Hint"

        Anything that reads input or prints a sentence belongs in the console. Anything about students, courses and enrolment rules belongs in the model.

## Next

The model no longer prints, but it still has a weak spot from week 4: account types are kept as strings, and the compiler cannot tell a real one from a typo. The next session fixes that.

<div class="page-nav" markdown>
[Enumerations](enums.md){ .page-nav__next }
</div>
