# Seminar 5. Making them work together

<p class="meta"><span class="badge">2 hours</span><span class="badge">Week 5 seminar</span><span class="badge badge--accent">Follows: Objects working together, Telling objects apart</span></p>

By the end of this session a `University` exists, it owns enrolment, and your classes know what makes two of them the same one.

You left a problem open in week 4. A student holds courses and a course holds students, and if only one side is updated they disagree. Neither class can fix that, because neither can see both sides.

<svg viewBox="0 0 480 96" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the two hours are divided</title><desc>A horizontal bar split into four parts: twenty minutes on identity, sixty minutes building the university and enrolment, twenty-five minutes trying to break it, and fifteen minutes to commit.</desc><rect x="20" y="30" width="71" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="95" y="30" width="214" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="313" y="30" width="89" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="406" y="30" width="54" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="55" y="52">Identity</text><text x="202" y="52">Enrolment</text><text x="357" y="52">Break it</text><text x="433" y="52">Commit</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)" text-anchor="middle"><text x="55" y="80">20</text><text x="202" y="80">60 min</text><text x="357" y="80">25</text><text x="433" y="80">15</text></g></svg>

---

## Identity

Twenty minutes.

If `Student` does not already have an id that never changes, add one now, generated the way you generated course codes or account numbers in week 3. Same for `Professor`.

Then a second constructor on `Student`, for a student whose id already exists:

```java
public Student(String studentId, String name, int enrolmentYear)
```

It does not touch the counter. You need it today to get two objects for one student, and you will need it in week 11 when students come back from a file.

Now `equals` and `hashCode` on `Student`, `Professor` and `Course`.

| Class | Compare on | Not on |
|---|---|---|
| `Student` | the id | the name, the year, the courses |
| `Professor` | the id | the name |
| `Course` | the code | the title, the credits, the semester |

Three rules that apply to all of them.

- The parameter is `Object`, not `Student`. Put `@Override` on it and let the compiler check.
- `hashCode` uses `Objects.hash` on exactly the field `equals` compares.
- A course with a corrected title is the same course.

Check it:

```java
Student ada = new Student("Ada Lovelace", 2024);
Student same = new Student(ada.getStudentId(), "Ada Lovelace", 2024);

System.out.println(ada == same);        // false
System.out.println(ada.equals(same));   // true
```

---

## Enrolment

Sixty minutes.

### The class

```java
public class University {

    private final String name;
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
```

Then the methods you already know the shape of:

- `addStudent(String name, int enrolmentYear)` makes the student and returns it
- `addCourse(String code, String title, int credits, String semester)`
- `findStudent(String studentId)` and `findCourse(String code)`, returning `null` when nothing matches
- `getStudents()` and `getCourses()`, each returning `List.copyOf`

`findStudent` loops over the university's own list. `findCourse` does the same. Neither one reaches into anything.

### Capacity

Add a maximum to `Course`, set in the constructor, and `isFull()`. A course cannot hold more students than its room.

### The method the session is about

```java
    public boolean enrol(String studentId, String courseCode)
```

Both sides of the relationship get updated here, and nowhere else. Write the checks in an order where the system is still correct at the end of every line:

1.  Student or course not found. Nothing has changed.
2.  Already enrolled. Nothing has changed.
3.  Student is at their course limit. Nothing has changed.
4.  Course is full. Nothing has changed.
5.  Only now, add the course to the student and the student to the course.

Then `withdraw(String studentId, String courseCode)`, which undoes both sides.

!!! mistake "Leaving `register` and `enrol` both public"

    `Student.register` from week 4 still exists and still works, and it only updates one side.

    If both are public, somebody will call the wrong one and the two lists will disagree. Decide what happens to `register`. Making it package-private so only `University` can call it is one answer. There are others. Say which you chose and why.

### The check that proves it

Count the enrolments from both directions. They have to match.

```java
    public int enrolmentsFromStudents()   // sum of each student's course count
    public int enrolmentsFromCourses()    // sum of each course's student count
```

Those two numbers must be equal after every operation, including the ones that failed. Print both after each `enrol` and each `withdraw`. This is the university's version of the bank total that never moves.

---

## Break it

Twenty-five minutes. Extend `BreakIt`.

Identity first:

```java
List<Course> list = ada.getCourses();
System.out.println(list.contains(javaCourse));
```

Then rename `equals` to `equals(Student other)` and delete `@Override`. It compiles. Run it again and say what `contains` does now.

Then enrolment:

- Enrol the same student on the same course twice
- Enrol a student id that does not exist
- Fill a course to capacity and enrol one more
- Enrol a student who already has their maximum courses
- Withdraw a student from a course they were never on

After every one of those, print `enrolmentsFromStudents()` and `enrolmentsFromCourses()`. If they ever differ, a half-finished `enrol` got through, and the order of your checks is wrong.

`BreakIt` scores zero before you commit.

---

## Commit

Fifteen minutes.

```powershell
git add .
git commit -m "Week 5: University owns enrolment, equals and hashCode on the model"
git push
```

## Before you leave

- [ ] `Student`, `Professor` and `Course` each have an id or code that never changes
- [ ] `equals` and `hashCode` on all three, compared on that field only
- [ ] `@Override` on every `equals`, with `Object` as the parameter
- [ ] `University` holds students and courses, both lists private and final
- [ ] `enrol` updates both sides and nothing else does
- [ ] The two enrolment counts agree after every operation
- [ ] `BreakIt` covers the new methods and scores zero

## Going further

**Waiting lists.** When a course is full, `enrol` returns `false`. Add a waiting list to `Course` and put the student on it instead. Then say who is responsible for moving somebody off the waiting list when a place opens, and whether that is the same class that owns `enrol`.

**Two universities.** Make a second `University` and try to enrol a student from the first one on a course from the second. Your code probably allows it. Decide whether it should, and if not, what `enrol` has to check.

**The room rule.** "A course cannot be scheduled into a room too small for the students enrolled in it." You first met this in week 1 and your answer has changed twice. Write it down again now that `University` exists.

<div class="page-nav" markdown>
[Back to the module](index.md){ .page-nav__next }
</div>
