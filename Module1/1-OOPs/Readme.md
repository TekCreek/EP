# Object Oriented Programming Essential Features (Overview)

## Opening the Source code 

1. Open Intellij
2. Choose open option 
3. Choose pom.xml from the Source\OopsBasics folder
4. Open as project

## Topics

* Encapsulation
* Overloading
* Inheritance
* Overriding 
* Abstract classes
* Polymorphism

## Encapsulation 

The process of combining the data and operations over the data is referred to as encapsulation. This gives us some additional flexibility to restrict direct access to data and allow only operations to operate on it. This process of restricting direct access to data is referred as data hiding i.e. hidden from direct access. 

These are object oriented concepts and they are not related to security in anyway.

In the following example there are four members present in the class Account

1) balance field - private
2) debit method  - public 
3) credit method - public 
4) showBalance method - public 

Out of these balance is a private member and the rest are public members, which means that balance is private to the class Account and no outsider can access it. Where as other members are made public i.e. given public access. 

Think about this, can you change the balance in your bank account directly? NO, you need to go through the credit and debit operations to modify it. And also balance is not hidden from you, it is only hidden from direct modifications. 

You may have another question, balance being a private member can I access it in debit operation ?

Yes, because debit is also part of the same class definition and hence there is no restriction. 

```java
class Account {
    
    // Attributes

    // default is zero hence initialization not required.
    private double balance = 0; 
	
    // Operations 
    
    public void debit(double amount) {
	 if (amount > 0 && amount <= balance) {
	    balance -= amount;
	 }
    }
	
    public void credit(double amount) {
	 if (amount > 0) {
	    balance += amount;
	 }
    }
	
    public void showBalance() {
	    System.out.println("Current balance - " + balance);
    }
}

public class EncapsulationEx1 {

    public static void main(String[] args) {
        Account a1;
            
        a1 = new Account();
            
        a1.credit(2000);
        a1.debit(1000);
            
        a1.showBalance();
            
        // balance is a private field in 
        // the class Account, hence 
        // directly referring to it through object
        // is invalid.

        // a1.balance = -2000; // invalid 

        // debit operation validates the given amount
        // and rejects it if invalid. In the below case
        // because amount is not greater than 0 it won't
        // modify the balance.

        a1.debit(-2000);
            
        a1.showBalance();
		
   }

}
```

By restricting the direct access to data and allowing only operations to operate on it we can also ensure the modifications are valid by performing any validation checks.

Technical Note -

Does this mean operations are always public ?

Doesn't matter, we need to understand that all are members and any member which need to be restricted from outside the class definition should be private and which should be given access might be public. 

E.g.

```java

class Demo {
    private void f() {}
    public void g() {}
}

class Main {
    public static void main(String[] args) {
      Demo obj = new Demo();
      d.f(); // invalid as f() is private 
      d.g(); // valid as it is public. 
    }
}

```

You can think of private member as something that is written for the internal purpose of the class and not for outsiders.


## Overloading -

A method or a function is said to be overloaded when there are multiple defintions available for that function with different signatures.
In the below example print method is said to be overloaded. i.e. there are three methods with name print. 

You may get a question, when you call print which method gets invoked ?

Although there are three methods, there is a change in the parameter. 
i.e. first one accepts integer argument, where as second method is written for double and third is written for String. 

Based on the type of value you pass the corresponding definition gets invoked.

e.g. 

``` java

    print(10) calls the print method which accepts integer.

    print(10.2) calls the print method which accepts double

    print("abc") calls the print method that accepts String. 

```

Example -

```java

class Sample {
	
    public void print(int a) {
	    System.out.println("int - " + a);
    }
	
    public void print(double a) {
	    System.out.println("double - " + a);
    }
	
    public void print(String a) {
	    System.out.println("string - " + a);
    }
	
}

public class OverloadingEx1 {
    public static void main(String[] args) {
        Sample obj = new Sample();
        obj.print(10); 
        obj.print(10.2);
        obj.print("abc");
    }
}
```

Technical Notes -

1) You are using System.out.println() for printing values, this itself is a best example for function overloading. The println method in the class PrintStream is overloaded for accepting different types, e.g. println(int), println(float) etc. It is a good idea, why? just think if you need to use printInt(..) printFloat(..) etc. how many names you need to remember? instead you are asked to remember one name i.e. println and use the same for printing different types of values, compiler determines which method to call based on the arguments passed to the function. The behavior is said to be polymorphic behavior (explained later) i.e. single element takes many forms. 

2) Note then you can not overload by simply changing the return type, you must change at least one parameter.

e.g. INVALID - because parameters are of same type for both the methods i.e. (int, int), so when you say add(10, 20) it will match with both the methods and create ambiguity.

```java

int add(int a, int b) { 
    return a + b;
}
   
double add(int a, int b) { 
    return a + b;
}

```
e.g. VALID - because there is at least one change in the parameter types, first one has (int, int) and second one has (int, double). If you call add(10, 20) it matches with first definition where as add(10, 20.0) will match with the second definition.

```java

int add(int a, int b) { 
    return a + b;
}
   
double add(int a, double b) { 
    return a + b;
}

```

## Inheritance - 

Allows us to extend an existing class inorder to reuse its functionality. Use "extends" keyword to inherit the properties of an existing class. A class that is used as the basis for inheritance is called a superclass or base class. A class that inherits from a superclass is called a subclass or derived classes.  

Example - 

In the following example ScientificCalc extends BasicCalc, here BasicCalc is referred as base class and ScientificCalc is referred as derived class or sub class. Through extension ScientificCalc inherits the properties of BasicCalc. So, operations present in BasicCalc are [add, sub] where as operations present in ScientificCalc are [add, sub, sin].

```java

class BasicCalc {
	
    public int add(int a, int b) {
	    return a + b;
    }
	
    public int sub(int a, int b) {
	    return a - b;
    }
}

// Inheritance
//                    is a 
class ScientificCalc extends BasicCalc {
	
    public double sin(int deg) {
	    double rad = deg * 3.14159 / 180;
	    return Math.sin(rad);
    }
}

public class InheritanceEx1 {

    public static void main(String[] args) {
		
        BasicCalc bcalc1 = new BasicCalc();
            
        // Through BasicCalc reference 
        // we can call add and sub methods.

        System.out.println( bcalc1.add(10, 20));
        System.out.println( bcalc1.sub(10, 20));
            
            
        ScientificCalc sCalc1 = new ScientificCalc();

        // Through ScientificCalc reference
        // we can call add, sub and sin methods.

        System.out.println( sCalc1.add(10, 20));
        System.out.println( sCalc1.sub(10, 20));
        System.out.println( sCalc1.sin(90) );

    }
}

```

Also to note that extends establishes an "is a" relation between classes, i.e. ScientificCalc is a BasicCalc. 

What does this mean? 

If some argument or LHS of the assignment is expecting BasicCalc object, we can replace it with ScientificCalc object as well. 

e.g.

```java

// bcalc2 is a reference variable(not object)
BasicCalc bcalc2;
bcalc2 = new ScientificCalc(); 

```

LHS is expecting an object of type BasicCalc. But the object supplied in RHS is of type ScientificCalc. This is also valid because it extends BasicCalc and hence there is an "is a" relation, i.e. ScientificCalc is a BasicCalc.

But there is a limitation, because bcalc2 is of type BasicCalc we can only refer to the members of BasicCalc. As compiler only knows type information but not the object, object on which operation is being performed is only known at the runtime. 

```java

int x = bcalc2.add(10, 20); // valid.
int y = bcalc2.sub(10, 20); // valid
double z = bcalc2.sin(90);  // Invalid.  

```

To resolve this we need to do an explicit type cast inorder to make a call. 

```java

double z = ((ScientificCalc)bcalc2).sin(90); 
                // valid but dangerous.

```

Here you are instructing the compiler to consider bcalc2 as ScientificCalc and then make a call.

General Example - Let us take some general example to help you understand this easily, if I say 

```java

Animal a = ........;

```

Here the expectation is that RHS will be an Animal. And through 'a' we can only refer to the properties of Animal. Can I say ?
```java

a.bark();

```

NO.  And this is the state of the compiler, compiler doesn't know what the object is? compiler only knows the type information and not the object information, hence it declares this as INVALID. If I say

```java 

((Dog)a).bark();

```

Is this valid ? 

May be !! because 'a' might point to any Animal, and it may be Dog as well, here you are instructing the compiler that "consider the a as Dog" and validate, then it say YES, it is VALID. As you suspected, if it is not a Dog then this type cast will result in ClassCastException hence you should be careful with this. 

## Overriding - 

Overriding is the process of redefining an existing method of the base class in the subclass. This is done in order to modify an existing definition with effect from the sub class. This is how we modify the existing behavior inherited from the base class.

Example -

In the following example class Base has two methods f() and g() and Derived extends Base, which means that it inherits the properties of Base, hence it too has two methods f() and g(). Having said that g() is redefined in Derived i.e. g() is said to be overridden in Derived. If you consider the Base object g() definition in Base is used where as if you consider the Derived object g() definition in Derived is used.

```java

class Base {
    public void f() {
	    System.out.println("f() in Base");
    }
    
    public void g() {
	    System.out.println("g() in Base");
    }
}

// Derived inherits the functionality from Base.

class Derived extends Base {
	
    // overrides the definition g()
    public void g() {
	    System.out.println("g() in Derived");
    }
	
    public void h() {
	    System.out.println("h() in Derived");
    }
}
 
public class Main {

    public static void main(String[] args) {
		
        Base b1 = new Base();
        b1.f(); // in Base
        b1.g(); // in Base
            
        System.out.println();
            
        Derived d1 = new Derived();
        d1.f(); // in Base
        d1.g(); // in Derived
        d1.h(); // in Derived
            
        // Here b2 is a reference variable of type Base
        // Object is of type Derived (RHS).

        Base b2 = new Derived();
        b2.f(); // in Base
        b2.g(); // in Derived because Object is of type Derived.
    }
}

```

Whether you can refer to the member or not is based on type, which method to call is based on Object, this is referred as Dynamic Binding or Runtime Polymorphism (Explained later).

In the above example third case is a peculiar case, b2 is expecting Base object, but it was given Derived object. Through b2 you can only call the members of the Base directly, but which method is invoked depends on object, since g() is overridden in Derived the method in Derived is invoked during the call b2.g().

General Example - 

Consider the statements, 

```java

Dog g = .....;
g.bark();

```

Is g.bark() a valid call over the Dog ? YES

Which Dog is barking ? depends on Object(the actual Dog instance pointed by g). 

This process is called dynamic binding i.e. the actual behavior is determined at runtime i.e. when the object is known. 

## Abstract classes - 

Abstract in object oriented programming indicate incomplete in definition or not to be considered as concrete. You can not create object for an abstract class, it is meant to be a base class always. In Java if you declare a class as abstract  using abstract keyword. The opposite of this is a concrete class which can be instantiated.

abstract method - is the one with out definition i.e. only declaration of the method is present. If a class has an abstract method then the corresponding class should be declared abstract. 

Subclasses which extend an abstract class must define all the abstract methods of that class to become concrete, otherwise they too remain abstract.

In the following example class Graphic is declared as abstract, it also has an abstract method draw(). As you can imagine Graphic doesn't know what to draw. The subclasses Line, Rectangle etc define the draw method and are concrete. 

Example - 

```java

abstract class Graphic {
	
    protected int x1, y1;
    protected int x2, y2;
	
    public void setStart(int x, int y) {
	    x1 = x;
	    y1 = y;
    }
	
    public void setEnd(int x, int y) {
	    x2 = x;
	    y2 = y;
    }
	
    // abstract method i.e. only declaration, no definition
    public abstract void draw();
	
}

class Line extends Graphic {
	
    @Override
    public void draw() {
        System.out.printf(
            " draw line from (%d, %d) to (%d, %d) %n", x1, y1, x2, y2 );
    }
}

class Rectangle extends Graphic {
	
    @Override
    public void draw() {
        System.out.printf(
            " draw rectangle from (%d, %d) to (%d, %d) %n", x1, y1, x2, y2 );
    }
}

public class AbstractEx1 {
	
    // This method is applicable for all Graphics
    
    static void drawUtil(int x1, int y1, int x2, int y2, Graphic g) {
        g.setStart(x1, y1);
	    g.setEnd(x2, y2);
	    g.draw();
    }

    public static void main(String[] args) {
		
        // INVALID: You can not instantiate Graphic as it is abstract.
        // drawUtil(10, 10, 20, 20, new Graphic());

        // VALID
        drawUtil(30, 30, 40, 40, new Line());

        // VALID
        drawUtil(50, 50, 60, 60, new Rectangle());
    }
}

```
You can see that the drawUtil method is expecting a Graphic object, and hence it is applicable for all Graphic sub classes. This way we can write the generalized code. 

Q: What if I don't even declare draw() in Graphic ?

Ans: You can not invoke it through Graphic reference, i.e.

Graphic g = .....;

in order to call 

g.draw();

draw should be a member in Graphic. 

## Polymorphism -

The behavior of an identifier is said to be polymorphic if it takes many forms, i.e. it has multiple definitions and the definition is picked based on the context. In the following example the behaviour of the print method is said to be polymorphic.

If you supply integer then print(int) is selected. If you supply double then print(double) is selected etc.

Example - 

```java

class Sample {
	
    public void print(int a) {
	    System.out.println("int value - " + a);
    }
	
    public void print(double a) {
	    System.out.println("double value - " + a);
    }

    public void print(String a) {
	    System.out.println("String value - " + a);
    }
}

class Main {
    
    public static void main(String[] args) {
        Sample s = new Sample();
        s.print(10);
        s.print(10.2);
        s.print("abc");
    }
}

```

### Static binding vs Dynamic binding -

Binding actually stands for linking, in other words linking the call with the corresponding definition. Suppose you are making a method call, compiler should actually link it to the actual definition. And this is what we call binding. Static binding is also called as compile time binding or early binding. In this case compiler itself determines which method should be invoked. Whereas in case of dynamic binding the actual definition could not determined till runtime. And hence it is also called as runtime binding or runtime polymorphism or late binding.

Example -

```java

class Base {
    public static void f() {
	    System.out.println("f() in Base");
    }

    public final void g() {
	    System.out.println("g() in Base");
    }

    public void h() {
	    System.out.println("h() in Base");
    }
}

class Derived extends Base {
	
    @Override
    public void h() {
	    System.out.println("h() in Derived");
    }
}

class Main {

    public static void main(String[] args) {
		
        // Since f() is static
        Base.f(); // f() in Base (1)

        Base b1 = new Base();
        b1.g(); // g() in Base (2)
        b1.h(); // h() in Base (3)

        Base b2 = new Derived();
        b2.g(); // g() in Base
        b2.h(); // h() in Derived
    }
}
```

(1) & (2)

In case of static method call, we can term this as static binding. Static methods can not be overridden and hence compiler can pick the exact method definition. 

Now in case of g() method call, g() is a final method and it can not be overridden. Hence it is also a candidate for static binding because compiler knows that this is the method in Base which should be linked with. 

In both the cases compiler an directly bind the call with the exact function. Hence this is called static binding or compile time binding.

(3)

When it comes to h() it is neither static not final. And hence the actual definition could not be determined unless we know the object. Hence Dynamic Binding is used here

i.e. the binding in other words linking to the actual definition is delayed till runtime until the object is actually known. In this case if the object is a Base object then the definition in Based is invoked. If the object is a Derived object then the definition in Derived is invoked.       

```java 

        // Since f() is static
        Base.f(); // f() in Base (1)

        Base b1 = new Base();
        b1.g(); // g() in Base (2)
        b1.h(); // h() in Base (3)

        Base b2 = new Derived();
        b2.g(); // g() in Base
        b2.h(); // h() in Derived

```       

## Summary 

Object-Oriented Programming (OOP) relies on four core principles: encapsulation, inheritance, abstraction, and polymorphism. These principles enable modular, reusable, and maintainable code by modeling real-world entities as objects.

1. **Encapsulation**: Encapsulation bundles data and methods within a class, restricting direct access to internal details using access modifiers like private or public. This protects object state and exposes only necessary interfaces.

2. **Inheritance**: Allows a new class (subclass) to inherit properties and behaviors from an existing class (superclass), promoting code reuse.

3. **Abstraction**: Abstraction hides complex implementation details behind simple interfaces, letting users interact with high-level methods without knowing internals. Abstract classes or interfaces enforce this.

4. **Polymorphism**: Polymorphism enables objects of different classes to be treated uniformly through a common interface, often via method overriding or overloading. This supports flexibility, like different shapes sharing a "draw" method.

By leveraging these OOP principles, developers can create software that is easier to understand, extend, and maintain over time.