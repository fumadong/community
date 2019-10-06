package com.fu.community.test;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class StudentComparable implements Comparable<StudentComparable>{
    private int id;
    private int age;
    private String name;

    public StudentComparable(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    @Override
    public int compareTo(StudentComparable o) {

        if(this.age==o.age){
            return this.id-o.id;
        }else {
            return this.age - o.age;
        }
    }

    public static void main(String[] args) {
        List<StudentComparable> list = new ArrayList<>();
        list.add(new StudentComparable(1,25,"关羽"));
        list.add(new StudentComparable(2,21,"张飞"));
        list.add(new StudentComparable(7,21,"刘备"));
        list.add(new StudentComparable(4,21,"袁绍"));
        list.add(new StudentComparable(5,36,"赵云"));
        list.add(new StudentComparable(6,16,"曹操"));
        Collections.sort(list);
        for (StudentComparable studentComparable : list) {
            System.out.println(studentComparable);
        }
    }
}
