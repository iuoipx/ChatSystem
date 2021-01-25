package com.iuoip.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库操作类
 */
public class DBOperator {
    Connection conn = null;

    public DBOperator() {
        try {
            conn = JDBCUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向数据库添加用户
     *
     * @param user 用户注册输入的信息
     * @return 数据库更新条数
     */
    public int addUser(User user) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            //开启事务
            conn.setAutoCommit(false);

            //获取数据库操作对象
            String sql = "insert into tb_user(username, password, email) values (?,?,?)";
            ps = conn.prepareStatement(sql);

            //执行sql语句
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            count = ps.executeUpdate();

            conn.commit(); //提交事务
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); //事物回滚
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, ps, null);
        }

        return count;
    }


    /**
     * 用户校验
     *
     * @param user 用户输入的信息
     * @return 检验结果
     */
    public int findUser(User user) {
        int result = 0;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String username = user.getUsername();
        String password = user.getPassword();
        try {

            //获取数据库操作对象
            String sql = "select password from tb_user where username=?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String db_password = resultSet.getString("password");
                if (password.equals(db_password)) {
                    result = 1;
                } else {
                    result = 0;
                }
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, ps, resultSet);
        }

        return result;
    }

    /**
     * 找回用户密码
     *
     * @param user 用户输入的用户名和邮箱信息
     * @return 密码
     */
    public String findPassword(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select password from tb_user where username = ? and email = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, email);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                password = resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, ps, resultSet);
        }
        return password;
    }


    /**
     * 账号注销
     * @param username 用户名
     * @return
     */
    public int deleteUser(String username) {
        PreparedStatement ps = null;
        int count = 0;

        try {
            String sql = "delete from tb_user where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, ps, null);
        }
        return count;
    }

    /**
     * 修改用户密码
     *
     * @param user
     * @return
     */
    public int changePassword(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        int count = 0;
        PreparedStatement ps = null;

        String sql = "update tb_user set password = ? where username = ?";
        try {
            ps = conn.prepareStatement(sql);

            ps.setString(1, password);
            ps.setString(2, username);

            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, ps, null);
        }
        return count;
    }
}
