package com.fu.community.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestComparator {
    public static void main(String[] args) {
        List<StudentComparator> list = new ArrayList<>();
        list.add(new StudentComparator(1,25,"关羽"));
        list.add(new StudentComparator(2,21,"张飞"));
        list.add(new StudentComparator(7,21,"刘备"));
        list.add(new StudentComparator(4,21,"袁绍"));
        list.add(new StudentComparator(5,36,"赵云"));
        list.add(new StudentComparator(6,16,"曹操"));
        Collections.sort(list,new MyComparator());

    }
}
