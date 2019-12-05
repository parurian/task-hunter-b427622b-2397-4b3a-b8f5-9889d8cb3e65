package dev.mher.taskhunter.models.misc.task;

/**
 * User: MheR
 * Date: 12/5/19.
 * Time: 2:51 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.misc.task.
 */

public class CreateTaskParams {
    private Integer projectId;
    private Integer parentTaskId;
    private String name;
    private String text;

    public Integer getProjectId() {
        return projectId;
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
