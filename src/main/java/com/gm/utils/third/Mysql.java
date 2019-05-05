package com.gm.utils.third;

import com.gm.utils.Utils;
import com.gm.utils.base.ExceptionUtils;
import com.gm.utils.base.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mysql工具类
 *
 * @author Jason
 */
public class Mysql implements Utils {
    public static final String SELECT = "select";

    /**
     * Exec t.
     *
     * @param url      the url
     * @param username the username
     * @param password the password
     * @param sql      the sql
     * @return the t
     */
    public static List<Map<String, Object>> exec(String url, String username, String password, String sql) {
        Connection con = connection(url, username, password);
        try {
            if (!con.isClosed()) {
                //3、创建Statement对象
                Statement st = con.createStatement();
                //6. 转换Json
                List<Map<String, Object>> result = getResult(sql, st);
                //7. 关闭数据库
                con.close();
                //8. 转换Obj
                return result;
            }
        } catch (Exception e) {
            Logger.debug("执行{}出错", sql, e);
            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return ExceptionUtils.cast();
    }

    private static List<Map<String, Object>> getResult(String sql, Statement st) throws SQLException {
        List<Map<String,Object>> array = new ArrayList();
        //4、执行sql语句
        if(sql.startsWith(SELECT)) {
            ResultSet rs = st.executeQuery(sql);
            //5.1 获得结果集结构信息,元数据
            ResultSetMetaData md = rs.getMetaData();
            //5.2 收集结果
            int count = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> data = new HashMap(0);
                for (int i = 1; i <= count; i++) {
                    data.put(md.getColumnName(i), rs.getObject(i));
                }
                array.add(data);
            }
            //关闭资源
            rs.close();
        }else{
            Map data = new HashMap(1);
            st.execute(sql);
            data.put("count", st.getUpdateCount());
            array.add(data);
        }
        return array;
    }

    private static Connection connection(String url, String username, String password) {
        try {
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2、链接数据库
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            Logger.debug("获取连接{}:{}出错", username, password, e);
            return ExceptionUtils.cast(e);
        }
    }
}