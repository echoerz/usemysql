package com.example.zf.usemysql.tools;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static java.sql.DriverManager.getConnection;

/**
 * Created by 18810 on 2018/6/24.
 */

public class dianzan {

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

    public static String AddZan(String username) {
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement();
            String sql = "update fragment set zan=zan+1 where username = '" + username + "'";
            st.executeUpdate(sql);
            conn.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
        return null;
    }

    public static String CutZan(String username) {
        HashMap<String, String> map = new HashMap<>();
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement();
            String sql = "update fragment set zan=zan-1 where username = '" + username + "'";
            st.executeUpdate(sql);
            conn.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
        return null;
    }

}
