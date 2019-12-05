package dev.mher.taskhunter.controllers.v1;

import dev.mher.taskhunter.models.TaskModel;
import dev.mher.taskhunter.models.misc.task.Task;
import dev.mher.taskhunter.services.TaskService;
import dev.mher.taskhunter.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 9:49 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.controllers.v1.
 */


@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity getTasks(
            @RequestParam int offset,
            @RequestParam int limit,
            @RequestParam int projectId
    ) {
        try {
            List<TaskModel> tasks = taskService.list(projectId, limit, offset);
            if (tasks == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "LIST_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(tasks));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }

    @PostMapping()
    public ResponseEntity createTask(
            @RequestBody Task task
    ) {
        try {
            TaskModel taskModel = taskService.save(task);
            if (taskModel == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "INSERT_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(task));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }



    @GetMapping("/{taskId}")
    public ResponseEntity retrieveTask(
            @PathVariable("taskId") Integer taskId
    ) {
        try {
            TaskModel task = taskService.retrieve(taskId);
            if (task == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "RETRIEVE_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(task));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }



    @PutMapping("/{taskId}")
    public ResponseEntity updateTask(
            @PathVariable("taskId") Integer taskId,
            @RequestBody Task task
    ) {
        try {
            task.setTaskId(taskId);
            TaskModel taskModel = taskService.update(task);
            if (taskModel == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "UPDATE_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(taskModel));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity deleteTask(
            @PathVariable("taskId") Integer taskId
    ) {
        try {
            Boolean isDeleted = taskService.delete(taskId);
            if (isDeleted == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "DELETE_ERROR"));
            }
            Map<String, Boolean> response = new HashMap<>();
            response.put("isDeleted", isDeleted);
            return ResponseEntity.ok(new ResponseUtils(response));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }



    @GetMapping("/{taskId}/sub-tasks")
    public ResponseEntity listSubTasks(
            @PathVariable("taskId") Integer taskId,
            @RequestParam int offset,
            @RequestParam int limit
            ) {
        try {
            List<TaskModel> tasks = taskService.listSubTasks(taskId, limit, offset);
            if (tasks == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "SUB_TASKS_LIST_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(tasks));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }


}
