package dev.mher.taskhunter.models;

import dev.mher.taskhunter.models.misc.task.Task;
import dev.mher.taskhunter.utils.DataSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 9:54 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.
 */

@Component
public class TaskModel extends Task {

    private static final Logger logger = LoggerFactory.getLogger(TaskModel.class);

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public TaskModel() {

    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TaskModel save() {
        String queryString = "INSERT INTO tasks (project_id, parent_task_id, name, text)\n" +
                "VALUES (?, ?, ?, ?)\n" +
                "RETURNING task_id AS \"taskId\";";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {

            con = this.getDataSource().getConnection();
            pst = con.prepareStatement(queryString);
            pst.setInt(1, this.getProjectId());
            pst.setInt(1, this.getParentTaskId());
            pst.setString(2, this.getName());
            pst.setString(3, this.getText());

            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                this.setTaskId(rs.getInt("taskId"));
            }

        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(con, pst, rs);
        }
        return this;
    }


    public List<TaskModel> list(int limit, int offset, int projectId) {
        String queryString = "SELECT task_id AS \"taskId\",\n" +
                "       parent_task_id AS \"parentTaskId\",\n" +
                "       project_id AS \"projectId\",\n" +
                "       name,\n" +
                "       text,\n" +
                "       created_at AS \"createdAt\",\n" +
                "       updated_at AS \"updatedAt\"\n" +
                "FROM tasks\n" +
                "WHERE (?::INT IS NULL OR project_id=?)\n" +
                "LIMIT ?\n" +
                "OFFSET ?;";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<TaskModel> tasks = new ArrayList<>();
        try {

            con = this.getDataSource().getConnection();
            pst = con.prepareStatement(queryString);

            pst.setInt(1, projectId);
            pst.setInt(2, projectId);
            pst.setInt(3, limit);
            pst.setInt(4, offset);

            rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    TaskModel task = new TaskModel();
                    task.setTaskId(rs.getInt("taskId"));
                    task.setProjectId(rs.getInt("projectId"));
                    task.setParentTaskId(rs.getInt("name"));
                    task.setName(rs.getString("name"));
                    task.setText(rs.getString("text"));
                    task.setCreatedAt(rs.getTimestamp("createdAt"));
                    task.setUpdatedAt(rs.getTimestamp("updatedAt"));

                    tasks.add(task);
                }
                return tasks;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(con, pst, rs);
        }
        return tasks;
    }

    public TaskModel retrieve(Integer taskId) {
        String queryString = "SELECT task_id AS \"taskId\",\n" +
                "       parent_task_id AS \"parentTaskId\",\n" +
                "       project_id AS \"projectId\",\n" +
                "       name,\n" +
                "       text,\n" +
                "       created_at AS \"createdAt\",\n" +
                "       updated_at AS \"updatedAt\"\n" +
                "FROM tasks\n" +
                "WHERE task_id=?;";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {

            con = this.getDataSource().getConnection();
            pst = con.prepareStatement(queryString);
            pst.setInt(1, taskId);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {

                TaskModel task = new TaskModel();

                task.setTaskId(rs.getInt("taskId"));
                task.setProjectId(rs.getInt("projectId"));
                task.setParentTaskId(rs.getInt("name"));
                task.setName(rs.getString("name"));
                task.setText(rs.getString("text"));
                task.setCreatedAt(rs.getTimestamp("createdAt"));
                task.setUpdatedAt(rs.getTimestamp("updatedAt"));

                return task;
            }

        } catch (SQLException e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            DataSourceUtils.closeConnection(con, pst, rs);
        }
        return null;
    }
}
