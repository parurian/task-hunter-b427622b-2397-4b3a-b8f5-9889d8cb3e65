package dev.mher.taskhunter.models;

import dev.mher.taskhunter.utils.DataSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: MheR
 * Date: 12/5/19.
 * Time: 4:09 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.
 */
@Component
public class TaskAssigneeModel {

    private static final Logger logger = LoggerFactory.getLogger(TaskAssigneeModel.class);

    private DataSource dataSource;

    private Integer taskAssigneeId;
    private Integer taskId;
    private Integer assigneeId;
    private Integer assignerId;

    public TaskAssigneeModel() {
    }

    @Autowired
    public TaskAssigneeModel(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getTaskAssigneeId() {
        return taskAssigneeId;
    }

    public void setTaskAssigneeId(Integer taskAssigneeId) {
        this.taskAssigneeId = taskAssigneeId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Integer getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(Integer assignerId) {
        this.assignerId = assignerId;
    }


    public void createAssignees(int taskId, int userId, int[] assigneeIds) throws SQLException {
//        List<TaskAssigneeModel> taskAssigneeModels = null;
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);

            bulkCreate(conn, taskId, userId, assigneeIds);
            createAssignmentHistory(conn, taskId, userId, assigneeIds, true);

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
//        return taskAssigneeModels;
    }


    private boolean bulkCreate(Connection conn, int taskId, int userId, int[] assigneeIds) {

        String queryString = "INSERT INTO task_assignees (task_id, assignee_id, assigner_id)\n" +
                "VALUES (?, ?, ?) ON CONFLICT (task_id, assignee_id) DO NOTHING \n" +
                "RETURNING task_assignee_id AS \"taskAssigneeId\", task_id AS \"taskId\", assignee_id AS \"assigneeId\", assigner_id AS \"assignerId\";";

        PreparedStatement pst = null;
//        ResultSet rs = null;

        List<TaskAssigneeModel> taskAssignees = new ArrayList<>();

        try {
            pst = conn.prepareStatement(queryString);

            for (int assigneeId : assigneeIds) {
                pst.setInt(1, taskId);
                pst.setInt(2, assigneeId);
                pst.setInt(3, userId);
                pst.addBatch();
            }
            pst.executeBatch();
//            if (rs != null) {
//                while (rs.next()) {
//                    TaskAssigneeModel task = new TaskAssigneeModel();
//                    task.setTaskAssigneeId(rs.getInt("taskAssigneeId"));
//                    task.setAssigneeId(rs.getInt("assigneeId"));
//                    task.setAssignerId(rs.getInt("assignerId"));
//                    task.setTaskId(rs.getInt("taskId"));
//                    taskAssignees.add(task);
//                }
//                return taskAssignees;
//            }
            return true;

        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(null, pst, null);
        }
        return false;
    }

    private void createAssignmentHistory(Connection conn, int taskId, int userId, int[] assigneeIds, boolean isAttached) {
        String queryString = "INSERT INTO assignment_history (task_id, assignee_id, assigner_id, is_attached) VALUES (?, ?, ?, ?);";
        PreparedStatement pst = null;
        try {

            pst = conn.prepareStatement(queryString);
            for (int assigneeId : assigneeIds) {
                pst.setInt(1, taskId);
                pst.setInt(2, assigneeId);
                pst.setInt(3, userId);
                pst.setBoolean(4, isAttached);
                pst.addBatch();
            }
            pst.executeBatch();

        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(null, pst, null);
        }
    }

    public List<TaskAssigneeModel> listAssignees(Integer taskId, Integer limit, Integer offset) {
        String queryString = "SELECT task_assignee_id AS \"taskAssigneeId\", task_id AS \"taskId\", assignee_id AS \"assigneeId\",\n" +
                "       assigner_id AS \"assignerId\"\n" +
                "FROM task_assignees\n" +
                "WHERE task_id=?\n" +
                "ORDER BY task_assignee_id DESC\n" +
                "LIMIT ?\n" +
                "OFFSET ?;";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<TaskAssigneeModel> taskAssigneeModels = new ArrayList<>();
        try {

            conn = dataSource.getConnection();
            pst = conn.prepareStatement(queryString);

            pst.setInt(1, taskId);
            pst.setInt(2, limit);
            pst.setInt(3, offset);

            rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    TaskAssigneeModel taskAssigneeModel = new TaskAssigneeModel();
                    taskAssigneeModel.setTaskAssigneeId(rs.getInt("taskAssigneeId"));
                    taskAssigneeModel.setTaskId(rs.getInt("taskId"));
                    taskAssigneeModel.setAssigneeId(rs.getInt("assigneeId"));
                    taskAssigneeModel.setAssignerId(rs.getInt("assignerId"));
                    taskAssigneeModels.add(taskAssigneeModel);
                }
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(conn, pst, rs);
        }
        return taskAssigneeModels;
    }

    public boolean deleteAssignee(int taskId, int taskAssigneeId, int  userId) throws SQLException {
        Connection conn = null;

        int[] assigneeIds = new int[]{userId};
        boolean isSucceed = false;

        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);

            deleteAssigneeById(conn, taskId, taskAssigneeId);
            createAssignmentHistory(conn, taskId, userId, assigneeIds, false);

            conn.commit();
            isSucceed = true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            logger.info(e.getMessage(), e);
            if (conn != null) {
                conn.rollback();
            }
        } finally {
            DataSourceUtils.closeConnection(conn, null, null);
        }
        return isSucceed;
    }

    private void deleteAssigneeById(Connection conn, int taskId, int taskAssigneeId) {
        String queryString = "DELETE FROM task_assignees WHERE task_id=? AND task_assignee_id=?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(queryString);
            pst.setInt(1, taskId);
            pst.setInt(2, taskAssigneeId);
            pst.execute();
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(null, pst, null);
        }
    }
}
