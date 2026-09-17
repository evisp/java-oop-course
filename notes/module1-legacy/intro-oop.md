Object-Oriented Programming (OOP) is a way to **design software around *things* instead of just steps**. If basic programming taught variables and functions, OOP asks a bigger question

> What are the key things in this problem, what do they know, and what can they do? 

That shift—from actions first to objects first—is the paradigm change. 

Imagine you are asked to build a simple `Student Management System` for a small university. The goal is straightforward: let students sign up for courses, let instructors record grades, and let admins view clean reports. The system should be easy to use, keep data consistent, and adapt as policies change.

There are some essential things to consider: 
- Who uses the system and what they need (e.g., students, instructors, administrators).
- The core actions, such as enroll in a course, drop a course, record a grade, generate a transcript.
- Basic rules and limits: course capacity, prerequisites, add/drop deadlines.
- Data integrity and clarity: one source of truth for student records, courses, and enrollments.

![SMS](https://i.imgur.com/tg7Xf7T.jpeg)


### Why OOP (vs procedural)
If you were to build this in a procedural language like `C`, you’d likely write functions that pass around student IDs, course codes, and grade arrays, *carefully coordinating who updates what and in which order*. 

As features grow (capacity checks, prerequisites, transcripts), the function calls multiply and it becomes harder to see where each rule truly lives.

But, ...
>  OOP models (this) differently 

**Data and behavior travel together inside objects**. A Course object owns its roster and capacity rules; a Student requests enrollment; an Enrollment records the student–course–term; a Transcript computes GPA behind a simple method. Responsibilities sit with the *thing* that owns them, so changes stay local and the system scales without turning into a tangle of cross-cutting updates.

### The four pillars

![4 Pillars](https://i.imgur.com/yrYRSDz.jpeg)

**Encapsulation** keeps an object’s data and its related methods together and limits direct access. In a Student Management System, `Course` would expose `enroll(student)` rather than letting outside code edit the roster; this protects integrity and centralizes rules.

**Abstraction** presents a simple interface and hides inner details. A `Transcript` might offer `gpa()` and `addResult(course, grade)` without revealing storage or formulas, so the inside can change without breaking callers.

**Inheritance** shares common traits through a general type and specializes where needed. If Person holds name and email, `Student` and `Professor` can extend it with their own responsibilities, reducing duplication and keeping shared updates easy.

**Polymorphism** lets code work with many specific types through a common interface. A notifier that targets a `Notifiable` role can message `Student` or `Professor` the same way, while each decides how to receive it, keeping the system flexible as it grows.


Think of the four pillars as four levers that keep a growing codebase simple without dumbing it down. 

Encapsulation wraps data together with the rules that keep it valid, so changes happen in one safe place instead of leaking across files. Abstraction gives a clean surface to the outside world, so callers talk in the language of the problem while the moving parts stay hidden and free to improve. 

Inheritance collects what’s truly shared into a common type, so specialization adds details instead of copying the same code again. Polymorphism lets different kinds of things respond to the same request in their own way, so new cases fit without rewriting the caller. 

Used together, these ideas reduce coupling, make intent obvious, and keep feature work focused where it belongs.


### Things to remember

- Think in objects, not steps: identify the main *things* in the problem and what each is responsible for.
- Keep data with its rules: put the logic that protects or updates data next to the data itself.
- Show a simple surface: expose clear methods for what something does; hide how it does it.
- Keep changes local: design so new features or policies touch only the objects that own those rules.

### Further learning

- These ideas can be tricky at first. To go deeper, explore beginner-friendly tutorials that walk through [classes, objects](https://www.w3schools.com/java/java_oop.asp), and the [four pillars](https://backend.turing.edu/module1/lessons/four_pillars_of_oop) step by step.
- Prefer video? Try a short [OOP walkthrough for newcomers](https://www.youtube.com/watch?v=1ONhXmQuWP8) that illustrates concepts with clear examples.
- Want practice? Pick a [hands-on guide](https://medium.com/@brandon93.w/full-step-by-step-guide-in-oop-java-ba887394b966) with small projects to apply encapsulation, inheritance, abstraction, and polymorphism right away.