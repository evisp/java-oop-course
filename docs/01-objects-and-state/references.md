# What a variable really holds

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 2, session 2</span><span class="badge badge--accent">Needs: Your first class</span></p>

By the end of this page you can say what an object variable actually contains, predict what happens when two variables refer to the same object, and explain why comparing two objects with `==` almost never does what you want.

This is the session that decides whether the rest of the semester feels logical or feels like magic. Almost every confusing bug a first-year Java programmer hits comes from believing a variable holds an object. It does not, and once you can see the real picture, a whole family of problems stops being mysterious.

## What `new` actually did

Last session you wrote this and moved on:

```java
Account ada = new Account("AC-1001", "Ada Lovelace", 500.00);
```

Three separate things happened.

Java set aside space somewhere for an account and filled it in. Then it produced a **reference**, which is the way to reach that space. Then it stored that reference in `ada`.

The account and the variable are not the same thing and are not in the same place.

<svg viewBox="0 0 480 260" role="img" xmlns="http://www.w3.org/2000/svg"><title>A variable holding a reference to an object</title><desc>On the left a small box labelled ada contains a dot rather than any data. An arrow runs from that dot to a larger box on the right named Account, which holds the account number, the owner name and the balance. The variable does not contain the account; it contains the way to reach it.</desc><text x="20" y="28" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">Your variable</text><text x="264" y="28" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">The object</text><line x1="210" y1="40" x2="210" y2="210" stroke="var(--c-border)" stroke-width="1" stroke-dasharray="4 4"/><rect x="20" y="62" width="130" height="46" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="38" y="90" font-family="JetBrains Mono, monospace" font-size="14" fill="var(--c-ink)">ada</text><circle cx="132" cy="85" r="5" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="132" y1="85" x2="258" y2="85"/><polyline points="251,80 259,85 251,90"/></g><rect x="264" y="52" width="196" height="120" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="282" y="76" font-family="Source Serif 4, Georgia, serif" font-size="16" font-weight="700" fill="var(--c-ink)">Account</text><line x1="282" y1="86" x2="442" y2="86" stroke="var(--c-border)" stroke-width="1.5"/><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)"><text x="282" y="108">AC-1001</text><text x="282" y="130">Ada Lovelace</text></g><text x="282" y="154" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-accent)">500.00</text><text x="20" y="200" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">ada holds an arrow, not an account.</text><text x="20" y="222" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The account exists whether or not any variable points at it.</text></svg>

This picture is the one to keep. Every module from here zooms into some part of it. When inheritance arrives in week 7, it is this picture with the object drawn in two layers. When you save data to a file in week 11, it is this picture with an arrow leaving the program.

Primitives are different. A `double` variable really does hold the number. `int`, `double`, `boolean`, `char` and the rest hold their value directly. Everything else in Java holds a reference.

## Two names, one account

Here is where it bites.

```java
Account ada = new Account("AC-1001", "Ada Lovelace", 500.00);
Account backup = ada;

backup.withdraw(200.00);

System.out.println(ada.getBalance());    // 300.00
```

Nothing was copied. `backup = ada` copied the arrow, so now there are two arrows and still one account. Withdraw through either name and the other one sees it, because there is nothing else to see.

```java
Account ada  = new Account("AC-1001", "Ada Lovelace", 500.00);
Account alan = new Account("AC-1001", "Ada Lovelace", 500.00);
```

These two lines make two accounts. Identical values, separate things. Withdraw from one and the other is untouched.

<svg viewBox="0 0 480 320" role="img" xmlns="http://www.w3.org/2000/svg"><title>Two variables sharing one object compared with two separate objects</title><desc>In the upper half two variables named ada and backup both have arrows pointing at a single Account object with a balance of three hundred. In the lower half two variables named ada and alan each point at their own Account object, both holding five hundred, showing that identical values do not mean the same object.</desc><text x="20" y="24" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">Two names, one object</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="36" width="116" height="36" rx="6"/><rect x="20" y="82" width="116" height="36" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="34" y="59">ada</text><text x="34" y="105">backup</text></g><circle cx="122" cy="54" r="4" fill="var(--c-accent)"/><circle cx="122" cy="100" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="122" y1="54" x2="264" y2="66"/><polyline points="257,62 265,66 257,71"/><line x1="122" y1="100" x2="264" y2="88"/><polyline points="257,84 265,88 257,93"/></g><rect x="270" y="40" width="190" height="72" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="288" y="64" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><text x="288" y="90" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-accent)">300.00</text><text x="20" y="140" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Withdraw through either name. There is only one balance to change.</text><line x1="20" y1="158" x2="460" y2="158" stroke="var(--c-border)" stroke-width="1" stroke-dasharray="4 4"/><text x="20" y="184" font-family="Inter, sans-serif" font-size="13" font-weight="600" fill="var(--c-muted)">Two objects that look alike</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="196" width="116" height="36" rx="6"/><rect x="20" y="248" width="116" height="36" rx="6"/><rect x="270" y="192" width="190" height="44" rx="8"/><rect x="270" y="246" width="190" height="44" rx="8"/></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="34" y="219">ada</text><text x="34" y="271">alan</text></g><circle cx="122" cy="214" r="4" fill="var(--c-accent)"/><circle cx="122" cy="266" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="122" y1="214" x2="264" y2="214"/><polyline points="257,209 265,214 257,219"/><line x1="122" y1="266" x2="264" y2="268"/><polyline points="257,263 265,268 257,273"/></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="288" y="220">Account  500.00</text><text x="288" y="274">Account  500.00</text></g><text x="20" y="312" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Same values, different things. Changing one leaves the other alone.</text></svg>

## `==` compares arrows

Given the picture, this stops being surprising.

```java
Account ada  = new Account("AC-1001", "Ada Lovelace", 500.00);
Account alan = new Account("AC-1001", "Ada Lovelace", 500.00);

System.out.println(ada == alan);   // false
```

`==` asks whether the two arrows point at the same place. They do not. It never looked at the values inside, and it never will, no matter how many fields match.

```java
Account backup = ada;
System.out.println(ada == backup); // true
```

So `==` answers a real question: *are these the same object?* That is sometimes exactly what you want. It is almost never what you want when you are asking whether two accounts are the same account.

The proper answer is a method called `equals`, which compares what you decide matters. Writing it correctly has rules, and we do it in week 5 where it belongs. For now, know that `==` on objects means identity, not sameness.

!!! mistake "This applies to `String` too, and it will catch you"

    `String` is an object, so `==` on two strings compares arrows.

    ```java
    String a = new String("AC-1001");
    String b = new String("AC-1001");
    System.out.println(a == b);        // false
    System.out.println(a.equals(b));   // true
    ```

    Confusingly, `String a = "AC-1001";` written twice often does give `true`, because Java reuses identical literals behind the scenes. So `==` on strings appears to work, right up until the string came from a file or from user input, and then it silently stops working.

    Use `.equals()` for strings. Always. There is no case in this course where `==` on strings is the right answer.

## An arrow pointing nowhere

A reference variable can hold no arrow at all.

```java
Account closed = null;
System.out.println(closed.getBalance());
```

```text
Exception in thread "main" java.lang.NullPointerException:
Cannot invoke "Account.getBalance()" because "closed" is null
```

`null` is not an empty account. It is the absence of one. Asking it for a balance is following an arrow that does not exist, and the program stops.

Modern Java tells you exactly which variable was null, which is a large improvement over what your predecessors got. Read that message. It names the variable and the method, and between those two facts the cause is usually obvious.

Where does `null` come from? Usually from a field nobody set, or from a method that searched for something and did not find it. Week 4 gives you the second case for real, when `findAccount` has to answer "no such account".

## Handing an object to a method

Now the part that looks like an exception to the rules and is not.

```java
public static void applyFee(Account account) {
    account.withdraw(5.00);
}
```

```java
Account ada = new Account("AC-1001", "Ada Lovelace", 500.00);
applyFee(ada);
System.out.println(ada.getBalance());   // 495.00
```

The change survived, which surprises people who were told Java always copies.

Java did copy. It copied the **arrow**. The method got its own variable called `account`, holding a second arrow to the same object, exactly like `backup` earlier. Two arrows, one account, so the fee lands on the real thing.

<svg viewBox="0 0 480 200" role="img" xmlns="http://www.w3.org/2000/svg"><title>A method parameter holding a second reference to the same object</title><desc>Two boxes on the left represent variables in different places: ada in the main method and account inside applyFee. Both have arrows pointing at the same Account object on the right, which is why a change made inside the method is visible outside it.</desc><text x="20" y="24" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">in main</text><text x="20" y="110" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">inside applyFee</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="34" width="120" height="36" rx="6"/><rect x="20" y="120" width="120" height="36" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="34" y="57">ada</text><text x="34" y="143">account</text></g><circle cx="126" cy="52" r="4" fill="var(--c-accent)"/><circle cx="126" cy="138" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="126" y1="52" x2="274" y2="82"/><polyline points="267,77 275,82 266,87"/><line x1="126" y1="138" x2="274" y2="108"/><polyline points="266,103 275,108 267,113"/></g><rect x="280" y="58" width="180" height="74" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="298" y="82" font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)">Account</text><text x="298" y="110" font-family="JetBrains Mono, monospace" font-size="13" fill="var(--c-accent)">495.00</text><text x="20" y="188" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">The arrow was copied. The account was not.</text></svg>

There is a real limit to this, and it is worth seeing once.

```java
public static void replace(Account account) {
    account = new Account("AC-9999", "Nobody", 0.00);
}
```

Call that with `ada` and `ada` is unchanged. The method pointed **its own arrow** somewhere else. The caller's arrow never moved. You can change the object a method was handed. You cannot change which object the caller is looking at.

> **If you remember one thing.** A variable never holds an object. It holds the way to reach one, and several variables can hold the way to reach the same one.

## Check yourself

!!! verify "Predict first, then run"

    Write this in `main`. Before you run it, write down on paper what each of the four lines will print.

    ```java
    Account ada    = new Account("AC-1001", "Ada Lovelace", 500.00);
    Account backup = ada;
    Account twin   = new Account("AC-1001", "Ada Lovelace", 500.00);

    backup.withdraw(200.00);

    System.out.println(ada.getBalance());     // 1
    System.out.println(twin.getBalance());    // 2
    System.out.println(ada == backup);        // 3
    System.out.println(ada == twin);          // 4
    ```

    Now run it. For every line you got wrong, do not just accept the answer. Draw the boxes and arrows on paper until the picture explains the output, then check your drawing against the first diagram on this page.

    Then one more, which is the one that matters:

    ```java
    Account nothing = null;
    System.out.println(nothing == null);       // fine
    System.out.println(nothing.getBalance());  // not fine
    ```

    Run it and read the exception message properly, all of it. You will meet this message more often than any other in Java, and being able to read it is a skill worth ten minutes now.

## Think about it

1.  `backup = ada` copies an arrow rather than the account. Suppose you genuinely wanted a separate account with the same values. You cannot get it from an assignment. What would you have to do instead, and what would you have to decide along the way?

2.  `==` compares arrows, which means two accounts with the same number are not equal. For a bank, is that the right default or the wrong one? Argue it, then consider a system where the same customer record is loaded twice from a file.

3.  A method can change the object you handed it but cannot change which object you are looking at. Is that a sensible rule or an accident of how Java works? Think about what the alternative would let a method do to your variables.

4.  Nobody points an arrow at the object made inside `replace` once the method ends. What happens to it? You have not been taught the answer. Work out what *must* happen, and what would go wrong if it did not.

<div class="page-nav" markdown>
[Week 3, session 1. Objects that refuse](objects-that-refuse.md){ .page-nav__next }
</div>
