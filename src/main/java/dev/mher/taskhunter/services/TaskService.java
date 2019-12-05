package dev.mher.taskhunter.services;

import dev.mher.taskhunter.models.TaskModel;
import dev.mher.taskhunter.models.misc.task.CreateTaskParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 9:50 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.services.
 */

@Service
public class TaskService {

    private final TaskModel taskModel;

    @Autowired
    public TaskService(TaskModel taskModel) {
        this.taskModel = taskModel;
    }

    public TaskModel save(CreateTaskParams task) {
        this.taskModel.setName(task.getName());
        this.taskModel.setText(task.getText());
        this.taskModel.setProjectId(task.getProjectId());
        this.taskModel.setParentTaskId(task.getParentTaskId());
        try {
            return this.taskModel.save();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TaskModel> list(Integer projectId, Integer limit, Integer offset) {
        try {
            return this.taskModel.list(projectId, limit, offset);
        } catch (Exception e) {
            return null;
        }
    }

    public TaskModel retrieve(Integer taskId) {
        try {
            return this.taskModel.retrieve(taskId);
        } catch (Exception e) {
            return null;
        }
    }

    public TaskModel update(Integer taskId, CreateTaskParams task) {
        try {
            TaskModel model = taskModel.retrieve(taskId);
            if (model == null) {
                // invalid task
                return null;
            }
            taskModel.setTaskId(model.getTaskId());
            taskModel.setParentTaskId(task.getParentTaskId());
            taskModel.setName(task.getName());
            taskModel.setText(task.getText());
            taskModel.setProjectId(task.getProjectId());
            taskModel.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            if (taskModel.update() == null) {
                // unknown error
                return null;
            }
            return taskModel;
        } catch (Exception e) {
            return null;
        }
    }


    public Boolean delete(Integer taskId) {
        try {
            TaskModel model = taskModel.retrieve(taskId);
            if (model == null) {
                // invalid task
                return null;
            }
            if (!taskModel.delete(model)) {
                // unknown error
                return null;
            }
            return true;
        } catch (Exception e) {
            return null;
        }
    }


    public List<TaskModel> listSubTasks(int taskId, int limit, int offset) {
        try {
            return this.taskModel.listSubTasks(taskId, limit, offset);
        } catch (Exception e) {
            return null;
        }
    }

    public void createAssignees(int taskId, int userId, int[] assigneeIds) {
        try {
            this.taskModel.createAssignees(taskId, userId, assigneeIds);
        } catch (Exception e) {
//            return null;
        }
    }
}
