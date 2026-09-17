# Objects and State

<p class="meta"><span class="badge">Weeks 1 to 4</span><span class="badge">Module 1 of 4</span><span class="badge badge--accent">Sprint 1 opens in week 4</span></p>

This module is where you stop writing lists of instructions and start building things that hold their own data. By the end you can take a description of a problem, decide what the objects are, write them, and keep a collection of them.

Everything that follows in this course rests on these four weeks. Inheritance, testing, files and the final application all assume you can look at a problem and see the things in it. That skill is the actual subject of Module 1, and the Java syntax is just how you write it down.

## The lectures

<div class="grid cards" markdown>

-   :material-thought-bubble-outline:{ .lg .middle } **Week 1, session 1**

    ---

    **Thinking in things.** Cutting a problem into things that know something and can be asked to do something. No code at all.

    [:octicons-arrow-right-24: Start](thinking-in-things.md)

-   :material-lightbulb-outline:{ .lg .middle } **Week 1, session 2**

    ---

    **Why objects.** What breaks when a rule lives away from its data, and exactly what Java adds to the C you already know.

    [:octicons-arrow-right-24: Open](why-objects.md)

-   :material-cube-outline:{ .lg .middle } **Week 2**

    ---

    **Your first class.** Fields, constructors and methods. What a reference points at, and why two variables can be the same object.

    [:octicons-arrow-right-24: Open](first-class.md)

-   :material-shield-outline:{ .lg .middle } **Week 3**

    ---

    **State and behaviour.** Keeping an object valid from the moment it is born. Invariants, and what belongs to the class rather than the object.

    [:octicons-arrow-right-24: Open](state-and-behaviour.md)

-   :material-format-list-bulleted:{ .lg .middle } **Week 4**

    ---

    **Holding many objects.** Arrays and lists, iterating over your own objects, and a first look at looking things up by key.

    [:octicons-arrow-right-24: Open](many-objects.md)

</div>

## How a week works

Four hours, split in two.

**The lecture, two hours.** The pages above, plus live coding. Read them with the editor open rather than on your phone.

**The seminar, two hours.** Where you write code yourself. Week 1's seminar is [setting up your machine](../00-tools/index.md), which you need finished before week 2. After that, seminars are the [exercises](exercises.md).

The division matters. The lectures are about deciding what to build. The seminars are about building it. People who skip the first half write code faster and design worse.

## One example, growing

Every tutorial in this module builds the same thing: a small bank. It is a good problem to learn on because everyone already knows the rules, and because those rules are strict. A balance cannot go negative. An account number never changes. Money that leaves one place has to arrive somewhere.

Strict rules are exactly what objects are for.

<svg viewBox="0 0 520 180" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the bank example grows over three weeks</title><desc>Three panels side by side joined by arrows. In week two an Account class holds a balance. In week three the same Account gains validation and an identifier that cannot change. In week four a Customer class holds a list of several Accounts.</desc><g font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-muted)"><text x="20" y="26">Week 2</text><text x="189" y="26">Week 3</text><text x="358" y="26">Week 4</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="38" width="150" height="112" rx="8"/><rect x="185" y="38" width="150" height="112" rx="8"/></g><rect x="354" y="38" width="150" height="112" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g stroke="var(--c-accent)" stroke-width="4" stroke-linecap="round"><line x1="34" y1="60" x2="90" y2="60"/><line x1="203" y1="60" x2="259" y2="60"/><line x1="372" y1="60" x2="428" y2="60"/></g><g stroke="var(--c-border)" stroke-width="1.5"><line x1="34" y1="72" x2="148" y2="72"/><line x1="203" y1="72" x2="317" y2="72"/><line x1="372" y1="72" x2="486" y2="72"/></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-muted)"><text x="34" y="94">balance</text><text x="203" y="94">balance</text><text x="203" y="112">id, fixed</text><text x="203" y="130">rules enforced</text><text x="372" y="94">name</text><text x="372" y="112">accounts</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="1.5"><rect x="372" y="120" width="34" height="18" rx="3"/><rect x="410" y="120" width="34" height="18" rx="3"/><rect x="448" y="120" width="34" height="18" rx="3"/></g><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="170" y1="94" x2="181" y2="94"/><polyline points="176,89 182,94 176,99"/><line x1="339" y1="94" x2="350" y2="94"/><polyline points="345,89 351,94 345,99"/></g></svg>

You are not starting a new project every week. You are extending one, in one folder, in the repository you set up in week 1. In week 4 that folder becomes the starting point for Sprint 1.

!!! note "The exercises use a different problem on purpose"

    The lectures build a bank. The [exercises](exercises.md) build a university system: students, professors, courses, classrooms.

    That is deliberate. Following a worked example teaches you the syntax. Applying the same idea to a problem you have not been shown teaches you the skill. The second one is what the exam and the sprints ask for.

## By the end of this module you can

-   Read a description of a system and say what each thing knows, what it does, and what it can refuse
-   Decide which of those things deserve to be classes, and say why the others do not
-   Write a class with private fields, a constructor that refuses bad data, and methods that do one thing
-   Explain what a reference is, and predict what happens when two variables point at the same object
-   Hold many objects in a list, search it, and work through it
-   Tell the difference between what belongs to one object and what belongs to the whole class

## A note on reading these pages

Type the code rather than copying it. The compiler errors you make are half the lesson and you will not make them by pasting.

Each page ends with a task that proves you understood, and a few questions with no answer you can look up. Those questions are what we argue about in the seminar, so arrive with an opinion rather than a correct answer.

All worked code lives in the course repository, one folder per week. Read the page first, write it yourself, then compare. Looking at the finished version before you have tried is the fastest way to feel like you understand something you cannot yet do.

<div class="page-nav" markdown>
[Week 1, session 1. Thinking in things](thinking-in-things.md){ .page-nav__next }
</div>
