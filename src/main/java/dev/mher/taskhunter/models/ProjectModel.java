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
import java.beans.Transient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
public class ProjectModel {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private int projectId;
    private String name;
    private Timestamp createdAt;
    private boolean isOwner;

    private List<ProjectOwnerModel> projectOwnersList = new ArrayList<>();

    private DataSource dataSource;

    private ProjectOwnerModel projectOwnerModel;

    public ProjectModel() {
    }

    public ProjectModel(String name) {
        this.name = name;
    }

    @Autowired
    public ProjectModel(DataSource dataSource, ProjectOwnerModel projectOwnerModel) {
        this.dataSource = dataSource;
        this.projectOwnerModel = projectOwnerModel;
    }

    public ProjectModel save(int userId) throws SQLException {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);

            // insert project
            this.insertProject(conn);

            this.projectOwnerModel.setProjectId(getProjectId());
            this.projectOwnerModel.setUserId(userId);

            // insert project owner
            ProjectOwnerModel projectOwner = this.projectOwnerModel.insertProjectOwner(conn);
            this.projectOwnersList.add(projectOwner);

            // finalize transaction
            conn.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            logger.info(e.getMessage(), e);
            if (conn != null) {
                conn.rollback();
            }
        } finally {
            DataSourceUtils.closeConnection(conn, null, null);
        }
        return this;
    }


    private ProjectModel insertProject(Connection con) {
        String queryString = "INSERT INTO projects (name) VALUES (?) RETURNING project_id AS \"projectId\", created_at AS \"createdAt\";";
        PreparedStatement pst;
        try {
            pst = con.prepareStatement(queryString);
            pst.setString(1, getName());
            ResultSet rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                setProjectId(rs.getInt("projectId"));
                setCreatedAt(rs.getTimestamp("createdAt"));
            }
            DataSourceUtils.closeConnection(null, pst, rs);

        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return this;
    }

    @Transient
    public List<ProjectOwnerModel> getProjectOwnersList() {
        return projectOwnersList;
    }

    public void setProjectOwnersList(List<ProjectOwnerModel> projectOwnersList) {
        this.projectOwnersList = projectOwnersList;
    }

    public ProjectOwnerModel getProjectOwnerModel() {
        return projectOwnerModel;
    }

    public void setProjectOwnerModel(ProjectOwnerModel projectOwnerModel) {
        this.projectOwnerModel = projectOwnerModel;
    }

    public List<ProjectModel> list(int userId, int limit, int offset) {

        String queryString = "SELECT p.project_id AS \"projectId\", name, p.created_at AS \"createdAt\",\n" +
                "       po.user_id IS NOT NULL AND po.user_id = ? AS \"isOwner\"\n" +
                "FROM projects p\n" +
                "         LEFT JOIN project_owners po ON p.project_id = po.project_id\n" +
                "ORDER BY \"isOwner\" DESC, p.created_at DESC\n" +
                "LIMIT ?\n" +
                "OFFSET ?;";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement(queryString);
            pst.setInt(1, userId);
            pst.setInt(2, limit);
            pst.setInt(3, offset);

            rs = pst.executeQuery();
            if (rs != null) {

                List<ProjectModel> projects = new ArrayList<>();
                while (rs.next()) {
                    ProjectModel project = new ProjectModel();
                    project.setProjectId(rs.getInt("projectId"));
                    project.setName(rs.getString("name"));
                    project.setCreatedAt(rs.getTimestamp("createdAt"));
                    project.setOwner(rs.getBoolean("isOwner"));

                    projects.add(project);
                }

                return projects;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(con, pst, rs);
        }
        return null;
    }

    public ProjectModel retrieve(Integer projectId, Integer userId) {

        String queryString = "SELECT p.project_id AS \"projectId\", name, p.created_at AS \"createdAt\",\n" +
                "       po.user_id IS NOT NULL AS \"isOwner\"\n" +
                "FROM projects p\n" +
                "         LEFT JOIN project_owners po ON p.project_id = po.project_id AND po.user_id = ?\n" +
                "WHERE p.project_id = ?;";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement(queryString);
            pst.setInt(1, userId);
            pst.setInt(2, projectId);

            rs = pst.executeQuery();
            if (rs != null && rs.next()) {

                ProjectModel project = new ProjectModel();
                project.setProjectId(rs.getInt("projectId"));
                project.setName(rs.getString("name"));
                project.setCreatedAt(rs.getTimestamp("createdAt"));
                project.setOwner(rs.getBoolean("isOwner"));

                return project;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(con, pst, rs);
        }
        return null;
    }

    public boolean update(ProjectModel model) {
        String queryString = "UPDATE projects SET name=? WHERE project_id=?;";
        Connection con = null;
        PreparedStatement pst = null;
        boolean isUpdated = false;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement(queryString);
            pst.setString(1, model.getName());
            pst.setInt(2, model.getProjectId());
            isUpdated = pst.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(con, pst, null);
        }
        return isUpdated;
    }

    public boolean delete(ProjectModel model) throws SQLException {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            // delete owners
            projectOwnerModel.setProjectId(model.getProjectId());
            projectOwnerModel.deleteOwnersByProjectId(conn);
            // delete project
            model.deleteProjectById(conn);
            // finalize transaction
            conn.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            logger.info(e.getMessage(), e);
            if (conn != null) {
                conn.rollback();
            }
        } finally {
            DataSourceUtils.closeConnection(conn, null, null);
        }
        return true;
    }

    private boolean deleteProjectById(Connection conn) throws SQLException {
        boolean isInTransaction = conn != null;
        if (!isInTransaction) {
            conn = dataSource.getConnection();
        }
        String queryString = "DELETE FROM projects WHERE project_id=?;";
        PreparedStatement pst = null;
        boolean isDeleted = false;
        try {
            pst = conn.prepareStatement(queryString);
            pst.setInt(1, this.getProjectId());
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

    private boolean deleteProjectTasks(Connection conn) throws SQLException {
        boolean isInTransaction = conn != null;
        if (!isInTransaction) {
            conn = dataSource.getConnection();
        }
        String queryString = "DELETE FROM tasks WHERE project_id=?;";
        PreparedStatement pst = null;
        boolean isDeleted = false;
        try {
            pst = conn.prepareStatement(queryString);
            pst.setInt(1, this.getProjectId());
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
