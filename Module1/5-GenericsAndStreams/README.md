# Generics and Streams

This module covers the concepts of generics and streams in programming. Generics allow you to create classes, interfaces, and methods that operate on a parameterized type, enabling code reusability and type safety. Streams provide a way to process sequences of elements, such as collections, in a functional style.

## Generics

Generics enable you to define classes, interfaces, and methods with a placeholder for the type of data they operate on. This allows you to create more flexible and reusable code. For example, you can create a generic class `Box<T>` that can hold any type of object:

```java

public class Box<T> {
    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}
```
You can then create instances of `Box` for different types:

```java

Box<Integer> intBox = new Box<>();
intBox.setItem(10);

Box<String> strBox = new Box<>();
strBox.setItem("Hello");

```

Generics also support bounded type parameters, allowing you to restrict the types that can be used. For example:

```java

public <T extends Number> double add(T number1, T number2) {
    // add the the number
    return number1.doubleValue() + number2.doubleValue();
}

```

This method can only accept types that are subclasses of `Number`. For example, `Integer`, `Double`, etc.

```java

 add(10, 20); // valid
 add(10.5, 20.5); // valid

 add("Hello", "World"); // invalid

```

## Functional Style Programming (Functional Programming)

Functional programming is a programming paradigm that treats computation as the evaluation of mathematical functions and avoids changing state and mutable data. It emphasizes the use of higher-order functions, first-class functions, and immutability.

* higher-order functions: Functions that can take other functions as arguments or return them as results.
* first-class functions: Functions that can be treated like any other variable, meaning they can be assigned to variables, passed as arguments, and returned from other functions.
* immutability: The concept of data that cannot be changed after it is created.

Example for functional style programming using Java:

```java

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

List<Integer> squaredNumbers = numbers.stream()
    .map(n -> n * n)
    .collect(Collectors.toList());

System.out.println(squaredNumbers); // Output: [1, 4, 9, 16, 25]

``` 

In this example, we use the `map` function to apply a lambda expression that squares each number in the list, demonstrating the functional programming style.

### Why Functional Programming?

Functional programming offers several advantages, including:

* Improved readability: Functional code is often more concise and easier to understand, as it focuses on what to do rather than how to do it.
* Easier maintenance: Functional code is often easier to maintain and modify, as functions are self-contained and do not rely on external state.
* Enhanced modularity: Functions can be reused and composed, promoting code reuse and modular design.
* Better support for parallelism: Functional programming can make it easier to write concurrent and parallel code, as functions do not have side effects and can be executed independently

### Drawbacks of Functional Programming

Functional programming can sometimes lead to performance issues due to the creation of intermediate objects and the use of recursion instead of iteration. Additionally, it may not be suitable for all types of problems, especially those that require mutable state or side effects(means changes to data).

For example, in scenarios where performance is critical and low-level optimizations are necessary, functional programming may introduce overhead due to the creation of multiple intermediate data structures. Consider the following example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> doubledNumbers = numbers.stream()
    .map(n -> n * 2)
    .collect(Collectors.toList());
```

In this case, each operation in the stream may create intermediate lists, which can lead to increased memory usage and slower performance compared to imperative programming approaches that modify data in place.


### Functional Interfaces and Lambdas

Functional interfaces are interfaces that contain a single abstract method. They are used to represent functions as first-class citizens in programming languages that support functional programming concepts. In Java, functional interfaces can be implemented using lambda expressions or method references.

Interface `Runnable` is a functional interface because it has a single abstract method `run()`, similarly `Callable`, `Comparator`, `Supplier`, `Consumer`, and `Function` are also functional interfaces.

Consider the following add example of a functional interface:

```java

@FunctionalInterface
interface MyAdder {
    int add(int a, int b);
}

```

Now this can be implemented using a lambda expression:

```java
MyAdder adder = (a, b) -> a + b;
int result = adder.add(5, 10);
System.out.println(result); // Output: 15
```

In the above example, we define a functional interface `MyAdder` with a single abstract method `add()`. We then create an instance of `MyAdder` using a lambda expression that implements the `add()` method. Lambda expressions provide a concise way to represent functions and can be used wherever a functional interface is expected.

## Streams

Streams provide a powerful way to process sequences of elements in a functional style. They support operations such as filtering, mapping, and reducing. The purpose of streams is to enable functional-style operations on collections of data.

* filtering: Selecting elements that meet certain criteria.
* mapping: Transforming elements from one form to another.
* reducing: Combining elements to produce a single result.

Streams are composed of three main components:

* source: The data source for the stream (e.g., a collection).
* intermediate operations: Operations that transform the stream (e.g., filter, map).
* terminal operations: Operations that produce a result or side effect (e.g., collect, forEach, reduce).

Streams can be created from various data sources, such as collections, arrays, or I/O channels.

For example, you can create a stream from a list and perform various operations:

```java

List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
List<String> filteredNames = names.stream()
    .filter(name -> name.startsWith("A"))
    .collect(Collectors.toList());
System.out.println(filteredNames); // Output: [Alice]

```

You can also use streams to perform transformations on data. For example, you can convert a list of strings to uppercase:

```java

List<String> upperCaseNames = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

System.out.println(upperCaseNames); // Output: [ALICE, BOB, CHARLIE, DAVID]

``` 

Streams also support reduction operations, such as finding the sum of a list of numbers:

```java

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

int sum = numbers.stream()
    .reduce(0, Integer::sum);   

System.out.println(sum); // Output: 15

```    
