# Thinking in things

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 1, session 1</span><span class="badge">No prerequisites</span></p>

By the end of this page you can look at a problem and describe it as a set of things that know something and can be asked to do something, rather than as a list of steps. You can also say who should be responsible for each rule in a system.

There is no Java on this page. Not one line. This session is about a way of seeing a problem, and you cannot see it clearly while you are also worrying about semicolons. The code starts in session 2.

## A cash machine, told twice

Here is a withdrawal at a cash machine, described the way you have always described programs.

> Read the card. Look up the account. Check that the balance is enough. Check the daily limit. Subtract the amount. Count out the notes. Write a line in the log.

Seven steps in order. You could write that today.

Now here is the same withdrawal, described differently.

> There is a **card**, which knows whose account it belongs to. There is an **account**, which knows its balance and decides whether a withdrawal is allowed. There is a **cash drawer**, which knows how many notes it holds and hands them out. There is a **log**, which records what happened.
>
> The machine asks the card whose account this is. It asks the account to allow a withdrawal of fifty euro. The account decides, and says yes or no. If yes, the machine asks the drawer for the notes and tells the log.

Same machine. Same money. Nothing has been added and nothing has been removed.

What changed is where you put the boundaries. The first description cuts the problem into **steps**. The second cuts it into **things**.

That is the whole paradigm. Everything else this semester is consequences.

<svg viewBox="0 0 480 330" role="img" xmlns="http://www.w3.org/2000/svg"><title>One problem cut into steps, and the same problem cut into things</title><desc>The upper half shows five steps in a row, each joined by a line to a single shared store of data underneath, and the lines cross one another. The lower half shows three boxes named Card, Account and Cash drawer, each listing what it knows and what it does, joined to each other by two connections.</desc><text x="20" y="22" font-family="Inter, sans-serif" font-size="14" font-weight="600" fill="var(--c-muted)">Cut into steps</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="32" width="84" height="34" rx="5"/><rect x="109" y="32" width="84" height="34" rx="5"/><rect x="198" y="32" width="84" height="34" rx="5"/><rect x="287" y="32" width="84" height="34" rx="5"/><rect x="376" y="32" width="84" height="34" rx="5"/></g><g font-family="Inter, sans-serif" font-size="11" fill="var(--c-ink)" text-anchor="middle"><text x="62" y="53">read card</text><text x="151" y="53">check limit</text><text x="240" y="53">subtract</text><text x="329" y="53">dispense</text><text x="418" y="53">write log</text></g><g stroke="var(--c-border)" stroke-width="1.5" fill="none"><line x1="62" y1="66" x2="390" y2="104"/><line x1="151" y1="66" x2="90" y2="104"/><line x1="240" y1="66" x2="300" y2="104"/><line x1="329" y1="66" x2="60" y2="104"/><line x1="418" y1="66" x2="200" y2="104"/></g><rect x="20" y="106" width="440" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-border)" stroke-width="2"/><text x="38" y="128" font-family="Inter, sans-serif" font-size="13" fill="var(--c-ink)">the balance, the daily limit, the notes, the log</text><text x="20" y="158" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Every step needs the data, so the data belongs to nobody in particular.</text><line x1="20" y1="176" x2="460" y2="176" stroke="var(--c-border)" stroke-width="1" stroke-dasharray="4 4"/><text x="20" y="202" font-family="Inter, sans-serif" font-size="14" font-weight="600" fill="var(--c-muted)">Cut into things</text><g stroke="var(--c-border)" stroke-width="1.5"><line x1="160" y1="258" x2="170" y2="258"/><line x1="310" y1="258" x2="320" y2="258"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="212" width="140" height="90" rx="8"/><rect x="320" y="212" width="140" height="90" rx="8"/></g><rect x="170" y="212" width="140" height="90" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)"><text x="36" y="234">Card</text><text x="186" y="234">Account</text><text x="336" y="234">Cash drawer</text></g><g stroke="var(--c-border)" stroke-width="1.5"><line x1="36" y1="242" x2="144" y2="242"/><line x1="186" y1="242" x2="294" y2="242"/><line x1="336" y1="242" x2="444" y2="242"/></g><g font-family="Inter, sans-serif" font-size="11" fill="var(--c-muted)"><text x="36" y="262">knows: whose</text><text x="186" y="262">knows: balance</text><text x="336" y="262">knows: notes left</text></g><g font-family="Inter, sans-serif" font-size="11" fill="var(--c-accent)"><text x="36" y="284">does: identify</text><text x="186" y="284">does: allow, refuse</text><text x="336" y="284">does: hand out</text></g><text x="20" y="324" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Each thing keeps what it knows and decides what it does.</text></svg>

Look at the top half again. Five steps, one pile of data, and every step reaching into it. Nothing in that picture is responsible for the balance. The balance is just lying there.

## A thing knows, and a thing does

An object is not complicated. It is two things bolted together, and it is worth saying the definition plainly because everything else rests on it.

<svg viewBox="0 0 480 220" role="img" xmlns="http://www.w3.org/2000/svg"><title>An object holds what it knows and what it does</title><desc>A single box named Account divided into two columns. The left column lists what it knows: the balance, who owns it, whether it is frozen. The right column lists what it does: accept a deposit, allow a withdrawal, refuse one.</desc><rect x="70" y="24" width="340" height="156" rx="10" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="92" y="52" font-family="Source Serif 4, Georgia, serif" font-size="19" font-weight="700" fill="var(--c-ink)">Account</text><line x1="92" y1="62" x2="388" y2="62" stroke="var(--c-border)" stroke-width="1.5"/><line x1="240" y1="62" x2="240" y2="180" stroke="var(--c-border)" stroke-width="1.5"/><text x="92" y="86" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">It knows</text><text x="262" y="86" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-accent)">It does</text><g font-family="Inter, sans-serif" font-size="13" fill="var(--c-ink)"><text x="92" y="112">the balance</text><text x="92" y="136">who owns it</text><text x="92" y="160">if it is frozen</text><text x="262" y="112">accept a deposit</text><text x="262" y="136">allow a withdrawal</text><text x="262" y="160">refuse one</text></g><text x="70" y="206" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">What it knows is its state. What it does is its behaviour. One thing holds both.</text></svg>

The left column is called **state**. The right column is called **behaviour**. Those are the only two words you need this week.

Notice the third item on the right. An account can refuse. That is not decoration, it is the point. A thing that can only hand over its data is a container. A thing that can say no is an object.

## Who is responsible?

Here is the question that turns this from a nice picture into a design skill.

Take any rule in the system. "A withdrawal cannot exceed the balance." Then ask two things.

**Who should know this?** The rule is about a balance. One thing in the system owns a balance. So the account should know it.

**Who should decide?** Whoever knows. If the account knows the balance and knows the rule, the account decides. Nobody else needs to.

Work through the cash machine and it resolves quickly. The daily limit is about the card, so the card knows it. How many notes are left is about the drawer, so the drawer decides whether it can pay out. The machine itself knows almost nothing. It just asks the right thing each time.

That is why "where does this code go" stops being a matter of taste. The code goes where the data is, because that is the only place the decision can be made honestly.

!!! mistake "Verbs are not things"

    The most common first design has a class called `WithdrawalManager` or `TransactionHandler` or `BalanceProcessor`. It feels right, because withdrawing is clearly important.

    Ask what it knows. Usually the answer is nothing. It holds no data of its own, so it spends its whole life reaching into other objects and changing their numbers for them. That is a step wearing a noun's name, and you have rebuilt the top half of the diagram with extra files.

    If a class ends in `Manager`, `Handler`, `Processor` or `Helper`, stop and ask what it knows. There are real exceptions and you will meet one in Module 2, but they are exceptions and they still own something.

## Ask, do not take

The second half of the paradigm, and the half most courses skip.

You do not reach into someone's wallet and remove fifty euro. You ask them to pay you, and they decide whether they can. If they cannot, they say so. The money never leaves without the owner's agreement, and the owner is the only one who has to know how much is in there.

Programs work the same way, or they should.

<svg viewBox="0 0 480 300" role="img" xmlns="http://www.w3.org/2000/svg"><title>Reaching into another object compared with sending it a request</title><desc>In the upper half a box named Cashier sends an arrow that passes straight through the wall of a box named Account and changes the balance inside it, with no rule consulted. In the lower half the Cashier sends a request named withdraw to the boundary of the Account, and the Account decides and sends an answer back.</desc><text x="20" y="22" font-family="Inter, sans-serif" font-size="14" font-weight="600" fill="var(--c-muted)">Taking</text><rect x="20" y="42" width="120" height="54" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="80" y="74" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)" text-anchor="middle">Cashier</text><path d="M300 32 a8 8 0 0 1 8-8 h144 a8 8 0 0 1 8 8 v78 a8 8 0 0 1 -8 8 h-144 a8 8 0 0 1 -8 -8 v-14 M300 54 v-22" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="318" y="50" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><text x="318" y="76" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-ink)">balance</text><text x="318" y="98" font-family="Inter, sans-serif" font-size="11" fill="var(--c-muted)">no rule was asked</text><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="142" y1="69" x2="310" y2="69"/><polyline points="303,64 311,69 303,74"/></g><text x="152" y="62" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)">balance = balance - 50</text><text x="20" y="140" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The wall is open. The account never found out, and never agreed.</text><line x1="20" y1="158" x2="460" y2="158" stroke="var(--c-border)" stroke-width="1" stroke-dasharray="4 4"/><text x="20" y="184" font-family="Inter, sans-serif" font-size="14" font-weight="600" fill="var(--c-muted)">Asking</text><rect x="20" y="204" width="120" height="54" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="80" y="236" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)" text-anchor="middle">Cashier</text><rect x="300" y="186" width="160" height="92" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="318" y="210" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><text x="318" y="234" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-ink)">balance</text><text x="318" y="258" font-family="Inter, sans-serif" font-size="11" fill="var(--c-accent)">checks its own rule</text><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="142" y1="220" x2="294" y2="220"/><polyline points="287,215 295,220 287,225"/></g><text x="158" y="213" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)">withdraw 50</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="294" y1="244" x2="142" y2="244"/><polyline points="149,239 141,244 149,249"/></g><text x="172" y="258" font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-muted)">yes, or no</text></svg>

In the top picture the cashier knows how an account stores its money, so if that ever changes, the cashier breaks. In the bottom picture the cashier knows only what to ask for. The account can change everything about how it works, and nobody outside notices.

The difference between those two pictures is the difference between a program you can change and one you cannot.

## What it costs

This is not free, and you should hear that from me rather than discover it while resenting me.

You will write more files. You will spend time deciding what to call things, and some of those decisions will feel arbitrary. Following what happens during a single operation means reading three small files instead of one big function, which is genuinely harder the first few times.

For a fifty-line script that you will run once and delete, none of this is worth it. Write the steps. Go home.

It becomes worth it the moment somebody changes the requirements, which in this course is every week and at work is every day.

## What this is not

??? note "\"So we are modelling the real world\""

    Nearly, and the gap matters.

    You are modelling the **problem**, not reality. A real bank has buildings, regulators, a marketing department and a coffee machine. None of those belong in your program. And in Module 3 you will build a `Transaction` and in Module 4 a `Controller`, neither of which is a thing you could point at in a bank.

    Ask what needs to exist for this program to be correct and easy to change. That question keeps working. "What exists in the world" stops working around week six.

??? note "\"So object orientation is about inheritance\""

    That is the part most courses lead with, which is why people finish them able to recite four words and unable to design anything.

    Inheritance is one tool for sharing behaviour between classes, and Module 2 covers it properly, including when using it is a mistake. It is not what objects are for. If you finish this module able to design good classes and unable to spell `extends`, you are in a better position than the reverse.

??? note "\"This is the right way to write software\""

    No. It is one way, and this course teaches it because it is the one you will meet most often and because it teaches you to think about design.

    In week twelve you will write Java in a different style, where data does not change and you transform it instead of asking it to change itself. Some problems fit that far better. Knowing both, and knowing when each is awkward, is worth more than believing in either.

> **If you remember one thing.** An object is a thing that knows something and can be asked to do something. Designing is deciding who knows what, and therefore who decides what.

## Check yourself

!!! verify "Cut a problem you were not shown"

    A university library lends books to students. A book can be borrowed if a copy is available. A student cannot borrow more than five books at once. Overdue books carry a fine.

    On paper. No Java, so nobody can hide behind syntax.

    1.  Name three or four things. Not ten.
    2.  For each, write what it **knows**.
    3.  For each, write what it **does**, including at least one thing it can refuse.
    4.  Write one sentence describing how a book gets borrowed, using only "X asks Y to ...". No thing is allowed to reach into another and change its data.

    Then the hard part. The rule "a student cannot borrow more than five books" needs an owner. Whoever you chose, write down what that thing must already know in order to decide. If it does not know it, your design has a gap. Finding that on paper costs nothing. Finding it in week nine costs a weekend.

    Bring it to the seminar. There is more than one defensible answer and the argument is the lesson.

## Think about it

1.  Both descriptions of the cash machine are accurate, and the first one is shorter. So what has the second one actually bought, given that the money moves either way? Now suppose the bank adds an overdraft, and then a second kind of account with different rules.

2.  The account can refuse a withdrawal. That means the cashier has to cope with being told no. Has the cashier's job got easier or harder? Answer honestly, then say who you would rather be if the rule changes next month.

3.  You were told to ask what a class knows, and to be suspicious of one that knows nothing. Try to think of something in a program that genuinely has no data of its own but still deserves to exist as a thing. Bring the best example you can find, even if you are not sure it is right.

4.  The cash machine description gave the log its own box. A log only records what other things did. Does it know anything? Does it decide anything? Argue either way, and notice which of your two answers you would defend in front of the room.

<div class="page-nav" markdown>
[Session 2. Why objects](why-objects.md){ .page-nav__next }
</div>
