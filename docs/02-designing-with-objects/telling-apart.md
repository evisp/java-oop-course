# Object Equality: equals and hashCode

*Same object, or same thing?*

<p class="meta"><span class="badge">50 minutes</span><span class="badge">Week 5, session 2</span><span class="badge badge--accent">Needs: Object Collaboration</span><a class="badge badge--link" href="https://github.com/evisp/java-oop-lectures/tree/main/week-05-objects-together">Lecture code</a></p>

**What you will learn**

- The difference between two objects being *identical* and two objects being *equal*
- How to decide what "equal" should mean for a class you design
- How to write `equals` and `hashCode` so that Java's collections agree with your decision

## The problem

Open the contacts on your phone and you will probably find someone saved twice. Maybe your friend Ana appears as "Ana" and again as "Ana Hoxha (work)". Your phone notices and offers to merge the duplicates.

To do that, the phone has to answer a question that sounds simple: **when are two contacts the same person?**

Try a few answers.

*Same name?* Then two different friends who are both called Ana get merged into one, and you lose a contact.

*Same name and same number?* Then "Ana" and "Ana Hoxha (work)" are never merged, because the names differ, even though it is obviously the same person.

*Same phone number?* That is much closer. Ana can change how she is saved, or even change her surname, and she is still the same person, because her number has not changed.

Nobody could have answered this for the phone in advance. Somebody who understood what a contact *represents* had to make a decision. Java has exactly the same problem with your objects, and it leaves the decision to you.

## Building the idea

The word "same" is hiding two different questions.

**Is it the same object?** Your phone might show Ana under "Favourites" and under "Recent calls". Those are two ways of reaching one contact card. Edit the card in one place and the change appears in the other, because there is only one card.

**Does it count as the same thing?** The two cards "Ana" and "Ana Hoxha (work)" are separate cards. Editing one does not touch the other. Yet they describe the same person, and you would like the phone to treat them that way.

<svg viewBox="0 0 520 250" role="img" xmlns="http://www.w3.org/2000/svg"><title>Same object compared with same thing, using phone contacts</title><desc>Two panels. On the left, titled same object, two labels, Favourites and Recent calls, both point with arrows at a single contact card for Ana with her phone number. On the right, titled same thing, two separate contact cards, one saved as Ana and one saved as Ana Hoxha work, show the same phone number. A note between them asks whether they are the same person and says that you decide.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="16" y="22">Same object</text><text x="284" y="22">Same thing</text></g><line x1="262" y1="34" x2="262" y2="222" stroke="var(--c-border)" stroke-width="1.5" stroke-dasharray="5 5"/><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="52" width="104" height="34" rx="6"/><rect x="16" y="168" width="104" height="34" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)"><text x="28" y="73">Favourites</text><text x="28" y="189">Recent calls</text></g><circle cx="112" cy="69" r="4" fill="var(--c-accent)"/><circle cx="112" cy="185" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="112" y1="69" x2="146" y2="112"/><polyline points="138,108 147,113 146,103"/><line x1="112" y1="185" x2="146" y2="142"/><polyline points="145,151 147,141 138,146"/></g><rect x="150" y="98" width="100" height="58" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="162" y="120" font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)">Ana</text><text x="162" y="142" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">069 000 1234</text><g fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"><rect x="290" y="52" width="150" height="58" rx="8"/><rect x="290" y="150" width="150" height="58" rx="8"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="13" font-weight="700" fill="var(--c-ink)"><text x="302" y="74">Ana</text><text x="302" y="172">Ana Hoxha (work)</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="302" y="96">069 000 1234</text><text x="302" y="194">069 000 1234</text></g><rect x="384" y="116" width="124" height="28" rx="4" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="1.5"/><text x="394" y="134" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">same person? you decide</text><text x="16" y="240" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Left: one card reached two ways. Right: two cards that may describe one person.</text></svg>

The first question has a fixed answer that never needs your help. The second question has no fixed answer, because it depends on what the thing represents. And once you look at a few examples, a pattern appears: things fall into two groups.

Some things are equal when **everything about them** matches. Two 10-euro notes are interchangeable when you pay for coffee. A date is a date: 5 November 2026 is the same day whoever writes it down. These are *values*.

Other things are equal when **their identity** matches, however much else changes. A person who changes their name is still the same person. A contact is the same contact as long as the number is the same. These have an identifier that says who they are, and everything else is just information about them.

| | Equal when | Examples |
|---|---|---|
| **Value** | Every part matches | An amount of money, a date, a colour, a point on a map |
| **Entity** | The identifier matches, whatever else has changed | A person, a contact, an order, a bank account |

### Finding duplicates quickly

There is one more part of the story. A phone with two thousand contacts cannot compare every card with every other card, because that is about two million comparisons. Instead it sorts the cards into groups, say by the first letter of the name, and only compares cards within the same group.

That works only if two cards that count as the same person always land in the same group. Look at what goes wrong here:

<svg viewBox="0 0 520 256" role="img" xmlns="http://www.w3.org/2000/svg"><title>Two contacts for one person filed in different groups</title><desc>Four drawers labelled A, B, an ellipsis, and H represent contacts grouped by the first letter of the name. A card saved as Ana with number 069 000 1234 sits in drawer A. A card saved as Hoxha, Ana with the same number sits in drawer H. The duplicate finder only compares cards inside each drawer, so these two cards, which count as the same person because their numbers match, are never compared.</desc><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="16" y="50" width="116" height="120" rx="6"/><rect x="144" y="50" width="116" height="120" rx="6"/><rect x="272" y="50" width="96" height="120" rx="6" stroke-dasharray="4 4"/><rect x="380" y="50" width="124" height="120" rx="6"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="15" font-weight="700" fill="var(--c-ink)"><text x="28" y="74">A</text><text x="156" y="74">B</text><text x="304" y="74">...</text><text x="392" y="74">H</text></g><g fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="1.5"><rect x="26" y="92" width="96" height="46" rx="4"/><rect x="390" y="92" width="104" height="46" rx="4"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)"><text x="34" y="110">Ana</text><text x="398" y="110">Hoxha, Ana</text></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)"><text x="34" y="128">069 000 1234</text><text x="398" y="128">069 000 1234</text></g><text x="156" y="110" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">Besa</text><text x="156" y="128" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">Bledi</text><path d="M74 172 C 120 206 400 206 442 172" fill="none" stroke="var(--c-accent)" stroke-width="1.5" stroke-dasharray="5 4"/><text x="176" y="222" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">same person, never compared</text><text x="16" y="34" font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)">Grouped by the first letter of the name</text><text x="16" y="248" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Equal by number, grouped by name: the duplicate finder never sees the pair.</text></svg>

The phone decided that sameness depends on the number, but it chose the group using the name. The two cards for Ana sit in different drawers and are never compared, so the duplicate is never found.

The fix is a rule worth remembering: **whatever information decides "same" must also decide the group.** If cards are equal by number, group them by something taken from the number, such as its last digit. Then equal cards always land in the same group.

## Naming it

!!! abstract "Definitions"

    **Identity.** Two references point to the very same object in memory. In Java, `==` tests identity.

    **Equality.** Two objects count as the same thing according to a rule the class defines. In Java, the `equals` method tests equality, and you write that method.

    **Value and entity.** A value is equal to another when all its parts match. An entity is equal to another when its identifier matches.

    **Hash code.** A number calculated from an object, used by some collections to decide which group (Java calls it a *bucket*) the object goes into. It is computed by the `hashCode` method.

    **The hash code contract.** Objects that are equal must have equal hash codes. This is the "same information decides the group" rule from the contacts story.

## In our bank

### Two objects, one account

The bank meets this problem as soon as account data comes from outside the program. When accounts are saved and loaded back, or arrive from another system, they already have numbers. `Account` has a second constructor for that case, which accepts an existing number instead of issuing a new one:

```java
    public Account(String accountNumber, double balance) {
```

That means the program can end up holding two objects for one account: the one Ada opened, and a copy that came from outside.

```java
Account account = ada.openAccount(500.00);
String number = account.getAccountNumber();

Account restored = new Account(number, 500.00);
```

```text
Account AC-1001: 500.00 EUR
Account AC-1001: 500.00 EUR
account == restored      : false
```

`==` reports `false`. It is testing identity, and these are two separate objects. But anyone working at the bank would say they are the same account, just as the two cards are the same Ana.

So which kind of thing is an account? It has an identifier, the account number, and its balance changes every day without making it a different account. An account is an **entity**, and two accounts are equal when their numbers match.

<svg viewBox="0 0 520 270" role="img" xmlns="http://www.w3.org/2000/svg"><title>Two objects for one account: identity says different, equality says the same</title><desc>On the left, two variables named account and restored each hold an arrow to a separate Account object. Both objects hold the number AC-1001; one holds 500 and the other 1500. Two labels sit between the objects. The first says double equals compares the arrows and gives false. The second says equals compares the numbers and gives true. The balance is shown faded, because it is not part of the comparison.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="20" y="22">Variables</text><text x="240" y="22">Objects</text></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="48" width="118" height="40" rx="6"/><rect x="20" y="172" width="118" height="40" rx="6"/></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-ink)"><text x="34" y="73">account</text><text x="34" y="197">restored</text></g><circle cx="130" cy="68" r="4" fill="var(--c-accent)"/><circle cx="130" cy="192" r="4" fill="var(--c-accent)"/><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="130" y1="68" x2="234" y2="62"/><polyline points="227,57 235,62 227,67"/><line x1="130" y1="192" x2="234" y2="198"/><polyline points="227,193 235,198 227,203"/></g><g fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"><rect x="240" y="32" width="200" height="60" rx="8"/><rect x="240" y="168" width="200" height="60" rx="8"/></g><g font-family="Source Serif 4, Georgia, serif" font-size="14" font-weight="700" fill="var(--c-ink)"><text x="256" y="54">Account</text><text x="256" y="190">Account</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-accent)"><text x="256" y="78">AC-1001</text><text x="256" y="214">AC-1001</text></g><g font-family="JetBrains Mono, monospace" font-size="12" fill="var(--c-muted)" opacity="0.55"><text x="360" y="78">500.00</text><text x="360" y="214">1500.00</text></g><rect x="150" y="112" width="160" height="36" rx="4" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="1.5"/><text x="160" y="127" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">== compares arrows</text><text x="160" y="141" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-ink)">false</text><rect x="326" y="112" width="180" height="36" rx="4" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="1.5"/><text x="336" y="127" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">equals compares numbers</text><text x="336" y="141" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">true</text><text x="20" y="258" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Two objects, so not the same arrow. One number, so the same account.</text></svg>

### Writing equals

Every Java class inherits an `equals` method from the built-in `Object` class, and by default it does the same as `==`. To give `Account` the rule we just decided on, you replace that method with your own version. Replacing an inherited method like this is called **overriding**.

```java
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Account)) {
            return false;
        }
        Account that = (Account) other;
        return accountNumber.equals(that.accountNumber);
    }
```

Read it as four short steps:

1.  `this == other`: if it is literally the same object, there is nothing to compare.
2.  `other instanceof Account`: the parameter is an `Object`, so someone could pass a `String`, or `null`. Both fail this test, which also saves you a separate null check.
3.  The cast is safe now, because step 2 proved that `other` is an `Account`.
4.  The decision: compare the account numbers, and nothing else.

`@Override` asks the compiler to confirm that you really are replacing an inherited method. You will see in a moment why that matters.

```text
account == restored      : false
account.equals(restored) : true
```

### What equals gives you

Java's collections call `equals` whenever they search for something. Before `Account` had its own `equals`, a list could only find the exact object you put in. Now it can find the account:

```java
List<Account> accounts = ada.getAccounts();
System.out.println("contains(restored)       : "
        + accounts.contains(restored));
System.out.println("indexOf(restored)        : "
        + accounts.indexOf(restored));
```

```text
contains(restored)       : true
indexOf(restored)        : 0
```

Without your `equals`, those lines print `false` and `-1`.

This also explains the line in last session's `Bank.transfer` that refuses a transfer from an account to itself, `from.equals(to)`. The decision about what makes two accounts the same now lives in one place, inside `Account`, and every other class simply asks.

The balance plays no part, exactly as we decided. After a deposit into the copy, the two still count as one account:

```text
Account AC-1001: 1500.00 EUR
still equal              : true
```

### Writing hashCode

`ArrayList` searches by checking each element in turn, so it only needs `equals`. But Java also has `HashSet` and `HashMap`, which find things fast by sorting objects into buckets, just like the phone sorting contacts into groups. They choose the bucket from the object's hash code.

The default `hashCode` is based on the object's identity, so two separate objects almost always get different hash codes, and land in different buckets. For our two equal accounts, that is the "Ana under A, Hoxha under H" problem again:

<svg viewBox="0 0 520 260" role="img" xmlns="http://www.w3.org/2000/svg"><title>A hash set files an object in one drawer and searches in another</title><desc>A row of five drawers numbered zero to four represents a hash set. In the top half, labelled add, an account object with number AC-1001 is filed in drawer one. In the bottom half, labelled contains, an equal account object with the same number is looked up. Without a matching hashCode its hash leads to drawer three, which is empty, so the search reports false even though an equal object is in the set.</desc><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)"><text x="20" y="24">add(account)</text><text x="20" y="226">contains(restored)</text></g><rect x="20" y="40" width="120" height="36" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="32" y="63" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)">AC-1001</text><rect x="20" y="180" width="120" height="36" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="32" y="203" font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-ink)">AC-1001</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="200" y="104" width="56" height="46" rx="5"/><rect x="262" y="104" width="56" height="46" rx="5"/><rect x="324" y="104" width="56" height="46" rx="5"/><rect x="386" y="104" width="56" height="46" rx="5"/><rect x="448" y="104" width="56" height="46" rx="5"/></g><g font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)"><text x="206" y="118">0</text><text x="268" y="118">1</text><text x="330" y="118">2</text><text x="392" y="118">3</text><text x="454" y="118">4</text></g><rect x="268" y="124" width="44" height="18" rx="3" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="1.5"/><text x="272" y="137" font-family="JetBrains Mono, monospace" font-size="9" fill="var(--c-accent)">AC-1001</text><g stroke="var(--c-accent)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><path d="M140 58 C 220 58 270 70 288 98"/><polyline points="281,92 289,99 290,89"/></g><text x="168" y="52" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-accent)">hash leads to drawer 1</text><g stroke="var(--c-muted)" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-dasharray="5 4"><path d="M140 198 C 260 198 390 190 412 156"/></g><polyline points="404,162 413,155 414,166" fill="none" stroke="var(--c-muted)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/><text x="228" y="214" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">a different hash leads to drawer 3: empty</text><text x="20" y="252" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)">Equal objects with different hash codes are filed in one drawer and searched for in another.</text></svg>

The cure is the same as for the phone. Build the hash code from the same information that `equals` uses:

```java
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
```

`Objects.hash` combines the fields you give it into one number. Give it exactly the fields that `equals` compares, no more and no fewer, and equal objects will always land in the same bucket.

```text
equal objects            : true
equal hash codes         : true
```

### Customer is an entity too

A customer has an id, and the name is only information about them. So `Customer` makes the same decision:

```java
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Customer)) {
            return false;
        }
        Customer that = (Customer) other;
        return customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
```

And just like Ana in the contacts, a customer who changes her name stays the same customer:

```java
ada.setName("Ada Byron");
Customer found = bank.findCustomer(ada.getCustomerId());
System.out.println("after a name change      : " + ada.equals(found));
```

```text
after a name change      : true
```

## Common mistakes

!!! mistake "Writing `equals(Account other)`"

    This compiles:

    ```java
    public boolean equals(Account other) {
        return accountNumber.equals(other.accountNumber);
    }
    ```

    It does not replace the inherited `equals(Object)`. It adds a second method with the same name but a different parameter type, which is called **overloading**. Your own code might call the new method and seem to work. Java's collections only know about `equals(Object)`, so `contains`, `indexOf` and `remove` quietly go back to comparing identity.

    With `@Override` on it, the compiler checks that the method really does replace an inherited one, and refuses this version. Always put `@Override` on `equals`.

!!! mistake "Writing equals and forgetting hashCode"

    Everything appears to work, because lists only use `equals`. The problem stays hidden until the first `HashSet` or `HashMap` in the program, possibly weeks later, in code that looks perfectly correct. The method that is wrong is not the one that fails, which is what makes this bug so hard to find. Write the two methods together, from the same fields.

## The bigger picture

Every database uses this idea. A table of customers has a *primary key*, an id column, and two rows describe the same customer exactly when their keys match. That is the entity decision, made by the people who designed the table.

Values get easier later in the course. In week 12 you will meet *records*, a kind of Java class that writes `equals` and `hashCode` for you from all of its fields. That suits values perfectly. For entities like `Account`, where only the identifier should count, you will still write them yourself.

??? note "`instanceof` or `getClass()`?"

    You will also see `equals` written like this:

    ```java
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    ```

    The difference only matters once one class extends another, which happens in week 7. With `instanceof`, a savings account and a plain account with the same number count as equal. With `getClass()`, they do not. Which is right depends on what you mean. This course uses `instanceof`, because an account number identifies one account at one bank, whatever kind of account it is.

## Summary

- `==` tests **identity**: are these the very same object?
- `equals` tests **equality**: do these count as the same thing? You decide what that means.
- Values are equal when all their parts match. Entities, such as accounts and customers, are equal when their identifiers match.
- Equal objects must have equal hash codes, so build `hashCode` from exactly the fields `equals` uses.

**Key terms:** identity, equality, value, entity, overriding, overloading, hash code, hash code contract.

## Practice

**Quick checks.**

1.  For each of these, would `equals` compare one identifying field or every field? An amount of money with a currency. A customer. A transaction with a transaction id. A date.
2.  After `restored.deposit(1000.00)`, what does `account.equals(restored)` return?
3.  `equals` compares account numbers, but `hashCode` is built from the balance. Two equal accounts hold different balances. Is the contract broken?

??? success "Answers"

    1.  Money: every field, so the amount and the currency, because it is a value. Customer: the id. Transaction: its id. Date: every field, because it is a value.
    2.  `true`. The balance is not part of the decision.
    3.  Yes. The accounts are equal but will almost certainly have different hash codes, so a `HashSet` can store one and fail to find the other. The hash code must come only from fields that `equals` compares.

!!! verify "Coding exercise"

    First, add the balance to the comparison in `Account.equals`:

    ```java
    return accountNumber.equals(that.accountNumber)
            && balance == that.balance;
    ```

    Run `EqualityApp` and look at "still equal" after the deposit. Write one sentence on what is wrong with an account that stops being itself when money is paid in. Then put it back.

    Second, delete `hashCode` from `Account` entirely and run `EqualityApp` again. Everything looks fine. Now add these lines at the end of `main`, with `import java.util.HashSet;` and `import java.util.Set;` at the top:

    ```java
    Set<Account> set = new HashSet<>(List.of(account));
    System.out.println("set.contains(restored)   : "
            + set.contains(restored));
    ```

    Explain the result using the contacts story, then put `hashCode` back and run it once more.

**Think about it.**

1.  One `Account` object was loaded from last night's backup, and another is the live one. Their balances differ, and `equals` says they are the same account. Is that a problem with `equals`, or with something else? Who should decide which balance is right?

    ??? success "Hint"

        `equals` answers "is this the same account?", not "is this information up to date?". Those are different questions, and they may belong to different classes.

2.  The bank discovers that customers `C-1001` and `C-1340` are the same person, who registered twice. This is the contacts problem again. What does `equals` say about them now, what should it say, and what would it cost to make it say that?

    ??? success "Hint"

        `equals` can only be as good as the identifier it compares. Where would a better identifier come from, and what happens to the accounts already attached to each id?

## Next

The bank now has classes that collaborate and know what makes them the same. Next week looks at what each class should be responsible for, starting with the `main` method you wrote for Sprint 1.

<div class="page-nav" markdown>
[One job per class](one-job.md){ .page-nav__next }
</div>
