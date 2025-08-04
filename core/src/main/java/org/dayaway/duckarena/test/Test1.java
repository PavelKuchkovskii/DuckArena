package org.dayaway.duckarena.test;

import java.util.ArrayList;
import java.util.List;

public class Test1 {
    public static void main(String[] args) {

        List<String> l1 = new ArrayList<>();

        l1.add("l1-1");
        l1.add("l1-2");
        l1.add("l1-3");
        l1.add("l1-4");
        l1.add("l1-5");
        l1.add("l1-6");
        l1.add("l1-7");
        l1.add("l1-8");
        l1.add("l1-9");

        List<String> l2 = new ArrayList<>();

        l2.add("l2-1");
        l2.add("l2-2");
        l2.add("l2-3");
        l2.add("l2-4");
        l2.add("l2-5");
        l2.add("l2-6");
        l2.add("l2-7");
        l2.add("l2-8");
        l2.add("l2-9");


        List<String> l3 = new ArrayList<>();


        l3.addAll(l1);
        l3.addAll(l2);

        System.out.println(l3);

    }
}
