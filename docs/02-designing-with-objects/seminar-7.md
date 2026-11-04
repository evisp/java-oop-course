# Seminar 7. A family of things

<p class="meta"><span class="badge">2 hours</span><span class="badge">Week 7 seminar</span><span class="badge badge--accent">Follows: Building on another class, When inheritance hurts</span></p>

By the end of this session `Student` and `Professor` are built on a shared parent, and you have written one subclass that breaks its callers and worked out why.

<svg viewBox="0 0 480 96" role="img" xmlns="http://www.w3.org/2000/svg"><title>How the two hours are divided</title><desc>A horizontal bar split into four parts: fifteen minutes finding the duplication, fifty minutes building the Person hierarchy, forty minutes on the subclass that breaks, and fifteen minutes to commit.</desc><rect x="20" y="30" width="53" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><rect x="77" y="30" width="178" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="259" y="30" width="143" height="34" rx="6" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><rect x="406" y="30" width="53" height="34" rx="6" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="46" y="52">Find it</text><text x="166" y="52">Person</text><text x="330" y="52">Break it</text><text x="432" y="52">Commit</text></g><g font-family="JetBrains Mono, monospace" font-size="11" fill="var(--c-muted)" text-anchor="middle"><text x="46" y="80">15</text><text x="166" y="80">50 min</text><text x="330" y="80">40 min</text><text x="432" y="80">15</text></g></svg>

---

## Find the duplication

Fifteen minutes.

Open `Student` and `Professor` side by side. In week 4 you were told to notice this and leave it. List everything that appears in both files:

- A name
- An id, generated the same way
- A `private final List<Course>`
- A register or assign method that differs only in the limit
- A total credits method that is identical
- A `getCourses` that is identical
- `equals` and `hashCode` with the same shape

That list is the brief for the next fifty minutes.

Write down one thing that is in only one of them. You will need it in a minute.

---

## Person

Fifty minutes.

### The parent

```java
public abstract class Person {

    private final String id;
    private String name;
    private final List<Course> courses = new ArrayList<>();

    protected Person(String idPrefix, String name) { ... }
```

Make it `abstract` now rather than later. There is no such thing as a person at this university who is neither a student nor a member of staff.

What goes in `Person`: the id, the name, the list, `getCourses()` returning a copy, `totalCredits()`, `getCourseCount()`, `equals` and `hashCode` on the id.

The list stays **private**. Both subclasses need to add to it and neither may reach it.

### The method that differs

```java
    // In Person
    public boolean register(Course course) {
        if (course == null || courses.contains(course)) {
            return false;
        }
        courses.add(course);
        return true;
    }
```

The common part: refuse nonsense, refuse duplicates, add. Then each subclass does its own check first and hands the rest up:

```java
    // In Student
    @Override
    public boolean register(Course course) {
        if (getCourseCount() >= level.getMaxCourses()) {
            return false;
        }
        return super.register(course);
    }
```

`Professor` is the same shape with four as the limit. Write both, then say out loud what `super.register` is doing and why neither subclass touches the list.

### The abstract method

Each subclass has something the other does not, which is what you wrote down in the warm-up. Pick one and make it an abstract method on `Person`:

```java
    public abstract String getRole();
```

A student returns its level's label. A professor returns its title or department. Nothing in `Person` guesses.

### One loop

In `University`, or in a small test app:

```java
List<Person> everybody = new ArrayList<>();
everybody.add(ada);
everybody.add(alan);
everybody.add(professorHoxha);

for (Person person : everybody) {
    System.out.printf("%-24s %-16s %d credits%n",
            person.getName(), person.getRole(), person.totalCredits());
}
```

No `if`, no `instanceof`, no `getType`. If you need any of those, the abstract method is in the wrong place.

---

## Break it

Forty minutes. This is the half of the session that matters.

### A method written against the parent

```java
    private static void registerForAll(Person person, List<Course> courses) {
        for (Course course : courses) {
            if (person.register(course)) {
                System.out.println("  added " + course.getCode());
            } else {
                System.out.println("  refused " + course.getCode());
            }
        }
    }
```

No `try`, no `catch`, no check for what kind of person it was handed. Run it over a student and a professor. It works.

### Now break it

```java
public class VisitingLecturer extends Professor {

    @Override
    public boolean register(Course course) {
        throw new UnsupportedOperationException(
                "A visiting lecturer is not assigned courses.");
    }
}
```

The reasoning is good. A visiting lecturer really is not assigned courses, and `false` looks like "already at the limit", which is a different thing.

Run `registerForAll` over a list containing one. Read the stack trace and answer four things in writing.

1.  Which file did you edit, and which file crashed?
2.  What would `registerForAll` have to do to survive this, and what does that cost the next person who adds a kind of person?
3.  Give a way to make the refusal loud that does not break `registerForAll`. There is more than one.
4.  Should `VisitingLecturer` extend `Professor` at all? Write what you would build instead.

### The one that is not inheritance

A teaching assistant is a doctoral student who also teaches a course.

Try it as `class TeachingAssistant extends Student` and then as `extends Professor`. Pick either one and write down what you lose. Then do it without `extends` at all, by holding the other object, and say which version you would rather maintain.

A class gets one parent. This is the limit, and next week is how you get round it.

### The usual list

- `register(null)`
- Register the same course twice through `University.enrol`
- A doctoral student registering for a third course
- Compare a `Student` and a `Professor` that somehow share an id. What does `equals` say, and what should it say?

`BreakIt` scores zero.

---

## Commit

Fifteen minutes.

```powershell
git add .
git commit -m "Week 7: Person as a shared parent, and the subclass that breaks its callers"
git push
```

## Before you leave

- [ ] `Person` is abstract and holds the id, the name and the course list
- [ ] The list is private and neither subclass reaches it
- [ ] `Student.register` and `Professor.register` call `super.register`
- [ ] One abstract method on `Person` that both subclasses answer
- [ ] One loop over a `List<Person>` with no `instanceof` in it
- [ ] `VisitingLecturer` written, run, and your four answers written down
- [ ] `BreakIt` scores zero

## Going further

**Make the limit abstract instead.** Delete `register` from both subclasses and give `Person` an abstract `maxCourses()` that the common `register` calls. Both designs work. Write down which one you would hand to somebody else and why.

**Department.** Give it a `List<Professor>` and a `totalTeachingLoad()` that asks each professor, who asks each course. Three levels, and no level reaching past the next one.

**The substitution test on your own code.** Take every class in your project that uses `extends`, including the ones from earlier weeks. For each, write one sentence answering: can anything written for the parent be handed this class without being told? If you cannot answer yes, say what you would do instead.

<div class="page-nav" markdown>
[Back to the module](index.md){ .page-nav__next }
</div>
