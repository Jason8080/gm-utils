package com.gm.utils;

import com.gm.utils.third.Mysql;

public class MysqlTests {
    public static void main(String[] args) {
        System.out.println(Mysql.exec("jdbc:mysql://192.168.1.22:3306/ecsp",
                "ecsp", "ecsp",
                "insert into ids (id) values (1),(2)"
                )
        );
    }
}
