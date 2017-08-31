package com.example.contentproviderdemo;

/**
 * Description:
 * Author: qiubing
 * Date: 2017-08-29 20:33
 */
public class Student {
    private int age;
    private String name;
    public Student(String name,int age){
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name = " + name + " ,age = " + age;
    }
}
