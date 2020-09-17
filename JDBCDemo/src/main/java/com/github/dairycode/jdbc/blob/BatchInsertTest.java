package com.github.dairycode.jdbc.blob;

import com.github.dairycode.jdbc.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 使用PreparedStatement实现批量数据的操作
 *
 * update,delete本身就具有批量操作的效果
 * 此时的批量操作,主要指的是批量插入,使用PreparedStatement如何实现更高效的批量插入?
 *
 * 题目: 向goods表中插入20000条数据
 * CREATE TABLE goods(
 *   id INT PRIMARY KEY AUTO_INCREMENT,
 *   NAME VARCHAR(25)
 *   );
 * 方式一: 使用Statement
 * Connection conn = JDBCUtils.getConnection();
 * Statement st = conn.createStatement();
 * for (int i = 1; i <= 20000; i++) {
 *     String sql = "insert into goods(name)values('name_" + i + "')";
 *     st.execute(sql);
 * }
 */
public class BatchInsertTest {

    // 批量插入的方式二: 使用PreparedStatement
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    // 批量插入的方式三:
    // 1.addBatch(),executeBatch(),clearBatch()
    // 2.mysql服务器默认是关闭批处理的,我们需要通过一个参数,让mysql开启批处理的支持
    //   ?rewriteBatchedStatement=true 写在配置文件的url后面
    // 3.使用教新的mysql驱动
    @Test
    public void testInsert3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);

                // 1."攒"sql
                ps.addBatch();

                if (i % 500 == 0) {
                    // 2.执行batch
                    ps.executeBatch();

                    // 3.清空batch
                    ps.clearBatch();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    // 批量插入的方式四: 设置连接不允许自动提交数据
    @Test
    public void testInsert4() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();

            // 设置不允许自动提交数据
            conn.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);

                // 1."攒"sql
                ps.addBatch();

                if (i % 500 == 0) {
                    // 2.执行batch
                    ps.executeBatch();

                    // 3.清空batch
                    ps.clearBatch();
                }
            }
            // 提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
