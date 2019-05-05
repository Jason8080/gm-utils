package com.gm.utils;

import com.gm.utils.base.Collection;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * The type Range test.
 *
 * @author Jason
 */
public class RangeTest {


    /**
     * com.gm.utils.Main.
     */
    @Test
    public void main() {
        String[] all = {"6", "5", "3", "8"};
        System.out.println(Collection.less("5","4", (String[]) null));
        System.out.println(Collection.less("5","4", (List<String>) null));
        System.out.println(Collection.less("5","4", all));
        System.out.println(Collection.less("5","5", all));
        System.out.println(Collection.less("5","6", all));
        System.out.println(Collection.less("5","3", all));
        System.out.println(Collection.less("5","8", all));
        System.out.println(Collection.getLess("6", Arrays.asList(all)));
        System.out.println(Collection.getLess("5", Arrays.asList(all)));
        System.out.println(Collection.getLess("4", Arrays.asList(all)));
        System.out.println(Collection.getLess("3", Arrays.asList(all)));
        System.out.println(Collection.getLess("8", Arrays.asList(all)));
    }
}
