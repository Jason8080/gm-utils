package com.gm.utils;

import com.gm.utils.base.Collection;
import com.gm.utils.base.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CollectionTest {

    @Test
    public void testMapSort() {
        Map<String, String> map = new HashMap();
        map.put("d","b");
        map.put("你","b");
        map.put("a","a");
        map.put("我","d");
        map.put("b","c");
        map.put("他","9");
        map.put("c","888");
//        map = Collection.sortK(map);
//        Logger.info(map);
        map = Collection.sortV(map);
        Logger.info(map);
    }
}
