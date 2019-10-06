package com.fu.community.test;

import lombok.Data;
@Data
public class StudentComparator  {
    private int id;
    private int age;
    private String name;

    public StudentComparator(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }


}
