package org.example;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * Custom classes with Collections -
 *      - If working with hash structures we need to implement hashCode and
 *        equals methods.
 *      - If working with tree structures we need to implement Comparable interface
 *        or we need to provide Comparator.
 */
public class TreeSetDemo4 {
    public static void main(String[] args) {
        // TreeSet<Student> students = new TreeSet<>(new StudentNumberComparator());
        TreeSet<Student> students = new TreeSet<>(new ScoreComparator());
        // TreeSet<Student> students = new TreeSet<>();

        students.add(new Student(2, "b", 90));
        students.add(new Student(1, "a", 92));
        students.add(new Student(3, "c", 91));

        System.out.println( students );
    }
}

// comparator.compare(s1, s2)
class StudentNumberComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        return o1.getSno() - o2.getSno();
    }
}

class ScoreComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        if (o1.getScore() > o2.getScore() ) {
            return 1;
        } else if (o1.getScore() < o2.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }
}

class Student implements Comparable<Student> {
    private int sno;
    private String name;
    private double score;

    public Student(int sno, String name, double score) {
        this.sno = sno;
        this.name = name;
        this.score = score;
    }

    public int getSno() {
        return sno;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sno=" + sno +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    // s1.compareTo(s2)
    @Override
    public int compareTo(Student o) {
        return this.name.compareTo(o.name);
    }
}