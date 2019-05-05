package com.gm.utils;

import com.gm.model.response.JsonResult;
import com.gm.utils.base.Excel;
import com.gm.utils.base.Logger;
import com.gm.utils.ext.Resource;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The type com.gm.utils.Main.
 *
 * @author Jason
 */
public class Main {

    @Value("name")
    String name;


    @Test
    public void mainJason() {
        Logger.debug(JsonResult.as(true));
    }

    @Test
    public void mainTests() {
        System.out.println(Resource.getProperty("name"));
//        System.out.println("======="+Regex.find("[abc]----dcw[998[*[e]+[r]", Regexp.BRACKET_STR.getCode())+"=======");
    }


    char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    @Test
    public void main() throws Exception {
        List cns = new ArrayList();
        for (int i = 0; i < chars.length; i++) {
            for (int j=i;j<chars.length;j++) {
//                for (int k=j;k<chars.length;k++) {
//                    for (int l=k;l<chars.length;l++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(chars[i]);
                        sb.append(chars[j]);
//                        sb.append(chars[k]);
//                        sb.append(chars[l]);
                        sb.append(".cn");
                        cns.add(sb.toString());
//                    }
//                }
            }
        }
        Workbook wk = Excel.news(cns);
        wk.write(new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\123.xls")));
    }
}
