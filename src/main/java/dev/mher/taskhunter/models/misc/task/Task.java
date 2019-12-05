package dev.mher.taskhunter.models.misc.task;

import org.springframework.stereotype.Component;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 10:13 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.misc.task.
 */

@Component
public class Task {
    private Integer taskId;
    private Integer projectId;
    private Integer parentTaskId;
    private String name;
    private String text;
}
