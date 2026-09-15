---
hide:
  - navigation
  - toc
---

<div class="rm" markdown>

<div class="rm-hero" markdown>
<div class="rm-hero__inner" markdown>
<div class="rm-hero__text" markdown>

# The path

Fourteen weeks, four modules, and one application that grows the whole way. Each module opens the week we reach it, so you can always see where you are and what is coming.

</div>
<div class="rm-hero__art">
<svg viewBox="0 0 360 210" role="img" xmlns="http://www.w3.org/2000/svg"><title>The four modules as a rising path</title><desc>A path rises from the lower left to the upper right, passing through four markers. The first marker is a single block, the second is two blocks joined together, the third is a block inside a protective ring, and the fourth is an application window. The first section of the path is drawn in a solid accent colour to show the part of the course already covered.</desc><path d="M40 168 C 88 168 90 128 137 128 C 184 128 186 88 233 88 C 280 88 284 48 330 48" fill="none" stroke="var(--c-border)" stroke-width="2" stroke-dasharray="6 7" stroke-linecap="round"/><path d="M40 168 C 88 168 90 128 137 128" fill="none" stroke="var(--c-accent)" stroke-width="2.5" stroke-linecap="round"/><g fill="var(--c-surface)" stroke="var(--c-accent)" stroke-width="2"><rect x="26" y="154" width="28" height="28" rx="6"/></g><line x1="33" y1="163" x2="47" y2="163" stroke="var(--c-accent)" stroke-width="3.5" stroke-linecap="round"/><line x1="33" y1="172" x2="43" y2="172" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><line x1="133" y1="120" x2="145" y2="132" stroke="var(--c-border)" stroke-width="1.5"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="117" y="108" width="22" height="22" rx="5"/><rect x="137" y="126" width="22" height="22" rx="5"/></g><line x1="123" y1="116" x2="133" y2="116" stroke="var(--c-accent)" stroke-width="3" stroke-linecap="round"/><line x1="143" y1="134" x2="153" y2="134" stroke="var(--c-accent)" stroke-width="3" stroke-linecap="round"/><circle cx="233" cy="88" r="21" fill="none" stroke="var(--c-accent)" stroke-width="1.5" stroke-dasharray="4 5" opacity="0.65"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="220" y="75" width="26" height="26" rx="6"/></g><line x1="226" y1="84" x2="238" y2="84" stroke="var(--c-accent)" stroke-width="3.5" stroke-linecap="round"/><line x1="226" y1="93" x2="234" y2="93" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="308" y="30" width="44" height="36" rx="6"/></g><path d="M308 36a6 6 0 0 1 6-6h32a6 6 0 0 1 6 6v4h-44z" fill="var(--c-accent)" opacity="0.85"/><line x1="314" y1="50" x2="330" y2="50" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.45"/><line x1="314" y1="58" x2="340" y2="58" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.45"/></svg>
</div>
</div>
</div>

<!-- =====================================================================
     RELEASING A MODULE
     1. change data-state="locked" to data-state="open" on the module
     2. in its .rm-foot, swap the <span class="rm-when"> line for the
        commented-out button line sitting next to it
     3. do the matching two edits in mkdocs.yml (draft_docs and nav)
     ===================================================================== -->

<div class="rm-track" markdown>

<div class="rm-step" data-state="open" markdown>
<div class="rm-art">
<svg viewBox="0 0 120 120" role="img" xmlns="http://www.w3.org/2000/svg"><title>One object holding its own data</title><desc>A single rounded rectangle with a coloured title bar and two lines of fields inside it, representing one object that owns its data.</desc><rect x="20" y="28" width="80" height="64" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><line x1="32" y1="46" x2="66" y2="46" stroke="var(--c-accent)" stroke-width="5" stroke-linecap="round"/><line x1="32" y1="60" x2="88" y2="60" stroke="var(--c-border)" stroke-width="1.5"/><line x1="32" y1="72" x2="80" y2="72" stroke="var(--c-muted)" stroke-width="3" stroke-linecap="round" opacity="0.4"/><line x1="32" y1="82" x2="60" y2="82" stroke="var(--c-muted)" stroke-width="3" stroke-linecap="round" opacity="0.4"/></svg>
</div>
<div class="rm-body" markdown>

<p class="rm-meta"><span class="rm-num">Module 1</span><span class="rm-weeks">Weeks 1 to 4</span></p>

### Objects and State

You stop writing lists of instructions and start building things that hold their own data.

<p class="rm-topics"><span>Tools and first code</span><span>Classes and objects</span><span>State and behaviour</span><span>Holding many objects</span></p>

<p class="rm-label">By the end you can</p>

-   Turn a description of a problem into a set of classes
-   Explain what a reference actually points at, and why that matters
-   Keep a collection of your own objects and work through it

<div class="rm-foot" markdown>
[Open module](01-foundations/index.md){ .md-button }
<span class="rm-flag">Sprint 1 opens in week 4</span>
</div>

</div>
</div>

<div class="rm-step" data-state="locked" markdown>
<div class="rm-art">
<svg viewBox="0 0 120 120" role="img" xmlns="http://www.w3.org/2000/svg"><title>Objects connected to one another</title><desc>Three small rounded rectangles joined by lines, showing objects that collaborate rather than standing alone.</desc><line x1="42" y1="38" x2="78" y2="38" stroke="var(--c-border)" stroke-width="1.5"/><line x1="36" y1="52" x2="56" y2="76" stroke="var(--c-border)" stroke-width="1.5"/><line x1="84" y1="52" x2="64" y2="76" stroke="var(--c-border)" stroke-width="1.5"/><rect x="12" y="24" width="48" height="28" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="60" y="24" width="48" height="28" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="36" y="76" width="48" height="28" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><line x1="20" y1="34" x2="40" y2="34" stroke="var(--c-accent)" stroke-width="4" stroke-linecap="round"/><line x1="68" y1="34" x2="88" y2="34" stroke="var(--c-accent)" stroke-width="4" stroke-linecap="round"/><line x1="44" y1="86" x2="64" y2="86" stroke="var(--c-accent)" stroke-width="4" stroke-linecap="round"/><line x1="20" y1="44" x2="52" y2="44" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><line x1="68" y1="44" x2="100" y2="44" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><line x1="44" y1="96" x2="76" y2="96" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/></svg>
</div>
<div class="rm-body" markdown>

<p class="rm-meta"><span class="rm-num">Module 2</span><span class="rm-weeks">Weeks 5 to 8</span></p>

### Designing with Objects

One class is easy. The skill is deciding how classes relate to each other, and knowing when the popular answer is the wrong one.

<p class="rm-topics"><span>Identity and equality</span><span>Inheritance, and when not to</span><span>Polymorphism</span><span>Interfaces and enums</span></p>

<p class="rm-label">By the end you can</p>

-   Put your own objects in a set and have it behave the way you expect
-   Choose between inheritance and composition, and say why
-   Write code against an interface instead of a specific class

<div class="rm-foot" markdown>
<span class="rm-when">Opens in week 5</span>
<!-- [Open module](02-core-oop/index.md){ .md-button } -->
<span class="rm-flag">Sprint 2 opens in week 8</span>
</div>

</div>
</div>

<div class="rm-step" data-state="locked" markdown>
<div class="rm-art">
<svg viewBox="0 0 120 120" role="img" xmlns="http://www.w3.org/2000/svg"><title>An object protected and tested</title><desc>A rounded rectangle inside a dashed protective boundary, with a tick mark beside it, representing code that is guarded against failure and checked by tests.</desc><rect x="10" y="22" width="100" height="76" rx="12" fill="none" stroke="var(--c-accent)" stroke-width="1.5" stroke-dasharray="5 5" opacity="0.7"/><rect x="26" y="38" width="68" height="44" rx="7" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><line x1="36" y1="52" x2="62" y2="52" stroke="var(--c-accent)" stroke-width="4.5" stroke-linecap="round"/><line x1="36" y1="64" x2="84" y2="64" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><line x1="36" y1="72" x2="70" y2="72" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><polyline points="78,88 85,95 99,80" fill="none" stroke="var(--c-accent)" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round"/></svg>
</div>
<div class="rm-body" markdown>

<p class="rm-meta"><span class="rm-num">Module 3</span><span class="rm-weeks">Weeks 9 to 12</span></p>

### Robust and Modern Java

Code that survives bad input, saves what it made, and proves it works. This is where your programs start behaving like real software.

<p class="rm-topics"><span>When things go wrong</span><span>Proving it works</span><span>Data that outlives the program</span><span>Lambdas and streams</span></p>

<p class="rm-label">By the end you can</p>

-   Fail honestly with a clear message instead of crashing
-   Write tests before you claim something works
-   Save your data and load it back next time
-   Do in three lines what used to take fifteen

<div class="rm-foot" markdown>
<span class="rm-when">Opens in week 9</span>
<!-- [Open module](03-io-exceptions/index.md){ .md-button } -->
<span class="rm-flag">Sprint 3 opens in week 12</span>
</div>

</div>
</div>

<div class="rm-step" data-state="locked" markdown>
<div class="rm-art">
<svg viewBox="0 0 120 120" role="img" xmlns="http://www.w3.org/2000/svg"><title>An application window built on top of your objects</title><desc>An application window with a title bar, a sidebar and content, sitting above a smaller object below it, showing that the interface rests on the model built earlier in the course.</desc><rect x="14" y="20" width="92" height="58" rx="7" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><path d="M14 27a7 7 0 0 1 7-7h78a7 7 0 0 1 7 7v5H14z" fill="var(--c-accent)" opacity="0.85"/><circle cx="23" cy="26" r="2" fill="var(--c-surface)"/><circle cx="31" cy="26" r="2" fill="var(--c-surface)"/><line x1="46" y1="32" x2="46" y2="78" stroke="var(--c-border)" stroke-width="1.5"/><line x1="24" y1="42" x2="38" y2="42" stroke="var(--c-muted)" stroke-width="3" stroke-linecap="round" opacity="0.45"/><line x1="24" y1="52" x2="38" y2="52" stroke="var(--c-muted)" stroke-width="3" stroke-linecap="round" opacity="0.45"/><line x1="24" y1="62" x2="34" y2="62" stroke="var(--c-muted)" stroke-width="3" stroke-linecap="round" opacity="0.45"/><rect x="56" y="42" width="38" height="12" rx="3" fill="none" stroke="var(--c-border)" stroke-width="1.5"/><line x1="56" y1="64" x2="94" y2="64" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><line x1="56" y1="71" x2="80" y2="71" stroke="var(--c-muted)" stroke-width="2.5" stroke-linecap="round" opacity="0.4"/><line x1="60" y1="78" x2="60" y2="90" stroke="var(--c-accent)" stroke-width="1.5" stroke-dasharray="3 3"/><rect x="38" y="90" width="44" height="20" rx="5" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><line x1="46" y1="100" x2="64" y2="100" stroke="var(--c-accent)" stroke-width="4" stroke-linecap="round"/></svg>
</div>
<div class="rm-body" markdown>

<p class="rm-meta"><span class="rm-num">Module 4</span><span class="rm-weeks">Weeks 13 to 14</span></p>

### Building an Application

You put a window on the program you have been growing all semester. If the design underneath is good, this part is quick. That is the whole lesson.

<p class="rm-topics"><span>Putting a face on it</span><span>Making it work</span></p>

<p class="rm-label">By the end you can</p>

-   Build a desktop application people can open and use
-   Connect what happens on screen to the objects underneath
-   Package it so it runs on someone else's machine

<div class="rm-foot" markdown>
<span class="rm-when">Opens in week 13</span>
<!-- [Open module](04-modern-java/index.md){ .md-button } -->
<span class="rm-flag">Sprint 3 due in week 14</span>
</div>

</div>
</div>

</div>

## The three sprints

One application. You do not start over, you keep going.

<div class="rm-sprints" markdown>

-   **Sprint 1** &middot; weeks 4 to 7

    Your first working program, built from classes you designed yourself. It runs in the console and it does something real.

-   **Sprint 2** &middot; weeks 8 to 12

    The same program, redesigned now that you know what a weak design costs. It saves your data, it handles mistakes, and it has tests.

-   **Sprint 3** &middot; weeks 12 to 14

    The same program again, with a window on top and packaged so you can send it to someone.

</div>

## What you walk away with

<div class="rm-skills" markdown>

-   **Thinking in objects**

    ---

    -   Turn a problem description into classes
    -   Know what a reference points at
    -   Read an error and find the cause
    -   Keep and work through collections

-   **Design judgement**

    ---

    -   Identity and equality that behave
    -   Inheritance or composition, on purpose
    -   Program to an interface
    -   Model a domain with records and enums

-   **Software that holds up**

    ---

    -   Handle failure honestly
    -   Test your own work with JUnit
    -   Read and write files safely
    -   Process data with streams

-   **Shipping it**

    ---

    -   Build and manage dependencies with Maven
    -   Build a desktop application
    -   Package it so others can run it
    -   Keep a project on GitHub worth showing

</div>

<div class="rm-cta" markdown>

Ready to start?

[Set up your tools](00-tools/index.md){ .md-button .md-button--primary }

</div>

</div>
