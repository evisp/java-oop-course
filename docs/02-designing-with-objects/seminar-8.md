# Seminar 8. Many kinds, one loop

<p class="meta"><span class="badge">2 hours</span><span class="badge">Week 8 seminar</span><span class="badge badge--accent">Follows: Promises a class must keep, Many classes, one call</span><span class="badge badge--accent">Sprint 2 opens</span></p>

By the end of this session your classes answer the same question through an interface without sharing a parent, and you have the Sprint 2 brief.

This is the last seminar of Module 2. What you finish with is what Sprint 2 starts from.

<svg viewBox="0 0 480 96" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the two hours are divided</title><desc>A horizontal bar split into four parts: ten minutes tightening the parent class, forty minutes writing an interface, thirty-five minutes on sorting and one mixed loop, and thirty-five minutes on the sprint briefing.</desc><rect x="20" y="30" width="36" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="60" y="30" width="143" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="207" y="30" width="125" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="336" y="30" width="124" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="38" y="52">Tidy</text><text x="131" y="52">Interface</text><text x="269" y="52">One loop</text><text x="398" y="52">Sprint 2</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)" text-anchor="middle"><text x="38" y="80">10</text><text x="131" y="80">40 min</text><text x="269" y="80">35 min</text><text x="398" y="80">35 min</text></g></svg>

---

## Tidy the parent

Ten minutes.

Three changes to `Person`, all small.

- It should already be `abstract`. If it is not, make it so and find out what stops compiling.
- Make its constructor `protected`. Only a subclass was ever going to call it.
- If `getRole()` still has a body in `Person`, delete the body:

```java
    public abstract String getRole();
```

Then write a subclass with only a constructor and read the error. That error is the point of last week's first lecture, stated by the compiler rather than by a page.

---

## An interface

Forty minutes.

Three classes in your project can tell you a name and a number of credits. A `Course` has its own credits. A `Student` has the credits they have earned. A `University` has the total it offers.

They share no parent, and they cannot be given one. `Student` has already spent its single parent on `Person`.

```java
public interface CreditBearing {

    String getLabel();

    int getCredits();
}
```

Two method names, no bodies, no fields.

Now implement it on all three.

| Class | `getLabel()` | `getCredits()` |
|---|---|---|
| `Course` | the code and the title | its own credits |
| `Student` | the id and the name | credits earned from passed courses |
| `University` | the university name | the total credits of every course offered |

`Course.getCredits()` probably exists already. On `Student` and `University` you likely have a method that does the work under a better name, so have the interface method call yours. Keeping both is normal.

### The report

```java
List<CreditBearing> report = new ArrayList<>();
report.add(university);
report.addAll(university.getStudents());
report.addAll(university.getCourses());

for (CreditBearing item : report) {
    System.out.printf("%-40s %6d%n", item.getLabel(), item.getCredits());
}
```

A university, some students and some courses in one list, printed by one loop. The loop asks two questions and never finds out what it is holding.

!!! mistake "Inventing a parent so two classes can share a method"

    If you were tempted to write `abstract class UniversityThing` with the three of them underneath it, stop.

    It compiles and it produces the same output. It is also a lie about what those classes are, and it burns the one parent each of them gets. `Student` cannot extend it anyway.

    A class you cannot name without inventing a word is usually an interface you have not written yet.

---

## One loop, and an order

Thirty-five minutes.

### Sorting

```java
public class Student extends Person implements CreditBearing, Comparable<Student> {
```

`compareTo` orders students by credits earned, lowest first. Then add a tiebreaker on the id, because two students with the same credits are not the same student and a tie says they are.

```java
List<Student> students = new ArrayList<>(university.getStudents());
Collections.sort(students);
```

Then do it the other way, with the rule supplied at the call instead of living in the class:

```java
students.sort(Comparator.comparing(Student::getName));
```

Say in one sentence which of those two belongs in the class and which does not.

!!! verify "Three loops with no branching in them"

    Write these three and check that none of them contains `if`, `switch` or `instanceof`.

    1.  Over `List<Person>`, printing each one's role and credits.
    2.  Over `List<CreditBearing>`, printing the report above.
    3.  Over `List<Student>`, printing them in credit order.

    If any of them needs a branch to work, something that should be a method on the object is still a question being asked about it.

### Break it

- `report.add(null)` and then run the loop
- A student with no passed courses in the credit report
- A course with zero credits
- Sort a list containing two students with identical credits twice, and check the order is the same both times

`BreakIt` scores zero.

---

## Sprint 2

Thirty-five minutes. The rest of the session is reading and planning, not typing.

**Sprint 2 opens this week** and runs into Module 3. You do not start a new project. You take the application you shipped for Sprint 1 and rebuild it with the four weeks you have just done.

The full brief is on the projects page. Before you leave, do these three things.

1.  **Open your Sprint 1 code.** Find the `main` method and count the jobs in it. Find every `System.out` outside your console class. Find every `if` that asks what kind of thing it is holding. Write the three numbers down.

2.  **List what has no owner.** Find one rule in your Sprint 1 application that is enforced in two places, or in none. That rule is a class you have not written.

3.  **Plan the first commit.** Not the whole rebuild. Just the first thing you will change, and why that one first.

Then commit today's work:

```powershell
git add .
git commit -m "Week 8: CreditBearing across unrelated classes, Person abstract"
git push
```

## Before you leave

- [ ] `Person` is abstract, its constructor is `protected`, `getRole()` has no body
- [ ] `CreditBearing` written, implemented by `Course`, `Student` and `University`
- [ ] One list holding all three kinds, printed by one loop
- [ ] `Student` implements `Comparable<Student>` with a tiebreaker
- [ ] Three loops, none of them branching on type
- [ ] `BreakIt` scores zero
- [ ] Your three Sprint 1 numbers written down
- [ ] Pushed, with eight weeks of history on GitHub

## Module 2 is done

Eight commits, and the shape of them is the thing worth looking at. Weeks 1 to 4 built classes that hold their own data. Weeks 5 to 8 turned them into a design: a class that owns a job, a model that never talks, a family of things, and one loop that does not care what it is holding.

Module 3 starts with two things in your own code that have been wrong since week 5. `findStudent` hands back `null` and hopes you check it. `enrol` returns `false` for a student who does not exist and `false` for a course that is full, which are completely different problems wearing the same answer.

<div class="page-nav" markdown>
[Back to the module](index.md){ .page-nav__next }
</div>
