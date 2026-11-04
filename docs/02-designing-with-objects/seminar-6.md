# Seminar 6. Splitting what you built

<p class="meta"><span class="badge">2 hours</span><span class="badge">Week 6 seminar</span><span class="badge badge--accent">Follows: One job per class, A fixed set of values</span></p>

By the end of this session nothing in your model prints anything, and the strings the compiler never checked are gone.

<svg viewBox="0 0 480 96" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the two hours are divided</title><desc>A horizontal bar split into four parts: fifteen minutes counting the printing, forty-five minutes moving it out, forty minutes on enums, and twenty minutes to break it and commit.</desc><rect x="20" y="30" width="53" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="77" y="30" width="160" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="241" y="30" width="143" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="388" y="30" width="71" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="46" y="52">Count</text><text x="157" y="52">Split it out</text><text x="312" y="52">Enums</text><text x="423" y="52">Commit</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)" text-anchor="middle"><text x="46" y="80">15</text><text x="157" y="80">45 min</text><text x="312" y="80">40 min</text><text x="423" y="80">20</text></g></svg>

---

## Count the printing

Fifteen minutes.

Search your whole project for `System.out`. In Eclipse that is **Ctrl+H**, File Search.

Write down the number, and which classes it is in. Then open `UniversityApp.main` and count the jobs it is doing. Most of you will find input, arithmetic, decisions and output in one method.

That is the whole brief for the next 45 minutes.

---

## Split it out

Forty-five minutes.

### The console

```java
public class UniversityConsole {

    private final University university;
    private final Scanner input;

    public UniversityConsole(University university, Scanner input) { ... }

    public void run() { ... }
}
```

It is handed the university and the scanner rather than making them. `run` is a menu loop. Everything the user sees and everything they type goes through this class.

A menu that covers what you already have:

```text
1  List students
2  List courses
3  Enrol a student on a course
4  Withdraw a student from a course
5  Show a student's transcript
0  Quit
```

### The model goes quiet

Move every `System.out` out of `Student`, `Professor`, `Course` and `University`. Where a method was printing a result, it returns one instead.

`toString` is the one exception. It stays, and it is for you while debugging.

!!! mistake "Adding a `describe()` method instead"

    A method on `Student` that returns a nicely formatted sentence looks like it solves this. It does not. `Student` now owns the wording, the order of the information and the language the program is written in.

    If a method's return value is a sentence meant for a person to read, it belongs in the console.

### What is left of main

```java
public class UniversityApp {

    public static void main(String[] args) {
        University university = new University("University Metropolitan Tirana");
        seed(university);

        Scanner input = new Scanner(System.in);
        new UniversityConsole(university, input).run();
    }
}
```

Build the model, build the thing that talks, hand one to the other.

### Then search again

`System.out` should now appear in exactly one file.

---

## Enums

Forty minutes. Two of them, and they are different kinds of job.

### Grade

Your `Student` currently takes a `gpa` as a `double` in the constructor, which means somebody outside decides it and nothing checks it.

```java
public enum Grade {
    A(4.0), B(3.0), C(2.0), D(1.0), F(0.0);
```

Each constant holds its grade point. Then on `Student`:

- A way to record a grade for a course the student is enrolled on
- `gpa()`, calculated from the recorded grades, never stored
- `creditsEarned()`, which counts only the courses with a passing grade

Delete the `gpa` field and the constructor parameter. A GPA that is stored is a second copy of a fact, and it will be wrong within a month.

### StudentLevel

```java
public enum StudentLevel {
    UNDERGRADUATE("Undergraduate", 6, 180),
    MASTERS("Masters", 4, 120),
    DOCTORAL("Doctoral", 2, 60);
```

A label, a maximum course load, and the credits needed to graduate. Add the field to `Student` and use it in the registration limit, replacing the hard-coded six from week 4.

Note what just happened to that limit. It was a number in an `if`, and now it is data on the type, so there is no branch anywhere.

### The one that will not go away

```java
    public boolean isEligibleToGraduate()
```

Undergraduate and masters students need their credits. Doctoral students need the credits **and** a submitted thesis, so `Student` also needs a `thesisSubmitted` flag.

Write it with a `switch` on the level. Then look at what you had to write:

- A `default` branch that can never run, because every constant is listed above it.
- A method that will quietly give the wrong answer the day somebody adds a fourth level.

Leave it exactly as it is and bring it to next week. That switch is the whole subject of week 7.

---

## Break it and commit

Twenty minutes.

- Record a grade for a course the student is not enrolled on
- Record two grades for the same course
- Call `gpa()` on a student with no grades at all
- Register a masters student for a fifth course
- `Grade.valueOf("E")`
- `Grade.valueOf("a")`

Then commit:

```powershell
git add .
git commit -m "Week 6: console split out of the model, Grade and StudentLevel enums"
git push
```

## Before you leave

- [ ] `System.out` appears in exactly one file
- [ ] `UniversityApp.main` is under ten lines
- [ ] `UniversityConsole` is handed its university and its scanner
- [ ] `Grade` carries its own grade points
- [ ] `gpa()` is calculated and the `gpa` field is gone
- [ ] `StudentLevel` carries the course limit, and no `if` decides it
- [ ] `isEligibleToGraduate` has a switch with a `default` you cannot delete
- [ ] `BreakIt` scores zero

## Going further

**A Semester enum.** `"Fall 2026"` is one string holding two facts. Split it into a `Semester` enum and a year, then say what breaks in your existing code and what gets easier.

**Format in one place.** Your console now has printing in several methods. Pull the formatting of a student line into one private method and use it everywhere. Then say how many places you would have to edit to add a column.

**A second console.** Without changing a single line of your model, write a `ReportConsole` that takes the same `University` and prints a summary with no menu and no input at all. If you have to touch the model to do it, the split is not finished.

<div class="page-nav" markdown>
[Back to the module](index.md){ .page-nav__next }
</div>
