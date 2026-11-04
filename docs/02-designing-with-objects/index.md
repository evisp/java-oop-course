# Designing with Objects

<p class="meta"><span class="badge">Weeks 5 to 8</span><span class="badge">Module 2 of 4</span><span class="badge">8 lectures, 4 seminars</span><span class="badge badge--accent">Sprint 2 opens in week 8</span></p>

One class is easy. This module is about what happens when there are several of them, several kinds of them, and they have to work together. By the end you can decide which class owns a piece of work, say what makes two of your objects the same object, and choose between inheritance and holding another object rather than guessing.

Module 1 taught you to build one good object. If you can do that, this module is the one that turns it into a design.

## How a week works

The same shape as Module 1. Four hours, split in two.

**The lecture, two hours.** Two sessions of fifty minutes, the pages below, plus code written live. The finished code for each week is in the [lecture repository](https://github.com/evisp/java-oop-lectures), one folder per week. Read the page first and write it yourself second.

**The seminar, two hours.** You keep building [the university system](../01-objects-and-state/university-system.md), the same one from Module 1. It now needs things that work together, things that know what makes them the same, and a family of people who are not all the same kind of person.

## Week 5. Working together

<div class="grid cards" markdown>

-   :material-bank-transfer:{ .lg .middle } **Objects working together**

    ---

    Ada wants to send 200 to Alan and no class can do it. Writing the class that exists because a job needed a home.

    [:octicons-arrow-right-24: Lecture 1](working-together.md)

-   :material-equal:{ .lg .middle } **Telling objects apart**

    ---

    What makes two of your objects the same object. `equals`, `hashCode`, and the mistake that compiles and never runs.

    [:octicons-arrow-right-24: Lecture 2](telling-apart.md)

-   :material-laptop:{ .lg .middle } **Making them work together**

    ---

    Enrolment touches two of your objects at once. Find the rule that has no owner and give it one.

    Seminar opens in week 5.

-   :material-file-document-outline:{ .lg .middle } **Week 5 slides**

    ---

    The transfer with nowhere to live, and two objects that are one account.

    Slides open in week 5.

</div>

## Week 6. One job each

<div class="grid cards" markdown>

-   :material-scissors-cutting:{ .lg .middle } **One job per class**

    ---

    Your `main` method reads input, does arithmetic and prints results. Splitting the model from the thing that talks.

    [:octicons-arrow-right-24: Lecture 1](one-job.md)

-   :material-format-list-checks:{ .lg .middle } **A fixed set of values**

    ---

    Three strings that the compiler never checked. Enums, and constants that carry data of their own.

    [:octicons-arrow-right-24: Lecture 2](enums.md)

-   :material-laptop:{ .lg .middle } **Splitting what you built**

    ---

    Take the printing out of your own classes, then find the strings that should have been an enum.

    Seminar opens in week 6.

-   :material-file-document-outline:{ .lg .middle } **Week 6 slides**

    ---

    The model that stops talking, and the switch an enum cannot remove.

    Slides open in week 6.

</div>

## Week 7. A family of things

<div class="grid cards" markdown>

-   :material-file-tree:{ .lg .middle } **Building on another class**

    ---

    One object in two layers. `extends`, `super`, and why `protected` is usually the wrong answer.

    [:octicons-arrow-right-24: Lecture 1](inheritance.md)

-   :material-alert-outline:{ .lg .middle } **When inheritance hurts**

    ---

    Overriding, `@Override`, and the subclass that compiles, looks sensible and breaks every caller.

    [:octicons-arrow-right-24: Lecture 2](inheritance-hurts.md)

-   :material-laptop:{ .lg .middle } **A family of things**

    ---

    Students and lecturers are both people. Find the thing in your design that looks like inheritance and is not.

    Seminar opens in week 7.

-   :material-file-document-outline:{ .lg .middle } **Week 7 slides**

    ---

    The object in two layers, and the test a subclass has to pass.

    Slides open in week 7.

</div>

## Week 8. Promises and questions

<div class="grid cards" markdown>

-   :material-shape-outline:{ .lg .middle } **Promises a class must keep**

    ---

    `abstract`. The class you should not be able to build, and the method the parent must not be allowed to guess.

    Lecture opens in week 8.

-   :material-call-split:{ .lg .middle } **Many classes, one call**

    ---

    Interfaces. Why `List` is on the left and `ArrayList` on the right, and how to sort in one line.

    Lecture opens in week 8.

-   :material-laptop:{ .lg .middle } **Many kinds, one loop**

    ---

    One loop over a mixed list of your own objects. Sprint 2 is briefed in this session.

    Seminar opens in week 8.

-   :material-file-document-outline:{ .lg .middle } **Week 8 slides**

    ---

    The switch, four weeks later, and the loop that replaced it.

    Slides open in week 8.

</div>

## The same bank, four more weeks

The example does not change. You are still extending the bank you started in week 2, and the point of the module is visible in how it grows.

<svg viewBox="0 0 520 170" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the bank grows across Module 2</title><desc>Four panels in a row joined by arrows. In week five a Bank class appears above the customers and owns the transfer. In week six the model separates from a console class and an account type enum arrives. In week seven the single Account class becomes three subclasses. In week eight the parent becomes abstract and an interface cuts across unrelated classes.</desc><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-muted)"><text x="16" y="24">Week 5</text><text x="144" y="24">Week 6</text><text x="272" y="24">Week 7</text><text x="400" y="24">Week 8</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="12" y="36" width="110" height="104" rx="8"/><rect x="140" y="36" width="110" height="104" rx="8"/><rect x="268" y="36" width="110" height="104" rx="8"/></g><rect x="396" y="36" width="110" height="104" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g stroke="var(--c-accent)" stroke-width="4" stroke-linecap="round"><line x1="28" y1="56" x2="76" y2="56"/><line x1="156" y1="56" x2="204" y2="56"/><line x1="284" y1="56" x2="332" y2="56"/><line x1="412" y1="56" x2="460" y2="56"/></g><g stroke="var(--c-border)" stroke-width="1.5"><line x1="28" y1="66" x2="106" y2="66"/><line x1="156" y1="66" x2="234" y2="66"/><line x1="284" y1="66" x2="362" y2="66"/><line x1="412" y1="66" x2="490" y2="66"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="28" y="86">Bank</text><text x="28" y="102">transfer</text><text x="28" y="118">equals</text><text x="156" y="86">model</text><text x="156" y="102">console</text><text x="156" y="118">enum</text><text x="284" y="86">Checking</text><text x="284" y="102">Savings</text><text x="284" y="118">TermDeposit</text><text x="412" y="86">abstract</text><text x="412" y="102">interface</text><text x="412" y="118">one loop</text></g><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="126" y1="88" x2="136" y2="88"/><polyline points="131,83 137,88 131,93"/><line x1="254" y1="88" x2="264" y2="88"/><polyline points="259,83 265,88 259,93"/><line x1="382" y1="88" x2="392" y2="88"/><polyline points="387,83 393,88 387,93"/></g></svg>

Week 5 adds a class so a rule has somewhere to live. Week 6 splits what was doing two jobs. Week 7 turns one class into a family. Week 8 says what a family member must promise, and what unrelated classes can still have in common.

## By the end of this module you can

-   Decide which class owns a piece of work, and which one only asks for it
-   Write a class whose reason to exist is a job rather than a noun in the problem
-   Say what makes two of your objects the same object, and make the library agree
-   Keep a model that never prints and never reads input, and say why that is worth it
-   Replace a set of strings with a type the compiler checks
-   Build one class on another, and explain what a subclass may and may not change
-   Recognise the subclass that compiles and still breaks every caller
-   Choose between inheritance and holding another object, and give the reason
-   Write one loop that works on several kinds of object without asking what it has

## Sprint 2

**Sprint 2 opens in week 8** and runs into Module 3. You do not start a new project. You take the application you shipped for Sprint 1, which you now know enough to be unhappy with, and redesign it with everything in these four weeks.

That is the point of running it this way round. Nobody believes an argument about design until they have felt the cost of a weak one in their own code.

<div class="page-nav" markdown>
[Week 5, Objects working together](working-together.md){ .page-nav__next }
</div>
