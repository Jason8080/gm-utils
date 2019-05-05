package com.gm.utils;

import com.gm.help.base.Quick;
import com.gm.utils.base.Prop;
import org.junit.Test;

import java.io.File;

public class IoTests {


    public static void main(String[] args) {
        Prop.load();
        Integer i = Prop.getInt("gm.generator.id.init");
        System.out.println(i);
        final File file = (File) Prop.put("gm.generator.id.init", 6);
        Prop.store(file);
        i = Prop.getInt("gm.generator.id.init");
        System.out.println(i);
        Prop.put("gm.generator.id.init", 9);
        Prop.store(file);
        i = Prop.getInt("gm.generator.id.init");
        System.out.println(i);
    }

    @Test
    public void testQuick2() {
        Quick.loop(new File("C:\\Users\\Administrator\\Desktop\\hello"), (f1) -> {
            if (f1 instanceof File) {
                File f = (File) f1;
                System.out.println(f.getName());
                if (f.isFile() && f.exists()) {
                } else {
                    return f.listFiles();
                }
            } else if (f1 instanceof File[]) {
                for (File f : (File[]) f1) {
                    return f;
                }
            }
            return null;
        });
    }
}
