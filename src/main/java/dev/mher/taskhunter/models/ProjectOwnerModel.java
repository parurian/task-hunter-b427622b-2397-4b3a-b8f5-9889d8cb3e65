package dev.mher.taskhunter.models;

import dev.mher.taskhunter.services.AuthenticationService;
import dev.mher.taskhunter.utils.DataSourceUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

/**
 * User: MheR
 * Date: 12/3/19.
 * Time: 01:14 AM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.
 */

@Component
@Getter
@Setter
public class ProjectOwnerModel {
    private int projectOwnerId;
    private int projectId;
    private int userId;
    private Timestamp createdAt;

    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    @Autowired
    public ProjectOwnerModel(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ProjectOwnerModel insertProjectOwner(Connection conn) throws SQLException {
        boolean isInTransaction = conn != null;
        if (!isInTransaction) {
            conn = dataSource.getConnection();
        }
        String queryString = "INSERT INTO project_owners (project_id, user_id) VALUES (?, ?) RETURNING project_owner_id AS \"projectOwnerId\", created_at AS \"createdAt\";";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(queryString);
            pst.setInt(1, getProjectId());
            pst.setInt(2, getUserId());
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                setProjectOwnerId(rs.getInt("projectOwnerId"));
                setCreatedAt(rs.getTimestamp("createdAt"));
            }
            if (isInTransaction) {
                DataSourceUtils.closeConnection(null, pst, rs);
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            if (!isInTransaction) {
                DataSourceUtils.closeConnection(conn, pst, rs);
            }
        }
        return this;
    }


    public boolean deleteOwnersByProjectId(Connection conn) throws SQLException {
        boolean isInTransaction = conn != null;
        if (!isInTransaction) {
            conn = dataSource.getConnection();
        }
        String queryString = "DELETE FROM project_owners WHERE project_id=?;";
        PreparedStatement pst = null;
        boolean isDeleted = false;
        try {
            pst = conn.prepareStatement(queryString);
            pst.setInt(1, getProjectId());
            isDeleted = pst.execute();
            if (isInTransaction) {
                DataSourceUtils.closeConnection(null, pst, null);
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            if (!isInTransaction) {
                DataSourceUtils.closeConnection(conn, pst, null);
            }
        }
        return isDeleted;
    }


}
