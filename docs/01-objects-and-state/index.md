# Objects and State

<p class="meta"><span class="badge">Weeks 1 to 4</span><span class="badge">Module 1 of 4</span><span class="badge">8 lectures</span><span class="badge badge--accent">Sprint 1 opens in week 4</span></p>

This module is where you stop writing lists of instructions and start building things that hold their own data. By the end you can take a description of a problem, decide what the objects are, write them so they cannot be put into an invalid state, and hold many of them.

Everything that follows in this course rests on these four weeks. Inheritance, testing, files and the final application all assume you can look at a problem and see the things in it. That skill is the actual subject of Module 1, and the Java syntax is just how you write it down.

## Week 1. Seeing objects

<div class="grid cards" markdown>

-   :material-thought-bubble-outline:{ .lg .middle } **Thinking in things**

    ---

    Cutting a problem into things that know something and can be asked to do something. No code at all.

    [:octicons-arrow-right-24: Session 1](thinking-in-things.md)

-   :material-lightbulb-outline:{ .lg .middle } **Why objects**

    ---

    What breaks when a rule lives away from its data, and exactly what Java adds to the C you already know.

    [:octicons-arrow-right-24: Session 2](why-objects.md)

</div>

## Week 2. Writing them down

<div class="grid cards" markdown>

-   :material-cube-outline:{ .lg .middle } **Your first class**

    ---

    Fields, a constructor, methods and `toString`. The first code of the bank you will build all semester.

    [:octicons-arrow-right-24: Session 1](first-class.md)

-   :material-arrow-top-right:{ .lg .middle } **What a variable really holds**

    ---

    References, aliasing, `null`, and why `==` on objects almost never answers the question you meant.

    [:octicons-arrow-right-24: Session 2](references.md)

</div>

## Week 3. Keeping them valid

<div class="grid cards" markdown>

-   :material-shield-outline:{ .lg .middle } **Objects that refuse**

    ---

    Invariants, the constructor as a gate, and the difference between a caller asking for too much and a caller passing you nonsense.

    [:octicons-arrow-right-24: Session 1](objects-that-refuse.md)

-   :material-lock-outline:{ .lg .middle } **What belongs to whom**

    ---

    `final` fields, immutability, and values that belong to the class rather than to any one object.

    [:octicons-arrow-right-24: Session 2](what-belongs-to-whom.md)

</div>

## Week 4. Many of them

<div class="grid cards" markdown>

-   :material-format-list-bulleted:{ .lg .middle } **Many objects**

    ---

    Arrays, `List` and `ArrayList`, searching and looping, and what a list of objects actually contains.

    [:octicons-arrow-right-24: Session 1](many-objects.md)

-   :material-account-multiple-outline:{ .lg .middle } **Objects that own other objects**

    ---

    A customer with accounts. Keeping a collection private, and coordinating work without doing it yourself.

    [:octicons-arrow-right-24: Session 2](owning-objects.md)

</div>

## How a week works

Four hours, split in two.

**The lecture, two hours.** Two sessions of fifty minutes, the pages above, plus live coding. Read them with the editor open rather than on your phone.

**The seminar, two hours.** Where you write code yourself. Week 1's seminar is [setting up your machine](../00-tools/index.md), which you need finished before week 2. After that, seminars are practice exercises.

The division matters. The lectures are about deciding what to build. The seminars are about building it. People who skip the first half write code faster and design worse.

## One example, growing

Every session in this module builds the same thing: a small bank. It is a good problem to learn on because everyone already knows the rules, and because those rules are strict. A balance cannot go negative. An account number never changes. Money that leaves one place has to arrive somewhere.

Strict rules are exactly what objects are for.

<svg viewBox="0 0 520 180" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the bank example grows over three weeks</title><desc>Three panels side by side joined by arrows. In week two an Account class holds a balance. In week three the same Account gains enforced rules and an identifier that cannot change. In week four a Customer class holds a list of several Accounts.</desc><g font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-muted)"><text x="20" y="26">Week 2</text><text x="189" y="26">Week 3</text><text x="358" y="26">Week 4</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="38" width="150" height="112" rx="8"/><rect x="185" y="38" width="150" height="112" rx="8"/></g><rect x="354" y="38" width="150" height="112" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g stroke="var(--c-accent)" stroke-width="4" stroke-linecap="round"><line x1="34" y1="60" x2="90" y2="60"/><line x1="203" y1="60" x2="259" y2="60"/><line x1="372" y1="60" x2="428" y2="60"/></g><g stroke="var(--c-border)" stroke-width="1.5"><line x1="34" y1="72" x2="148" y2="72"/><line x1="203" y1="72" x2="317" y2="72"/><line x1="372" y1="72" x2="486" y2="72"/></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-muted)"><text x="34" y="94">balance</text><text x="203" y="94">balance</text><text x="203" y="112">id, fixed</text><text x="203" y="130">rules enforced</text><text x="372" y="94">name</text><text x="372" y="112">accounts</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="1.5"><rect x="372" y="120" width="34" height="18" rx="3"/><rect x="410" y="120" width="34" height="18" rx="3"/><rect x="448" y="120" width="34" height="18" rx="3"/></g><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="170" y1="94" x2="181" y2="94"/><polyline points="176,89 182,94 176,99"/><line x1="339" y1="94" x2="350" y2="94"/><polyline points="345,89 351,94 345,99"/></g></svg>

You are not starting a new project every week. You are extending one, in one folder, in the repository you set up in week 1. In week 4 that folder becomes the starting point for Sprint 1.

!!! note "The seminar exercises use a different problem on purpose"

    The lectures build a bank. The seminar exercises build a university system: students, professors, courses, classrooms.

    That is deliberate. Following a worked example teaches you the syntax. Applying the same idea to a problem you have not been shown teaches you the skill. The second one is what the exam and the sprints ask for.

## By the end of this module you can

-   Read a description of a system and say what each thing knows, what it does, and what it can refuse
-   Decide which of those things deserve to be classes, and say why the others do not
-   Explain what a reference is, and predict what happens when two variables point at the same object
-   Write a class with private fields, a constructor that refuses bad data, and methods that do one thing
-   Tell the difference between an outcome a caller should expect and a mistake a caller should have prevented
-   Say whether a value belongs to one object, to the class, or to nobody after the moment it was set
-   Hold many objects in a list, search it, and keep it safe from the code outside

## A note on reading these pages

Type the code rather than copying it. The compiler errors you make are half the lesson and you will not make them by pasting.

Each page ends with a task that proves you understood, and a few questions with no answer you can look up. Those questions are what we argue about in the seminar, so arrive with an opinion rather than a correct answer.

All worked code lives in the course repository, one folder per week. Read the page first, write it yourself, then compare. Looking at the finished version before you have tried is the fastest way to feel like you understand something you cannot yet do.

<div class="page-nav" markdown>
[Week 1, session 1. Thinking in things](thinking-in-things.md){ .page-nav__next }
</div>
