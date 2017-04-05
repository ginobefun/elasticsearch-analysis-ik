package org.wltea.analyzer.db;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.wltea.analyzer.dic.Monitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简易的数据库操作工具类。
 * Created by ginozhang on 2017/4/5.
 */
public final class JDBCUtils {

    private static final Logger logger = ESLoggerFactory.getLogger(Monitor.class.getName());

    public static Map<WordType, Long> queryMaxVersion(String dbUrl) throws Exception {
        Map<WordType, Long> maxVersionMap = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        WordType wordType;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbUrl);
            stmt = conn.createStatement();
            String sql = "SELECT word_type WORD_TYPE, max(version) VERSION FROM es_word_def GROUP BY word_type";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if ((wordType = WordType.fromCode(rs.getInt("VERSION"))) != null) {
                    maxVersionMap.put(wordType, rs.getLong("VERSION"));
                }
            }
        } finally {
            closeQuietly(conn, stmt, rs);
        }

        return maxVersionMap;
    }

    public static List<String> getDynWords(String dictDBUrl, WordType wordType, long maxVersion) {
        List<String> list = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dictDBUrl);
            stmt = conn.createStatement();
            String sql = "select word from es_word_def where version <= " + maxVersion
                    + " and word_type = " + wordType.getCode() + " and status = 1";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString("word"));
            }
        } catch (ClassNotFoundException e) {
            logger.error("Load words from DB failed.", e);
        } catch (SQLException e) {
            logger.error("Load words from DB failed.", e);
        } finally {
            closeQuietly(conn, stmt, rs);
        }

        return list;
    }

    public static void recordLog(String dictDBUrl, String logContent) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dictDBUrl);
            stmt = conn.createStatement();
            String sql = "insert es_word_log(log_content) values('" + logContent + "')";
            stmt.execute(sql);
        } catch (ClassNotFoundException e) {
            logger.error("Record log to DB failed. logContent=" + logContent, e);
        } catch (SQLException e) {
            logger.error("Record log to DB failed. logContent=" + logContent, e);
        } finally {
            closeQuietly(conn, stmt, null);
        }
    }

    private static void closeQuietly(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

}
