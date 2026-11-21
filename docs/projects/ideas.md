# Project ideas

*Ten programs worth building, and how to check your own*

<p class="meta"><span class="badge">Pick one, adapt one, or propose your own</span><span class="badge badge--accent">Not a bank, not a university</span></p>

Each idea below is a program that someone could actually use, with a suggested design: the main classes, the class hierarchy, the class that owns a list, an enum, and the menu. The design is a starting point. Change the names, add a class, drop one, and write your own version on the [project card](project-card.md).

The last line of each idea shows how it grows in Sprint 2, so you can see that it has somewhere to go.

**Starter** ideas have a clear design and suit a team of two. **Standard** ideas have one rule that needs some thought. **Ambitious** ideas have several objects that must agree, and suit a team of three or four.

## At a glance

| Idea | Level | The hierarchy |
|---|---|---|
| [Restaurant orders](#restaurant) | Starter | `MenuItem`: drink, dish, dessert |
| [Library loans](#library) | Starter | `Item`: book, magazine, board game |
| [Parking garage](#parking) | Starter | `Vehicle`: car, motorbike, van |
| [Flat share expenses](#flat-share) | Standard | `Expense`: split evenly, split by shares |
| [Gym and classes](#gym) | Standard | `Membership`: monthly, pay per visit, student |
| [Event tickets](#tickets) | Standard | `Event`: concert, match, talk |
| [Car rental](#car-rental) | Standard | `Vehicle`: car, van, scooter |
| [Pet clinic](#pet-clinic) | Standard | `Pet`: dog, cat, rabbit |
| [Student marketplace](#marketplace) | Ambitious | `Listing`: books, electronics, furniture |
| [Football league](#football) | Ambitious | `Player`: goalkeeper, defender, forward |

## Starter

### Restaurant orders { #restaurant }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level">Starter</span><span>2 to 3 people</span></p>

Grace's café from week 6, grown into a system. Waiters open an order for a table, add items, and close it with a bill.

Classes
:   `Restaurant`, `Table`, `Order`, `MenuItem`

Hierarchy
:   abstract `MenuItem`, with `Drink`, `Dish` and `Dessert`. Each works out its own price: drinks come in sizes, dishes have a preparation time, desserts can be shared.

Owns a list
:   `Order` holds its items and works out its own total. `Restaurant` holds the tables and the menu.

Enum
:   `Size`: small, medium and large, each with a price factor.

Menu
:   show the menu · open an order for a table · add an item · close and print the bill · today's takings

Sprint 2
:   load the menu from a file, refuse an order for a table that is already taken, test the bills, a kitchen screen in a window.

</div>

### Library loans { #library }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level">Starter</span><span>2 to 3 people</span></p>

A small library for a neighbourhood, a club or a dorm. Members borrow items, return them, and pay if they are late.

Classes
:   `Library`, `Member`, `Item`, `Loan`

Hierarchy
:   abstract `Item`, with `Book`, `Magazine` and `BoardGame`. Each has its own loan period and its own late fee.

Owns a list
:   `Member` holds their loans, at most three at a time. `Library` holds the items and the members.

Enum
:   `Genre`, each with a label and a shelf code.

Menu
:   add an item · lend · take back · list overdue items · show a member's loans

Sprint 2
:   real dates with `LocalDate`, save everything to a file, test the fee rules, search the catalogue in a window.

</div>

### Parking garage { #parking }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level">Starter</span><span>2 people</span></p>

A garage with spots of different sizes. Vehicles come in, park in a spot that fits, and pay when they leave.

Classes
:   `Garage`, `Spot`, `Vehicle`, `Ticket`

Hierarchy
:   abstract `Vehicle`, with `Car`, `Motorbike` and `Van`. Each says which spot sizes it fits and what it pays per hour.

Owns a list
:   `Garage` holds its spots and the open tickets, and finds a free spot itself.

Enum
:   `SpotSize`: small, standard and large, each with a number of spots on each level.

Menu
:   park a vehicle · leave and pay · free spots by size · list parked vehicles · today's income

Sprint 2
:   fees from real arrival and departure times, save the day to a file, test the fee rules, a window that draws the levels.

</div>

## Standard

### Flat share expenses { #flat-share }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level pj-level--standard">Standard</span><span>2 to 3 people</span></p>

Flatmates record what they spend for the flat, and the program tells them who owes whom. Useful the day you share a flat.

Classes
:   `Flat`, `Flatmate`, `Expense`, `Payment`

Hierarchy
:   abstract `Expense`, with `EqualExpense`, split evenly between everyone, and `ShareExpense`, split by percentages. Each answers "how much does this flatmate owe?" in its own way.

Owns a list
:   `Flat` holds its flatmates, expenses and payments, and hands out none of those lists.

Enum
:   `Category`: rent, groceries, bills and outings, each with a label.

Menu
:   add a flatmate · record an expense · record a payment · show who owes whom · totals by category

Sprint 2
:   save the flat to a file, refuse shares that do not add up to 100 percent, test that the balances always add up to zero, a window with the balance table.

</div>

### Gym and classes { #gym }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level pj-level--standard">Standard</span><span>2 to 4 people</span></p>

Members of a gym book places in fitness classes that have a limited number of places.

Classes
:   `Gym`, `Member`, `FitnessClass`, `Booking`

Hierarchy
:   abstract `Membership`, with `Monthly`, `PayPerVisit` and `Student`. Each decides what a booking costs and how many bookings a week it allows.

Owns a list
:   `FitnessClass` holds its bookings and refuses one when it is full.

Enum
:   `ClassType`: yoga, spinning and boxing, each with a length in minutes and a number of places.

Menu
:   register a member · show this week's classes · book a place · cancel a booking · a member's bookings · income for the month

Sprint 2
:   a waiting list when a class is full, save to a file, test the booking limits, a timetable in a window.

</div>

### Event tickets { #tickets }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level pj-level--standard">Standard</span><span>2 to 4 people</span></p>

A ticket office for concerts, football matches and talks. Tickets are sold until the venue is full.

Classes
:   `TicketOffice`, `Event`, `Ticket`, `Customer`

Hierarchy
:   abstract `Event`, with `Concert`, `Match` and `Talk`. Each prices a ticket its own way: a concert by distance from the stage, a match by stand, a talk free for students.

Owns a list
:   `Event` holds its sold tickets and knows how many places are left. `Customer` holds the tickets they bought.

Enum
:   `TicketType`: standard, student and VIP, each with a price factor.

Menu
:   list the events · buy a ticket · return a ticket · places left · sales by event

Sprint 2
:   save the sales to a file, refuse a sale when the event is full, test the prices, choose seats in a window.

</div>

### Car rental { #car-rental }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level pj-level--standard">Standard</span><span>2 to 4 people</span></p>

A rental agency. Customers rent vehicles for a number of days and bring them back.

Classes
:   `Agency`, `Customer`, `Vehicle`, `Rental`

Hierarchy
:   abstract `Vehicle`, with `Car`, `Van` and `Scooter`. Each has its own daily price and its own rule, such as a van needing a deposit.

Owns a list
:   `Agency` holds the vehicles and the customers. `Customer` holds their rentals, past and current.

Enum
:   `FuelType`: petrol, diesel and electric, each with a cost per kilometre.

Menu
:   list available vehicles · rent · return · a customer's history · income by vehicle type

Sprint 2
:   real dates and late returns, save to a file, test the price rules, a booking calendar in a window.

</div>

### Pet clinic { #pet-clinic }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level pj-level--standard">Standard</span><span>2 to 3 people</span></p>

A veterinary clinic keeps track of owners, their pets and every visit.

Classes
:   `Clinic`, `Owner`, `Pet`, `Visit`

Hierarchy
:   abstract `Pet`, with `Dog`, `Cat` and `Rabbit`. Each has its own check-up interval and its own visit price.

Owns a list
:   `Owner` holds their pets. `Pet` holds its visits and knows when its next check-up is due.

Enum
:   `VisitType`: check-up, vaccination and treatment, each with a price.

Menu
:   register an owner and a pet · record a visit · pets due for a check-up · an owner's bill

Sprint 2
:   real dates, save to a file, test when check-ups fall due, a window for the day's appointments.

</div>

## Ambitious

### Student marketplace { #marketplace }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level pj-level--ambitious">Ambitious</span><span>3 to 4 people</span></p>

Students sell used books, electronics and furniture to each other. Buyers make offers, and sellers accept one.

Classes
:   `Marketplace`, `User`, `Listing`, `Offer`

Hierarchy
:   abstract `Listing`, with `BookListing`, `ElectronicsListing` and `FurnitureListing`. Each describes itself differently and has its own rule, such as electronics needing a warranty date.

Owns a list
:   `Listing` holds its offers. `User` holds their listings. Accepting one offer must close the listing and turn down the others, which is a rule for more than one object.

Enum
:   `Condition`: new, good and worn, each with a suggested price factor.

Menu
:   post a listing · browse by type · make an offer · accept an offer · my listings and offers

Sprint 2
:   search and filter with streams, save to a file, test the offer rules, a window with photos of the listings.

</div>

### Football league { #football }

<div class="pj-idea" markdown>

<p class="pj-tags"><span class="pj-level pj-level--ambitious">Ambitious</span><span>3 to 4 people</span></p>

The transfer story from week 5, grown into a league: clubs, squads, transfers and a table.

Classes
:   `League`, `Club`, `Player`, `Match`

Hierarchy
:   abstract `Player`, with `Goalkeeper`, `Defender` and `Forward`. Each works out its own rating for a match from different numbers.

Owns a list
:   `Club` holds its squad. A transfer changes two clubs at once, so `League` owns that rule and asks each club to do its part.

Enum
:   `Position`, each with the most players of that position allowed in a squad.

Menu
:   register a club · sign a player · transfer a player · record a result · show the table

Sprint 2
:   sort the table with a comparator, save the season to a file, test the transfer rules, a window showing the table.

</div>

## Your own idea

You can build something that is not on this page. Check it against this list before you write the project card. If it ticks every box, it is big enough for Sprint 1 and has room for Sprint 2.

<div class="pj-check" markdown>

- [ ] Someone would actually use it, and you can say who in one sentence
- [ ] At least four classes of your own, not counting the console class and `main`
- [ ] An abstract class with at least two subclasses that behave differently, not only hold different data
- [ ] One class that owns a list of other objects and does the work on that list
- [ ] An enum whose constants carry at least one value each
- [ ] At least four menu actions that change something or report something
- [ ] Not a bank and not a university system
- [ ] Something obvious to add in Sprint 2: saving, a rule that can fail, a window

</div>

<div class="page-nav" markdown>
[Project card](project-card.md){ .page-nav__next }
</div>
