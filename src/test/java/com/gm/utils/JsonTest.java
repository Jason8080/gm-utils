package com.gm.utils;

import com.gm.utils.base.Logger;
import com.gm.utils.ext.Json;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The type Json utils test.
 *
 * @author Jason
 */
public class JsonTest {


    @Test
    public void testFormat(){
        System.out.println(Json.format("{\"content\":\"你好, hello\",\"msg\":2}"));
    }

    /**
     * com.gm.utils.Main.
     */
    @Test
    public void main(){

        boolean assignableFrom = Collection.class.isAssignableFrom(List.class);
        Logger.info(assignableFrom);


        String toJson = Json.toJson("");
        System.out.println(toJson);
        toJson = Json.toJson(null);
        System.out.println(toJson);
        toJson = Json.toJson(6666L);
        System.out.printf(toJson);
    }

    @Test
    public void subClass(){

        boolean assignableFrom = Collection.class.isAssignableFrom(Set.class);
        Logger.info(assignableFrom);
    }

    @Test
    public void convertClass(){
        String s = Json.toObject("123", String.class);
        Logger.info(s);
    }
}
