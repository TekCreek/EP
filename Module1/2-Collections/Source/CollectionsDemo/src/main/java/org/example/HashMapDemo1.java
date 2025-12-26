package org.example;

import java.util.HashMap;

public class HashMapDemo1 {
    public static void main(String[] args) {
        HashMap<String, Integer> fruitMap = new HashMap<>();

        // store -> put(key, value)
        fruitMap.put("apple", 20);
        fruitMap.put("mango", 30);
        fruitMap.put("banana", 20);

        // value = get(key)
        Integer applePrice = fruitMap.get("apple");
        System.out.println( applePrice );

        System.out.println("content of the fruitMap");
        for (String key : fruitMap.keySet()) {
            Integer price = fruitMap.get(key);
            System.out.println( key + " -- " + price );
        }

    }
}
