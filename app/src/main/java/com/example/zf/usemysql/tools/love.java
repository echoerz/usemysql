package com.example.zf.usemysql.tools;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by zf on 2018/6/21.
 */

public class love {
    private static final String TAG = "love";

    public static int LastID;
    public static int cnt=0;
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


    public static HashMap<String, String> Addlove(int loveid,String user) {
        HashMap<String, String> map = new HashMap<>();
       // int a= Integer.parseInt(loveid);
        Connection connadd = getConnection("test");
        PreparedStatement ps = null;
        try {
            String sql = "insert into "+user+"(love) " +
                    "values("+loveid+")";
            ps = connadd.prepareStatement(sql);
            ps.executeUpdate();
            connadd.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
        return null;
    }



    public static String findlovezong(String username) {
        Connection conn = getConnection("test");
        String ss= null;
        int id=0;
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select * from "+username;
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                while (res.next()) {
                    id = res.getInt("love");
                    if(ss == null){
                        ss = love.findlove(id);
                    }else {
                        ss = ss + love.findlove(id);
                    }
                }
                conn.close();
                st.close();
                res.close();
                return ss;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
    }

    public static String findlove(int id) {
        Connection conn = getConnection("test");
        String ss= null;
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select username,title,context,zan,id,piccheck from framgment where id= "+id;
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                while (res.next()) {
                    ss =  res.getString("username") + "cccc\n"
                            + res.getString("title") + "cccc\n"
                            + res.getString("context") + "cccc\n"
                            + res.getInt("zan")+ "cccc\n"
                            + res.getInt("id") + "cccc\n"
                            + res.getInt("piccheck")+ "ddd\n";
                }
                conn.close();
                st.close();
                res.close();
                return ss;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
    }
}
