package com.gm.utils.ext;

import com.gm.security.base.Md5;
import com.gm.security.Security;
import com.gm.strong.Str;
import com.gm.utils.base.Collection;
import com.gm.utils.base.Convert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 签名工具类
 *
 * @author Jason
 */
public class Sign implements Security {

    /**
     * The constant parameters.
     */
    public static String[] parameters = new String[]{SIGN_FIELD_TIMESTAMP,SIGN_FIELD_USERNAME,SIGN_FIELD_SYS_CODE};


    /**
     * 签名.
     *
     * @param <T>  the type parameter
     * @param map  参与签名的参数
     * @param salt 签名密钥/md5盐
     * @return 签名 string
     */
    public static <T> String sign(Map<String,T> map, Object... salt){
        map = Collection.sortK(map);
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, T>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, T> next = it.next();
            T os = next.getValue();
            if(null==os){

            }else if(os.getClass().isArray()){
                for (Object o : (T[]) os){
                    sb.append(String.valueOf(o));
                }
            }else if(os instanceof java.util.Collection){
                for (Object o : (java.util.Collection) os){
                    sb.append(String.valueOf(o));
                }
            }else {
                sb.append(os);
            }
        }
        return Md5.encrypt(sb.append(Json.toJson(salt)).toString());
    }

    /**
     * 签名.
     *
     * @param req  参与签名的参数
     * @param salt 签名密钥/md5盐
     * @return 签名 string
     */
    public static String sign(HttpServletRequest req, Object... salt){
        Map<String,Object> map = new HashMap(0);
        Enumeration<String> names = req.getHeaderNames();
        while (names.hasMoreElements()){
            String next = names.nextElement();
            if(new Str(parameters).part(next)){
                map.put(next,req.getHeader(next));
            }
        }
        return sign(map,salt);
    }

    /**
     * 验证签名
     *
     * @param req  the req
     * @param salt the salt
     * @return the boolean
     */
    public static boolean check(HttpServletRequest req, Object... salt){
        String sign = sign(req, salt);
        String param = Convert.toEmpty(req.getHeader(SIGN_FIELD_SIGN));
        return sign.equals(param);
    }
}
