package com.gm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gm.enums.Regexp;
import com.gm.model.request.BasePageReq;
import com.gm.model.request.BaseReq;
import com.gm.model.response.HttpResult;
import com.gm.model.response.JsonResult;
import com.gm.utils.base.*;
import com.gm.utils.ext.Json;
import com.gm.utils.ext.Math;
import com.gm.utils.ext.Regex;
import com.gm.utils.ext.Time;
import com.gm.utils.third.Http;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Utils test.
 *
 * @author Jason
 */
public class UtilsTest {
    @Test
    public void httpAsync() throws InterruptedException {
        Http.asyncGet("http://192.168.1.223:8081/demo/one?msg=666", new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                HttpResult res = Http.getHttpResult(httpResponse);
            }

            @Override
            public void failed(Exception e) {
                Logger.debug("fail!!!");
            }

            @Override
            public void cancelled() {
                Logger.debug("cancel!!!");
            }
        });
        Logger.info("我就是传说中的async~~~~");
        Thread.sleep(3000L);
    }


    @Test
    public void httpMultimediaFile() throws IOException {
        File file = new File("5.js");
        Logger.debug(file.isFile());
    }

    @Test
    public void httpMultimedia() throws IOException {
        HttpPost req = new HttpPost("http://192.168.1.223:8080/demo/four");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("pageNo", "777");
        req.setEntity(builder.build());
        CloseableHttpClient client = HttpClients.custom().build();
        CloseableHttpResponse res = client.execute(req);
        Logger.debug(EntityUtils.toString(res.getEntity()));
    }

    @Test
    public void httpMap(){
        Map<String, String[]> map = new HashMap();
        map.put("a", new String[]{"b","c"});
        map.put("d", new String[]{"e","f"});
        Logger.debug(map.get("a"));
    }

    @Test
    public void httpUtils() throws IOException {
//        Logger.info(Http.doPost("http://192.168.1.23:8080/a/api/permission/getUserPermissionsTree"));
        Map<String,String> params = new HashMap();
        params.put("timestamp",System.currentTimeMillis()+"");
        params.put("sign",System.currentTimeMillis()+"");
        params.put("sysCode",System.currentTimeMillis()+"");
        params.put("username",System.currentTimeMillis()+"");
        Map<String,String> header = new HashMap();
        HttpResult content = Http.doGet("http://gu.qq.com/sh601989/gp", header, null);
        Logger.info(content.getResult());
    }

    @Test
    public void generatorUtils(){
        Logger.info(Generator.uuid());
//        Logger.info(GeneratorUtils.idGenerator());
//        Logger.info(GeneratorUtils.uuidGenerator());
    }

    @Test
    public void testMap(){
        Map<Integer, Object> map = new HashMap<>();
        map.put(4,new BaseReq());
        map.put(1,new BaseReq());
        map.put(3,new BaseReq());
        map.put(2,new BaseReq());
        TreeMap<Integer, Object> tree = new TreeMap<>();
        tree.putAll(map);
        Logger.info(tree);
    }

    @Test
    public void testClass(){
        List<Class<BaseReq>> classes = ClassUtils.getSubClasses(BaseReq.class);
        Logger.info(classes);
    }


    @Test
    public void testBytes(){
        byte[] bytes = Convert.toBytes(JsonResult.SUCCESS_);
        System.out.println(bytes);
        Object obj = Convert.toObj(bytes);
        System.out.println(obj);
    }

    /**
     * Test reg ex p.
     */
    @Test
    public static void main(String[] args) {
        String str = "[INFO][2018-04-23 10:29:08 911][http-nio-6900-exec-8]";
        System.out.println(Regex.findDate(str));

    }
    /**
     * Test reg ex p.
     */
    @Test
    public void testFindDate() {
        String s = "我的手机号是18837112195，曾经用过13767663237，还用过18812345678, 家里座机是020-43215491";
        List<String> x = Regex.find(s, Regexp.FIND_MOBILE.getCode());
        System.out.println(x);

    }

    /**
     * Test json.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void testJson() throws JsonProcessingException {
        List<Map> list = new ArrayList();
        Map map = new HashMap();
        map.put("666", JsonResult.SUCCESS_);
        map.put("777", JsonResult.SERVERS_EXCEPTION_);
        list.add(map);
        String json = Json.toJson(list);
        System.out.println(json);
        List<Map<String, JsonResult>> os = Json.toObject(json, List.class, Map.class, JsonResult.class);
        System.out.println(os);
    }

    /**
     * Test calendar.
     *
     * @throws ParseException the parse exception
     */
    @Test
    public void testCalendar() throws ParseException {
        String last = Day.getLastTime("yyyy-MM-dd HH:mm:ss");
        Date date = Day.getLastTime(new Date());
        System.out.println(last);
        System.out.println(date);
//        Date date = getTomorrow(new Date());
//        System.out.println(date);
//        String tomorrow = getTomorrow("2017-07-07");
//        System.out.println(tomorrow);
//        String currentTime = getCurrentTime(String.class);
//        System.out.println(currentTime);
//        Date day = getYesterday(date);
//        System.out.println(day);
//        String afterDay = getYesterday("2017-07-07 17:33:44");
//        System.out.println(afterDay);
//        Object o = getCurrentTime(Object.class);
//        System.out.println(o.toString());
//        String after = getAfter("2017-07-31 17:33:44", 60);
//        System.out.println(after);
//        String before = getBefore("2017-07-31 17:33:44", 60);
//        System.out.println(before);
    }


    /**
     * Test time range.
     */
    @Test
    public void testTimeRange() {
        System.out.println(Time.insofar("2017-10-12 12:02:23~2017-10-13 10:02:33"));
        System.out.println(Time.insofar("07:10~22:00"));
        System.out.println(Time.insofar("~22:00"));
    }

    /**
     * Test math.
     *
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException     the no such method exception
     * @throws InstantiationException    the instantiation exception
     * @throws IllegalAccessException    the illegal access exception
     */
    @Test
    public void testMath() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println(Math.execute("6-3*2+*3/3", BigDecimal.class));
//        System.out.println("!1!=2&&!2==2||!1>2" + ":" + Math.execute("!1!=2&&!2==2||!1>2", Boolean.class));
//        System.out.println("1!=2&&!2==2" + ":" + Math.execute("1!=2&&!2==2", Boolean.class));
//        System.out.println("!1<2&&2!=2" + ":" + Math.execute("!1<2&&2!=2", Boolean.class));
//        System.out.println("!1<2" + ":" + Math.execute("!1<2", Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2+3>1&&1<2-1",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2+3>1&&1<2-1+6",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>1&&1<2",Boolean.class));
//        System.out.println("2>2&&1<2"+":"+execute("2>2&&1<2",Boolean.class));
//        System.out.println("2>1&&1>2"+":"+execute("2>1&&1>2",Boolean.class));
//        System.out.println("2>=1&&1<2"+":"+execute("2>=1&&1<2",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>2&&1<=2",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>=1&&1==2",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>=1||1==2",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>=1&&2==3||1==1",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>=1&&2==3||1==2",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>=1||2==3||1==2",Boolean.class));
//        System.out.println("2>1&&1<2"+":"+execute("2>=3||2!=3&&1==1",Boolean.class));
//        System.out.println("12+13<25+1>6+25"+":"+execute("12+13<25+1>6+25",Boolean.class));
//        System.out.println("12+13<25"+":"+execute("12+13<25",Boolean.class));
//        System.out.println("12+13<=25"+":"+execute("12+13<=25",Boolean.class));
//        System.out.println("12>13>14>15"+":"+execute("12> 13> 1 4>15",Boolean.class));
//        System.out.println("12>13>14>15"+":"+execute("12>13>14>15",Boolean.class));
//        System.out.println("12<13<13<15"+":"+execute("12<13<13<15",Boolean.class));
//        System.out.println("12>13>14"+":"+execute("12>13>14",Boolean.class));
//        System.out.println("12>13<14"+":"+execute("12>13<14",Boolean.class));
//        System.out.println("12>13==14"+":"+execute("12>13==14",Boolean.class));
//        System.out.println("12<13<14"+":"+execute("12<13<14",Boolean.class));
//        System.out.println("12<13<=14"+":"+execute("12<13<=14",Boolean.class));
//        System.out.println("12<=13>14"+":"+execute("12<=13>14",Boolean.class));
//        System.out.println("12<=13>=14"+":"+execute("12<=13>=14",Boolean.class));
//        System.out.println("12<13==14"+":"+execute("12<13==14",Boolean.class));
//        System.out.println("12==13==14"+":"+execute("12==13==14",Boolean.class));
//        System.out.println("12<=13<=14"+":"+execute("12<=13<=14",Boolean.class));
//        System.out.println("12>=13<=14"+":"+execute("12>=13<=14",Boolean.class));
//        System.out.println("12>13"+":"+execute("12>13",Boolean.class));
//        System.out.println("13>12"+":"+execute("13>12",Boolean.class));
//        System.out.println("12<13"+":"+execute("12<13",Boolean.class));
//        System.out.println("13<12"+":"+execute("13<12",Boolean.class));
//        System.out.println("12==12"+":"+execute("12==12",Boolean.class));
//        System.out.println("12==13"+":"+execute("12==13",Boolean.class));
//        System.out.println("13==12"+":"+execute("13==12",Boolean.class));
//        System.out.println("12>=12"+":"+execute("12>=12",Boolean.class));
//        System.out.println("12>=13"+":"+execute("12>=13",Boolean.class));
//        System.out.println("13>=12"+":"+execute("13>=12",Boolean.class));
//        System.out.println("12<=12"+":"+execute("12<=12",Boolean.class));
//        System.out.println("12<=13"+":"+execute("12<=13",Boolean.class));
//        System.out.println("13<=12"+":"+execute("13<=12",Boolean.class));
//        System.out.println("12!=12"+":"+execute("12!=12",Boolean.class));
//        System.out.println("12!=13"+":"+execute("12!=13",Boolean.class));
//        System.out.println("13!=12"+":"+execute("13!=12",Boolean.class));
//        System.out.println(execute("1+2989866858568686875757*(3+2)*1-5",BigInteger.class));
    }

    /**
     * Test assert.
     */
    @Test
    public void testAssert() {
        Assert.isEqual("123456", "123456", "呵呵~");
    }

    /**
     * Test assert.
     */
    @Test
    public void testReflection() {
        System.out.println(Reflection.getOwnFieldsInfo(new BasePageReq()));
    }
}
