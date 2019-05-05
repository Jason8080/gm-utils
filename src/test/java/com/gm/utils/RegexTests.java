package com.gm.utils;

import com.gm.enums.Regexp;
import com.gm.model.response.HttpResult;
import com.gm.utils.base.Logger;
import com.gm.utils.ext.Regex;
import com.gm.utils.third.Http;
import org.junit.Test;

import java.util.List;

public class RegexTests {

    @Test
    public void url() {
        Logger.info(Regex.find("/foo/app/", "/foo/\\w+"));
    }

    @Test
    public void mobile() {
        Logger.info(Regex.find("18,67288113", Regexp.FIND_MOBILE.getCode()));
    }

    @Test
    public void htmlTitle() {
        HttpResult result = Http.doGet("http://www.chinawutong.com/");
        List<String> title = Regex.find(new String(result.getResult()), Regexp.FIND_HTML_TITLE.getCode());
        List<String> mobile = Regex.find(new String(result.getResult()), Regexp.FIND_MOBILE.getCode());
        System.out.println(title);
        System.out.println(mobile);
    }

    @Test
    public void htmlCharset() {
        System.out.println(Regex.getCharset("<head><meta http-equiv=\"Cache-Control\" " +
                "content=\"no-transform\"><meta http-equiv=\"Cache-Control\" " +
                "content=\"no-siteapp\">\n" +
                "<meta charset=\"GB2312\">\n" +
                "<title>赛乐赛一疗程多少钱 吃多久 能瘦几斤_楚雄减肥瘦身_楚雄列举网</title>\n" +
                "<meta name=\"keywords\" content=\"赛乐赛一疗程多少钱 吃多久 能瘦几斤,楚雄美容美体\">\n" +
                "<meta name=\"description\" content=\"抱怨是一种致命的消极心态,一旦自我的抱怨成为恶习,那么人生就会暗无天日," +
                "不仅仅自我好心境全无,而且别人跟着也倒霉赛乐赛一个疗程是几盒可以吃多久赛乐赛,是2017年目前全球认可的中国药,主要成分为美国，赛乐赛一疗程多少钱 吃多久 " +
                "能瘦几斤，楚雄列举网美容美体信息。\">\n" +
                "<meta name=\"mobile-agent\" content=\"format=html5; url=http://m.cx.lieju" +
                ".com/meirongmeiti/19817281.htm\">\n" +
                "<meta name=\"mobile-agent\" content=\"format=xhtml; url=http://m.cx.lieju" +
                ".com/meirongmeiti/19817281.htm\">\n" +
                "<meta name=\"mobile-agent\" content=\"format=wml; url=http://m.cx.lieju" +
                ".com/meirongmeiti/19817281.htm\">\n" +
                "<script type=\"text/javascript\" async=\"\" src=\"http://cpro.baidustatic" +
                ".com/cpro/ui/pr.js\"></script><script src=\"https://hm.baidu.com/hm" +
                ".js?5cd87e13c10c75712540335c0f6fd6a5\"></script><script src=\"http://push" +
                ".zhanzhang.baidu.com/push.js\"></script><script type=\"text/javascript\" " +
                "async=\"\" src=\"http://un1.lieju.com/rlptqpn.js\"></script><script> /* " +
                "预防吉林铁通篡改百度js删除cookie */ (function() {  var _setInterval = setInterval;  try {   " +
                "setInterval = function(f) {    var src = f.toString();    if (!/_hao_pg/.test" +
                "(src)) {     return _setInterval.apply(window, arguments);    }   };  } catch " +
                "(e) {   /* slient */  } })(); </script>\n" +
                "<link rel=\"stylesheet\" href=\"http://www.lieju.com/images/bencandy" +
                ".css?v=1801288\" type=\"text/css\">\n" +
                "<link rel=\"shortcut icon\" href=\"http://www.lieju.com/favicon.ico\" " +
                "type=\"image/x-icon\">\n" +
                "<script type=\"text/javascript\" src=\"http://apps.bdimg" +
                ".com/libs/jquery/2.0.0/jquery.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"http://www.lieju.com/images/jquery" +
                ".lazyload.js\"></script>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "$(function() {\n" +
                "$(\"img.lazy\").lazyload({\n" +
                "placeholder : \"http://www.lieju.com/images/grey.gif\",\n" +
                "effect : \"fadeIn\"\n" +
                "});\n" +
                "});\n" +
                "</script>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "$(document).ready(function(){\n" +
                "  $(\"#hide\").click(function(){\n" +
                "  $(\"#more_quyu\").hide();\n" +
                "  $(\"#hide\").hide();\n" +
                "  $(\"#show\").show();\n" +
                "  });\n" +
                "  $(\"#show\").click(function(){\n" +
                "  $(\"#more_quyu\").show();\n" +
                "  $(\"#show\").hide();\n" +
                "  $(\"#hide\").show();\n" +
                "  });\n" +
                "\n" +
                "  $(\"#btn-slide\").click(function(){ \n" +
                "  $(\"#panel\").slideToggle(\"slow\"); \n" +
                "  $(\"#slide\").slideToggle(\"hide\"); \n" +
                "  });\n" +
                "});\n" +
                "</script>\n" +
                "\n" +
                "    <style type=\"text/css\">\n" +
                "        .ad{ width:150px; height:170px; background:#fafafa; position:fixed; " +
                "right:5px; bottom:5px;border:1px solid #eeeeee;text-align:center;padding:10px;" +
                "color:#ff6600;}\n" +
                "    </style>\n" +
                "<script src=\"http://bdimg.share.baidu.com/static/api/js/share" +
                ".js?cdnversion=429627\"></script><link rel=\"stylesheet\" href=\"http://bdimg" +
                ".share.baidu.com/static/api/css/share_style0_16.css?v=8105b07e.css\"><link " +
                "rel=\"stylesheet\" href=\"http://bdimg.share.baidu" +
                ".com/static/api/css/slide_share.css?v=855af98e.css\"><link rel=\"stylesheet\" " +
                "href=\"http://bdimg.share.baidu.com/static/api/css/share_popup.css?v=ecc6050c" +
                ".css\"><link rel=\"stylesheet\" href=\"http://bdimg.share.baidu" +
                ".com/static/api/css/select_share.css?v=cab3cb22.css\"></head>"));
//        String x = "http://www.go007.com/guangzhou/nvshizhenghun/4ffe876186ebcf3b.htm";
//        System.out.println(x.substring(0,x.lastIndexOf("/")));
    }
}
