# Java Collections and HashMaps

Collections are Java's **data containers** that scale better than raw arrays: they grow, shrink, and provide common operations directly. 

Arrays *can* work, but once the program needs lots of inserts, deletes, or fast lookups, Collections become the clean, safe tool for the job.  



## Why Collections are important

### The pain of *just arrays*
Arrays are rigid: their size is fixed, and resizing usually means creating a new array and copying everything over manually.    
Searching without a good structure often becomes `loop through everything until you find it`, which gets slow and messy when you have thousands of items.  

### What Collections give you instead
Collections are designed for real programs:

- Flexible sizing (you can keep adding/removing without manual resizing work).  
- Rich built-in methods for common tasks (add, remove, search, iterate).  
- Type safety through generics (e.g., `List<Customer>` instead of "array of Object").  

A good mental model: **Collections are tools for organizing data to match the problem**, not just storing it.  

![Collections](https://i.imgur.com/MIlIUOg.jpeg)

## What Collections are

Java Collections come in a few "families", each optimized for different needs:

- **List**: order matters (you care about position 0, 1, 2…).  
- **Set**: uniqueness matters (no duplicates allowed).  
- **Map**: store pairs so a key can instantly find its value.  
- **Queue / Stack**: process items in a certain order (like "first come, first served").  

When choosing a collection, four properties guide the decision:

- Order (does it matter?).  
- Duplicates (are they allowed?).  
- Access speed (instant lookup vs scanning).  
- Memory trade-offs (speed often costs extra space).  

![Choosing Collections Guide](https://i.imgur.com/uBQHwcL.png)

## Common generic methods (the shared "verbs")

Many collections support a shared set of everyday operations:

- `add(element)`: insert one element.  
- `remove(element)`: remove the first occurrence (if present).  
- `size()` and `isEmpty()`: quick checks for "how many" and "is there anything here?".  
- `contains(element)`: checks if an element exists (speed depends on the collection).  

### Iteration (looping) safely

The enhanced for-loop (also called the `for-each` loop) is the simplest way to *read* elements from a collection, one by one, in a clean and readable style.   

```java
for (Customer c : customers) {
    // read c
}
```

> One important rule: avoid adding or removing elements from the collection inside this loop, because modifying a collection while iterating can cause errors or unexpected behavior.   

**If modification is needed during traversal, use an explicit iterator**, which is the safe, intended approach for "iterate + modify" workflows.  


### Iteration with an Iterator

An **iterator** is a small helper object that gives a collection a controlled, step-by-step way to move through its elements. It’s slightly more verbose than the for-each loop, but it’s the standard tool when you need precise control over the traversal (especially when removing items).  

```java
Iterator<Customer> it = customers.iterator();

while (it.hasNext()) {
    Customer c = it.next();
    // read or inspect c
}
```

> The key benefit is safety: if you need to remove elements while looping, call `it.remove()` right after `next()` for the current element, instead of modifying the collection directly.

```java
Iterator<Customer> it = customers.iterator();

while (it.hasNext()) {
    Customer c = it.next();
    if (c.isInactive()) {
        it.remove(); // safe removal during iteration
    }
}
```

**Rule of thumb:** use for-each when you only read; use an iterator when you might change the collection during traversal.

![Iterator](https://i.imgur.com/Fc9bVtB.jpeg)


### Bulk operations (think "batch edits")
- `addAll(other)`: merge collections by adding everything from `other`.  
- `retainAll(other)`: keep only items also in `other` (intersection).  
- `removeAll(other)`: delete any item that appears in `other`.  

### Sorting in Collections

Sorting is simply putting items in a consistent order (small to large, A to Z, oldest to youngest). For numbers and strings, Java already knows how to compare them, so sorting "just works". For your own classes (like `Customer`), Java has no idea what "comes first" unless you **teach it a rule**.

That rule is called the object’s *natural order*. You define it by implementing `Comparable<T>` and writing `compareTo()`, which compares `this` object to another one. Then, when you call `Collections.sort(list)` (or `list.sort(...)`), Java keeps asking `compareTo()` questions like 
"Should A go before B?" until the entire list is arranged according to your rule.


```java
class Customer implements Comparable<Customer> {
    private final int id;
    private final String name;

    @Override
    public int compareTo(Customer other) {
        // Example natural order: by id (ascending)
        return Integer.compare(this.id, other.id);
    }
}
```

#### Why `equals()` also matters
When you define an ordering, you should also define what it means for two objects to be "the same." That's what `equals()` is for. A key consistency rule is:

> If `a.compareTo(b) == 0`, then `a.equals(b)` should usually be `true`.

This matters because some collection types (especially sorted ones) may treat `compareTo returns 0` as meaning **duplicate**, which can cause surprising behavior if `equals()` disagrees.

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Customer)) return false;
    Customer other = (Customer) obj;
    return this.id == other.id; // must match your compareTo logic
}
```

If you override `equals()`, also override `hashCode()` with the same fields used in `equals()` (required for correct behavior in hash-based structures like `HashSet` and `HashMap`).


---

## HashMaps (special focus)

Lists are great when data is mainly used by *position* (first, second, third) or when you usually process everything in order. But many real problems are not "find the 57th item" - they are "find the item with this ID". With a list, that kind of search usually means scanning from the start until you find a match, and that gets slower as the list grows.

A `HashMap<K,V>` stores **key–value pairs**: you provide a key, and you get the associated value back.  
It’s not index-based like a list; it’s more like a directory: “Account ID → Account details.”  
The main reason to use it is speed: lookups are designed to be nearly instant even when the map gets large.


### The core API you'll use most


#### What a `Map` represents

A `Map<K, V>` stores pairs: each **key** (`K`) points to exactly one **value** (`V`).  
In our example, the key is a `String` (a person's name) and the value is an `Integer` (their age), so the map models: `name → age`.

**Why `Map<String, Integer> nameToAge = new HashMap<>();` looks like that**

- `Map<String, Integer>` is the *interface type* (the “what it can do”).  
- `new HashMap<>()` is one *implementation* (the “how it does it”).  

This style lets you switch implementations later (e.g., to `TreeMap`) without changing the rest of your code.


#### The "daily" operations

**`put(key, value)` - add or update**
`put("Ada", 20)` stores the pair `"Ada" → 20`.  
If `"Ada"` is already in the map, `put` does not create a duplicate key; it **updates** the value for that key.

**`get(key)` - retrieve by key**
`get("Ada")` returns the value linked to `"Ada"`.  
If the key is not present, `get` returns `null`, which is why students should either check with `containsKey(...)` first or handle `null` safely.

**`remove(key)` - delete by key**
`remove("Ada")` deletes the entire pair for that key.  
After removal, `get("Ada")` will return `null` (because the key is no longer in the map).

**`containsKey(key)` - check existence**
`containsKey("Ada")` answers the question: “Is there an entry for this key?”  
This is often safer than relying on `get(...) != null`, especially if `null` could be a valid stored value in some designs.


#### Iterating: keys, values, and entries ("views")

A HashMap can give you three different “windows” into its data:

**1) `keySet()` - iterate over keys**
Use this when you only care about *which keys exist* (names, IDs, etc.).  
It gives a collection-like view of all keys, so you can loop through them with for-each.

**2) `values()` - iterate over values**
Use this when you only care about the stored values (ages, objects, totals).  
This is useful for aggregations (like finding the maximum age), but you don’t see the corresponding keys while looping.

**3) `entrySet()` - iterate over key–value pairs**
Use this when you need both the key and the value together.  
Each `Map.Entry<K,V>` is one pair, so `e.getKey()` and `e.getValue()` let you print or process both sides cleanly-this is the most common pattern for “iterate through everything in a map.”

Below an example

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> nameToAge = new HashMap<>();

nameToAge.put("Ada", 20);          // store/update a pair
int age = nameToAge.get("Ada");    // retrieve by key (may return null)
nameToAge.remove("Ada");           // delete by key
boolean hasAda = nameToAge.containsKey("Ada");
```
`put`, `get`, `remove`, and `containsKey` are the “daily” operations.    
To view or iterate, you commonly use `keySet()`, `values()`, or `entrySet()`.  

Example iteration patterns:
```java
// 1) keys
for (String key : nameToAge.keySet()) {
    System.out.println(key);
}

// 2) values
for (Integer value : nameToAge.values()) {
    System.out.println(value);
}

// 3) entries (key + value)
for (Map.Entry<String, Integer> e : nameToAge.entrySet()) {
    System.out.println(e.getKey() + " -> " + e.getValue());
}
```
These "views" are standard ways to access all keys, all values, or both together.  

### A "toy HashMap" mental picture (buckets + collisions)

![Hash Map Steps](https://i.imgur.com/NsDVJrd.jpeg)

#### Step 1: keys become numbers (hashing)
A hash function converts a key (like `"Ada"`) into a number called a hash code.    
That number points to a bucket (an internal “slot”) where the key–value pair should live.  

A common simplified picture is:
- Compute an index like `index = hash % arraySize` to choose a bucket.  

#### Step 2: buckets hold entries
Imagine the map has an internal array of buckets:

```
buckets  ->  (empty)
buckets  ->  ("Ada" -> 20)[1]
buckets  ->  (empty)[2]
buckets  ->  ("Evis" -> 42)
buckets  ->  (empty)
```

So `get("Ada")` works like:
1) hash `"Ada"` → pick bucket 1  
2) look inside bucket 1 → find `"Ada"` → return `20`  

#### Step 3: collisions (two keys land in the same bucket)
Sometimes different keys produce the same bucket index: this is a **collision**.    
A HashMap handles this by chaining multiple entries in the same bucket (you can picture a small linked list in that bucket).  

Toy collision picture:

```
buckets -> ("Ada" -> 20) -> ("Ava" -> 19) -> (end)
```

Now `get("Ava")` still starts in bucket 1, then checks entries inside that bucket until it finds the right key.    
Good hashing tries to distribute keys across buckets to reduce collisions and keep lookups fast.  

### HashMap intuition (bank system examples)

Use a `HashMap` when you keep asking: **“Given this identifier, what's the data?”**  
In a bank system, the identifier is often something unique (ID/code/IBAN/username), so direct lookup matters more than “the 5th item in a list”.

Good real-world `HashMap` scenarios:

- `customerId -> Customer`  
  Quickly load a customer record when you only know their ID.

- `accountNumber -> Account`  
  Find an account instantly during deposits/withdrawals/transfers.

- `cardNumber -> Card` (or card token)  
  Validate a card and fetch its linked account/customer.

- `branchCode -> Branch`  
  Jump to a branch object (address, staff, services) by code.

- `departmentName -> List<Customer>`  
  Group customers by department (Loans, Savings, Fraud) and fetch the group quickly.

- `customerId -> List<Account>`  
  Get all accounts owned by a customer without scanning all accounts in the system.

Rule of thumb: if the workflow starts with a key (ID/code) and you want the result **immediately**, a `HashMap` is usually the right tool.


### When to choose HashMap (rule-of-thumb)

Use a `HashMap` when your data naturally forms **key → value** pairs and your main question is: “Given this key, what’s the value?”  
It’s ideal for fast lookups like `accountId → Account`, `username → Profile`, or `courseCode → CourseInfo`.

Avoid it if you mainly care about order (use a `List`) or you only need membership testing without a value (use a `Set`).

### Quick wrap-up

Collections help you store and work with groups of data in a way that scales: they handle resizing, provide useful built-in operations, and make code cleaner than manual array management.  

Choose the structure that matches your question:

- Need order and positional access? Use a `List`.
- Need uniqueness? Use a `Set`.
- Need fast “given this key, find the value”? Use a `Map`-especially `HashMap`.

> If you understand **what question your code asks most often**, you can usually pick the right collection immediately.
