# One job per class

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 6, session 1</span><span class="badge badge--accent">Needs: Telling objects apart</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-06-one-job-per-class">Lecture code</a></p>

By the end of this page you can look at a class and say in one sentence what its job is, move everything that reads input or prints output into a class of its own, and explain why that is worth doing now rather than later.

There is no new syntax today. You will type a lot and learn no keywords. What changes is where things live.

## Open your own code first

Before you read any further, open the `main` method you wrote for Sprint 1.

Count the jobs it is doing. Most of them look like this: it reads what somebody typed, works out what that means, does some arithmetic, decides whether the arithmetic was allowed, and prints a sentence about it. Five jobs in one method, and the method cannot be called from anywhere, cannot be tested, and has no name that describes it.

You are not being shown a bad example. You wrote one.

## What "one job" means

The test is a sentence. Say what the class does without using the word "and".

- `Account` holds money and enforces the rules about it. That is one job, said two ways.
- `Customer` keeps a set of accounts and answers questions about them. One job.
- `main` reads input, interprets it, performs operations, formats results and prints them. Not one job.

Two names worth being suspicious of. A class called `Manager`, `Handler`, `Processor` or `Util` is usually a class whose job nobody could name. And a class that both holds the data and decides how the data should be described has two jobs, however small the second one looks.

## The split

Everything that talks to a person moves into one class.

```java
import java.util.Scanner;

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
                default -> System.out.println("There is no option " + choice + ".");
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
    }
```

The class holds two things: the bank it is talking about, and the thing it is reading from. It is handed both rather than making them, which matters more than it looks right now and will matter a great deal in week 13.

What is left of the program is six lines:

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

Build the model. Build the thing that talks. Hand one to the other. That is all `main` is for.

## Why this is worth doing

Not tidiness. Three concrete things.

**A method that prints cannot be tested.** A test calls a method and checks what came back. It cannot read the screen. If `withdraw` explains its refusal by printing, there is nothing for a test to look at, and in week 10 you will be writing tests for exactly this code.

**A method that prints has already decided who it is talking to.** `System.out` means a terminal. In week 13 the same bank gets a window, and the only file that gets thrown away is the one you just wrote. `Account`, `Customer` and `Bank` will not change at all. That is not a prediction, it is the reason for doing this in week 6 rather than week 13.

**A class with two jobs has to be read twice.** Come back in a month to change a wording and you read the balance rules on the way past. Come back to change the balance rules and you read the wording. Every time.

<svg viewBox="0 0 520 300" role="img" xmlns="http://www.w3.org/2000/svg"><title>The model on one side of a line and the console on the other</title><desc>On the left, three stacked boxes named Bank, Customer and Account make up the model, and none of them prints anything. A dashed vertical line separates them from the right side, where a ConsoleApp box hands control to a BankConsole box holding the Scanner, the printing and the menu. Two arrows cross the line: the console asks, and the model answers. Below the console sits a faint dashed box labelled as a window in week thirteen, showing what replaces it.</desc><g font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)"><text x="20" y="30">The model</text><text x="320" y="30">What the person sees</text></g><line x1="250" y1="38" x2="250" y2="268" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="50" width="170" height="52" rx="6"/><rect x="20" y="114" width="170" height="52" rx="6"/><rect x="20" y="178" width="170" height="52" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)"><text x="38" y="82">Bank</text><text x="38" y="146">Customer</text><text x="38" y="210">Account</text></g><text x="20" y="252" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">no println, no Scanner, anywhere</text><rect x="320" y="40" width="180" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="336" y="62" font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)">ConsoleApp</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="410" y1="76" x2="410" y2="84"/><polyline points="405,79 410,85 415,79"/></g><rect x="320" y="88" width="180" height="94" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="336" y="112" font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)">BankConsole</text><line x1="336" y1="120" x2="484" y2="120" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="336" y="140">Scanner</text><text x="336" y="158">println</text><text x="336" y="174">the menu</text></g><rect x="320" y="204" width="180" height="48" rx="8" fill="none" stroke="var(--c-muted)" stroke-width="1.5" stroke-dasharray="5 5" opacity="0.6"/><text x="336" y="233" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">a window, week 13</text><line x1="410" y1="184" x2="410" y2="202" stroke="var(--c-muted)" stroke-width="1.5" stroke-dasharray="3 3" opacity="0.6"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="316" y1="130" x2="196" y2="130"/><polyline points="203,125 195,130 203,135"/><line x1="196" y1="158" x2="316" y2="158"/><polyline points="309,153 317,158 309,163"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="224" y="124">asks</text><text x="216" y="174">answers</text></g><text x="20" y="288" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Method calls cross the line. Nothing else does.</text></svg>

## Check your own model

This is a check you can run rather than a claim you have to believe. Search `Account.java`, `Customer.java` and `Bank.java` for `System.out`.

```text
0 results
```

Three classes, several hundred lines, and not one of them says anything out loud. In Eclipse it is **Ctrl+H**, File Search.

!!! mistake "Returning a String instead of printing one"

    This is the half-fix almost everybody reaches for:

    ```java
    // In Account
    public String describe() {
        return "Account " + accountNumber + " holds " + balance + " EUR";
    }
    ```

    Nothing is printed, so it looks like the problem is solved. It is not. `Account` now owns the wording, the order of the information, the currency symbol and the language. Translate the program and you edit `Account`. Put the balance in a table column and you edit `Account`.

    `toString` is the one exception, and it exists for you while you are debugging, not for the person using the program. The moment a sentence is meant for a user, it belongs with the other sentences.

## The console asks, the model decides

Here is what a handler looks like once the split is done.

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

        if (bank.transfer(from, to, amount)) {
            System.out.printf("Sent %.2f EUR.%n", amount);
        } else {
            System.out.println("That transfer did not go through.");
        }
    }
```

Read what is not there. No arithmetic. No balance check. No idea which account belongs to whom. The console gathers three pieces of text, asks the bank, and turns `true` or `false` into a sentence.

The check for a positive amount is the one that might look misplaced. It is not. `Bank.transfer` throws on a non-positive amount, because that is a bug in the calling code. Somebody typing a negative number is not a bug, it is Tuesday. So the console catches the typing mistake and the bank catches the programming mistake. Two different problems, two different places.

??? note "Where does logging go, then?"

    Logging is not talking to a user, so the rule above does not apply to it. A log line is a message from the program to whoever has to work out why it broke at three in the morning, and it belongs wherever the interesting thing happened, model included.

    Java has a logging framework for this and it is not `System.out`. The important part for now is only that the two things are different and should not be confused: one is part of what the program does, the other is part of what the program says.

> **If you remember one thing.** A class that does the work and also describes the work has two jobs. You will have to read both every time you change either.

## Check yourself

!!! verify "Put the printing back and watch what it costs"

    Add this line inside `Account.withdraw`, in the branch that refuses:

    ```java
    System.out.println("Refused: not enough money in " + accountNumber);
    ```

    Run the program. It works, and the message is genuinely useful.

    Now answer three things in writing.

    1.  Who reads that message in week 13, when the program has a window and no terminal?
    2.  A test wants to check that a withdrawal of 900 from an account holding 500 is refused. What can the test look at? What would it have to do to see your message?
    3.  The bank opens a branch in Tirana and needs the message in Albanian. Which file do you edit, and how many classes know it is a banking program in the first place?

    Then take the line out, and put the same message in `BankConsole` instead. Nothing about the behaviour changes. Everything about those three answers does.

## Think about it

1.  `BankConsole` is handed its `Bank` and its `Scanner` rather than making them. Say what that buys you. Then suppose you want to run the same console against a test bank filled with fake data, and say whether you could.

2.  The console checks that the amount is positive, and `Bank.transfer` also checks. That is the same rule written twice. Argue that this is duplication worth removing. Then argue that it is two different rules that happen to look alike. One of those arguments is better and you should be able to say why.

3.  `main` builds the model, builds the console, and connects them. That is a job too. Does it deserve a class of its own, or is `main` the right place for it? Then imagine the program has six classes to wire together instead of two.

4.  The rule on this page is that the model never prints. Find a case where you would break it on purpose. Then say what you would have to accept in exchange, and whether a one-line exception stays one line for long.

## Next

The model is clean. There is still a set of three strings in it that the compiler has never once checked, and they have been there since week 4.

<div class="page-nav" markdown>
[A fixed set of values](enums.md){ .page-nav__next }
</div>
