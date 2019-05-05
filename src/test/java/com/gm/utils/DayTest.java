package com.gm.utils;

import com.gm.enums.Pattern;
import com.gm.utils.base.Day;
import com.gm.utils.base.Logger;
import org.junit.Test;

import java.util.Date;

/**
 * The type Calendar utils test.
 *
 * @author Jason
 */
public class DayTest {
    @Test
    public void convertTests() {
//        String currentTime = Day.getCurrentTime("");
//        String currentTime = Day.getCurrentTime("yyy");
//        String currentTime = Day.getCurrentTime(String.class);
//        String lastTime = Day.getLastTime(currentTime);
//        Long lastTime = Day.getTime(currentTime);
//        Logger.debug(currentTime);
//        Long lastTime = Day.getTime(new Date());
//        String date = Day.offsetDay(currentTime, 1);
//        String date = Day.offsetDay(currentTime, -1);
//        Calendar calendar = Day.getCurrentTime(Calendar.class);
//        String date = Day.offsetHour(currentTime, -1);
        Date currentTime = Day.getCurrentTime(Date.class);
        Date c = Day.switchT(currentTime, new Date());
        Logger.debug(c);
    }

    /**
     * com.gm.utils.Main.
     */
    @Test
    public void main(){
        String currentTime = Day.getCurrentTime(Pattern.DATE_DAY);
        String offsetDay = Day.offsetYear(currentTime, 1);
        Logger.debug(offsetDay);
//        Object after = Day.getCurrentTime(Integer.class);
//        String day = Day.offsetDay((String)after,-1);
//        System.out.println(after);
//        System.out.println(day.substring(0,10));
//        String date = Day.switchT(String.class, new Date());
//        Logger.info(date);
    }
}
