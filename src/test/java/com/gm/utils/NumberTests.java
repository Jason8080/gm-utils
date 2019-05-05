package com.gm.utils;

import com.gm.utils.base.Logger;
import com.gm.utils.base.Num;

public class NumberTests {
    public static void main(String[] args) {
        int insofar = Num.insofar(6.5, "6.49~");
        Logger.debug(insofar);
    }
}
