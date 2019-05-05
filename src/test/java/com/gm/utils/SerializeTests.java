package com.gm.utils;

import com.gm.utils.base.Generator;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SerializeTests {

    @Test
    public void write() throws Exception {
        FileOutputStream out = new FileOutputStream(Generator.class.getSimpleName());
        out.write(100);
        out.flush();
        out.close();
    }


    @Test
    public void read() throws Exception {
        FileInputStream in = new FileInputStream(Generator.class.getSimpleName());
        System.out.println(in.read());
        in.close();
    }
}
