# Project card

*One page, before any code*

<p class="meta"><span class="badge">One per team</span><span class="badge badge--accent">Due Friday 4 December, 23:59</span></p>

The project card is your plan on one page: who is in the team, what the program is for, which classes it needs, and who builds what first. Writing it takes an hour. It is much cheaper to fix a weak design on one page than in five hundred lines of code.

You get feedback on your card in the seminar the week after you submit it.

## How to submit

1.  One member creates the team's GitHub repository and adds the others as collaborators.
2.  Copy the template below into a file called `PROJECT_CARD.md` at the top of the repository, fill it in, then commit and push it.
3.  Submit the repository link by Friday 4 December, 23:59.

The card stays in the repository. If your plan changes during the sprint, update the card and say what changed and why.

## The template

```markdown
# Project card

## Team

Team name:

| Name | GitHub username |
|------|-----------------|
|      |                 |

Repository: https://github.com/...

## The idea

What the program does, in two sentences:

Who uses it, and what they want to get done:

## Classes

Four to eight classes. Leave out the console class and main.

| Class | What it is responsible for, in one sentence |
|-------|---------------------------------------------|
|       |                                             |

## Relationships

- Hierarchy: abstract class ___ with subclasses ___ and ___.
  The method each subclass does differently: ___
- Owns a list: ___ holds a list of ___ and is the only class that changes it.
- Enum: ___ with the constants ___, each carrying ___.

## Menu

At least four actions:

1.
2.
3.
4.

## Sprint 2

What the finished program will do that this version will not, in one sentence:

## Who builds what first

| Member | The part they build first |
|--------|---------------------------|
|        |                           |
```

## A filled card

??? example "Turing Wheels, a car rental"

    **Team:** Turing Wheels. Ada Lovelace (`ada-l`), Alan Turing (`aturing`), Grace Hopper (`ghopper`).

    **The idea.** A small agency rents cars, vans and scooters by the day. Staff use the program to see what is available, rent a vehicle to a customer, and take it back.

    | Class | What it is responsible for |
    |---|---|
    | `Agency` | Keeps the fleet and the customers, and decides whether a rental can go ahead |
    | `Customer` | Keeps their contact details and their rentals, past and current |
    | `Vehicle` | Keeps its plate and state, and works out the price for a number of days |
    | `Rental` | Records which vehicle, which customer, how many days, and the price |

    **Relationships.**

    - Hierarchy: abstract `Vehicle`, with `Car`, `Van` and `Scooter`. Each works out `priceFor(days)` its own way, and a van adds a deposit.
    - Owns a list: `Agency` holds the list of vehicles and is the only class that adds to it or removes from it.
    - Enum: `FuelType`, with `PETROL`, `DIESEL` and `ELECTRIC`, each carrying a label and a cost per kilometre.

    **Menu.** 1 List available vehicles. 2 Rent a vehicle. 3 Return a vehicle. 4 Show a customer's history. 5 Income by vehicle type.

    **Sprint 2.** Rentals get real dates, late returns are charged, and staff use a window instead of the console.

    | Member | The part they build first |
    |---|---|
    | Ada | `Vehicle` and its three subclasses |
    | Alan | `Agency`, `Customer` and `Rental` |
    | Grace | The console menu and the sample data |

## What makes a good card

-   **The idea fits in two sentences.** If it needs a paragraph, it is probably two programs.
-   **Each class has one responsibility,** and you can say it in one short sentence. "Keeps the vehicles and prints the menu" mixes the rules with the talking, so it is two classes.
-   **The subclasses behave differently.** If `Car` and `Van` differ only in a field value, an enum is the better design, and you still need a real hierarchy somewhere else.
-   **The work is split by class,** not by "Ada writes the code, Alan writes the README". Everyone writes Java.

<div class="page-nav" markdown>
[Sprint 1](sprint-1.md){ .page-nav__next }
</div>
