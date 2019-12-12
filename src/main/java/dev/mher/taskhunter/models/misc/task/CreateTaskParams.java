package dev.mher.taskhunter.models.misc.task;

import lombok.Getter;
import lombok.Setter;

/**
 * User: MheR
 * Date: 12/5/19.
 * Time: 2:51 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.misc.task.
 */

@Getter
@Setter
public class CreateTaskParams {
    private Integer projectId;
    private Integer parentTaskId;
    private String name;
    private String text;
}
