package com.gm.utils;

import com.gm.utils.base.Collection;
import com.gm.utils.ext.Gps;

public class GpsTests {
    public static void main(String[] args) {
        Double distance = Gps.getDistance(113.43456,23.135286, 113.43451,23.134403);
        System.out.println(distance);
        Double[] around = Gps.getAround(113.43456, 23.135286, 100);
        System.out.println(Collection.toArrString(around,","));
    }
}
