package com.fu.community.test;

import java.util.Comparator;

public class MyComparator implements Comparator<StudentComparator> {
    @Override
    public int compare(StudentComparator o1, StudentComparator o2) {
      return   o1.getAge()-o2.getAge();
    }


}
