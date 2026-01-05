package org.example;

class Box<T> {
    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}

public class GenericsDemo1 {
    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>();
        intBox.setItem(10);

        Box<String> strBox = new Box<>();
        strBox.setItem("Hello");

        System.out.println(intBox.getItem());
        System.out.println(strBox.getItem());
    }
}
