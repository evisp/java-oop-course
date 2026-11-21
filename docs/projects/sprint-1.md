# Sprint 1

*Your own program, built from Modules 1 and 2*

<p class="meta"><span class="badge">10 points</span><span class="badge">Teams of 2 to 4</span><span class="badge">Tag by Sunday 13 December, 23:59</span><span class="badge badge--accent">Demos 14 to 18 December</span></p>

**What you will hand in**

- A console program that does something useful, built from classes your team designed
- A repository with a README, a class diagram and commits from every member
- A demo where each of you explains the code and changes it live

## What you build

A program that runs in the console and manages the objects of your idea: it creates them, connects them, changes them through a menu, and reports on them. The rules live in your model classes, and the menu only asks and reports, the way `Bank` and `BankConsole` divide the work in the week 6 code.

Everything you need was covered in Modules 1 and 2. The links in the list below go to the matching cards on the [Key Concepts](../key-concepts/index.md) pages, so the list doubles as a revision guide.

## Requirements

### Classes that protect their state

<div class="pj-check" markdown>

- [ ] At least four classes of your own, not counting the console class and `main`
- [ ] Every field is `private`, and [`final`](../key-concepts/module-1.md#kc-final) where it never changes
- [ ] Constructors refuse invalid values, so no object can exist in a broken state ([invariant](../key-concepts/module-1.md#kc-invariant), [encapsulation](../key-concepts/module-1.md#kc-encapsulation))
- [ ] Setters only for fields that are allowed to change, and they check the new value too ([getter and setter](../key-concepts/module-1.md#kc-getter-setter))

</div>

### Relationships

<div class="pj-check" markdown>

- [ ] One inheritance hierarchy: an abstract class with at least two subclasses (weeks 7 and 8)
- [ ] At least one method that each subclass overrides, called in a loop over a list of the parent type, with no `instanceof` deciding what to do
- [ ] One class that owns a list of other objects and never hands that list out ([has-a relationship](../key-concepts/module-1.md#kc-has-a), [defensive copy](../key-concepts/module-1.md#kc-defensive-copy))
- [ ] Work done by the class that owns the data, not by a class that reaches into it ([responsibility](../key-concepts/module-2.md#kc-responsibility), [delegation](../key-concepts/module-2.md#kc-delegation))

</div>

### Data

<div class="pj-check" markdown>

- [ ] Your objects kept in a `List` and worked through with loops ([List](../key-concepts/module-1.md#kc-list), [for-each](../key-concepts/module-1.md#kc-foreach))
- [ ] An enum whose constants carry at least one value each ([enum constant](../key-concepts/module-2.md#kc-enum-constant))
- [ ] `equals` and `hashCode` for one class whose objects are identified by a code or a number ([equality](../key-concepts/module-2.md#kc-equality), [hashCode](../key-concepts/module-2.md#kc-hashcode))

</div>

### The console

<div class="pj-check" markdown>

- [ ] A menu that repeats until the user chooses to quit
- [ ] Wrong input, such as a letter where a number belongs or an option that does not exist, gets a message instead of a crash. Read every answer with `nextLine()` and convert it the way `BankConsole` does in the week 6 code
- [ ] Only the console class reads input and prints. The model classes contain no `System.out` ([model and user interface](../key-concepts/module-2.md#kc-model-ui))
- [ ] Sample data created when the program starts, so there is something to show straight away. The course uses Ada Lovelace, Alan Turing and Grace Hopper; you may too

</div>

### The repository

<div class="pj-check" markdown>

- [ ] A README with what the program does, how to run it, the menu, a class diagram, and how you used AI tools
- [ ] Commits from every member, spread across the sprint rather than all on the last night
- [ ] The version you submit tagged `sprint-1`

</div>

!!! tip "For a stronger project"

    None of these are required. Each one shows that you can choose a design, not only follow a list.

    - An interface implemented by two classes that are not related by inheritance
    - A place where you held another object instead of inheriting from it, with the reason in your README
    - A switch expression over your enum ([switch expression](../key-concepts/module-2.md#kc-switch-expression))
    - A rule that needs two objects to agree, such as a transfer between two clubs, owned by the class that holds both

!!! note "Not in Sprint 1"

    Exceptions of your own, reading and writing files, tests, and a window all belong to Modules 3 and 4, so they belong to Sprint 2. Keep Sprint 1 to what the requirements ask, and make it work well.

## Grading

Ten points in four areas. The first three are team marks: every member gets the same. The demo is marked for each person on their own, and it carries the most weight.

| Area | Points | Full marks look like | Points are lost for |
|---|---|---|---|
| **Design** | 3 | Classes with one clear job each. A hierarchy whose subclasses behave differently. A class that owns its list and does the work on it. Objects that cannot be put into a broken state. | Public fields. Subclasses that differ only in data. Lists handed out to be changed. Rules checked in the console instead of the model. |
| **Working program** | 2 | Every menu action works. Wrong input gets a message. The model never prints. It runs from the README instructions on a fresh clone. | Crashes on bad input. Menu options that do nothing. `System.out` in model classes. |
| **Repository** | 1 | A README with the class diagram and an honest note on AI use. Commits from every member across the sprint. The `sprint-1` tag. | No class diagram or AI note. One member's commits only. No tag. |
| **Demo and answers** | 4 | Marked per person. You explain any part of the program, including parts you did not write, and make a small change while we watch. | Not being able to explain code you handed in, whoever or whatever wrote it. |

The demo marks for each person:

| Points | What it looks like |
|---|---|
| 4 | Explains clearly why the code is the way it is, and makes the change without help |
| 3 | Explains the code correctly, and makes the change with a hint |
| 2 | Explains their own part, but not how it connects to the rest |
| 1 | Describes what the program does, but not how the code does it |
| 0 | Cannot explain the code they handed in |

## The demo

About twelve minutes per team, in your usual lecture or seminar session during the week of 14 December. The schedule is announced in class.

1.  **Run it, three minutes.** Show three menu actions of your choice and one piece of wrong input.
2.  **The design, two minutes.** Open your class diagram and say what each class is responsible for.
3.  **Questions, about two minutes each.** Each member is asked about a part of the code they did not write, then makes one small change, such as adding a constant to the enum or a check to a method, and runs the program again.

Bring the program open in Eclipse on one laptop, ready to run.

## Submitting { #submitting }

Commit and push your final version, then tag it:

```powershell
git tag sprint-1
git push origin sprint-1
```

The tag marks exactly what is graded. Commits after the tag do not count for Sprint 1, so you can keep working on Sprint 2 without worrying. If you tag too early, delete the tag and tag again before the deadline:

```powershell
git tag -d sprint-1
git push origin --delete sprint-1
```

## Before you submit

<div class="pj-check" markdown>

- [ ] A fresh clone runs by following only your README
- [ ] Every menu option works, and typing a letter where a number belongs does not crash it
- [ ] Searching the model classes for `System.out` finds nothing
- [ ] Every member can explain every class
- [ ] The README states how you used AI tools
- [ ] `git tag` lists `sprint-1`, and the tag is on GitHub

</div>

Most lost points come from a handful of mix-ups: [a reference and an object](../key-concepts/commonly-confused.md#vs-reference-object), [`static` and instance](../key-concepts/commonly-confused.md#vs-static-instance), [`==` and `equals`](../key-concepts/commonly-confused.md#vs-equals). Read those three pairs again before the demo.

<div class="page-nav" markdown>
[Back to the project overview](index.md){ .page-nav__next }
</div>
