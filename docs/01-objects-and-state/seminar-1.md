# Seminar 1. Setting up and designing

<p class="meta"><span class="badge">2 hours</span><span class="badge">Week 1 seminar</span><span class="badge badge--accent">Follows: Thinking in things, Why objects</span></p>

By the end of this session your machine compiles and runs Java, your work is in a repository on GitHub, and you have designed the university system on paper. You write no Java today.

Two jobs, and the first one is not optional. If you leave without a working toolchain, week 2 is a wasted lecture for you and a slow one for everyone who has to wait.

<svg viewBox="0 0 480 96" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the two hours are divided</title><desc>A horizontal bar split into three parts: sixty minutes setting up the machine, fifty minutes designing on paper, and ten minutes committing the work.</desc><rect x="20" y="30" width="248" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="272" y="30" width="146" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="422" y="30" width="38" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="144" y="52">Set up the machine</text><text x="345" y="52">Design on paper</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)" text-anchor="middle"><text x="144" y="80">60 min</text><text x="345" y="80">50 min</text><text x="441" y="80">10</text></g><text x="441" y="52" font-family="Inter, sans-serif" font-size="11" font-weight="600" fill="var(--c-ink)" text-anchor="middle">Push</text></svg>

---

## Part 1. Get your machine working

Sixty minutes, on your own. Work through the four setup pages in order and do not skip ahead.

1.  [Install Java](../00-tools/java.md) — Temurin 25, then verify it from a terminal.
2.  [Install Eclipse](../00-tools/eclipse-setup.md) — including the checksum step.
3.  [Git and GitHub](../00-tools/git-github.md) — your own repository, your first push.

Each page has a troubleshooting section with the exact error messages you are likely to see. Read the message, find it on the page, then ask.

!!! mistake "Not in OneDrive, Desktop or Documents"

    Those folders sync to the cloud, and the sync client rewrites files while the compiler is reading them. The result is not a clear error. It is builds that fail then succeed, files that revert while you are editing, and eventually a git repository that corrupts itself.

    Put your workspace at `C:\dev\workspace` and your repository at `C:\dev\`. This costs you nothing now and saves you a weekend in week nine.

!!! verify "You are through part 1 when all four of these are true"

    In a fresh PowerShell window:

    ```powershell
    java -version      # reports 25
    javac -version     # reports 25
    git --version      # reports something
    ```

    And on github.com, your repository exists and contains at least one commit that you pushed from this machine.

    If any of those is missing, stay on part 1. Part 2 needs the repository.

---

## Part 2. Design before you code

Fifty minutes. Paper and pen. Close the laptop.

Read the scenario at the top of [the university system](university-system.md) and stop there. Do not scroll down to the class specifications. The whole value of this exercise is that you work it out first.

### Task 1. Find the things

List what you think should be classes. Aim for three to five, not ten.

For each one, write a single sentence saying why it earns a class, using the three questions from the lecture: does it have data of its own that must stay correct, are there rules about that data, and can you ask it to do something.

### Task 2. What each one knows

Under each class, list the data it owns. Beside each piece of data, write whether it can ever change after the object is created.

That second column is doing more work than it looks. It is the difference between a field and a fixed field, and week 3 turns it into a `final` keyword.

### Task 3. What each one does, including one refusal

Under each class, list what you can ask it to do.

Then the part that matters: for each class, write **one thing it can refuse**, and say what it needs to know in order to refuse it. A class that can only hand over its data is a container. A class that can say no is an object.

If one of your classes has nothing it can refuse, look again. Either you missed a rule in the scenario, or that thing is not really a class.

### Task 4. Who asks whom

Write three sentences describing what happens when a student enrols in a course. Use only this shape:

> X asks Y to ...

No sentence may say that one thing reaches into another and changes its data. If you cannot write it without doing that, you have found a place where a rule lives in the wrong class.

### Task 5. Two nouns that are not classes

The scenario contains nouns that should not become classes. Find two of them and write one sentence each saying why.

This is the exam question you will see in some form, and it is the one most people get wrong, because underlining nouns is easy and thinking about them is not.

### Task 6. Compare

Now open the class specifications on [the university system](university-system.md) page.

For every place you differ, write one line: what you had, what is there, and whether you would defend your version.

Some of your answers will be better. `Enrolment` is a perfectly reasonable class that the specification does not have. A different set of rules on `Course` may well be the right call. The comparison is the lesson, not the correction. From week 2 onward you use the specification's names, so that everyone's code can be talked about, but that is a convention, not a verdict on your design.

---

## Part 3. Commit your design

Ten minutes. Your design is work, so it belongs in the repository like everything else.

Type your notes into a file called `design.md` in your repository. Keep it rough. Bullet points are fine.

```text
## Student
knows:  studentId (never changes), name, gpa, enrolmentYear (never changes)
does:   register for a course, update gpa, report academic standing
refuses: registering for a seventh course
        needs to know: how many courses it already has

## Not classes
Enrolment year - a number that belongs to a student
Teaching load  - counted from the courses a professor has
```

Then:

```powershell
cd C:\dev\java-oop-yourname
git add design.md
git commit -m "Week 1: university system design"
git push
```

Refresh GitHub and check it is there. That is your second commit and your first piece of real work under version control.

---

## Before you leave

- [ ] `java -version` and `javac -version` both report 25
- [ ] Eclipse opens and runs a class
- [ ] Your repository exists on GitHub with your name on it
- [ ] `design.md` is pushed and visible in the browser
- [ ] You have written down at least one place where your design differs from the specification

If the first four are done, week 2 will work for you. If any are missing, finish them before the next lecture rather than during it.

## Going further

Only if you are done and have time left.

**Design something else.** Take a system you actually use, a food delivery app or a library or the university's own portal, and run tasks 1 to 5 on it in ten minutes. There is no specification to compare against, which is closer to real work.

**Find the hard rule.** In the scenario, "a course cannot be scheduled into a room too small for the students enrolled in it" involves three things at once. Work out which one should enforce it and what it needs to know. There is more than one defensible answer and you will meet this again in week 6.

**Set up your editor properly.** Turn on line numbers in Eclipse, and learn three shortcuts: ++ctrl+shift+t++ to open any class, ++alt+shift+r++ to rename, ++ctrl+shift+f++ to format. You will use them several hundred times this semester.

<div class="page-nav" markdown>
[Week 2, session 1. Your first class](first-class.md){ .page-nav__next }
</div>
