package com.gm.utils;

import com.gm.help.base.Quick;
import com.gm.utils.base.ExceptionUtils;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuickTests {

    @Test
    public void runTest() {
        Person p = new Person();
        p.setAge(1);
        System.out.println(p);
        Quick.run(x -> {
            testJdk(p);
        });
        System.out.println(p);
    }

    public static void testJdk(Person p) {
        p.setAge(2);
        p = new Person();
        p.setAge(3);
    }

    @Test
    public void mapsTest() {
        Map<String, Object> map = new HashMap();
        map.put("name", "李四");
        map.put("age", 12);
        map.put("stature", 1.80);
        Quick.echo(map, (name, age) -> {
            System.out.println(name + age);
        });
        System.out.println(map.size());
    }

    @Test
    public void each() {
        List<Person> list = new ArrayList();
        list.add(new Person(6));
        list.add(new Person(8));
        list.add(new Person(4));
        Quick.echo(list, x -> {
            System.out.println(x.age);
        });
    }

    @Test
    public void execTest() {
        final Integer[] i = {1};
        Integer exec = Quick.exec(x -> {
            i[0] += 2;
            return i[0];
        });
        System.out.println(exec);
    }


    @Test
    public void startupTest() {
        String[] str = new String[]{"A"};
    }


    @Test
    public void loopTest() {
        Person person = new Person();
        Quick.loop(person, (Person x) -> {
            x.setAge(++x.age);
            if (x.age > 9) {
                return ExceptionUtils.cast();
            }
            return x;
        });
        System.out.println(person);
    }

    @Data
    public static class Person {

        private Integer age = 0;

        public Person() {
        }

        public Person(Integer age) {
            this.age = age;
        }
    }
}
