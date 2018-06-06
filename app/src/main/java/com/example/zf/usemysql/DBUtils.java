package com.example.zf.usemysql;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
/**
 * Created by zf on 2018/6/5.
 */

public class DBUtils {
    private static final String TAG = "DBUtils";
    private static Connection getConnection(String dbName) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver"); //加载驱动
            String ip = "123.207.151.226";
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + ip + ":3306/" + dbName,
                    "root", "root");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return conn;
    }
    public static HashMap<String, String> getUserInfoByName(int name) {
        HashMap<String, String> map = new HashMap<>();
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement();
            String sql = "select * from framgment where id = '" + name + "'";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                int cnt = res.getMetaData().getColumnCount();
                //res.last(); int rowCnt = res.getRow(); res.first();
                res.next();
                for (int i = 1; i <= cnt; ++i) {
                    String field = res.getMetaData().getColumnName(i);
                    map.put(field, res.getString(field));
                }
                conn.close();
                st.close();
                res.close();
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
    }

    public static HashMap<String, String> adddata(String title,String context) {
        HashMap<String, String> map = new HashMap<>();
        Connection connadd = getConnection("test");
        try {
            Statement st = connadd.createStatement();
            String sql = "insert into framgment(username,title,context,zan) " +
                    "values( '梓杰','"+title+"','"+context+"',0)";
            st.executeUpdate(sql);
            connadd.close();
            st.close();
           // res.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
        return null;
    }
}
