# Seminar 4. Connecting the system

<p class="meta"><span class="badge">2 hours</span><span class="badge">Week 4 seminar</span><span class="badge badge--accent">Follows: Many objects, Objects that own other objects</span></p>

By the end of this session your classes are connected: students hold courses, professors hold courses, and neither hands its list out to anybody. This is the last seminar of Module 1, and what you finish with is where Sprint 1 starts.

Three things get built today, and the third one is the one the whole module has been aiming at. A class that owns a collection is responsible for what happens to it, and a class that owns other objects asks them to work rather than reaching in.

<svg viewBox="0 0 480 96" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the two hours are divided</title><desc>A horizontal bar split into four parts: fifteen minutes on a warm-up, sixty minutes building the relationships, twenty minutes trying to break them, and fifteen minutes to commit and read the sprint briefing.</desc><rect x="20" y="30" width="58" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="82" y="30" width="234" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="320" y="30" width="78" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="402" y="30" width="58" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="51" y="52">Warm-up</text><text x="199" y="52">Connect them</text><text x="359" y="52">Break</text><text x="431" y="52">Sprint</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)" text-anchor="middle"><text x="51" y="80">15</text><text x="199" y="80">60 min</text><text x="359" y="80">20</text><text x="431" y="80">15</text></g></svg>

---

## Warm-up. Give a student a list

Fifteen minutes.

Add one field to `Student`:

```java
private final List<Course> courses = new ArrayList<>();
```

Three deliberate choices in one line, and you should be able to say all three out loud.

`private`, so nobody outside can touch it. `final`, so the student always has exactly this list, even though what is in it changes. Created where it is declared, so there is no path through your code where a student exists without a list.

Then add `getCourseCount()`, which returns `courses.size()`, and print it for a new student. It should say zero, and your existing `UniversityApp` should still compile and run.

If Eclipse offers to import `java.util.*`, take the two specific imports instead. `java.util.ArrayList` and `java.util.List`.

---

## Connect them

Sixty minutes. Two relationships, both one-to-many, both with a rule.

### Students register for courses

- `register(Course course)` adds a course. A student may not hold more than six.
- `hasCourse(String courseCode)` answers whether they are registered.
- `drop(String courseCode)` removes one.
- `totalCredits()` returns the sum of the credits of every course they hold.

Two decisions to make and defend.

**What does `register` do when the student already has six?** You met this in the lecture: return a value for outcomes the caller should expect, throw for situations the caller should have prevented. A student reaching their limit is a normal thing that happens. Decide, and be able to say why.

**What does `drop` return when the course is not there?** Same question, different shape.

`totalCredits()` is the important method here, and not because it is hard. Look at what it does: it walks the list and asks each course for its credits. It does not know how a course stores them, and it does not do anything to the course. That is delegation, and it is the point of the session.

### Professors teach courses

Same shape, different limit. A professor may not be assigned more than four courses.

- `assign(Course course)`
- `totalTeachingCredits()`

You will notice that `Student.register` and `Professor.assign` are nearly the same method with a different number in them. That is real duplication and it is genuinely annoying. You cannot fix it properly with what you know today. Write it twice, notice it, and bring the annoyance to week seven, where it is the whole topic.

### Never hand the list out

Both classes need a way to let the outside see the courses.

The version everybody writes first gives the caller your actual list, and then they can call `clear()` on it. Every rule `register` was enforcing is gone, and the class did it to itself in one line.

```java
    public List<Course> getCourses() {
        return List.copyOf(courses);
    }
```

Be precise about what that protects. The **list** is a new one, so nobody can add to or clear yours. The **courses** in it are the same courses, because a list holds references. Somebody with the copy can still change a course's title. You have protected which courses the student holds, which is what was yours to protect.

### Check it works

In `UniversityApp`:

```java
Student ada = new Student("Ada Lovelace", 3.9, 2024);
Course java = new Course("CS101", "Programming in Java", 6, "Fall 2026");
Course maths = new Course("MA101", "Discrete Mathematics", 5, "Fall 2026");

ada.register(java);
ada.register(maths);

System.out.println(ada.getCourseCount());    // 2
System.out.println(ada.totalCredits());      // 11

for (Course course : ada.getCourses()) {
    System.out.println("  " + course.fullName());
}
```

Then register seven courses and watch the seventh be refused.

---

## Break them

Twenty minutes. Extend `BreakIt` from last week.

```java
ada.getCourses().clear();
System.out.println(ada.getCourseCount());
```

The count must still be right and the `clear()` must fail. Read the exception and note which class threw it.

Then:

```java
Course stolen = ada.getCourses().get(0);
stolen.setTitle("Nonsense");
System.out.println(ada.getCourses().get(0).fullName());
```

This one **does** change what the student sees, and it should. Write one sentence explaining why `clear()` was blocked and `setTitle()` was not. If you can answer that, you have understood both this session and week 2.

Then try these:

- Register the same course twice. Does your code allow it? Should it?
- Register a `null` course.
- Drop a course that was never registered.
- Assign a professor five courses.

Fix what you find. `BreakIt` scores zero again before you commit.

---

## Commit, and read the sprint

Fifteen minutes.

```powershell
git add .
git commit -m "Week 4: students register for courses, professors teach them"
git push
```

**Sprint 1 opens this week.** Everything you need is in this module. You will build your own application, from your own classes, in this repository. The sprint brief is on the projects page.

The four commits in your history are worth looking at before you start. Week 1 is a design on paper. Week 2 is three classes that accept anything. Week 3 is the same classes refusing to be broken. Week 4 is a system. That progression is the thing you show somebody later, and it is why the commits matter more than the final state.

## Before you leave

- [ ] `Student` and `Professor` each hold a `private final List`
- [ ] `register` and `assign` enforce their limits
- [ ] `totalCredits()` asks each course rather than reaching into it
- [ ] Neither class returns its internal list
- [ ] `BreakIt` covers the new methods and scores zero
- [ ] Pushed, with four weeks of history visible on GitHub

## Going further

**Let a course hold its students.** The specification says a course knows who is enrolled in it. Add `List<Student>` to `Course` and an `enrol` method.

Now the hard part: if a student registers for a course, and the course also holds its students, both sides have to agree. Write down what goes wrong if only one side is updated, and what happens if you make each side update the other. There is a trap in that second idea and finding it is the exercise. This is a Module 2 topic and you are not expected to solve it today.

**Department.** Give it a `List<Professor>`, a `facultyCount()` and a `totalTeachingLoad()` that asks each professor. Three levels of delegation, department to professor to course, and no level reaches past the next one.

**The rule nobody owns.** "A course cannot be scheduled into a room too small for the students enrolled in it." It involves a course, a classroom and a count of students. Decide who enforces it and what they need to know. You raised this in week 1. See whether your answer has changed.

<div class="page-nav" markdown>
[Back to the module](index.md){ .page-nav__next }
</div>
