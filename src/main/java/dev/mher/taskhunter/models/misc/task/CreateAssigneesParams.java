package dev.mher.taskhunter.models.misc.task;

import lombok.Getter;
import lombok.Setter;

/**
 * User: MheR
 * Date: 12/5/19.
 * Time: 2:50 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.misc.task.
 */
@Getter
@Setter
public class CreateAssigneesParams {
    private int[] assigneeIds;
}
