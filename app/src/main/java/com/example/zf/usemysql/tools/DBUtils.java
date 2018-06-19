package com.example.zf.usemysql.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by zf on 2018/6/5.
 */

public class DBUtils {
    private static final String TAG = "DBUtils";

    public static int LastID;

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

    public static HashMap<String, String> CheakUser(String name) {
        HashMap<String, String> map = new HashMap<>();
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement();
            String sql = "select * from users where username = '" + name + "'";
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

    public static int findhangshu() {
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select id from framgment";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return -1;
            } else {
                res.last();
                int count = res.getRow();
                res.beforeFirst();
                res.next();
                conn.close();
                st.close();
                res.close();
                return (count-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return -1;
        }
    }


/*
    public static void getBlob(int ID){//读取Blob数据
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = getConnection("test");
            String sql = "select picadd from framgment limit  " + ID + ",1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                Blob picture = rs.getBlob(1);//得到Blob对象
                //开始读入文件
                InputStream in = picture.getBinaryStream();
                OutputStream out = new FileOutputStream("picture"+ID+".png");
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
            }
            ps.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    public static HashMap<String, String> ChackID() {
        HashMap<String, String> map = new HashMap<>();
        int a= findhangshu();
        Random random = new Random();
        int name=random.nextInt(a) + 1;
        LastID = name;
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select username,title,context,zan from framgment limit  " + name + ",1";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                int cnt = res.getMetaData().getColumnCount();
                res.last();
                //int count = res.getRow();
                res.beforeFirst();
                //res.last(); int rowCnt = res.getRow(); res.first();
                res.next();
                for (int i = 1; i <= cnt; ++i) {
                    String field = res.getMetaData().getColumnLabel(i);
                    map.put(field, res.getString(field));
                    //Log.d("DBUtils","aaaa:"+res.getString(field));
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

    public static HashMap<String, String> AddData(String title,String context,String username,String path) {
        HashMap<String, String> map = new HashMap<>();
        Connection connadd = getConnection("test");
        PreparedStatement ps = null;
        try {
            String sql = "insert into framgment(zan,title,context,username,piccheck) " +
                    "values( 0,'"+title+"','"+context+"','"+username+"',0)";
            ps = connadd.prepareStatement(sql);
           // InputStream in = new FileInputStream(path);//生成被插入文件的节点流
            //设置Blob
           // ps.setBlob(1, in);
            ps.executeUpdate();
            connadd.close();
            ps.close();
           // res.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
        return null;
    }


    public static HashMap<String, String> adduser(String title,String context) {
        HashMap<String, String> map = new HashMap<>();
        Connection connadd = getConnection("test");
        try {
            Statement st = connadd.createStatement();
            String sql = "insert into users(username,password) " +
                    "values( '"+title+"','"+context+"')";
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

    public static HashMap<String, String> AddZan(String username) {
        HashMap<String, String> map = new HashMap<>();
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

    public static HashMap<String, String> CutZan(String username) {
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
