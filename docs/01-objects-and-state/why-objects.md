# Why objects

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 1, session 2</span><span class="badge badge--accent">Follows: Thinking in things</span></p>

By the end of this page you can explain why a rule that lives away from its data is optional, say precisely what Java adds to the C you already know, and pick out which things in a problem should be classes and which should not.

Last session made a promise: things that own their data survive change better than steps that share it. That is easy to say and easy to nod along to. This session tests it on code you have actually written.

## A program you already wrote

Something like this, in C, in your first year. Names and grades for a class.

```c
char  names[100][50];
float grades[100];
int   count = 0;

void add_student(const char *name, float grade) {
    strcpy(names[count], name);
    grades[count] = grade;
    count++;
}

float average(void) {
    float total = 0;
    for (int i = 0; i < count; i++) {
        total += grades[i];
    }
    return total / count;
}
```

Sixty lines, it works, you got the mark. There is nothing wrong with it.

## Four changes later

**Change one.** Grades must be between 0 and 10. You add a check to `add_student`.

**Change two.** Grades can now be imported from a file. The import function writes into `grades` too, so the check goes there as well. Two copies.

**Change three.** A teacher can correct a grade after entry. Another function, another copy. Three.

**Change four.** Someone, possibly you at 2am, writes this in the middle of an unrelated function:

```c
grades[3] = 11.0;
```

That compiles. It runs. No check was consulted, because checks live in functions and this line did not call one.

Now answer a simple question: where is the rule about grades in this program?

You cannot point at it. It is not in the program. It is in your head, and in the head of everyone else who has ever touched this file, and it survives exactly as long as all of you keep remembering it.

!!! mistake "\"But I would never write `grades[3] = 11.0`\""

    Maybe not today. You will write it in week eleven when you are tired and the deadline is tomorrow, and it will work, and you will move on.

    The point is not that you are careless. It is that the program offers no resistance. A design that depends on everyone being careful forever is not a design. It is a hope.

## And then it gets worse

Now drop a student who left the course. You shift `names` down by one. You shift `grades` down by one.

Anything holding index 3 is now pointing at a different student. And next month, when somebody adds `emails[100]` and forgets to shift it too, nothing breaks. No crash, no compiler error. The program keeps running and quietly reports the wrong email for every student after position three.

That is the failure that costs a weekend, because it never announces itself.

## Why this happened

Look at how the program was split. `add_student`, `average`, `print_all`, `load_file`. Split by **what it does**, exactly like the top half of last session's diagram.

Every one of those needs the data, so the data has to be reachable from all of them, which means file scope, which means reachable from everything else in the file too.

Reachable by everything means changeable by everything. And when every function shares responsibility for keeping the data correct, nobody has it.

This is not a C problem. You can write the same design in Java, Python or JavaScript, and people do, every day. The language does not save you. The way you divided the work does.

## You already half solved this in C

Here is the part worth noticing. You do not need a new language to fix the drift.

```c
struct Student {
    char  name[50];
    float grade;
};

struct Student students[100];
```

A student is now one thing. Delete `students[3]` and the name and grade go together, because they were never apart. You cannot shift one without the other.

So grouping data is not what Java is for. C does that, and you already knew how.

What C will not do is this:

```c
students[3].grade = 11.0;   /* still compiles, still runs */
```

The struct holds the data together. It does not defend it. There is still no way to make the account refuse.

## What Java adds

Two things, and one you will not need until week seven.

**The behaviour is attached to the thing.** `withdraw` is not a function that happens to take an account. It is something an account does. That is "ask, do not take" from last session, turned into something you can actually write down: the request has somewhere to arrive.

**The compiler decides who may touch the data.** Mark a field `private` and `students[3].grade = 11.0` stops being a line you should not write and becomes a line that does not compile. The rule stops being a convention and becomes something the machine checks, on every build, for everyone on the team, forever.

The third one is that a single request can run different code depending on what the thing actually is. That sounds strange now. Week seven.

> **If you remember one thing.** C already lets you group data. What Java adds is a compiler that enforces who is allowed to change it.

## The rule, moved inside

<svg viewBox="0 0 480 330" role="img" xmlns="http://www.w3.org/2000/svg"><title>The same rule scattered across functions, and the same rule inside an object</title><desc>The upper half shows an array of balances with three separate functions below it, each connected to the array and each containing its own copy of the balance check. The lower half shows a single Account box containing both the balance and the withdraw method, with the check written once inside it.</desc><text x="20" y="24" font-family="Inter, sans-serif" font-size="14" font-weight="600" fill="var(--c-muted)">Data here, rules over there</text><rect x="20" y="34" width="440" height="38" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="38" y="58" font-family="JetBrains Mono, monospace" font-size="14" fill="var(--c-ink)">balances</text><text x="120" y="58" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-muted)">{ 500.00, 1200.00, 30.00 }</text><text x="348" y="58" font-family="Inter, sans-serif" font-size="12" fill="var(--c-accent)">anyone can reach it</text><g stroke="var(--c-border)" stroke-width="1.5" fill="none"><line x1="88" y1="94" x2="88" y2="72"/><line x1="240" y1="94" x2="240" y2="72"/><line x1="392" y1="94" x2="392" y2="72"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="96" width="136" height="48" rx="6"/><rect x="172" y="96" width="136" height="48" rx="6"/><rect x="324" y="96" width="136" height="48" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-ink)"><text x="36" y="118">withdraw()</text><text x="188" y="118">transfer()</text><text x="340" y="118">monthlyFee()</text></g><g font-family="Inter, sans-serif" font-size="12" fill="var(--c-accent)"><text x="36" y="134">checks the balance</text><text x="188" y="134">checks it again</text><text x="340" y="134">and again</text></g><text x="20" y="164" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">One rule, written three times. Nothing stops a fourth place forgetting it.</text><line x1="20" y1="180" x2="460" y2="180" stroke="var(--c-border)" stroke-width="1" stroke-dasharray="4 4"/><text x="20" y="206" font-family="Inter, sans-serif" font-size="14" font-weight="600" fill="var(--c-muted)">Data and its rules together</text><rect x="100" y="216" width="280" height="90" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="120" y="240" font-family="Source Serif 4, Georgia, serif" font-size="17" font-weight="700" fill="var(--c-ink)">Account</text><line x1="120" y1="250" x2="360" y2="250" stroke="var(--c-border)" stroke-width="1.5"/><text x="120" y="272" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-ink)">balance</text><text x="215" y="272" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">only this object touches it</text><text x="120" y="294" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-ink)">withdraw()</text><text x="215" y="294" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">the rule, written once</text><text x="20" y="324" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The rule cannot be skipped, because it is part of the thing it protects.</text></svg>

## The first class

This is the account from last session, written down. We will build it together in the lecture, so watch rather than type.

```java
public class Account {

    private double balance;

    public Account(double openingBalance) {
        this.balance = openingBalance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
}
```

You are not expected to write this yet. Week 2 teaches every line. Five things to notice, and no more.

`balance` is `private`. That is the compiler enforcing the wall from last session's diagram.

The rule is inside, once. One place subtracts from a balance, so one place changes when the overdraft policy arrives.

`withdraw` can say no. The account decides, exactly as it did when the description was in English.

There is no index. An account is one thing, so its balance cannot drift away from its owner.

And `withdraw` does not print anything. It reports what happened and lets the caller decide what to say about it. That is deliberate, and in week ten you will be glad of it.

```java
Account ada = new Account(500.00);

ada.withdraw(200.00);   // true, balance is now 300.00
ada.withdraw(900.00);   // false, balance is unchanged
```

## Which things become classes

Last session you cut a problem into things on paper. Now the same skill with a filter, because not every thing you named deserves a class.

Three questions.

**Does it have data of its own that must stay correct?** An account has a balance. It has to be right, and it changes over time.

**Are there rules about that data?** You cannot withdraw more than the balance. Somebody must enforce that, and the best somebody is whoever owns the data.

**Can you ask it to do something?** You ask an account to accept a deposit. You do not ask a monthly total to do anything. You just calculate it.

Run those over a bank:

| Thing | Class? | Why |
|---|---|---|
| Account | Yes | Owns a balance, has rules, can be asked things |
| Customer | Yes | Owns a name and a set of accounts |
| Transaction | Yes | A record of what happened, with details of its own |
| Balance | No | A number that belongs to an account |
| Interest rate | No | A value, until the rules around it get complicated |
| Total holdings | No | Calculated from accounts, not a thing that exists |
| Withdrawing | No | Something an account does, not a thing |

The bottom three are where people go wrong, and they are the `Manager` trap from last session in a different costume.

## Common misreadings

??? note "\"Then everything should be private with a getter and a setter\""

    A class where every field is private and every field has a public getter and a public setter is not protected. It is the same open data with four extra lines per field, and `setBalance` hands back exactly the power that `private` took away.

    Ask what the thing is *for*. `Account` exposes `withdraw`, not `setBalance`, because withdrawing is what a bank account does and setting the balance to an arbitrary number is not. Week 3 is about this distinction.

??? note "\"My program works, so the design is fine\""

    Working is the minimum, not the goal. The grade tracker at the top of this page works too.

    Design is about what happens when the requirements change, and in this course they change every week on purpose. Sprint 2 is you rewriting Sprint 1 after you have felt the cost of your first design.

## Check yourself

!!! verify "Do this on your own code, not on mine"

    Find a program you wrote last year in C. Any of them, as long as it was more than about fifty lines.

    1.  Pick one change that was asked of you after you first wrote it, or invent a plausible one now.
    2.  Count the places in the file you had to edit, or would have to edit.
    3.  For each edit, write down what you had to already know in order to get it right. Not what you typed. What you had to know.
    4.  Find one piece of data in that program that any function could change, and name one rule about it that exists only in your head.

    That last one is the point of the session. Everyone will find one. Bring it to the seminar, because comparing them is more convincing than anything on this page.

## Think about it

1.  The grade rule lived in three functions and in your head. If you had written it in a comment at the top of the file, would anything have improved? Say precisely what a comment does and does not do. Now suppose the person reading that file next year does not speak your language.

2.  A `struct` fixed the parallel arrays, and it is available in C. So why did anyone bother inventing classes, when the drift problem was already solved? Answer without using the word encapsulation.

3.  `private` stops outside code from changing the balance. But `withdraw` is public, so outside code can still reduce it. What exactly was gained, if the money still leaves? Now a new rule arrives: withdrawals over 10,000 euro need a manager's approval. Where does it go, and how much existing code has to change?

4.  `withdraw` returns `true` or `false` and says nothing about why it failed. Was the amount negative, or was there not enough money? Decide whether that difference matters to whoever called it, and if it does, say what you would rather it returned. There is no clean answer available to you yet, which is why the question is here.

<div class="page-nav" markdown>
[Week 2. Your first class](first-class.md){ .page-nav__next }
</div>
