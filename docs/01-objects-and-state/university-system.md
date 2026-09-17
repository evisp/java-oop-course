# The university system

<p class="meta"><span class="badge">Reference</span><span class="badge">Weeks 1 to 4</span><span class="badge badge--accent">Used in every seminar</span></p>

This is the problem you build in the seminars. It is deliberately not the bank from the lectures.

Following a worked example teaches you the syntax. Building something you have not been shown teaches you the skill, and the skill is what the exam and the sprints ask for. You will be stuck more often here than in the lectures. That is the point of the seminar.

## The scenario

> A university runs courses. Each course has a code, a title and a number of credits, and it runs in a particular semester.
>
> Students enrol at the university in a given year and have a grade point average that changes as they complete courses. A student can register for courses, but not for more than six at a time.
>
> Professors are employed by the university, each with a specialisation and a number of years of experience. A professor teaches courses. Nobody may be assigned more than four courses in a semester.
>
> Courses are taught in classrooms. A classroom has a capacity, and a course cannot be scheduled into a room too small for the students enrolled in it.
>
> Departments group professors together. Each department has a head.

That is everything. Read it slowly, because in the week 1 seminar you design the system from this paragraph alone.

## What each week adds

| Week | Seminar | What you build |
|---|---|---|
| 1 | Setting up and designing | Working machine, and the design on paper. No Java. |
| 2 | Your first classes | Three classes with fields, constructors, methods and `toString`. |
| 3 | Making them refuse | The same classes, now impossible to put into an invalid state. |
| 4 | Connecting the system | Relationships, collections, and objects asking each other for work. |

One folder, extended each week, in the repository you set up in week 1. Not a fresh start every seminar.

---

## The canonical classes

!!! mistake "Do not read this section before the week 1 seminar"

    Week 1 asks you to work out what the classes should be, on paper, from the scenario above. That exercise is worth nothing if you have already read the answer.

    Design first. Then come back here and see where you differ. Some of your differences will be better than what is below, and noticing that is the most useful thing you can get out of week 1.

From week 2 onward everyone uses these names and these fields, so that code is comparable and you can ask a precise question when you are stuck.

**Required: `Student`, `Professor`, `Course`.** These three are enough for every seminar.

**Optional: `Classroom`, `Department`.** Build them if you finish early or want the full system.

### Student

| Knows | Type | Notes |
|---|---|---|
| `studentId` | `String` | Fixed at enrolment, for example `S-1001`. Never changes. |
| `name` | `String` | Can change. Never blank. |
| `gpa` | `double` | Between 0.0 and 4.0. Starts at 0.0. |
| `enrolmentYear` | `int` | Fixed. Between 2000 and the current year. |

**Does**

- `updateGpa(double newGpa)` — records a new average. There is no setter for `gpa`.
- `academicStanding()` — returns `"Good standing"` at 2.0 or above, `"Academic probation"` from 1.5 up to 2.0, `"Academic warning"` below 1.5.
- `yearsEnrolled(int currentYear)` — how long they have been here.
- `toString()`

**From week 4:** a student registers for courses, and cannot hold more than six at once.

### Professor

| Knows | Type | Notes |
|---|---|---|
| `employeeId` | `String` | Fixed, for example `P-2001`. |
| `name` | `String` | Can change. Never blank. |
| `specialisation` | `String` | Can change. Never blank. |
| `yearsOfExperience` | `int` | Never negative. |

**Does**

- `academicTitle()` — `"Assistant Professor"` up to 5 years, `"Associate Professor"` from 6 to 10, `"Professor"` above 10.
- `isExperienced()` — more than five years.
- `completeAnotherYear()` — adds one year. There is no setter.
- `toString()`

**From week 4:** a professor teaches courses, and may not be assigned more than four in a semester.

### Course

| Knows | Type | Notes |
|---|---|---|
| `courseCode` | `String` | Fixed, for example `CS101`. |
| `title` | `String` | Can change. Never blank. |
| `credits` | `int` | Between 1 and 6. |
| `semester` | `String` | For example `"Fall 2026"`. Never blank. |

**Does**

- `fullName()` — returns `"CS101: Introduction to Programming"`.
- `isMajorCourse()` — three credits or more.
- `toString()`

**From week 4:** a course holds the students enrolled in it, is taught by one professor, and is scheduled in one classroom.

### Classroom, optional

| Knows | Type | Notes |
|---|---|---|
| `roomNumber` | `String` | Fixed, for example `A205`. |
| `building` | `String` | Fixed. |
| `capacity` | `int` | Greater than zero. |
| `hasProjector` | `boolean` | Can change. |

**Does**

- `canAccommodate(int numberOfStudents)`
- `isLargeRoom()` — capacity above 50.
- `location()` — returns `"Building A, Room 205"`.
- `toString()`

### Department, optional

| Knows | Type | Notes |
|---|---|---|
| `name` | `String` | Fixed. |
| `building` | `String` | Can change. |
| `headName` | `String` | Can change. Never blank. |

**Does**

- `toString()`

**From week 4:** a department holds its professors, reports how many there are, and reports the total teaching load across all of them.

---

## Rules for all of them

**One constructor.** It takes everything the object needs to be usable. There is no empty constructor, because an object with no id and no name is not a student.

**Fixed fields have a getter and no setter.** An id does not change. In week 3 they also become `final`, so the compiler enforces it rather than your memory.

**No class prints anything.** No `displayStudentInfo()`, no `System.out.println` inside a method. `toString()` returns text and `main` decides what to show. Week 13 puts a window on this and nothing in these classes will change.

**Behaviour, not just storage.** `academicStanding()` and `academicTitle()` are the interesting part. A class that is only fields with getters and setters has not been designed, it has been typed.

## Naming

- Class names singular and capitalised: `Student`, not `Students`.
- Fields and methods in `camelCase`.
- Ids are `String`, so `"S-1001"` rather than `1001`. Nobody does arithmetic on an id.
- Methods that answer a question start with `is` or `can`: `isExperienced`, `canAccommodate`.
- Methods that do something are verbs: `updateGpa`, `completeAnotherYear`.

Consistent names matter more than you expect. When you ask for help in week 9, "my `updateGpa` throws" is a question somebody can answer in ten seconds.

## Where your code goes

One folder in your own repository, extended each week.

```text
university/
    Student.java
    Professor.java
    Course.java
    UniversityApp.java
```

Commit at the end of every seminar. By week 4 the history shows how the design grew, which is the thing you show someone later.

<div class="page-nav" markdown>
[Week 1 seminar. Setting up and designing](seminar-1.md){ .page-nav__next }
</div>
