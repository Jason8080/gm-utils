package com.gm.utils;

import com.gm.utils.base.Day;
import com.gm.utils.base.Logger;
import com.gm.utils.third.Aurora;
import com.gm.utils.third.Mail;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Aurora test.
 *
 * @author Jason
 */
public class AuroraTest {
    /**
     * com.gm.utils.Main.
     */
    @Test
    public void testNew() {
        Aurora a = new Aurora();
        Map<String, String> map = new HashMap();
        map.put(Aurora.SOUND,"orderCancel.caf");
//        map.put(AuroraUtils.SOUND,"newOrder.caf");
//        map.put(AuroraUtils.SOUND,"orderComplaint.caf");
//        map.put(AuroraUtils.SOUND,"orderComplaint.caf");
        Logger.info(map);
        a.send("您有新载消息,请注意查收!", Day.getCurrentTime(String.class), map, "4319236");
    }
    /**
     * com.gm.utils.Main.
     */
    @Test
    public void testOne() {
        Map<String, String> map = new HashMap();
        map.put(Aurora.SOUND,"orderCancel.caf");
//        map.put(AuroraUtils.SOUND,"newOrder.caf");
//        map.put(AuroraUtils.SOUND,"orderComplaint.caf");
//        map.put(AuroraUtils.SOUND,"orderComplaint.caf");
        Logger.info(map);
        Aurora.send("您有新载消息,请注意查收!", Day.getCurrentTime(String.class), map, "4319236");
    }

    /**
     * The type Thread main.
     */
    public class ThreadMain extends Thread {
        @Override
        public void run() {

            System.out.println(getName());
        }
    }



    @Test
    public void sendTest(){
//        long start = System.currentTimeMillis();
//        Mail.send("6月财务报表".concat(Day.getCurrentTime(String.class))
//                ,"日日夜夜的伤情因为就hi黑发hi还是我if黑如何I如果ieI热个人highie荣华富贵和胡<img src='cid:331.jpg'><img src='cid:332.jpg'>".concat(Day.getCurrentTime(String.class))
//                ,new String[]{"C:\\Users\\Administrator\\Desktop\\331.jpg","C:\\Users\\Administrator\\Desktop\\332.jpg"}
//                ,new String[]{"C:\\Users\\Administrator\\Desktop\\接口定义文档-Order.html","C:\\Users\\Administrator\\Desktop\\接口定义文档-Location.html"}
//                ,new String[]{"569284276@qq.com"}
//                ,new String[]{"1253532233@qq.com"}
//                ,new String[]{"xiaoku13141@163.com"}
//        );
//        Logger.debug("耗时: ".concat(String.valueOf(System.currentTimeMillis() - start)));
        long start = System.currentTimeMillis();
        Mail.send("汉王科技报表", "666", "569284276@qq.com");
        Logger.debug("耗时: ".concat(String.valueOf(System.currentTimeMillis() - start)));
    }

//
//    @Test
//    public void testMail(){
//        long start = System.currentTimeMillis();
//        SendAnnexMail.send("smtp.163.com","gmleemail@163.com","gmleemail@163.com","QQ123456",
//                "569284276@qq.com","666","777","");
//        Logger.debug("耗时: ".concat(String.valueOf(System.currentTimeMillis() - start)));
//    }

}
