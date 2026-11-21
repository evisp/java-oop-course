# Enumerations

*A fixed set of values the compiler checks*

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 6, session 2</span><span class="badge badge--accent">Needs: The Single Responsibility Principle</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-06-one-job-per-class">Lecture code</a></p>

**What you will learn**

- How to replace a set of strings with a type whose values the compiler checks
- How each value of that type can carry data of its own
- How to tell what an enum can solve from what it cannot

## The problem

Grace's café takes orders on a printed slip. Next to *Size*, the slip has an empty line, and customers write whatever they like on it.

Over one morning, customers write "large", "Large", "L", "big" and "venti". The barista has to guess what each one means. "Big" is probably large. "Venti" is a size from another café entirely. One customer writes "medum".

Nobody did anything strange. A blank line accepts anything, so anything is what Grace gets. The mistakes only come to light later, at the coffee machine, when someone has to decide what was meant.

Now picture the same slip with three boxes to tick: ☐ Small ☐ Medium ☐ Large. Every slip is now readable. There is no way to write "medum", because there is nothing to write.

Programs have blank lines too. How do you give a Java program boxes to tick?

## Building the idea

### A fixed list of options

The tick boxes work because the list of options is decided once, when the slip is printed, and nothing outside that list can appear. A slip can be wrong in only one way: a customer can tick the wrong box. It can no longer be unreadable.

<svg viewBox="0 0 520 230" role="img" xmlns="http://www.w3.org/2000/svg"><title>A free-text field compared with tick boxes</title><desc>Two order slips. On the left, a slip with a blank size line, surrounded by the different things customers wrote: large, Large, L, big, venti and medum, several shown in a warning colour. On the right, a slip with three tick boxes, Small, Medium and Large, with Large ticked. Only the three listed options can appear.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="22">A blank line</text><text x="284" y="22">Boxes to tick</text></g><line x1="262" y1="34" x2="262" y2="200" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><rect x="16" y="40" width="150" height="70" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="28" y="62" font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)">Order slip</text><text x="28" y="92" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)">Size:</text><line x1="70" y1="94" x2="150" y2="94" stroke="var(--c-muted)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="11"><text x="28" y="140" fill="var(--c-ink)">large</text><text x="88" y="140" fill="var(--c-ink)">Large</text><text x="148" y="140" fill="var(--c-ink)">L</text><text x="28" y="164" fill="var(--c-accent)">big</text><text x="88" y="164" fill="var(--c-accent)">venti</text><text x="148" y="164" fill="var(--c-accent)">medum</text></g><text x="28" y="192" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">anything can be written</text><rect x="284" y="40" width="210" height="140" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="298" y="62" font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)">Order slip</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="1.5"><rect x="300" y="80" width="14" height="14" rx="2"/><rect x="300" y="110" width="14" height="14" rx="2"/></g><rect x="300" y="140" width="14" height="14" rx="2" fill="var(--c-accent)" stroke="var(--c-accent)" stroke-width="1.5"/><polyline points="303,147 306,151 312,143" fill="none" stroke="var(--c-surface)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="326" y="92">Small</text><text x="326" y="122">Medium</text><text x="326" y="152">Large</text></g><text x="284" y="200" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">only these three can appear</text><text x="16" y="222" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">A blank line accepts anything. A list of boxes accepts only what is on the list.</text></svg>

### Options can carry information

Grace's menu says more than the names of the sizes. Each size comes with its own facts:

| Size | Volume | Price |
|---|---|---|
| Small | 250 ml | 1.50 EUR |
| Medium | 350 ml | 2.00 EUR |
| Large | 450 ml | 2.50 EUR |

Once the order says "Large", the volume and the price follow from it. Nobody needs a separate price list kept somewhere else, because the facts travel with the option. Add a fourth size, and you add one row, with its facts, in one place.

### Where that stops working

The café also has a fixed list of payment methods: cash, card and voucher. It looks like the same kind of list, but look at what changes from one option to the next.

<svg viewBox="0 0 520 230" role="img" xmlns="http://www.w3.org/2000/svg"><title>Options that differ in data compared with options that differ in behaviour</title><desc>Two panels. On the left, coffee sizes Small, Medium and Large each carry a volume and a price, which are values and fit in a table. On the right, payment methods Cash, Card and Voucher each need a different procedure: count the change, ask the bank, check the expiry date. These are actions, not values.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="22">Differ in data</text><text x="276" y="22">Differ in behaviour</text></g><line x1="262" y1="34" x2="262" y2="200" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><g fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"><rect x="16" y="40" width="232" height="40" rx="6"/><rect x="16" y="90" width="232" height="40" rx="6"/><rect x="16" y="140" width="232" height="40" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)"><text x="28" y="65">Small</text><text x="28" y="115">Medium</text><text x="28" y="165">Large</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-accent)"><text x="104" y="65">250 ml  1.50</text><text x="104" y="115">350 ml  2.00</text><text x="104" y="165">450 ml  2.50</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="276" y="40" width="228" height="40" rx="6"/><rect x="276" y="90" width="228" height="40" rx="6"/><rect x="276" y="140" width="228" height="40" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)"><text x="288" y="65">Cash</text><text x="288" y="115">Card</text><text x="288" y="165">Voucher</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="358" y="65">count the change</text><text x="358" y="115">ask the bank</text><text x="358" y="165">check the expiry date</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="16" y="200">values: fit in a table</text><text x="276" y="200">steps: do not fit in a table</text></g><text x="16" y="222" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">A fixed list works for both. Only the left side is fully described by its data.</text></svg>

The sizes differ in **data**: numbers you can write in a table. The payment methods differ in **behaviour**: each one needs a different procedure. "Count the change" is not a number you can put in a column. A fixed list still helps with payments, because it stops anyone writing "chequ" on the slip, but the list alone cannot say what each option does.

Keep that distinction in mind. It decides how far the tool in this lecture can take you.

## Naming it

!!! abstract "Definitions"

    **Enumeration (enum).** A type with a fixed set of values, listed once in its declaration. Java's `enum` keyword creates one, and no value outside the list can exist.

    **Enum constant.** One of those values, such as `SAVINGS`. Each constant is an object, created once when the program starts, so it can hold fields and answer method calls.

    **Type safety.** The compiler's guarantee that a variable can only hold values of its declared type. With an enum, a misspelt constant is a compile error instead of a bug you find later.

## In our bank

### Account types were a blank line

In week 4 the bank kept its account types like this:

```java
List<String> types = List.of("CHECKING", "SAVINGS", "DEPOSIT");
```

A `String` is the blank line on the slip. All three of these compile, and none of them works:

```java
if (type.equals("checking")) { ... }   // never matches: wrong case
if (type.equals("CHEKING"))  { ... }   // never matches: typo
account.setType("CRYPTO");             // not an account type at all
```

The first two fail silently: the code runs, and a branch that should fire never does. The third succeeds, and now the bank holds an account of a type that does not exist. The compiler could have caught all three, but to the compiler any text is a perfectly good `String`.

### Tick boxes for account types

In its simplest form, an enum is just the list of options:

```java
public enum AccountType {

    CHECKING,
    SAVINGS,
    TERM_DEPOSIT
}
```

Three values, and no way to create a fourth from outside. A field declared as `AccountType` can hold one of those three, or `null`, and nothing else. `AccountType.CHEKING` does not compile, so the typo is caught in the editor, before the program ever runs.

### Facts that travel with the type

Like the coffee sizes, each account type comes with facts of its own: a name a customer should see, and a monthly interest rate. Because each constant is an object, those facts can live on it:

```java
public enum AccountType {

    CHECKING("Current account", 0.0000),
    SAVINGS("Savings account", 0.0025),
    TERM_DEPOSIT("Term deposit", 0.0040);

    private final String label;
    private final double monthlyInterestRate;

    AccountType(String label, double monthlyInterestRate) {
        this.label = label;
        this.monthlyInterestRate = monthlyInterestRate;
    }

    public String getLabel() {
        return label;
    }

    public double getMonthlyInterestRate() {
        return monthlyInterestRate;
    }
```

Each of the first three lines calls the constructor below it. `SAVINGS` is an object built once, with the label "Savings account" and the rate 0.0025, and it is the same object every time you write `AccountType.SAVINGS`.

The constructor cannot be `public` or `protected`. Only those three lines are allowed to call it, which is what keeps the list fixed.

<svg viewBox="0 0 520 250" role="img" xmlns="http://www.w3.org/2000/svg"><title>An account type field pointing at one of three constant objects</title><desc>On the left, an Account object holds a number and a field called type. That field holds an arrow pointing at one of three objects on the right, which are the three constants of the AccountType enum. Each constant is an object holding its own label and its own monthly interest rate.</desc><text x="240" y="16" font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)">AccountType</text><rect x="20" y="80" width="150" height="84" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="36" y="104" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><line x1="36" y1="112" x2="154" y2="112" stroke="var(--c-border)" stroke-width="1.5"/><text x="36" y="130" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">AC-1001</text><rect x="36" y="136" width="118" height="20" rx="3" fill="none" stroke="var(--c-border)" stroke-width="1.5"/><text x="44" y="150" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">type</text><circle cx="146" cy="146" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="146" y1="146" x2="234" y2="122"/><polyline points="223,120 234,122 226,129"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="240" y="26" width="190" height="54" rx="6"/><rect x="240" y="158" width="190" height="54" rx="6"/></g><rect x="240" y="92" width="190" height="54" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)"><text x="256" y="48">CHECKING</text><text x="256" y="114">SAVINGS</text><text x="256" y="180">TERM_DEPOSIT</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="256" y="68">Current account   0.0000</text><text x="256" y="200">Term deposit      0.0040</text></g><text x="256" y="134" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">Savings account   0.0025</text><text x="20" y="238" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Three objects, built once. The field holds an arrow to one of them.</text></svg>

`Account` now has a field of this type, set once in the constructor:

```java
    private final AccountType type;
```

### Data: interest without a single decision

Because the rate travels with the type, paying interest needs no decision about what kind of account this is:

```java
    public double applyMonthlyInterest() {
        double interest = balance * type.getMonthlyInterestRate();
        balance += interest;
        return interest;
    }
```

This is the coffee price following from the size. Add a fourth account type with its own rate, and this method does not change at all. `TypesApp` runs it for one account of each type:

```text
--- Every type, listed once ---
CHECKING       Current account   0.00% a month
SAVINGS        Savings account   0.25% a month
TERM_DEPOSIT   Term deposit      0.40% a month

--- Interest: the rate is data ---
Current account AC-1001: 500.00 EUR
Savings account AC-1002: 2000.00 EUR
Term deposit AC-1003: 5000.00 EUR
Paid 25.00 EUR of interest
Current account AC-1001: 500.00 EUR
Savings account AC-1002: 2005.00 EUR
Term deposit AC-1003: 5020.00 EUR
```

### values() and valueOf()

Every enum comes with two methods you do not write yourself. `values()` returns all the constants in the order they were declared, and the console builds its menu from it:

```java
    private AccountType askForType() {
        AccountType[] types = AccountType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println("  " + (i + 1) + "  " + types[i].getLabel());
        }
```

```text
  1  Current account
  2  Savings account
  3  Term deposit
```

The menu cannot fall out of step with the enum, because it is made from it. A fourth constant appears in the menu without a change to `BankConsole`.

`valueOf()` goes the other way, from text to a constant, and refuses text it does not recognise:

```text
valueOf("SAVINGS")  : Savings account
valueOf("CRYPTO")   : No enum constant AccountType.CRYPTO
```

This is the one place where unchecked text can still try to get in, and it is exactly where you want the check: at the edge of the program, where text from outside becomes a value inside.

### Behaviour: withdrawing is a different story

Withdrawing is where the payment-methods problem comes back. How much may be taken out depends on the type, and it is not a number:

- a current account may go down to zero;
- a savings account must keep 100 EUR in;
- a term deposit refuses every withdrawal.

Those are three different rules, like cash, card and voucher. No field on the enum can hold them, so `Account.withdraw` has to decide by type:

```java
        boolean allowed = switch (type) {
            case CHECKING -> balance - amount >= 0.00;
            case SAVINGS -> balance - amount >= 100.00;
            case TERM_DEPOSIT -> false;
        };
```

This is a **switch expression**: it produces a value, here a `boolean`, and the arrow form runs exactly one branch. Because it produces a value, the compiler insists that every constant of `AccountType` is covered. Add a fourth constant and this line stops compiling:

```text
error: the switch expression does not cover all possible input values
```

That is the enum's type safety working for you again: the compiler finds the place you forgot.

```text
--- Withdrawing: the rule is behaviour ---
500 from the current account : true
2000 from the savings account: false
100 from the savings account : true
100 from the term deposit    : false
```

??? note "The older switch statement"

    You will also see the older form, a switch *statement* that assigns inside each branch:

    ```java
    double lowestAllowed;
    switch (type) {
        case CHECKING -> lowestAllowed = 0.00;
        case SAVINGS -> lowestAllowed = 100.00;
        case TERM_DEPOSIT -> { return false; }
        default -> { return false; }
    }
    ```

    A statement does not have to cover every constant, so the compiler cannot be sure `lowestAllowed` was given a value, and it makes you write a `default` branch. That branch hides a later mistake: add a fourth type, forget to update this switch, and the new type silently takes the `default`. Prefer the switch expression whenever you are choosing a value.

The switch expression is safe, but notice where the rules are. Everything that makes a savings account a savings account, its minimum balance and its withdrawal rule, sits inside `Account`, next to the rules for every other type. If more methods start to depend on the type, each one needs its own switch, and the rules for one kind of account end up spread across many methods. Next week takes the other approach and gives each kind of account a class of its own.

## Common mistakes

!!! mistake "Showing `name()` to a person"

    ```java
    System.out.println("Account type: " + type.name());
    ```

    ```text
    Account type: TERM_DEPOSIT
    ```

    `name()` returns the constant exactly as it is written in the source code, in capitals and with an underscore. It is for code. A person should see the label. Keeping the two apart also means you can rename a constant without changing what users read.

!!! mistake "Using an enum for a list that changes"

    An enum is fixed when the program is compiled. The account types a bank offers change rarely, and only when the bank changes its products, so an enum fits. The bank's branches, or the list of its customers, change while the program is running, and an enum cannot grow at run time. Those belong in ordinary data, such as a `List` you can add to.

## The bigger picture

Java's own library uses enums for fixed sets everywhere: `DayOfWeek` has exactly seven values, `Month` has twelve. Whenever you see a set of values that will not change while the program runs, and a typo in one of them would be a bug, an enum is usually the right tool.

The distinction from this lecture is worth carrying forward. When the options differ only in **data**, an enum with fields describes them completely, as it does for interest rates. When they differ in **behaviour**, an enum can still list them, but the behaviour has to live somewhere, and a switch over the type is only a first answer.

??? note "Can each constant have its own method?"

    Yes. Java lets each enum constant override a method with its own body, and you will meet code that does it. It works well for small, self-contained behaviour. When the behaviour grows, with state of its own or rules that change often, separate classes usually describe it better. That is the subject of next week.

## Summary

- An enum is a type with a fixed list of values, checked by the compiler. A misspelt value does not compile.
- Each constant is an object, so it can carry data, like a label and a rate, that travels with it.
- `values()` lists every constant; `valueOf()` turns text into a constant and refuses anything else.
- An enum fully describes options that differ in data. Options that differ in behaviour still need code that decides, and a switch expression at least makes the compiler check that every option is handled.

**Key terms:** enumeration, enum constant, type safety, switch expression.

## Practice

**Quick checks.** Enum or not?

1.  The days of the week.
2.  The bank's branches in Tirana, Durrës and Vlorë.
3.  The card networks a payment terminal accepts.
4.  The grades a course can give, from A to F.

??? success "Answers"

    1.  Enum. The set is fixed and will not change.
    2.  Not an enum. Branches open and close while the system is in use; keep them as data.
    3.  It depends. If the list changes only with a new version of the software, an enum fits. If the bank adds networks without releasing new code, it belongs in data.
    4.  Enum, and a good one: each grade can carry its grade points as a field. You will build this in the seminar.

!!! verify "Coding exercise"

    Add a fourth constant to `AccountType`, above `TERM_DEPOSIT`, so that the semicolon stays on the last line:

    ```java
    YOUTH_SAVINGS("Youth savings", 0.0030),
    ```

    Compile. Read the error and find which line it points at. Then write the rule for the new type: a youth savings account may go down to zero, but no single withdrawal may be larger than 200 EUR.

    Run `TypesApp` and the console. List every file you changed, and every place that started working without a change.

**Think about it.**

1.  `valueOf("CRYPTO")` throws an exception instead of returning `null`. Is that the right choice when the text came from a person typing in the console? What about when it came from a file saved by an older version of the program?

    ??? success "Hint"

        Think about what each caller would do with a `null`, and how soon a mistake would be noticed.

2.  In the seminar, students have a level (undergraduate, masters, doctoral) and a set of grades. For each, decide whether it is a fixed list that differs only in data, or one that differs in behaviour. Which rule about graduation will need a switch?

    ??? success "Hint"

        A maximum number of courses is data. "Doctoral students also need a submitted thesis" is a rule that applies to one level only.

## Next

The switch in `withdraw` is safe, and it still gathers the rules for every kind of account into one class. Next week, each kind of account becomes a class of its own, and that switch disappears.

<div class="page-nav" markdown>
[Inheritance](inheritance.md){ .page-nav__next }
</div>
