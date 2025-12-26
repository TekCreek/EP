package org.example;

import java.util.Map;
import java.util.TreeMap;

// Display the word along with its frequency.
public class TreeMapDemo1 {
    public static void main(String[] args) {
        String sentence = "this is sample sentence this is for map example";
        String [] words = sentence.split(" ");

        // Map<String, Integer> wordMap = new HashMap<String, Integer>();
        Map<String, Integer> wordMap = new TreeMap<String, Integer>();
        for (String word: words) {
            Integer value = wordMap.get(word);
            if (value == null) {
                value = 1;
            } else {
                value = value + 1;
            }
            wordMap.put(word, value);
        }

        System.out.println( wordMap );

    }
}
/*
  wordMap -
    this - 2
    is - 2
    sample - 1
    sentence - 1
    for - 1
    map - 1
    example - 1
 */