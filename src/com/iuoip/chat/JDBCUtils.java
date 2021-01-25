package com.iuoip.chat;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * 数据库操作工具类
 */
public class JDBCUtils {

    private final static String driver;
    private final static String url;
    private final static String user;
    private final static String pwd;

    private JDBCUtils() {
    }

    //静态代码块,在类加载时自动执行
    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc");
        driver = resourceBundle.getString("driver");
        url = resourceBundle.getString("url");
        user = resourceBundle.getString("user");
        pwd = resourceBundle.getString("password");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(url, user, pwd);
        return conn;
    }

    public static void close(Connection conn, Statement stmt, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
