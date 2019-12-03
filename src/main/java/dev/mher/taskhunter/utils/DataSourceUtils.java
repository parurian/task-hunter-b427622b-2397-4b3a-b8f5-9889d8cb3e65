package dev.mher.taskhunter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 10:37 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.utils.
 */
public class DataSourceUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtils.class);

    public static void closeConnection(Connection conn, Statement st, ResultSet rs) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
