package dev.mher.taskhunter.controllers.v1;

import dev.mher.taskhunter.models.ProjectModel;
import dev.mher.taskhunter.services.AuthenticationService;
import dev.mher.taskhunter.services.ProjectService;
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
 * Date: 12/3/19.
 * Time: 02:13 AM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.controllers.v1.
 */

@RestController
@RequestMapping("v1/projects")
public class ProjectController {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping()
    public ResponseEntity getProjects(
            @RequestParam int offset,
            @RequestParam int limit
    ) {

        int userId = 26;

        try {
            List<ProjectModel> projects = projectService.list(userId, limit, offset);
            if (projects == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "LIST_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(projects));

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }

        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }


    @PostMapping()
    public ResponseEntity createProject(
            @RequestParam String name
    ) {
        int userId = 26;
        try {
            ProjectModel project = projectService.save(userId, name);
            if (project == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "INSERT_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(project));

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }


    @GetMapping("{projectId}")
    public ResponseEntity retrieveProject(
            @PathVariable("projectId") Integer projectId
    ) {
        try {
            int userId = 26;

            ProjectModel project = projectService.retrieve(projectId, userId);
            if (project == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "RETRIEVE_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(project));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }

    @PutMapping("{projectId}")
    public ResponseEntity updateProject(
            @PathVariable("projectId") Integer projectId,
            @RequestParam String name
    ) {
        int userId = 26;
        try {
            ProjectModel project = projectService.update(projectId, userId, name);
            if (project == null) {
                return ResponseEntity.ok(new ResponseUtils(true, "UPDATE_ERROR"));
            }
            return ResponseEntity.ok(new ResponseUtils(project));
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseUtils(true, "UNKNOWN_ERROR"));
    }


    @DeleteMapping("{projectId}")
    public ResponseEntity deleteProject(
            @PathVariable("projectId") Integer projectId
    ) {
        int userId = 26;
        try {
            Boolean isDeleted = projectService.delete(projectId, userId);
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
}
