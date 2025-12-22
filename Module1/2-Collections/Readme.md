# Collections 

Java Collections Framework provides resizable data structures for storing and manipulating groups of objects. 
​
## Collection Interface

Collection is the root interface for most collection classes, defining basic operations like add, remove, size, and iteration. It serves as the foundation for List and Set. 
​
```
Iterable (root)
└── Collection (root interface)
    ├── List
    │   ├── AbstractList
    │   │   ├── ArrayList
    │   │   └── Vector
    │   └── AbstractSequentialList
    │       └── LinkedList
    ├── Set
    │   ├── AbstractSet
    │   │   ├── HashSet
    │   │   └── TreeSet
    │   └── LinkedHashSet
    └── Queue
        ├── AbstractQueue
        │   └── PriorityQueue
        └── Deque
            └── LinkedList (also implements List)

Map (separate hierarchy)
├── AbstractMap
│   ├── HashMap
│   ├── LinkedHashMap
│   └── TreeMap (also implements SortedMap)

```

## List Interface

List maintains insertion order and allows duplicate elements with indexed access. ArrayList and LinkedList implement are some wellknown classes which implement the List interface. 
​
### ArrayList (Class)
ArrayList uses dynamic arrays for fast random access but slower insertions/deletions in the middle. It grows automatically as elements are added.
​
```java

import java.util.ArrayList;

public class ArrayListExample {
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(15);
        numbers.add(20);
        System.out.println(numbers); // [10, 15, 20]
        System.out.println(numbers.get(0)); // 10
    }
}
```

### LinkedList (Class)

LinkedList uses doubly-linked nodes for efficient insertions/deletions anywhere but slower random access. It also implements Queue and Deque interfaces.
​
```java
import java.util.LinkedList;

public class LinkedListExample {
    public static void main(String[] args) {
        LinkedList<String> animals = new LinkedList<>();
        animals.add("Dog");
        animals.add("Cat");
        animals.addFirst("Lion");
        System.out.println(animals); // [Lion, Dog, Cat]
    }
}
```

## Set Interface

Set stores unique elements without duplicates. HashSet, LinkedHashSet, and TreeSet provide different ordering guarantees.
​
### HashSet (Class)
HashSet stores elements in hash table with no guaranteed order and constant-time performance for basic operations. Duplicates are automatically prevented.
​
```java
import java.util.HashSet;

public class HashSetExample {
    public static void main(String[] args) {
        HashSet<String> cars = new HashSet<>();
        cars.add("Volvo");
        cars.add("BMW");
        cars.add("BMW"); // Duplicate ignored
        cars.add("Ford");
        System.out.println(cars); // [Volvo, BMW, Ford] (order not guaranteed)
    }
}
```

### LinkedHashSet (Class)

LinkedHashSet maintains insertion order using hash table and linked list while ensuring uniqueness. It offers predictable iteration order.
​
```java

import java.util.LinkedHashSet;

public class LinkedHashSetExample {
    public static void main(String[] args) {
        LinkedHashSet<String> cars = new LinkedHashSet<>();
        cars.add("Volvo");
        cars.add("BMW");
        cars.add("Ford");
        cars.add("BMW"); // Duplicate ignored
        System.out.println(cars); // [Volvo, BMW, Ford]
    }
}

```

## SortedSet Interface

SortedSet maintains elements in ascending order with additional methods like first() and last(). TreeSet implements this interface.

### TreeSet (Class)

TreeSet uses red-black tree for sorted storage, automatic uniqueness, and log(n) performance. Elements must be comparable or provide Comparator.

```java

import java.util.TreeSet;

public class TreeSetExample {
    public static void main(String[] args) {
        TreeSet<String> cars = new TreeSet<>();
        cars.add("Volvo");
        cars.add("BMW");
        cars.add("Ford");
        cars.add("BMW"); // Duplicate ignored
        System.out.println(cars); // [BMW, Ford, Volvo]
    }
}

```

## Map Interface

Map stores key-value pairs with unique keys. HashMap, LinkedHashMap, and TreeMap offer different performance and ordering characteristics.
​
### HashMap (Class)

HashMap provides fast key-based access using hash table with no order guarantee. Keys must be unique; null keys/values allowed.
​
```java

import java.util.HashMap;

public class HashMapExample {
    public static void main(String[] args) {
        HashMap<String, Integer> fruits = new HashMap<>();
        fruits.put("Apple", 3);
        fruits.put("Banana", 5);
        fruits.put("Apple", 4); // Overwrites previous value
        System.out.println(fruits.get("Apple")); // 4
    }
}

```

### LinkedHashMap (Class)

LinkedHashMap maintains insertion order (or access order) while providing hash table performance. Ideal for caches requiring predictable iteration.

```java

import java.util.LinkedHashMap;

public class LinkedHashMapExample {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> numbers = new LinkedHashMap<>();
        numbers.put("One", 1);
        numbers.put("Two", 2);
        numbers.put("Three", 3);
        System.out.println(numbers); // {One=1, Two=2, Three=3}
    }
}

```

## SortedMap Interface

SortedMap maintains keys in ascending order with additional methods like firstKey() and lastKey(). TreeMap implements this interface.

### TreeMap (Class)

TreeMap uses red-black tree for sorted keys and log(n) operations. Keys must implement Comparable or provide Comparator.
​
```java

import java.util.TreeMap;

public class TreeMapExample {
    public static void main(String[] args) {
        TreeMap<String, Integer> numbers = new TreeMap<>();
        numbers.put("Second", 2);
        numbers.put("First", 1);
        numbers.put("Third", 3);
        System.out.println(numbers); // {First=1, Second=2, Third=3}
    }
}

```


```
Iterable (I)
    Collection - (I)
        List - (I)
            ArrayList - (C)
            LinkedList - (C)
        Set - (I)
            HashSet - (C)
            LinkedHashSet (C)
            SortedSet - (I)
            TreeSet - (C)

Map - (I)
    HashMap - (C)
    LinkedHashMap - (C)
    SortedMap - (I)
        TreeMap - (C)
 
Shortcuts -
 
   List - Allows duplicates
   Set - No duplicates
   Hash - Uses hashing ( insertion order not preserved)
   Linked - Insertion order preserved.
   Tree - Sorted
 
 ```