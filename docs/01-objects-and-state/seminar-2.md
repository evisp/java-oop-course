# Seminar 2. Your first classes

<p class="meta"><span class="badge">2 hours</span><span class="badge">Week 2 seminar</span><span class="badge badge--accent">Follows: Your first class, What a variable really holds</span></p>

By the end of this session you have three working Java classes from the design you made last week, and you can predict what happens when two variables refer to the same object.

No validation today. Your constructors will accept anything you hand them, including nonsense, and that is deliberate. Next week is about making them refuse. Write the classes first, feel where the holes are, then close them.

<svg viewBox="0 0 480 96" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the two hours are divided</title><desc>A horizontal bar split into four parts: twenty minutes on a warm-up class, sixty minutes on the three main classes, twenty minutes on a references drill, and ten minutes to commit.</desc><rect x="20" y="30" width="78" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="102" y="30" width="234" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="340" y="30" width="78" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="422" y="30" width="38" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="59" y="52">Warm-up</text><text x="219" y="52">Three classes</text><text x="379" y="52">Drill</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)" text-anchor="middle"><text x="59" y="80">20</text><text x="219" y="80">60 min</text><text x="379" y="80">20</text><text x="441" y="80">10</text></g></svg>

---

## Warm-up. Classroom

Twenty minutes. Build `Classroom` from [the specification](university-system.md). It is the simplest of the five, and it exercises everything you will need for the next hour.

Four fields, one constructor, getters, three behaviour methods, `toString`.

Two things to get right, because you will repeat them fifteen times today:

**`roomNumber` and `building` have getters and no setters.** A room does not move to another building. `hasProjector` has a setter, because equipment does change.

**`location()` returns text, it does not print it.** Same for `toString`. Nothing in this class calls `System.out.println`.

When it compiles, make two classrooms in a `UniversityApp` class with a `main`, print both, and call `canAccommodate` with a number that fits and one that does not.

If you are still fighting Eclipse at the twenty minute mark, stop and fix that first. Go back to the [setup pages](../00-tools/index.md).

---

## Main task. Student, Professor, Course

Sixty minutes. These three are the system, and you extend them for the rest of the module.

Work through [the specification](university-system.md) one class at a time. Finish one completely before starting the next, because three half-written classes compile into nothing useful and you will not know which one is wrong.

For each class:

1.  Fields, all `private`.
2.  One constructor that takes everything the object needs. No empty constructor.
3.  Getters. Setters **only** where the specification says the value can change.
4.  The behaviour methods. These are the interesting part.
5.  `toString()`, with `@Override` above it.

### Where people lose time

**The behaviour methods are the point, not the getters.** `academicStanding()` and `academicTitle()` are where a class stops being a box of data. If you run short, write those before you write the last few getters.

**`this` in the constructor.** Leave it out and `name = name` assigns the parameter to itself. The compiler says nothing and your object prints a blank name.

**Getter-only fields.** `studentId`, `employeeId` and `courseCode` have no setters. If your fingers generated one out of habit, delete it.

**One thing at a time.** `academicStanding()` returns a `String`. It does not print, does not update the GPA, and does not check anything else.

!!! mistake "Do not let Eclipse generate everything"

    Right-click, Source, Generate Getters and Setters is fast and it will produce a setter for every field, including the ones that must never change.

    Use it if you like, then read every method it wrote and delete the ones that should not exist. Deleting them is the design work. The generator does the typing and you do the thinking.

### Check it works

In `UniversityApp.main`, create two of each, print them all, and call every behaviour method at least once.

```java
Student ada = new Student("S-1001", "Ada Lovelace", 3.9, 2024);
System.out.println(ada);
System.out.println(ada.academicStanding());
```

Expected shape, not exact wording: one readable line per object, and standings and titles that change when you change the numbers.

Try a student with a GPA of 1.2 and one with 3.5, and check the standing differs. If both say the same thing, your comparison chain is in the wrong order.

---

## Drill. Two names, one object

Twenty minutes, and do not skip it because you are behind. This catches a misconception that otherwise surfaces in week eleven, on a deadline.

Write each block, **predict the output on paper first**, then run it.

**One**

```java
Student a = new Student("S-1001", "Ada Lovelace", 3.9, 2024);
Student b = a;
b.setName("Ada L.");
System.out.println(a.getName());
```

**Two**

```java
Student x = new Student("S-1001", "Ada Lovelace", 3.9, 2024);
Student y = new Student("S-1001", "Ada Lovelace", 3.9, 2024);
System.out.println(x == y);
System.out.println(x.getStudentId() == y.getStudentId());
System.out.println(x.getStudentId().equals(y.getStudentId()));
```

**Three**

```java
Student nobody = null;
System.out.println(nobody == null);
System.out.println(nobody.getName());
```

**Four**

```java
public static void promote(Student student) {
    student.updateGpa(4.0);
}

public static void replace(Student student) {
    student = new Student("S-9999", "Nobody", 0.0, 2024);
}
```

Call both with the same student and print the GPA and name after each. One of them changes something and one does not.

For every prediction you got wrong, draw the boxes and arrows on paper until the picture explains the output. Check your drawing against the diagram on [What a variable really holds](references.md).

Block two, line two is the one that catches almost everybody. Note down your answer and why.

---

## Commit

Ten minutes.

```powershell
cd C:\dev\java-oop-yourname
git add .
git commit -m "Week 2: Student, Professor, Course and Classroom"
git push
```

Check on GitHub that the `.java` files are there and the `bin` folder is not. If `bin` appears, your `.gitignore` is missing or in the wrong folder. Fix it now, because by week four that folder is noise on every commit.

## Before you leave

- [ ] Four classes compile: `Classroom`, `Student`, `Professor`, `Course`
- [ ] Every field is `private`
- [ ] No class contains `System.out.println`
- [ ] No setters on `studentId`, `employeeId`, `courseCode`, `roomNumber` or `building`
- [ ] `UniversityApp` creates objects and prints them
- [ ] All four drill blocks run, with your predictions written down
- [ ] Pushed, and `bin` is not in the repository

## Going further

**Write `Department`.** Three fields, and no relationships yet. Ten minutes.

**Break your own constructor.** Create a student with a GPA of 9.5, a negative year of experience, and a course with 200 credits. All of them work right now. Write down what each one lets through. That list is your task list for next week, written by you.

**Two constructors, honestly.** The specification says one constructor. Find a case where a second one would genuinely earn its place, not just save typing. Bring it to the lecture.

<div class="page-nav" markdown>
[Week 3, lecture 1. Objects that refuse](objects-that-refuse.md){ .page-nav__next }
</div>
