package com.gm.utils;

import com.gm.strong.Rules;
import com.gm.utils.base.Assert;
import com.gm.utils.ext.Math;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RulesTests {

    @Test
    public void testParse() {
        String expression = "[price]*2+1-2/[qty]";
        HashMap map = new HashMap();
        map.put("price","2.22");
        map.put("qty",2);
        String parse = Rules.parse(map, expression);
        System.out.println(Math.execute(parse,Math.getClass(expression)));
    }
    @Test
    public void testClass() {
        String expression = "[price]*2+1-2/[qty]";
        HashMap map = new HashMap();
        map.put("price","2.22");
        map.put("qty",2);
        Rules rules = new Rules(map,expression);
        String parse = rules.parse();
        Assert.isContains("null",parse,"666");
        System.out.println(Math.execute(parse,Math.getClass(expression)));
    }
    @Test
    public void testRules() {
        String expression1 = "[price]*2+1-2/[qty]";
        String expression7 = "[price]*2+1-4/[qty]";
        Rules.Rule rule1 = new Rules.Rule(expression1);
        rule1.setSort(8);
        Rules.Rule rule7 = new Rules.Rule(expression7);
        rule7.setSort(7);
        List array = new ArrayList();
        array.add(rule1);
        array.add(rule7);
        HashMap map = new HashMap();
        map.put("price","2.22");
        map.put("qty",2);
        map.put("ruleId",7);
        Rules rules = new Rules(map,array);
        String parse = rules.parse();
        Assert.isContains("null",parse,"666");
        System.out.println(Math.execute(parse,Math.getClass(expression1)));
    }
}
