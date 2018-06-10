package com.example.zf.usemysql.tools;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

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
    public static byte[] getpic() {
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select picadd from framgment";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                if(res.next()){
                    res.first();
                    Blob blob=res.getBlob("picadd");
                    BufferedInputStream is = null;
                    try {
                        is = new BufferedInputStream(blob.getBinaryStream());
                        byte[] bytes = new byte[(int) blob.length()];
                        int len = bytes.length;
                        int offset = 0;
                        int read = 0;

                        while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                            offset += read;
                        }
                        return bytes;
                    } catch (Exception e) {
                        return null;
                    } finally {
                        try {
                            is.close();
                            is = null;
                        } catch (IOException e) {
                            return null;
                        }
                    }
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
    }
*/
    public static HashMap<String, String> ChackID() {
        HashMap<String, String> map = new HashMap<>();
        int a= findhangshu();
        Random random = new Random();
        int name=random.nextInt(a) + 1;
        Connection conn = getConnection("test");
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select * from framgment limit  " + name + ",1";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                int cnt = res.getMetaData().getColumnCount();
                res.last();
                int count = res.getRow();
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

    public static HashMap<String, String> AddData(String title,String context,String username) {
        HashMap<String, String> map = new HashMap<>();
        Connection connadd = getConnection("test");
        try {
            Statement st = connadd.createStatement();
            String sql = "insert into framgment(zan,title,context,username) " +
                    "values( 0,'"+title+"','"+context+"','"+username+"')";
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
